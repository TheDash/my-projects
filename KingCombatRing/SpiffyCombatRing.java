import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.event.listener.PaintListener;


@Manifest(authors = "Spiffy", name = "SpiffyCombatRing", version = 1.00, description = "Spiffy ain't it.")
public class SpiffyCombatRing extends ActiveScript implements PaintListener, MouseListener {

	
	enum FOOD {
		TROUT, SALMON, TUNA, LOBSTER, SWORDFISH, PIKE, COOKED_CHICKEN, BASS, POTATO_WITH_CHEESE, SHARK, CAVEFISH, SEA_TURTLE, MANTA_RAY, TUNA_POTATO, ROCKTAIL, BARON_SHARK, JUJU_GUMBO
	}
	
	private boolean GUI_DONE =false;
	private final Long RUN_TIME_MS = System.currentTimeMillis();
	private String oppName = "";

	private int startDEF;
	private int startSTR;
	private int startATK;
	private int startHP;
	private int startRNG;
	private int startMAG;
	
	private Filter<Player> nearest = new Filter<Player>() {

		@Override
		public boolean accept(Player arg0) {
			return arg0.getName().equals(oppName);
		}
		
	};
	
	class SpiffyGUI extends JFrame {
		
		public SpiffyGUI() {
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("SpiffyAlcherV1.0");
			setBounds(new Rectangle(270, 90));	
			
			JPanel jp = new JPanel();
			jp.setLayout(null);
			
			final JTextField pName = new JTextField("Player to attack");
			pName.setSize(new Dimension(120, 30));
			pName.setVisible(true);
			pName.setLocation(new Point(10, 10));
			jp.add(pName);
			
			
			class SwingAction extends AbstractAction {

				public SwingAction() {
					putValue(NAME, "Start!");
				}
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					oppName = pName.getText();
					
					PlayerController p1c = new PlayerController();
					Strategy p1cs = new Strategy(p1c, p1c);
					provide(p1cs);
					
					GUI_DONE = true;
					dispose();
				}
				
			}
			
			Action startScript = new SwingAction();

			JButton btnNewButton = new JButton("Alch!");
			btnNewButton.setAction(startScript);
			btnNewButton.setBounds(150, 10, 100, 25);
			jp.add(btnNewButton);
			add(jp);
		}
	}
	
	@Override
	protected void setup() {
		
		startDEF = Skills.getExperience(Skills.DEFENSE);
		startATK = Skills.getExperience(Skills.ATTACK);
		startSTR = Skills.getExperience(Skills.STRENGTH);
		startRNG = Skills.getExperience(Skills.RANGE);
		startMAG = Skills.getExperience(Skills.MAGIC);
		startHP = Skills.getExperience(Skills.CONSTITUTION);
		
		SpiffyGUI kg = new SpiffyGUI();
		
		MoveBox mb = new MoveBox();
		Strategy mbs = new Strategy(mb, mb);
		provide(mbs);
		log.info("Setting paint up.");
	}
	
	class PlayerController extends Strategy implements Task {

		private void jumpInRing() {
			SceneEntities.getNearest(13137).interact("Climb-Over");
		}
		
		
		private boolean isOutsideRing() {
			Tile loc = Players.getLocal().getLocation();
			SceneObject objs = SceneEntities.getAt(loc);
			int id = objs.getId();
			return (id == 13137 || id == 13098 || id == 13099);
		}
		
		@Override
		public void run() {
			if (isOutsideRing()) {
				log.info("outside ring.");
				jumpInRing();
			} 
			if (!isOutsideRing()) {
				Players.getNearest(nearest).interact("Attack");
			}
		}
		
		@Override
		public boolean validate() {
			return true;
		}
		
	}
	
	public void useSpecialAttack() {
		if (Tabs.getCurrent() != Tabs.ATTACK) {
			Tabs.ATTACK.open();
		}
		
		if (Tabs.getCurrent() == Tabs.ATTACK) {
			if ((parseSpecPercent(Widgets.get(884).getChild(35).getChild(0).getText())) > 0 && 
					Widgets.get(884).getChild(35).getChild(4).getTextColor() != 16750623) {
				WidgetChild wc= Widgets.get(884).getChild(30);
				for (int i = 0; i < 4; i++) {
					wc.interact("Toggle Special Attack");
					Time.sleep(3500);
				}
			}
			
		}
	}
	
	public int parseSpecPercent(String specBar) {
		int brk1 = specBar.indexOf("(");
		int brk2 = specBar.indexOf(")");
		String num = specBar.substring(brk1+1, brk2-1);
		return Integer.parseInt(num);
		
	}

	public void switchToSecondary(int id) {
		Inventory.getItem(id).getWidgetChild().interact("Wield");
	}
	
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
		
		
		private boolean HIDEBOX = false;
		private boolean MOVEBOX = false;
		
		public void drawTotal(Graphics2D g,String total, int x, int y) {
			g.drawString(total, x, y);
		}
		
		public void drawMouseCursor(Graphics2D g, Color c) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			g.setColor(c);
			g.drawLine(0, y, 800, y);
			g.drawLine(x, 0, x, 800);
		}
		
		public void drawPerHour(Graphics2D g, String perHour, int x, int y) {
			g.drawString(perHour, x + 455, y + 45);
		}

		
		public void drawHideBox(Graphics2D g, int x, int y, Color c) {
			HIDEBOX_X = x + 521;
			HIDEBOX_Y = y - 52;
			
			g.setColor(c);
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

		public void drawProgress(Graphics2D g,double percent,  int x, int y, Color c) {
			g.setColor(c);
			g.drawRoundRect(x, y, PBAR_WIDTH, PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.fillRoundRect(x, y,
					(int) (PBAR_WIDTH * (percent / 100)),
					PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
			g.setColor(Color.black);
			g.drawString("Smithing: " + percent+ "%", x + 55,
					y + 15);
		}
		
		public void drawRunTime(Graphics2D g, String runTime, int x, int y) {
			g.drawString("Time: " + runTime, x + 455, y + 45);
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
		public String perHour(int number, String msg) {
			double seconds = (double) (((System.currentTimeMillis() - RUN_TIME_MS) / 1000));
			if (seconds != 0) {
				float gpPerHour = (float) (number / seconds);
				
				gpPerHour = gpPerHour * 60 * 60;
				return gpPerHour + " " +msg;
			}
			return " PER HOUR";
		}
		
		public void movePaintBox(Graphics2D g, int x, int y) {
			
				if (!HIDEBOX) {
					g.setFont(new Font("Arial", Font.TRUETYPE_FONT, 10));
					drawRectangle(g, x, y);
					g.setColor(Color.BLACK);
					
					drawPerHour(g, perHour((Skills.getExperience(Skills.MAGIC) - startMAG), "Magic "), x-200, y);
					drawPerHour(g, perHour((Skills.getExperience(Skills.RANGE) - startRNG), "Range "), x-200, y+10);
					drawPerHour(g, perHour((Skills.getExperience(Skills.DEFENSE) - startDEF), "Defense: "), x-200, y+20);
					drawPerHour(g, perHour((Skills.getExperience(Skills.ATTACK) - startATK), "Attack: "), x-200, y+30);
					drawPerHour(g, perHour((Skills.getExperience(Skills.STRENGTH) - startSTR), "Strength: "), x-200, y+40);
					drawPerHour(g, perHour((Skills.getExperience(Skills.CONSTITUTION) - startHP), "HP: " ), x-200, y+50);
				}
				drawMoveBox(g, x - 30, y + 50);
				drawHideBox(g, x - 30, y + 50, Color.red);
				drawHide(g);
				drawMove(g);
		}
		
		public String timeFormat() {
			Long currTime = System.currentTimeMillis();

			long aMs = currTime - RUN_TIME_MS;

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
		
		public String totalXp(int skill, int startXP) {
			int curr = Skills.getExperience(skill);
			return curr - startXP + "";
		}

		
		public double percentToLevel(int skill) {
			int base = Skills.XP_TABLE[Skills.getLevel(skill)];
			int roof = Skills.XP_TABLE[Skills.getLevel(skill) + 1];
			int curr = Skills.getExperience(skill);

			double gap = roof - base;
			double real = curr - base;

			return (int) ((real / gap) * 100);

		}
		
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
		public void onRepaint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				drawMouseCursor(g2d, Color.black);
				g2d.setColor(Color.black);
				movePaintBox(g2d, PAINT_X, PAINT_Y);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			MOVEBOX = false;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}


}
