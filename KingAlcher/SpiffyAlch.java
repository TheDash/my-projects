import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import libs.BARS;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;
import org.powerbot.game.bot.event.listener.PaintListener;
@Manifest(authors = "Spiffy", name = "Spiffy Alch'n'Heat", version = 1.00, description = "Spiffy Alching, Spiffy Heating")
public class SpiffyAlch extends ActiveScript implements PaintListener , MouseListener, MessageListener {
	
	enum ENCHANTS {
		ENCHANT_LVL_1(29), ENCHANT_LVL_2(41), ENCHANT_LVL_3(53), ENCHANT_LVL_4(30), ENCHANT_LVL_5(35), ENCHANT_LVL_6(36);
		
		ENCHANTS(int id) {
			
		}
	}
	
	private boolean USE_SUPERHEAT = false;
	private boolean USE_ALCHEMY = false;
	private boolean GUI_DONE = false;
	private boolean HIDEBOX = false;
	private boolean MOVEBOX = false;
	private boolean ALCH_PRICE_SET = false;
	private boolean ERR_NO_EQUIPS = false;
	
	private int BANK_TO_USE = 0;

	private int PAINT_X = 8;
	private int PAINT_Y = 342;
	private int RECT_HEIGHT = 128;
	private int RECT_WIDTH = 510;
	private int MOVEBOX_X;
	private int MOVEBOX_Y;
	private int HIDEBOX_X;
	private int HIDEBOX_Y;
	private int PBAR_HEIGHT = 20;
	private int PBAR_WIDTH = 300;
	private int PBAR_OVAL = 5;
	
	private int SECONDARY_ID = 0;
	private int PRIMARY_ID = 0;
	private int PRIMARY_INTERVAL = 0;
	private int COAL_INTERVAL = 0;
	private int CAST_ITEM;
	private int NATURE_RUNE_STACK_SIZE = 0;
	private int NATURE_RUNE_COST = 0;
	
	private int SMITHING_XP_PER_CAST = 0;
	private int MAGIC_XP_PER_CAST = 0;
	private int GOLD_PER_CAST = 0;

	private int TOTAL_ALCH_CASTS = 0;
	private int TOTAL_SUPERHEAT_CASTS = 0;
	private Long runTimeMs;
	private int smithingStartXP;
	private int magicStartXP;
	private final int NATURE_RUNE = 561;
	
	private final int[] depositables = new int[10];
	private final String ERR_NO_EQUIP = "You do not have the required equipment to run this script.";
	private final String ERR_REQUIREMENTS = "Requires: Fire staff and nature runes.";
	
	private Filter<Item> bankFilter = new Filter<Item>() {

		@Override
		public boolean accept(Item arg0) {
			for (int i = 0; i < depositables.length; i++) {
				if (arg0.getId() == depositables[i]) {
					return false;
				}
			}
			return true;
		}
		
	};
	
	private boolean hasAlchingEquipment() {
		return NATURE_RUNE_STACK_SIZE > 0;
	}
	
	@Override
	protected void setup() {
		
		Item natRune = Inventory.getItem(NATURE_RUNE);
		if (natRune == null) {
			log.severe("You have no nature runes.");
		} else {
			NATURE_RUNE_STACK_SIZE = natRune.getStackSize();
		}
		
		
		SceneObject bankCntr = SceneEntities.getNearest(Bank.BANK_COUNTER_IDS);
		if (bankCntr != null) {
			BANK_TO_USE = bankCntr.getId();
		} else {
			NPC bnkr = NPCs.getNearest(Bank.BANK_NPC_IDS); 
			if (bnkr != null) {
				BANK_TO_USE = bnkr.getId();
			} else {
				SceneObject b = SceneEntities.getNearest(Bank.BANK_CHEST_IDS);
				if (b != null) {
					BANK_TO_USE = b.getId();
				} else {
					SceneObject bo = SceneEntities.getNearest(Bank.BANK_BOOTH_IDS);
					if (bo != null) {
						BANK_TO_USE = bo.getId();
					} else {
						log.severe("Start near a bank or the script will not work.");
					}
				}
			}
		}
		
		runTimeMs = System.currentTimeMillis();
		smithingStartXP = Skills.getExperience(Skills.SMITHING);
		magicStartXP = Skills.getExperience(Skills.MAGIC);
		
		@SuppressWarnings("unused")
		SpiffyGUI kg = new SpiffyGUI();

		if (NATURE_RUNE_STACK_SIZE < 0) {
			log.severe(ERR_NO_EQUIP);
			log.severe(ERR_REQUIREMENTS);
		}
		
	}
	
	public void openBank() {
		NPC banker = NPCs.getNearest(BANK_TO_USE);
		if (banker != null) {
			Point p = banker.getCentralPoint();
			if (p != null) {
				Mouse.hop((int)p.getX(), (int)p.getY());
				Bank.open();
			}
		} else {
			SceneObject bnk = SceneEntities.getNearest(BANK_TO_USE);
			if (bnk != null) {
				Point p = bnk.getCentralPoint();
				if (p != null) {
					Mouse.hop((int)p.getX(), (int)p.getY());
					Bank.open();
				}
			}
		}
	}
	
	public void deposit(int id) {
		Item i = Inventory.getItem(id);
		if (i != null) {
			WidgetChild wc = i.getWidgetChild();
			if (wc != null) {
				Mouse.hop(wc.getAbsoluteX(), wc.getAbsoluteY());
				if (Menu.contains("Deposit-All")) {
					wc.interact("Deposit-All");
					Time.sleep(200);
				} else if (Menu.contains("Deposit")) {
					wc.interact("Deposit");
				} else {
					wc.click(false);
				}
			}
		}
	}
	
	public void withdraw(int id, int amount, int bankslot) {
		WidgetChild wc0 = Widgets.get(762).getChild(95).getChild(bankslot);
		if (wc0 != null) {
			Mouse.hop(wc0.getAbsoluteX(), wc0.getAbsoluteY());
			Bank.withdraw(id, amount);
		}
	}
	
	public void closeBank() {
		WidgetChild wc = Widgets.get(762).getChild(45);
		if (wc != null) {
			Mouse.hop(wc.getAbsoluteX(), wc.getAbsoluteY());
			wc.interact("Close");
		}
	}
	
	class Banking extends Strategy implements Task {

		@Override
		public void run() {
			if (!Bank.isOpen()) {
				openBank();
			} else {
				Item[] its = Inventory.getItems(bankFilter);
				for (int i = 0; i < its.length; i++) {
					deposit(its[i].getId());
					Time.sleep(100);
				}
				
				if (Inventory.getCount(SECONDARY_ID) < COAL_INTERVAL) {
					withdraw(SECONDARY_ID, COAL_INTERVAL, 1);
				}
				
				if (Inventory.getCount(PRIMARY_ID) < PRIMARY_INTERVAL) {
					withdraw(PRIMARY_ID, PRIMARY_INTERVAL, 0);
				}
				
				closeBank();
			}
		}
		
		@Override
		public boolean validate() {
			return USE_SUPERHEAT && Inventory.getCount(PRIMARY_ID) < 1;
		}
	}
	
	class Alching extends Strategy implements Task {

		public void castAlchemyOn(int id) {
			if (Tabs.getCurrent() != Tabs.MAGIC) {
				WidgetChild wic = Widgets.get(548).getChild(96);
				if (wic != null) {
					Mouse.hop(wic.getAbsoluteX(), wic.getAbsoluteY());
					wic.interact("Magic Spellbook");
				}
			} else {
				Widgets.get(192).getChild(59).interact("Cast");
				Item i = Inventory.getItem(id);
				if (i != null) {
					i.getWidgetChild().interact("Cast");
					Time.sleep(100);
					NATURE_RUNE_STACK_SIZE--;
					TOTAL_ALCH_CASTS++;
				}
			}
		}
		public void castSpellOn(int id, int spellID) {
			
			if (Bank.isOpen() && Inventory.getCount(PRIMARY_ID) > 0) {
				closeBank();
			}
			
			if (Tabs.getCurrent() != Tabs.MAGIC) {
				WidgetChild wic = Widgets.get(548).getChild(96);
				if (wic != null) {
					Mouse.hop(wic.getAbsoluteX(), wic.getAbsoluteY());
					wic.interact("Magic Spellbook");
				}
			} else {
				WidgetChild woc = Widgets.get(192).getChild(spellID);
				if (woc != null) {
					Mouse.hop(woc.getAbsoluteX(), woc.getAbsoluteY());
					woc.interact("Cast");
				}
				Item i = Inventory.getItem(1700);
				if (i != null) {
					WidgetChild wc = i.getWidgetChild();
					if (wc != null) {
						Mouse.hop(wc.getAbsoluteX(), wc.getAbsoluteY());
						
						if (Tabs.getCurrent() != Tabs.INVENTORY) {
							Tabs.INVENTORY.open();
						}
						wc.interact("Cast");
						

						NATURE_RUNE_STACK_SIZE--;
						TOTAL_SUPERHEAT_CASTS++;
					}
				}
			}
		}
		
		public void castSuperheatOn(int id, int spellID) {
			
			if (Bank.isOpen() && Inventory.getCount(PRIMARY_ID) > 0) {
				closeBank();
			}
			
			if (Tabs.getCurrent() != Tabs.MAGIC) {
				WidgetChild wic = Widgets.get(548).getChild(96);
				if (wic != null) {
					Mouse.hop(wic.getAbsoluteX(), wic.getAbsoluteY());
					wic.interact("Magic Spellbook");
				}
			} else {
				WidgetChild woc = Widgets.get(192).getChild(50);
				if (woc != null) {
					Mouse.hop(woc.getAbsoluteX(), woc.getAbsoluteY());
					woc.interact("Cast");
				}
				Item i = Inventory.getItem(1700);
				if (i != null) {
					WidgetChild wc = i.getWidgetChild();
					if (wc != null) {
						Mouse.hop(wc.getAbsoluteX(), wc.getAbsoluteY());
						
						if (Tabs.getCurrent() != Tabs.INVENTORY) {
							Tabs.INVENTORY.open();
						}
						wc.interact("Cast");
						

						NATURE_RUNE_STACK_SIZE--;
						TOTAL_SUPERHEAT_CASTS++;
					}
				}
			}
		}
		
		@Override
		public void run() {
			if (USE_ALCHEMY) {
				log.info("Casting alchemy!");
				castAlchemyOn(CAST_ITEM);
			}
			if (USE_SUPERHEAT) {
				log.info("Casting superheat!");
				castSpellOn(PRIMARY_ID, 50);
			}
		}
		
		@Override
		public boolean validate() {
			if (!hasAlchingEquipment() && !ERR_NO_EQUIPS) {
				log.severe("Out of alching equipment!");
				log.severe("Logging out!");
				@SuppressWarnings("unused")
				EndGUI eg = new EndGUI();
				Game.logout(true);
				ERR_NO_EQUIPS = true;
				return false;
			}
			if (USE_SUPERHEAT && USE_ALCHEMY || USE_SUPERHEAT) {
				return true;
			} else if (USE_ALCHEMY) {
				return NATURE_RUNE_STACK_SIZE >= 1;
			} else {
				return false;
			}
		}
	}

	@SuppressWarnings("serial")
	class EndGUI extends JFrame {
		
		public EndGUI() {
			
			setVisible(true);
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("KingAlcherV1.0");
			setBounds(new Rectangle(300, 300));	
			
			JLabel end = new JLabel("Out of equipment! Thanks for alching with the King.");
			end.setBounds(new Rectangle(100, 100));
			end.setLocation(new Point(100, 100));
			add(end);
		}
	}
	
	@SuppressWarnings("serial")
	class SpiffyGUI extends JFrame {
		
		public SpiffyGUI() {
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("KingAlcherV1.0");
			setBounds(new Rectangle(300, 300));	
			
			JPanel jp = new JPanel();
			jp.setLayout(null);
			
			final JComboBox barSelect = new JComboBox();
			barSelect.setVisible(true);
			barSelect.setBounds(new Rectangle(150, 25));
			barSelect.setLocation(new Point(10, 30));
			barSelect.setEditable(false);
			barSelect.setModel(new DefaultComboBoxModel(new BARS[] {
					BARS.ADDY, BARS.SILVER, BARS.GOLD, BARS.STEEL, BARS.MITH, BARS.IRON, BARS.RUNE, BARS.BRONZE
			}));
			jp.add(barSelect);
			
			final JLabel priceLabel = new JLabel("Nature price: ");
			priceLabel.setVisible(true);
			priceLabel.setBounds(new Rectangle(150, 25));
			priceLabel.setLocation(new Point(10, 50));
			jp.add(priceLabel);
			
			final JTextField naturePrice = new JTextField("125");
			naturePrice.setVisible(true);
			naturePrice.setBounds(new Rectangle(150, 25));
			naturePrice.setLocation(new Point(10, 70));
			jp.add(naturePrice);
			
			final JCheckBox superheat = new JCheckBox("Superheat");
			superheat.setVisible(true);
			superheat.setBounds(new Rectangle(100, 25));
			superheat.setLocation(new Point(70, 5));
			jp.add(superheat);
			
			final JCheckBox alchemy = new JCheckBox("Alchemy");
			alchemy.setVisible(true);
			alchemy.setBounds(new Rectangle(150, 25));
			alchemy.setLocation(new Point(200, 5));
			jp.add(alchemy);
			
			
			
			class ListItem {
				
				private String s = null;
				private String id = null;
				
				public ListItem(String name, String id) {
					this.s = name;
					this.id = id;
				}
				
				@Override
				public String toString() {
					return s+"["+id+"]";
				}
				
				public int getId() {
					return Integer.parseInt(id);
				}
			}
			
			final ListItem[] listItems = new ListItem[29];
			int listPos = 0;
			final DefaultListModel iii = new DefaultListModel();
			Item[] its = Inventory.getItems();
			for (int i = 0; i < its.length; i++) {
				if (its[i] != null) {
					Item z = its[i];
					String s = z.getName();
					String k = z.getId()+"";
					
					ListItem li = new ListItem(s, k);
					listItems[listPos++] = li;
					iii.addElement(li);
				}
			}
			JScrollPane jsp = new JScrollPane();
			jsp.setBounds(10, 110, 280, 150);
			jp.add(jsp);
			final JList itemList = new JList(iii);
			jsp.setViewportView(itemList);
			jsp.setColumnHeaderView(new JLabel("Inventory Items"));
			
			
			class SwingAction extends AbstractAction {

				public SwingAction() {
					putValue(NAME, "Start!");
				}
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					if (superheat.isSelected()) {
						USE_SUPERHEAT = true;
					}
					if (alchemy.isSelected()) {
						USE_ALCHEMY = true;
					}
					
					
					int k = itemList.getSelectedIndex();
					if (k > -1) {
						CAST_ITEM = listItems[k].getId();
					}
					NATURE_RUNE_COST = Integer.parseInt(naturePrice.getText());
					
					if (NATURE_RUNE_COST < 0) {
						log.severe("Please enter a price for nature runes.");
					}
					
					depositables[0] = 561;
					if (USE_SUPERHEAT) {
						MAGIC_XP_PER_CAST = 59;
						SMITHING_XP_PER_CAST = getExperience((BARS)barSelect.getSelectedItem());
					} 
					if (USE_ALCHEMY) {
						depositables[1] = CAST_ITEM;
						MAGIC_XP_PER_CAST = 55;
					}
					
					MoveBox mb = new MoveBox();
					Strategy mbs = new Strategy(mb, mb);
					provide(mbs);
					
					Alching a = new Alching();
					Strategy as = new Strategy(a, a);
					provide(as);
					
					a.setSync(true);
					a.setLock(false);
//					
//					Banking b = new Banking();
//					Strategy bs = new Strategy(b, b);
//					provide(bs);
					
					a.setSync(true);
					a.setLock(false);
					
					GUI_DONE = true;
					dispose();
				}
				
			}
			
			Action startScript = new SwingAction();

			JButton btnNewButton = new JButton("Alch!");
			btnNewButton.setAction(startScript);
			btnNewButton.setBounds(180, 50, 100, 25);
			jp.add(btnNewButton);
			add(jp);
		}
	}

	public int getExperience(BARS bar)  {
		switch (bar) {
		case BRONZE:
			PRIMARY_ID = 438;
			PRIMARY_INTERVAL = 12;
			COAL_INTERVAL = 12;
			SECONDARY_ID = 436;
			return 7;
		case IRON:
			PRIMARY_ID = 440;
			COAL_INTERVAL = 0;
			PRIMARY_INTERVAL = 28;
			return 13;
		case GOLD://Gold
			PRIMARY_ID = 444;
			COAL_INTERVAL = 0;
			PRIMARY_INTERVAL = 28;
			return 23;
		case SILVER://Silver
			PRIMARY_ID = 442;
			COAL_INTERVAL = 0;
			PRIMARY_INTERVAL = 28;
			return 14;
		case STEEL://Steel/iron
			PRIMARY_ID = 440;
			COAL_INTERVAL = 20;
			PRIMARY_INTERVAL = 8;
			SECONDARY_ID = 453;
			return 18;
		case MITH://Mith
			PRIMARY_ID = 447;
			COAL_INTERVAL = 20;
			PRIMARY_INTERVAL = 5;
			SECONDARY_ID = 453;
			return 30;
		case ADDY://Addy
			PRIMARY_ID = 449;
			COAL_INTERVAL = 24;
			PRIMARY_INTERVAL = 4;
			SECONDARY_ID = 453;
			return 38;
		case RUNE://Rune
			PRIMARY_ID = 451;
			COAL_INTERVAL = 24;
			PRIMARY_INTERVAL = 3;
			SECONDARY_ID = 453;
			return 50;
		}
		
		return 0;
	}
	
	@Override
	public void onRepaint(Graphics g) {
		if (GUI_DONE) {
			
			Graphics2D g2d = (Graphics2D) g;
			drawMouseCursor(g2d);
			movePaintBox(g2d, PAINT_X, PAINT_Y);
		}
	}

	public void drawTotalCasts(Graphics2D g,String totalCasts, int x, int y) {
		g.drawString("Total casts: "+totalCasts, x+295, y+80);
	}
	
	public void drawCastsPerHour(Graphics2D g,String castsPerHour,  int x, int y) {
		g.drawString("Casts/hr: " + castsPerHour, x+455, y+15);
	}
	
	public void drawCastsRemaining(Graphics2D g,String castsRemaining, int x, int y) {
		g.drawString("Casts remaining: " +castsRemaining, x+295, y+87);
	}
	
	public void drawMouseCursor(Graphics2D g) {
		int x = Mouse.getX();
		int y = Mouse.getY();

		g.setColor(Color.black);
		g.drawLine(0, y, 800, y);
		g.drawLine(x, 0, x, 800);
	}
	
	public String castsPerHour() {
		int gold = TOTAL_ALCH_CASTS + TOTAL_SUPERHEAT_CASTS;
		double seconds = (double) (((System.currentTimeMillis() - runTimeMs) / 1000));
		if (seconds != 0) {
			float gpPerHour = (float) (gold / seconds);

			gpPerHour = gpPerHour * 60 * 60;
			return gpPerHour + "";
		}
		return "";
	}

	public void movePaintBox(Graphics2D g, int x, int y) {

		if (GUI_DONE) {
			if (!HIDEBOX) {
				g.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
				drawRectangle(g, x, y);
				g.setColor(Color.BLACK);

				
				drawCastsPerHour(g, castsPerHour()+"", x -390, y+83);
				drawCastsRemaining(g, NATURE_RUNE_STACK_SIZE+"", x-230, y+20);
				drawTotalCasts(g, (TOTAL_ALCH_CASTS+TOTAL_SUPERHEAT_CASTS)+"", x-230, y+38);
				
				drawRunTime(g, timeFormat(), x - 390, y + 43);
				drawGoldPerHour(g, gpPerHour() + "gp/hr", x - 230, y + 43);
				drawSmithingXpPerHour(g, smithingXpPerHour() + "xp/hr",
						x - 230, y + 63);
				drawMagicXpPerHour(g, magicXpPerHour() + "xp/hr", x - 230,
						y + 73);

				drawTotalGold(g, totalGold(), x - 110, y + 63);
				drawTotalSmithing(g, totalSmithingXp(), x - 110, y + 83);
				drawTotalMagic(g, totalMagicXp(), x - 110, y + 93);

				drawSmithingProgress(g, x + 180, y + PBAR_HEIGHT - 10);
				drawMagicProgress(g, x + 180, y + 3 * PBAR_HEIGHT - 30);
			}
			drawMoveBox(g, x - 30, y + 50);
			drawHideBox(g, x - 30, y + 50);
			drawHide(g);
			drawMove(g);
		}
	}
	
	public void drawTotalGold(Graphics2D g, String totalGold, int x, int y) {
		g.drawString("Total Gold: " + totalGold, x + 455, y + 24);
	}
	
	public String totalGold() {
		return TOTAL_ALCH_CASTS * GOLD_PER_CAST +"";
	}
	
	public void drawGoldPerHour(Graphics2D g, String goldPerHour, int x, int y) {
		g.drawString("Gold: " + goldPerHour, x + 455, y + 45);
	}
	
	public String gpPerHour() {
		int gold = TOTAL_ALCH_CASTS* GOLD_PER_CAST;
		double seconds = (double) (((System.currentTimeMillis() - runTimeMs) / 1000));
		if (seconds != 0) {
			float gpPerHour = (float) (gold / seconds);

			gpPerHour = gpPerHour * 60 * 60;
			return gpPerHour + "";
		}
		return "";
	}
	
	public String timeFormat() {
		Long currTime = System.currentTimeMillis();

		long aMs = currTime - runTimeMs;

		String hours = (int) (aMs / 3600000) % 24 + "";
		String minutes = (int) (aMs / 60000) % 60 + "";
		String seconds = (int) (aMs / 1000) % 60 + "";

		if (hours.length() == 1) {
			hours = "0" + hours;
		}

		if (minutes.length() == 1) {
			minutes = "0" + minutes;
		}

		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}

		return hours + ":" + minutes + ":" + seconds;
	}
	
	public String smithingXpPerHour() {
		int totalXp = SMITHING_XP_PER_CAST * TOTAL_SUPERHEAT_CASTS;
		Long currTime = System.currentTimeMillis();
		Long overall = currTime - runTimeMs;
		double seconds = (double) ((overall / 1000));
		if (seconds != 0) {
			float xpPerHour = (float) (totalXp / seconds);
			xpPerHour = xpPerHour * 60 * 60;
			return xpPerHour + "";
		}
		return "";
	}

	public String magicXpPerHour() {
		int totalXp = MAGIC_XP_PER_CAST * (TOTAL_ALCH_CASTS+TOTAL_SUPERHEAT_CASTS);
		Long currTime = System.currentTimeMillis();
		Long overall = currTime - runTimeMs;
		double seconds = (double) ((overall / 1000));
		if (seconds != 0) {
			float xpPerHour = (float) (totalXp / seconds);
			xpPerHour = xpPerHour * 60 * 60;
			return xpPerHour + "";
		}
		return "";
	}
	
	public String totalSmithingXp() {
		int curr = Skills.getExperience(Skills.SMITHING);
		return curr - smithingStartXP + "";
	}

	public String totalMagicXp() {
		int curr = Skills.getExperience(Skills.MAGIC);
		return curr - magicStartXP + "";
	}
	
	public void drawHideBox(Graphics2D g, int x, int y) {
		HIDEBOX_X = x + 521;
		HIDEBOX_Y = y - 52;

		g.setColor(Color.red);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
		g.fillRoundRect(x + 521, y + 15, 20, (RECT_HEIGHT + 3) / 2, 5, 5);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g.setColor(Color.green);

	}
	
	public void drawHide(Graphics2D g) {
		g.setColor(Color.black);
		g.translate(HIDEBOX_X + 5, HIDEBOX_Y + 85);
		g.rotate(Math.PI / 2);
		g.setFont(new Font("Arial", 10, 15));
		g.drawString("Hide", 0, 0);
	}

	public void drawMove(Graphics2D g) {
		g.drawString("Move", -65, 0);
		
	}
	
	public double smithingPercentToLevel() {
		int base = Skills.XP_TABLE[Skills.getLevel(Skills.SMITHING)];
		int roof = Skills.XP_TABLE[Skills.getLevel(Skills.SMITHING) + 1];
		int curr = Skills.getExperience(Skills.SMITHING);

		double gap = roof - base;
		double real = curr - base;

		return (int) ((real / gap) * 100);

	}

	public double magicPercentToLevel() {
		int base = Skills.XP_TABLE[Skills.getLevel(Skills.MAGIC)];
		int roof = Skills.XP_TABLE[Skills.getLevel(Skills.MAGIC) + 1];
		int curr = Skills.getExperience(Skills.MAGIC);

		double gap = roof - base;
		double real = curr - base;

		return (int) ((real / gap) * 100);

	}
	
	public void drawSmithingProgress(Graphics2D g, int x, int y) {
		g.setColor(Color.darkGray);
		g.drawRoundRect(x, y, PBAR_WIDTH, PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
		g.fillRoundRect(x, y,
				(int) (PBAR_WIDTH * (smithingPercentToLevel() / 100)),
				PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
		g.setColor(Color.black);
		g.drawString("Smithing: " + smithingPercentToLevel() + "%", x + 55,
				y + 15);
	}

	public void drawMagicProgress(Graphics2D g, int x, int y) {
		g.setColor(Color.darkGray);
		g.drawRoundRect(x, y, PBAR_WIDTH, PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
		g.fillRoundRect(x, y,
				(int) (PBAR_WIDTH * (magicPercentToLevel() / 100)),
				PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
		g.setColor(Color.black);
		g.drawString("Magic: " + magicPercentToLevel() + "%", x + 55, y + 15);
	}
	
	public void drawRunTime(Graphics2D g, String runTime, int x, int y) {
		g.drawString("Time: " + runTime, x + 455, y + 45);
	}
	

	public void drawMagicXpPerHour(Graphics2D g, String xpPerHour, int x, int y) {
		g.drawString("Magic: " + xpPerHour, x + 455, y + 45);
	}

	public void drawSmithingXpPerHour(Graphics2D g, String xpPerHour, int x,
			int y) {
		g.drawString("Smithing: " + xpPerHour, x + 455, y + 45);
	}
	
	public void drawTotalMagic(Graphics2D g, String totalMagic, int x, int y) {
		g.drawString("Total MagicXp: " + totalMagic, x + 455, y + 24);
	}

	public void drawTotalSmithing(Graphics2D g, String totalSmithing, int x,
			int y) {
		g.drawString("Total SmithingXp: " + totalSmithing, x + 455, y + 24);
	}
	
	public void drawRectangle(Graphics2D g, int x, int y) {
		Color green = Color.GREEN;
		g.setColor(green);
		g.drawRoundRect(x, y, RECT_WIDTH, RECT_HEIGHT, 10, 10);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f));
		g.fillRoundRect(x, y, RECT_WIDTH, RECT_HEIGHT, 10, 10);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}


	public void drawMoveBox(Graphics2D g, int x, int y) {
		MOVEBOX_X = x + 521;
		MOVEBOX_Y = y - 52;

		g.setColor(Color.red);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
		g.fillRoundRect(x + 521, y - 52, 20, (RECT_HEIGHT + 3) / 2, 5, 5);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g.setColor(Color.green);

	}
	
	/**
	 * 
	 * Moves the paint
	 * 
	 * Author:KingJelly
	 * 
	 */
	private class MoveBox extends Strategy implements Task {

		@Override
		public boolean validate() {
			return MOVEBOX;
		}

		@Override
		public void run() {
			log.info("Moving box..");
			PAINT_X = Mouse.getX() - 495;
			PAINT_Y = Mouse.getY() - 20;
		}
	}

	public boolean mouseInMoveArea(int x, int y) {
		return x > MOVEBOX_X && x < MOVEBOX_X + 20 && y > MOVEBOX_Y
				&& y < MOVEBOX_Y + (RECT_HEIGHT / 2);
	}

	public boolean mouseInHideArea(int x, int y) {
		return x > HIDEBOX_X && x < HIDEBOX_X + 20
				&& y > HIDEBOX_Y + (RECT_HEIGHT / 2)
				&& y < HIDEBOX_Y + (RECT_HEIGHT);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Point mouseP = arg0.getPoint();
		int x = (int) mouseP.getX();
		int y = (int) mouseP.getY();
		if (mouseInMoveArea(x, y)) {
			log.info("In move area");
			if (!MOVEBOX) {
				log.info("Moving box");
				MOVEBOX = true;
			}
		}
		if (mouseInHideArea(x, y)) {
			log.info("In hide area");
			if (HIDEBOX) {
				HIDEBOX = false;

			} else if (!HIDEBOX) {
				HIDEBOX = true;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		MOVEBOX = false;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		String msg = arg0.getMessage();
		
		
		if (msg.contains("coins have been added to your money pouch") && !ALCH_PRICE_SET) {
			String coinCount = msg.substring(0, msg.indexOf(' '));
			GOLD_PER_CAST = Integer.parseInt(coinCount) - NATURE_RUNE_COST;
			ALCH_PRICE_SET = true;
		}
		
	}
}

