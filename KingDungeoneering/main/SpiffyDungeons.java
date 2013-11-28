package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import libs.ItemListInterface;
import objectives.BossObjective;
import objectives.DoorObjective;
import objectives.DungeonObjective;
import objectives.GuardianObjective;
import objectives.KeyObjective;
import objectives.SkillObjective;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.event.listener.PaintListener;

import rooms.DGRoom;

import doors.BasicDoor;
import doors.BossDoor;
import doors.DGDoor;
import doors.DGKey;
import doors.GuardianDoor;
import doors.KeyDoor;
import doors.SkillDoor;
import doors.UnknownDoor;
import dungeon.DGDungeon;
import dungeon.DGDungeon;
@Manifest(authors = "Spiffy", name = "SpiffyDungeons", version = 1.00, description = "Spiffy ain't it?")
public class SpiffyDungeons extends ActiveScript implements PaintListener,
		MouseListener {

	
	private static final DGDungeon currentDungeon = new DGDungeon();
	
	private static Graphics2D g2d;
	
	private boolean GUI_DONE = false;
	private final Long RUN_TIME_MS = System.currentTimeMillis();

	private final String SPC = " ";
	private static String status = "Starting..";
	private static String objective = "No objective set.";
	public final static ItemListInterface db = new ItemListInterface(false);

	private static int nrstObj = 0;
	private static int CURRENT_FLOOR = 28;
	private static int CURR_CPLXY = 1;

	@Override
	protected void setup() {
		SpiffyGUI kg = new SpiffyGUI();
		KeyChain kc = new KeyChain(currentDungeon);
		
		DungeonManager dm = new DungeonManager();
		Strategy dms = new Strategy(dm, dm);
		provide(dms);
		
		dm.setLock(false);
		dm.setSync(true);
		
		ObjectiveSolver os = new ObjectiveSolver();
		Strategy oss = new Strategy(os, os);
		provide(oss);
		
		os.setSync(true);
		os.setLock(false);
		
		LocationManager lm = new LocationManager();
		Strategy lms = new Strategy(lm, lm);
		provide(lms);
	}

	public boolean nrObjs(final Filter<SceneObject> objs, final Tile loc) {
		SceneObject obj = SceneEntities.getNearest(objs);
		if (obj != null) {
			if (Calculations.distance(obj.getLocation(), loc) <= 5) {
				nrstObj = obj.getId();
				return true;
			}
		}
		return false;
	}

	public void intrObj(int id, String option) {
		SceneObject obj = SceneEntities.getNearest(id);
		if (obj != null) {
			if (!obj.isOnScreen()) {
				Walking.walk(obj.getLocation());
				Camera.turnTo(obj);
			}
			obj.interact(option);
		}
	}

	public void invIntr(int id, String opt) {
		Item i = Inventory.getItem(id);
		if (i != null) {
			i.getWidgetChild().interact(opt);
		}
	}

	public boolean wigExist(int id) {
		return Widgets.get(id) != null;
	}

	public boolean txtChk(String txt) {
		return txt == null || txt.equals(SPC) || txt.length() == 0;
	}

	public void walkNr(Filter<SceneObject> objs) {
		SceneObject obj = SceneEntities.getNearest(objs);
		if (obj != null) {
			Walking.walk(obj.getLocation());
			Camera.turnTo(obj);
		}
	}

	private boolean inADungeon() {
		return Widgets.get(945).validate();
	}

	/**
	 * 
	 * Should be activated when we're outside of a dungeon. This class will move
	 * us from an end dungeon area to the start of a new dungeon, every action
	 * before we're in a dungeon will be handled by LocationManager.
	 * 
	 * @author Spiffy
	 * 
	 */
	class LocationManager extends Strategy implements Task {

		private final int[] dungEntrs = { 48496 };
		private final int DUNG_RING = 15707;
		private final int DUNG_CPLXY = 938;

		private final String SPC = " ";

		private final String STAT_CFRM_FLR = "Confirming floor.";
		private final String STAT_SCRL = "Scrolling.";
		private final String STAT_FP = "Forming party.";
		private final String STAT_WD = "Walking to dungeon.";
		private final String STAT_ED = "Entering dungeon.";
		private final String STAT_CF = "Current floor is: ";

		private final String INTR_CLMB_DN = "Climb-Down";
		private final String INTR_SEL_CPLXY = "Select-complexity";
		private final String INTR_JOIN_GRP = "Open party interface";
		private final String INTR_CFRM = "Confirm";
		private final String INTR_PO = "Party organiser";
		private final String INTR_FP = "Form-party";

		private Filter<SceneObject> entrDung = new Filter<SceneObject>() {

			@Override
			public boolean accept(SceneObject arg0) {
				for (int i = 0; i < dungEntrs.length; i++) {
					if (arg0.getId() == dungEntrs[i]) {
						return true;
					}
				}
				return false;
			}
		};

		private boolean nearDungEntr() {
			return nrObjs(entrDung, Players.getLocal().getLocation());
		}

		private void goDung(int id, String option) {
			intrObj(id, option);
		}

		private boolean dungTabOpen() {
			Widget w = Widgets.get(939);
			if (w != null) {
				WidgetChild wc = w.getChild(37);
				if (wc != null) {
					return wc.isOnScreen();
				}
			}
			return false;
		}

		private void formParty() {
			Widget w = Widgets.get(939);
			if (w != null) {
				WidgetChild wc = w.getChild(37);
				if (wc != null) {
					wc.interact(INTR_FP);
				}
			}
		}

		private void joinParty() {
			if (!dungTabOpen()) {
				status = "Opening dung tab..";
				invIntr(DUNG_RING, INTR_JOIN_GRP);
				openDungTab();
				Time.sleep(3000);
			}
			if (dungTabOpen()) {
				status = STAT_FP;
				formParty();
			}

		}

		private void openDungTab() {
			WidgetChild wc = Widgets.get(548).getChild(92);
			for (int i = 0; i < wc.getActions().length; i++) {
				if (wc.getActions()[i].equals(INTR_PO)) {
					wc.interact(INTR_PO);
				}
			}
		}

		private boolean inAParty() {
			Widget w = Widgets.get(939);
			if (w != null) {
				WidgetChild wc = w.getChild(59);
				if (wc != null) {
					if (wc.getText() != null) {
						return wc.getText()
								.equals(Players.getLocal().getName())
								&& wc.isOnScreen();
					}
				}
			}
			return false;
		}

		private int getFloorNum() {
			Widget w = Widgets.get(939);
			if (w != null) {
				WidgetChild wc = w.getChild(106);
				if (wc != null) {
					String txt = wc.getText();
					if (txt.trim().length() == 0) {
						txt = txt.trim() + "1";
					}
					return Integer.parseInt(txt);
				}
			}
			return 0;
		}

		private void confirmFloor() {
			Widgets.get(947).getChild(766).interact(INTR_CFRM);
		}

		private int wdwFloorNum() {
			String txt = Widgets.get(947).getChild(765).getText();
			if (txt == null || txt.length() == 0 || txt.equals(SPC)) {
				txt = "1";
			}
			return Integer.parseInt(txt);
		}

		private void setFloor(int id) {

			if (id > 0 && id < 56) {

				int floorNum = id + 47;
				Widget w = Widgets.get(947);
				WidgetChild scroll = w.getChild(47);
				WidgetChild floor = w.getChild(floorNum);
				if (wdwFloorNum() != CURRENT_FLOOR) {
					int y = scroll.getAbsoluteY();
					int hY = y + scroll.getHeight();
					int lY = y;
					int fY = floor.getAbsoluteY();
					if (fY > hY || fY < lY) {
						status = STAT_SCRL;
						Widgets.scroll(floor, scroll);
						status = STAT_CFRM_FLR;
					} else {
						for (int i = 0; i < 5; i++) {
							Mouse.move(Widgets.get(947).getChild(740)
									.getAbsoluteX() + 3, floor.getAbsoluteY());
							Mouse.click(true);
						}
					}
				}
			}
		}

		private int getCplxy() {
			return Integer.parseInt(Widgets.get(938).getChild(42).getText());
		}

		private void setCplxy(int id) {
			if (id > 0 && id < 7) {

				int wc = 0;
				switch (id) {
				case 1:
					wc = 60;
					break;
				case 2:
					wc = 61;
					break;
				case 3:
					wc = 66;
					break;
				case 4:
					wc = 71;
					break;
				case 5:
					wc = 76;
					break;
				case 6:
					wc = 81;
					break;
				}

				Widgets.get(938).getChild(wc).interact(INTR_SEL_CPLXY);
			}
		}

		private void confirmCplxy() {
			Widgets.get(938).getChild(37).interact(INTR_CFRM);
		}

		@Override
		public void run() {

			if (!inAParty()) {
				status = STAT_FP;
				joinParty();
			}

			if (!nearDungEntr() && inAParty()) {
				status = STAT_WD;
				walkNr(entrDung);
			}

			if (nearDungEntr() && inAParty() && !Widgets.get(947).validate()) {
				status = STAT_ED;
				goDung(nrstObj, INTR_CLMB_DN);
				Time.sleep(2000);
			}

			if (getFloorNum() != CURRENT_FLOOR) {

				if (Widgets.get(947).validate()) {
					status = STAT_CF + CURRENT_FLOOR;
					setFloor(CURRENT_FLOOR);
					Time.sleep(1000);
				}

				if (wdwFloorNum() == CURRENT_FLOOR) {
					confirmFloor();
				}
			}

			if (wigExist(DUNG_CPLXY)) {
				if (getCplxy() != CURR_CPLXY) {
					setCplxy(CURR_CPLXY);
				} else {
					confirmCplxy();
				}
			}

		}

		@Override
		public boolean validate() {
			return !inADungeon();
		}

	}
	
	class DungeonManager extends Strategy implements Task {
		
		public DungeonManager() {
			addDoors();
		}
		
		//********************** Door Management ***********************//
		private final String MSG_GDOOR  = "Added Guardian doors.";
		private final String MSG_SDOOR  = "Added Skill doors.";
		private final String MSG_BDOOR  = "Added Basic doors";
		private final String MSG_KDOOR  = "Added Key doors.";
		private final String MSG_BODOOR = "Added Boss doors.";
		private final String MSG_NEWRM  = "Added a room.";
		
		private final String MSG_KEYF = "New key/door relationship found: ";
		private final String CST_DOOR = "door";
		private final String CST_KEY = "key";
		private final String BKT_I = "(";
		private final String BKT_O = ")";

		private HashMap<Tile, DGDoor> doorsOnMap              = new HashMap<Tile, DGDoor>();
		
		private final int[] GDOOR_IDs = { 3543, 3986, 50346, 50347, 50348,
				53949, 55763 };

		private final int[] SDOOR_IDs = { 52381, 52382, 53956, 55739, 55741,
				55742, 55744, 55746, 55750, 55751, 55753, 55754, 55756, 55757,
				55758, 55759, 55760, 50288, 50289, 50294, 50295, 50299, 55300,
				55301, 55305, 55306, 55306, 55307, 55308, 55309, 55310, 55314,
				55315, 55316, 55317, 55318, 55319, 55324, 55325, 55326, 55327,
				55328, 55329, 55330, 55331, 55332, 55333, 55334, 55335, 55336,
				55337, 53972, 53951, 53953, 53954, 53956, 53958, 53960, 53962,
				53963, 53965, 53966, 53967, 53968, 53969, 53970, 53971, 53972,
				50336, 50337 };

		private final HashSet<Integer> keyDoors = new HashSet<Integer>();
		private final HashSet<Integer> guardianDoors = new HashSet<Integer>();
		private final HashSet<Integer> skillDoors = new HashSet<Integer>();
		private final HashSet<Integer> basicDoors = new HashSet<Integer>();
		private final HashSet<Integer> bossDoors = new HashSet<Integer>();
		private final HashSet<Integer> sideDoors = new HashSet<Integer>();
		
		private Filter<SceneObject> newDoors = new Filter<SceneObject>() {

			@Override
			public boolean accept(SceneObject arg0) {
				int id = arg0.getId();
				return !doorsOnMap.containsKey(arg0.getLocation())
						&& (keyDoors.contains(id) || guardianDoors.contains(id)
								|| skillDoors.contains(id)
								|| basicDoors.contains(id) || bossDoors
									.contains(id));
			}

		};

		public void addDoors() {
			
			sideDoors.add(51807);
			sideDoors.add(51808);
			
			for (int i = 0; i < GDOOR_IDs.length; i++) {
				guardianDoors.add(GDOOR_IDs[i]);
			}
			log.info(MSG_GDOOR);

			for (int i = 0; i < SDOOR_IDs.length; i++) {
				skillDoors.add(SDOOR_IDs[i]);
			}
			log.info(MSG_SDOOR);

			bossDoors.add(50350);
			bossDoors.add(50351);
			bossDoors.add(50352);
			bossDoors.add(53950);
			bossDoors.add(55764);
			log.info(MSG_BODOOR);

			basicDoors.add(3);
			basicDoors.add(22);
			basicDoors.add(59);
			basicDoors.add(77);
			basicDoors.add(81);
			basicDoors.add(82);
			basicDoors.add(92);
			basicDoors.add(93);
			basicDoors.add(99);
			basicDoors.add(102);
			basicDoors.add(131);
			basicDoors.add(137);
			basicDoors.add(138);
			basicDoors.add(140);
			basicDoors.add(141);
			basicDoors.add(143);
			basicDoors.add(144);
			basicDoors.add(145);
			basicDoors.add(1239);
			basicDoors.add(1240);
			basicDoors.add(1530);
			basicDoors.add(1531);
			basicDoors.add(1533);
			basicDoors.add(1534);
			basicDoors.add(1536);
			basicDoors.add(1537);
			basicDoors.add(1539);
			basicDoors.add(1540);
			basicDoors.add(1542);
			basicDoors.add(1543);
			basicDoors.add(1544);
			basicDoors.add(1545);
			basicDoors.add(1591);
			basicDoors.add(1804);
			basicDoors.add(1805);
			basicDoors.add(2002);
			basicDoors.add(2034);
			basicDoors.add(2035);
			basicDoors.add(2036);
			basicDoors.add(2037);
			basicDoors.add(2048);
			basicDoors.add(2049);
			basicDoors.add(2054);
			basicDoors.add(2069);
			basicDoors.add(2112);
			basicDoors.add(2184);
			basicDoors.add(2188);
			basicDoors.add(2189);
			basicDoors.add(2192);
			basicDoors.add(2193);
			basicDoors.add(2338);
			basicDoors.add(2373);
			basicDoors.add(2374);
			basicDoors.add(2397);
			basicDoors.add(2398);
			basicDoors.add(2399);
			basicDoors.add(2406);
			basicDoors.add(2427);
			basicDoors.add(2428);
			basicDoors.add(2429);
			basicDoors.add(2430);
			basicDoors.add(2431);
			basicDoors.add(2526);
			basicDoors.add(2527);
			basicDoors.add(2528);
			basicDoors.add(2529);
			basicDoors.add(2537);
			basicDoors.add(2538);
			basicDoors.add(2546);
			basicDoors.add(2547);
			basicDoors.add(2548);
			basicDoors.add(2549);
			basicDoors.add(2554);
			basicDoors.add(2555);
			basicDoors.add(2556);
			basicDoors.add(2557);
			basicDoors.add(2558);
			basicDoors.add(2559);
			basicDoors.add(2595);
			basicDoors.add(2621);
			basicDoors.add(2622);
			basicDoors.add(2624);
			basicDoors.add(2625);
			basicDoors.add(2626);
			basicDoors.add(2627);
			basicDoors.add(2628);
			basicDoors.add(2705);
			basicDoors.add(2706);
			basicDoors.add(2712);
			basicDoors.add(2861);
			basicDoors.add(2862);
			basicDoors.add(2863);
			basicDoors.add(2997);
			basicDoors.add(2998);
			basicDoors.add(3014);
			basicDoors.add(3017);
			basicDoors.add(3018);
			basicDoors.add(3019);
			basicDoors.add(3024);
			basicDoors.add(3025);
			basicDoors.add(3026);
			basicDoors.add(3220);
			basicDoors.add(3221);
			basicDoors.add(3270);
			basicDoors.add(3271);
			basicDoors.add(3333);
			basicDoors.add(3334);
			basicDoors.add(3335);
			basicDoors.add(3336);
			basicDoors.add(3434);
			basicDoors.add(3435);
			basicDoors.add(3724);
			basicDoors.add(3745);
			basicDoors.add(3747);
			basicDoors.add(3776);
			basicDoors.add(3777);
			basicDoors.add(3779);
			basicDoors.add(3782);
			basicDoors.add(4014);
			basicDoors.add(4015);
			basicDoors.add(4148);
			basicDoors.add(4165);
			basicDoors.add(4166);
			basicDoors.add(4247);
			basicDoors.add(4248);
			basicDoors.add(4250);
			basicDoors.add(4251);
			basicDoors.add(4285);
			basicDoors.add(4465);
			basicDoors.add(4466);
			basicDoors.add(4467);
			basicDoors.add(4468);
			basicDoors.add(4487);
			basicDoors.add(4490);
			basicDoors.add(4491);
			basicDoors.add(4492);
			basicDoors.add(4636);
			basicDoors.add(4637);
			basicDoors.add(4638);
			basicDoors.add(4639);
			basicDoors.add(4640);
			basicDoors.add(4696);
			basicDoors.add(4697);
			basicDoors.add(4701);
			basicDoors.add(4962);
			basicDoors.add(5126);
			basicDoors.add(5128);
			basicDoors.add(5172);
			basicDoors.add(5174);
			basicDoors.add(5183);
			basicDoors.add(5186);
			basicDoors.add(5187);
			basicDoors.add(5188);
			basicDoors.add(5244);
			basicDoors.add(5245);
			basicDoors.add(5501);
			basicDoors.add(6100);
			basicDoors.add(6101);
			basicDoors.add(6102);
			basicDoors.add(6103);
			basicDoors.add(6104);
			basicDoors.add(6105);
			basicDoors.add(6106);
			basicDoors.add(6107);
			basicDoors.add(6108);
			basicDoors.add(6109);
			basicDoors.add(6110);
			basicDoors.add(6111);
			basicDoors.add(6112);
			basicDoors.add(6113);
			basicDoors.add(6114);
			basicDoors.add(6115);
			basicDoors.add(6238);
			basicDoors.add(6239);
			basicDoors.add(6240);
			basicDoors.add(6241);
			basicDoors.add(6310);
			basicDoors.add(6363);
			basicDoors.add(6364);
			basicDoors.add(6614);
			basicDoors.add(6624);
			basicDoors.add(6625);
			basicDoors.add(6713);
			basicDoors.add(6714);
			basicDoors.add(6715);
			basicDoors.add(6716);
			basicDoors.add(6717);
			basicDoors.add(6718);
			basicDoors.add(6719);
			basicDoors.add(6720);
			basicDoors.add(6721);
			basicDoors.add(6722);
			basicDoors.add(6723);
			basicDoors.add(6724);
			basicDoors.add(6725);
			basicDoors.add(6726);
			basicDoors.add(6727);
			basicDoors.add(6728);
			basicDoors.add(6729);
			basicDoors.add(6730);
			basicDoors.add(6731);
			basicDoors.add(6732);
			basicDoors.add(6733);
			basicDoors.add(6734);
			basicDoors.add(6735);
			basicDoors.add(6736);
			basicDoors.add(6737);
			basicDoors.add(6738);
			basicDoors.add(6739);
			basicDoors.add(6740);
			basicDoors.add(6741);
			basicDoors.add(6742);
			basicDoors.add(6743);
			basicDoors.add(6744);
			basicDoors.add(6745);
			basicDoors.add(6746);
			basicDoors.add(6747);
			basicDoors.add(6748);
			basicDoors.add(6749);
			basicDoors.add(6750);
			basicDoors.add(6824);
			basicDoors.add(6975);
			basicDoors.add(6976);
			basicDoors.add(7114);
			basicDoors.add(7115);
			basicDoors.add(7127);
			basicDoors.add(7222);
			basicDoors.add(7223);
			basicDoors.add(7232);
			basicDoors.add(7234);
			basicDoors.add(7246);
			basicDoors.add(7259);
			basicDoors.add(7260);
			basicDoors.add(7274);
			basicDoors.add(7302);
			basicDoors.add(7317);
			basicDoors.add(7320);
			basicDoors.add(7323);
			basicDoors.add(7326);
			basicDoors.add(7354);
			basicDoors.add(8695);
			basicDoors.add(8696);
			basicDoors.add(8786);
			basicDoors.add(8787);
			basicDoors.add(8788);
			basicDoors.add(8789);
			basicDoors.add(8790);
			basicDoors.add(8791);
			basicDoors.add(8792);
			basicDoors.add(8793);
			basicDoors.add(8958);
			basicDoors.add(8959);
			basicDoors.add(8960);
			basicDoors.add(8961);
			basicDoors.add(8962);
			basicDoors.add(8963);
			basicDoors.add(8967);
			basicDoors.add(9565);
			basicDoors.add(10260);
			basicDoors.add(10261);
			basicDoors.add(10262);
			basicDoors.add(10263);
			basicDoors.add(10264);
			basicDoors.add(10265);
			basicDoors.add(10325);
			basicDoors.add(10326);
			basicDoors.add(10327);
			basicDoors.add(10361);
			basicDoors.add(10419);
			basicDoors.add(10420);
			basicDoors.add(10421);
			basicDoors.add(10423);
			basicDoors.add(10425);
			basicDoors.add(10427);
			basicDoors.add(10429);
			basicDoors.add(10431);
			basicDoors.add(10527);
			basicDoors.add(10528);
			basicDoors.add(10529);
			basicDoors.add(10530);
			basicDoors.add(11051);
			basicDoors.add(11052);
			basicDoors.add(11053);
			basicDoors.add(11054);
			basicDoors.add(11055);
			basicDoors.add(11056);
			basicDoors.add(11057);
			basicDoors.add(11058);
			basicDoors.add(11064);
			basicDoors.add(11065);
			basicDoors.add(11066);
			basicDoors.add(11067);
			basicDoors.add(11068);
			basicDoors.add(11069);
			basicDoors.add(11070);
			basicDoors.add(11071);
			basicDoors.add(11151);
			basicDoors.add(11152);
			basicDoors.add(11196);
			basicDoors.add(11197);
			basicDoors.add(11458);
			basicDoors.add(11470);
			basicDoors.add(11471);
			basicDoors.add(11503);
			basicDoors.add(11616);
			basicDoors.add(11617);
			basicDoors.add(11707);
			basicDoors.add(11708);
			basicDoors.add(11712);
			basicDoors.add(11713);
			basicDoors.add(11714);
			basicDoors.add(11715);
			basicDoors.add(11993);
			basicDoors.add(11994);
			basicDoors.add(12348);
			basicDoors.add(12444);
			basicDoors.add(12445);
			basicDoors.add(12761);
			basicDoors.add(13001);
			basicDoors.add(13002);
			basicDoors.add(13006);
			basicDoors.add(13007);
			basicDoors.add(13008);
			basicDoors.add(13009);
			basicDoors.add(13015);
			basicDoors.add(13016);
			basicDoors.add(13017);
			basicDoors.add(13018);
			basicDoors.add(13100);
			basicDoors.add(13101);
			basicDoors.add(13102);
			basicDoors.add(13103);
			basicDoors.add(13107);
			basicDoors.add(13108);
			basicDoors.add(13109);
			basicDoors.add(13110);
			basicDoors.add(13118);
			basicDoors.add(13119);
			basicDoors.add(13120);
			basicDoors.add(13121);
			basicDoors.add(13314);
			basicDoors.add(13315);
			basicDoors.add(13317);
			basicDoors.add(13318);
			basicDoors.add(13320);
			basicDoors.add(13321);
			basicDoors.add(13323);
			basicDoors.add(13324);
			basicDoors.add(13326);
			basicDoors.add(13327);
			basicDoors.add(13344);
			basicDoors.add(13345);
			basicDoors.add(13346);
			basicDoors.add(13347);
			basicDoors.add(13348);
			basicDoors.add(13349);
			basicDoors.add(13350);
			basicDoors.add(13351);
			basicDoors.add(13352);
			basicDoors.add(13353);
			basicDoors.add(13354);
			basicDoors.add(13355);
			basicDoors.add(14749);
			basicDoors.add(14750);
			basicDoors.add(14751);
			basicDoors.add(14752);
			basicDoors.add(14753);
			basicDoors.add(14754);
			basicDoors.add(14923);
			basicDoors.add(14924);
			basicDoors.add(14982);
			basicDoors.add(15204);
			basicDoors.add(15205);
			basicDoors.add(15535);
			basicDoors.add(15536);
			basicDoors.add(15641);
			basicDoors.add(15644);
			basicDoors.add(15647);
			basicDoors.add(15650);
			basicDoors.add(15653);
			basicDoors.add(15759);
			basicDoors.add(16343);
			basicDoors.add(16344);
			basicDoors.add(16774);
			basicDoors.add(16776);
			basicDoors.add(16777);
			basicDoors.add(16778);
			basicDoors.add(16779);
			basicDoors.add(16902);
			basicDoors.add(16903);
			basicDoors.add(17114);
			basicDoors.add(17115);
			basicDoors.add(17284);
			basicDoors.add(17316);
			basicDoors.add(17317);
			basicDoors.add(17391);
			basicDoors.add(17488);
			basicDoors.add(17600);
			basicDoors.add(17601);
			basicDoors.add(17973);
			basicDoors.add(18031);
			basicDoors.add(18032);
			basicDoors.add(18047);
			basicDoors.add(18048);
			basicDoors.add(18057);
			basicDoors.add(18058);
			basicDoors.add(18091);
			basicDoors.add(18092);
			basicDoors.add(18168);
			basicDoors.add(18651);
			basicDoors.add(18652);
			basicDoors.add(18653);
			basicDoors.add(18654);
			basicDoors.add(18655);
			basicDoors.add(18656);
			basicDoors.add(18657);
			basicDoors.add(18658);
			basicDoors.add(18659);
			basicDoors.add(18702);
			basicDoors.add(18703);
			basicDoors.add(18964);
			basicDoors.add(18965);
			basicDoors.add(18966);
			basicDoors.add(18967);
			basicDoors.add(18968);
			basicDoors.add(18971);
			basicDoors.add(18973);
			basicDoors.add(18978);
			basicDoors.add(18979);
			basicDoors.add(20199);
			basicDoors.add(20200);
			basicDoors.add(20201);
			basicDoors.add(20202);
			basicDoors.add(20203);
			basicDoors.add(20204);
			basicDoors.add(20205);
			basicDoors.add(20206);
			basicDoors.add(20207);
			basicDoors.add(20208);
			basicDoors.add(20209);
			basicDoors.add(20695);
			basicDoors.add(21065);
			basicDoors.add(21066);
			basicDoors.add(21067);
			basicDoors.add(21068);
			basicDoors.add(21160);
			basicDoors.add(21161);
			basicDoors.add(21162);
			basicDoors.add(21164);
			basicDoors.add(21166);
			basicDoors.add(21167);
			basicDoors.add(21169);
			basicDoors.add(21170);
			basicDoors.add(21171);
			basicDoors.add(21201);
			basicDoors.add(21243);
			basicDoors.add(21340);
			basicDoors.add(21341);
			basicDoors.add(21342);
			basicDoors.add(21343);
			basicDoors.add(21505);
			basicDoors.add(21506);
			basicDoors.add(21507);
			basicDoors.add(21508);
			basicDoors.add(22007);
			basicDoors.add(22113);
			basicDoors.add(22114);
			basicDoors.add(22115);
			basicDoors.add(22116);
			basicDoors.add(22117);
			basicDoors.add(22118);
			basicDoors.add(22119);
			basicDoors.add(22913);
			basicDoors.add(22914);
			basicDoors.add(22920);
			basicDoors.add(22921);
			basicDoors.add(23056);
			basicDoors.add(23119);
			basicDoors.add(23338);
			basicDoors.add(23378);
			basicDoors.add(23379);
			basicDoors.add(23631);
			basicDoors.add(23637);
			basicDoors.add(23640);
			basicDoors.add(24368);
			basicDoors.add(24375);
			basicDoors.add(24376);
			basicDoors.add(24377);
			basicDoors.add(24378);
			basicDoors.add(24379);
			basicDoors.add(24381);
			basicDoors.add(24382);
			basicDoors.add(24383);
			basicDoors.add(24384);
			basicDoors.add(24389);
			basicDoors.add(24565);
			basicDoors.add(24566);
			basicDoors.add(24567);
			basicDoors.add(24568);
			basicDoors.add(24685);
			basicDoors.add(24686);
			basicDoors.add(24759);
			basicDoors.add(24815);
			basicDoors.add(24816);
			basicDoors.add(24930);
			basicDoors.add(24931);
			basicDoors.add(24932);
			basicDoors.add(24933);
			basicDoors.add(24934);
			basicDoors.add(24935);
			basicDoors.add(24936);
			basicDoors.add(25252);
			basicDoors.add(25417);
			basicDoors.add(25418);
			basicDoors.add(25526);
			basicDoors.add(25527);
			basicDoors.add(25642);
			basicDoors.add(25643);
			basicDoors.add(25716);
			basicDoors.add(25717);
			basicDoors.add(25718);
			basicDoors.add(25719);
			basicDoors.add(25799);
			basicDoors.add(25800);
			basicDoors.add(25819);
			basicDoors.add(25820);
			basicDoors.add(25825);
			basicDoors.add(25826);
			basicDoors.add(25827);
			basicDoors.add(25828);
			basicDoors.add(26387);
			basicDoors.add(26787);
			basicDoors.add(26810);
			basicDoors.add(26865);
			basicDoors.add(26866);
			basicDoors.add(26916);
			basicDoors.add(26917);
			basicDoors.add(27332);
			basicDoors.add(27333);
			basicDoors.add(27776);
			basicDoors.add(27777);
			basicDoors.add(27988);
			basicDoors.add(27989);
			basicDoors.add(28407);
			basicDoors.add(28408);
			basicDoors.add(28409);
			basicDoors.add(28410);
			basicDoors.add(28588);
			basicDoors.add(28589);
			basicDoors.add(28590);
			basicDoors.add(29234);
			basicDoors.add(29237);
			basicDoors.add(29238);
			basicDoors.add(29269);
			basicDoors.add(29270);
			basicDoors.add(29273);
			basicDoors.add(29732);
			basicDoors.add(29775);
			basicDoors.add(29776);
			basicDoors.add(29777);
			basicDoors.add(29778);
			basicDoors.add(30503);
			basicDoors.add(30522);
			basicDoors.add(30523);
			basicDoors.add(30524);
			basicDoors.add(30526);
			basicDoors.add(30527);
			basicDoors.add(30864);
			basicDoors.add(30865);
			basicDoors.add(31030);
			basicDoors.add(31340);
			basicDoors.add(31341);
			basicDoors.add(31342);
			basicDoors.add(31343);
			basicDoors.add(31454);
			basicDoors.add(31455);
			basicDoors.add(31533);
			basicDoors.add(31534);
			basicDoors.add(31535);
			basicDoors.add(31539);
			basicDoors.add(31540);
			basicDoors.add(31824);
			basicDoors.add(31825);
			basicDoors.add(31826);
			basicDoors.add(31827);
			basicDoors.add(31829);
			basicDoors.add(31830);
			basicDoors.add(31831);
			basicDoors.add(31832);
			basicDoors.add(31838);
			basicDoors.add(31839);
			basicDoors.add(31841);
			basicDoors.add(31844);
			basicDoors.add(32370);
			basicDoors.add(32406);
			basicDoors.add(32711);
			basicDoors.add(32952);
			basicDoors.add(32953);
			basicDoors.add(33108);
			basicDoors.add(33109);
			basicDoors.add(33607);
			basicDoors.add(33634);
			basicDoors.add(33637);
			basicDoors.add(33654);
			basicDoors.add(33712);
			basicDoors.add(34042);
			basicDoors.add(34043);
			basicDoors.add(34044);
			basicDoors.add(34045);
			basicDoors.add(34046);
			basicDoors.add(34234);
			basicDoors.add(34269);
			basicDoors.add(34288);
			basicDoors.add(34289);
			basicDoors.add(34290);
			basicDoors.add(34291);
			basicDoors.add(34353);
			basicDoors.add(34354);
			basicDoors.add(34807);
			basicDoors.add(34808);
			basicDoors.add(34809);
			basicDoors.add(34810);
			basicDoors.add(34811);
			basicDoors.add(34812);
			basicDoors.add(34813);
			basicDoors.add(34816);
			basicDoors.add(34818);
			basicDoors.add(34819);
			basicDoors.add(34820);
			basicDoors.add(34822);
			basicDoors.add(34823);
			basicDoors.add(34865);
			basicDoors.add(35146);
			basicDoors.add(35273);
			basicDoors.add(35348);
			basicDoors.add(35630);
			basicDoors.add(35631);
			basicDoors.add(35863);
			basicDoors.add(35907);
			basicDoors.add(35991);
			basicDoors.add(35992);
			basicDoors.add(36021);
			basicDoors.add(36022);
			basicDoors.add(36087);
			basicDoors.add(36088);
			basicDoors.add(36089);
			basicDoors.add(36090);
			basicDoors.add(36100);
			basicDoors.add(36101);
			basicDoors.add(36109);
			basicDoors.add(36119);
			basicDoors.add(36315);
			basicDoors.add(36316);
			basicDoors.add(36317);
			basicDoors.add(36318);
			basicDoors.add(36319);
			basicDoors.add(36320);
			basicDoors.add(36321);
			basicDoors.add(36342);
			basicDoors.add(36343);
			basicDoors.add(36344);
			basicDoors.add(36359);
			basicDoors.add(36360);
			basicDoors.add(36590);
			basicDoors.add(36625);
			basicDoors.add(36639);
			basicDoors.add(36649);
			basicDoors.add(36844);
			basicDoors.add(36845);
			basicDoors.add(36846);
			basicDoors.add(36847);
			basicDoors.add(36848);
			basicDoors.add(36862);
			basicDoors.add(36863);
			basicDoors.add(36864);
			basicDoors.add(36865);
			basicDoors.add(37123);
			basicDoors.add(37130);
			basicDoors.add(37133);
			basicDoors.add(37170);
			basicDoors.add(37178);
			basicDoors.add(37181);
			basicDoors.add(37187);
			basicDoors.add(37198);
			basicDoors.add(37199);
			basicDoors.add(37201);
			basicDoors.add(37245);
			basicDoors.add(37246);
			basicDoors.add(37247);
			basicDoors.add(37486);
			basicDoors.add(37738);
			basicDoors.add(37995);
			basicDoors.add(37996);
			basicDoors.add(38123);
			basicDoors.add(38124);
			basicDoors.add(38141);
			basicDoors.add(38142);
			basicDoors.add(38799);
			basicDoors.add(39198);
			basicDoors.add(39199);
			basicDoors.add(39272);
			basicDoors.add(39305);
			basicDoors.add(39306);
			basicDoors.add(39307);
			basicDoors.add(39522);
			basicDoors.add(39854);
			basicDoors.add(39856);
			basicDoors.add(39863);
			basicDoors.add(39901);
			basicDoors.add(39965);
			basicDoors.add(40108);
			basicDoors.add(40109);
			basicDoors.add(40193);
			basicDoors.add(40893);
			basicDoors.add(40894);
			basicDoors.add(40902);
			basicDoors.add(40903);
			basicDoors.add(41123);
			basicDoors.add(41124);
			basicDoors.add(41125);
			basicDoors.add(41126);
			basicDoors.add(41127);
			basicDoors.add(41128);
			basicDoors.add(41129);
			basicDoors.add(41130);
			basicDoors.add(41463);
			basicDoors.add(41464);
			basicDoors.add(41509);
			basicDoors.add(41510);
			basicDoors.add(41522);
			basicDoors.add(41523);
			basicDoors.add(41524);
			basicDoors.add(41526);
			basicDoors.add(41529);
			basicDoors.add(41530);
			basicDoors.add(42480);
			basicDoors.add(42481);
			basicDoors.add(43176);
			basicDoors.add(43177);
			basicDoors.add(43178);
			basicDoors.add(43179);
			basicDoors.add(43180);
			basicDoors.add(43181);
			basicDoors.add(43400);
			basicDoors.add(43401);
			basicDoors.add(43402);
			basicDoors.add(43459);
			basicDoors.add(43460);
			basicDoors.add(43461);
			basicDoors.add(43776);
			basicDoors.add(44211);
			basicDoors.add(44212);
			basicDoors.add(44236);
			basicDoors.add(44957);
			basicDoors.add(44959);
			basicDoors.add(44960);
			basicDoors.add(44961);
			basicDoors.add(45003);
			basicDoors.add(45004);
			basicDoors.add(45018);
			basicDoors.add(45019);
			basicDoors.add(45476);
			basicDoors.add(45477);
			basicDoors.add(45537);
			basicDoors.add(45538);
			basicDoors.add(45539);
			basicDoors.add(45540);
			basicDoors.add(45801);
			basicDoors.add(45853);
			basicDoors.add(45854);
			basicDoors.add(45855);
			basicDoors.add(45964);
			basicDoors.add(45965);
			basicDoors.add(45966);
			basicDoors.add(45967);
			basicDoors.add(46251);
			basicDoors.add(46252);
			basicDoors.add(46305);
			basicDoors.add(46306);
			basicDoors.add(46307);
			basicDoors.add(46417);
			basicDoors.add(47127);
			basicDoors.add(47128);
			basicDoors.add(47377);
			basicDoors.add(47378);
			basicDoors.add(47379);
			basicDoors.add(47380);
			basicDoors.add(47381);
			basicDoors.add(47382);
			basicDoors.add(47383);
			basicDoors.add(47384);
			basicDoors.add(47385);
			basicDoors.add(47386);
			basicDoors.add(47387);
			basicDoors.add(47388);
			basicDoors.add(47389);
			basicDoors.add(47390);
			basicDoors.add(47391);
			basicDoors.add(47392);
			basicDoors.add(47393);
			basicDoors.add(47394);
			basicDoors.add(47395);
			basicDoors.add(47396);
			basicDoors.add(47397);
			basicDoors.add(47398);
			basicDoors.add(47399);
			basicDoors.add(47400);
			basicDoors.add(47401);
			basicDoors.add(47402);
			basicDoors.add(47403);
			basicDoors.add(47443);
			basicDoors.add(47444);
			basicDoors.add(47512);
			basicDoors.add(47513);
			basicDoors.add(48938);
			basicDoors.add(48939);
			basicDoors.add(48940);
			basicDoors.add(48941);
			basicDoors.add(48942);
			basicDoors.add(48943);
			basicDoors.add(48944);
			basicDoors.add(48945);
			basicDoors.add(48961);
			basicDoors.add(48962);
			basicDoors.add(48963);
			basicDoors.add(49306);
			basicDoors.add(49335);
			basicDoors.add(49336);
			basicDoors.add(49337);
			basicDoors.add(49338);
			basicDoors.add(49375);
			basicDoors.add(49376);
			basicDoors.add(49377);
			basicDoors.add(49378);
			basicDoors.add(49379);
			basicDoors.add(49380);
			basicDoors.add(49387);
			basicDoors.add(49388);
			basicDoors.add(49389);
			basicDoors.add(49462);
			basicDoors.add(49463);
			basicDoors.add(49464);
			basicDoors.add(49504);
			basicDoors.add(49505);
			basicDoors.add(49506);
			basicDoors.add(49516);
			basicDoors.add(49517);
			basicDoors.add(49518);
			basicDoors.add(49564);
			basicDoors.add(49565);
			basicDoors.add(49566);
			basicDoors.add(49574);
			basicDoors.add(49575);
			basicDoors.add(49576);
			basicDoors.add(49577);
			basicDoors.add(49578);
			basicDoors.add(49579);
			basicDoors.add(49603);
			basicDoors.add(49604);
			basicDoors.add(49605);
			basicDoors.add(49606);
			basicDoors.add(49607);
			basicDoors.add(49608);
			basicDoors.add(49625);
			basicDoors.add(49626);
			basicDoors.add(49627);
			basicDoors.add(49644);
			basicDoors.add(49645);
			basicDoors.add(49646);
			basicDoors.add(49650);
			basicDoors.add(49651);
			basicDoors.add(49652);
			basicDoors.add(49677);
			basicDoors.add(49678);
			basicDoors.add(49679);
			basicDoors.add(49693);
			basicDoors.add(49694);
			basicDoors.add(50275);
			basicDoors.add(50276);
			basicDoors.add(50277);
			basicDoors.add(50284);
			basicDoors.add(50285);
			basicDoors.add(50286);
			basicDoors.add(50290);
			basicDoors.add(50291);
			basicDoors.add(50292);
			basicDoors.add(50296);
			basicDoors.add(50297);
			basicDoors.add(50298);
			basicDoors.add(50302);
			basicDoors.add(50303);
			basicDoors.add(50304);
			basicDoors.add(50311);
			basicDoors.add(50312);
			basicDoors.add(50313);
			basicDoors.add(50320);
			basicDoors.add(50321);
			basicDoors.add(50322);
			basicDoors.add(50338);
			basicDoors.add(50339);
			basicDoors.add(50340);
			basicDoors.add(50341);
			basicDoors.add(50342);
			basicDoors.add(50343);
			basicDoors.add(50344);
			basicDoors.add(50706);
			basicDoors.add(50707);
			basicDoors.add(50708);
			basicDoors.add(50709);
			basicDoors.add(51256);
			basicDoors.add(51257);
			basicDoors.add(51258);
			basicDoors.add(51259);
			basicDoors.add(51805);
			basicDoors.add(51806);
			basicDoors.add(51807);
			basicDoors.add(51808);
			basicDoors.add(52193);
			basicDoors.add(52199);
			basicDoors.add(52302);
			basicDoors.add(52304);
			basicDoors.add(52312);
			basicDoors.add(52313);
			basicDoors.add(52314);
			basicDoors.add(52315);
			basicDoors.add(52474);
			basicDoors.add(52475);
			basicDoors.add(52610);
			basicDoors.add(52778);
			basicDoors.add(52779);
			basicDoors.add(52780);
			basicDoors.add(52781);
			basicDoors.add(53577);
			basicDoors.add(53651);
			basicDoors.add(53671);
			basicDoors.add(53672);
			basicDoors.add(53673);
			basicDoors.add(53674);
			basicDoors.add(53675);
			basicDoors.add(53948);
			basicDoors.add(53952);
			basicDoors.add(53955);
			basicDoors.add(53957);
			basicDoors.add(53959);
			basicDoors.add(53961);
			basicDoors.add(53964);
			basicDoors.add(53967);
			basicDoors.add(53973);
			basicDoors.add(53987);
			basicDoors.add(53988);
			basicDoors.add(53989);
			basicDoors.add(53990);
			basicDoors.add(53992);
			basicDoors.add(54000);
			basicDoors.add(54001);
			basicDoors.add(54006);
			basicDoors.add(54067);
			basicDoors.add(54070);
			basicDoors.add(54071);
			basicDoors.add(54072);
			basicDoors.add(54073);
			basicDoors.add(54106);
			basicDoors.add(54107);
			basicDoors.add(54108);
			basicDoors.add(54109);
			basicDoors.add(54236);
			basicDoors.add(54274);
			basicDoors.add(54284);
			basicDoors.add(54299);
			basicDoors.add(54300);
			basicDoors.add(54315);
			basicDoors.add(54316);
			basicDoors.add(54317);
			basicDoors.add(54318);
			basicDoors.add(54319);
			basicDoors.add(54320);
			basicDoors.add(54335);
			basicDoors.add(54360);
			basicDoors.add(54361);
			basicDoors.add(54362);
			basicDoors.add(54363);
			basicDoors.add(54404);
			basicDoors.add(54417);
			basicDoors.add(54418);
			basicDoors.add(54620);
			basicDoors.add(54767);
			basicDoors.add(54768);
			basicDoors.add(54769);
			basicDoors.add(54770);
			basicDoors.add(55310);
			basicDoors.add(55321);
			basicDoors.add(55322);
			basicDoors.add(55323);
			basicDoors.add(55324);
			basicDoors.add(55444);
			basicDoors.add(55445);
			basicDoors.add(55478);
			basicDoors.add(55479);
			basicDoors.add(55480);
			basicDoors.add(55481);
			basicDoors.add(55482);
			basicDoors.add(55740);
			basicDoors.add(55743);
			basicDoors.add(55745);
			basicDoors.add(55747);
			basicDoors.add(55749);
			basicDoors.add(55752);
			basicDoors.add(55755);
			basicDoors.add(55761);
			basicDoors.add(55762);
			basicDoors.add(56079);
			basicDoors.add(56080);
			basicDoors.add(56081);
			basicDoors.add(56082);
			basicDoors.add(56267);
			basicDoors.add(56526);
			basicDoors.add(56527);
			basicDoors.add(56528);
			basicDoors.add(56529);
			basicDoors.add(56530);
			basicDoors.add(56899);
			basicDoors.add(56900);
			basicDoors.add(56901);
			basicDoors.add(56902);
			basicDoors.add(56903);
			basicDoors.add(57057);
			basicDoors.add(57058);
			basicDoors.add(57059);
			basicDoors.add(57125);
			basicDoors.add(57137);
			basicDoors.add(57138);
			basicDoors.add(58496);
			basicDoors.add(58497);
			basicDoors.add(58507);
			basicDoors.add(58508);
			basicDoors.add(58518);
			basicDoors.add(58519);
			basicDoors.add(58529);
			basicDoors.add(58530);
			basicDoors.add(58540);
			basicDoors.add(58541);
			basicDoors.add(58551);
			basicDoors.add(58552);
			basicDoors.add(58560);
			basicDoors.add(58561);
			basicDoors.add(58574);
			basicDoors.add(58575);
			basicDoors.add(58587);
			basicDoors.add(58588);
			basicDoors.add(58596);
			basicDoors.add(58597);
			basicDoors.add(58607);
			basicDoors.add(58608);
			basicDoors.add(58618);
			basicDoors.add(58619);
			basicDoors.add(58629);
			basicDoors.add(58630);
			basicDoors.add(58640);
			basicDoors.add(58641);
			basicDoors.add(58651);
			basicDoors.add(58652);
			basicDoors.add(58662);
			basicDoors.add(58663);
			basicDoors.add(58673);
			basicDoors.add(58674);
			basicDoors.add(58684);
			basicDoors.add(58685);
			basicDoors.add(58695);
			basicDoors.add(58696);
			basicDoors.add(58706);
			basicDoors.add(58707);
			basicDoors.add(58717);
			basicDoors.add(58718);
			basicDoors.add(58728);
			basicDoors.add(58729);
			basicDoors.add(58737);
			basicDoors.add(58738);
			basicDoors.add(58748);
			basicDoors.add(58749);
			basicDoors.add(58761);
			basicDoors.add(58762);
			basicDoors.add(58772);
			basicDoors.add(58773);
			basicDoors.add(58783);
			basicDoors.add(58784);
			basicDoors.add(58792);
			basicDoors.add(58793);
			basicDoors.add(58803);
			basicDoors.add(58804);
			basicDoors.add(58814);
			basicDoors.add(58815);
			basicDoors.add(58827);
			basicDoors.add(58828);
			basicDoors.add(58838);
			basicDoors.add(58839);
			basicDoors.add(58861);
			basicDoors.add(58862);
			basicDoors.add(58872);
			basicDoors.add(58873);
			basicDoors.add(58883);
			basicDoors.add(58884);
			basicDoors.add(58892);
			basicDoors.add(58893);
			basicDoors.add(58905);
			basicDoors.add(58906);
			basicDoors.add(58916);
			basicDoors.add(58917);
			basicDoors.add(58927);
			basicDoors.add(58928);
			basicDoors.add(58938);
			basicDoors.add(58939);
			basicDoors.add(58949);
			basicDoors.add(58950);
			basicDoors.add(58960);
			basicDoors.add(58961);
			basicDoors.add(59941);
			basicDoors.add(60832);
			basicDoors.add(60833);
			basicDoors.add(60834);
			basicDoors.add(60835);
			basicDoors.add(60836);
			basicDoors.add(60837);
			basicDoors.add(60936);
			basicDoors.add(60937);
			basicDoors.add(60938);
			basicDoors.add(60939);
			basicDoors.add(60940);
			basicDoors.add(60941);
			basicDoors.add(61205);
			basicDoors.add(61206);
			basicDoors.add(61207);
			basicDoors.add(61208);
			basicDoors.add(61317);
			basicDoors.add(61318);
			basicDoors.add(61319);
			basicDoors.add(61320);
			basicDoors.add(61501);
			basicDoors.add(61502);
			basicDoors.add(61562);
			basicDoors.add(61563);
			basicDoors.add(61935);
			basicDoors.add(61936);
			basicDoors.add(61985);
			basicDoors.add(62007);
			basicDoors.add(62131);
			basicDoors.add(62337);
			basicDoors.add(62434);
			basicDoors.add(62435);
			basicDoors.add(63917);
			basicDoors.add(63918);
			basicDoors.add(63986);
			basicDoors.add(63987);
			basicDoors.add(63989);
			basicDoors.add(63990);
			basicDoors.add(63992);
			basicDoors.add(63993);
			basicDoors.add(63995);
			basicDoors.add(63996);
			basicDoors.add(63998);
			basicDoors.add(63999);
			basicDoors.add(64000);
			basicDoors.add(64001);
			basicDoors.add(64002);
			basicDoors.add(64003);
			basicDoors.add(64786);
			basicDoors.add(64787);
			basicDoors.add(64831);
			basicDoors.add(64832);
			basicDoors.add(65717);
			basicDoors.add(65801);
			basicDoors.add(65802);
			basicDoors.add(66525);
			basicDoors.add(66526);
			basicDoors.add(66599);
			basicDoors.add(66601);
			basicDoors.add(66750);
			basicDoors.add(66751);
			basicDoors.add(66752);
			basicDoors.add(66758);
			basicDoors.add(66759);
			basicDoors.add(66760);
			basicDoors.add(66761);
			basicDoors.add(66762);
			basicDoors.add(66763);
			basicDoors.add(66764);
			basicDoors.add(66765);
			basicDoors.add(66933);
			basicDoors.add(66934);
			basicDoors.add(66935);
			basicDoors.add(66936);
			basicDoors.add(66966);
			basicDoors.add(66967);
			basicDoors.add(67138);
			basicDoors.add(67139);
			basicDoors.add(67638);
			basicDoors.add(67639);
			basicDoors.add(67692);
			basicDoors.add(67693);
			basicDoors.add(67699);
			basicDoors.add(67700);
			basicDoors.add(67747);
			basicDoors.add(67748);
			basicDoors.add(67806);
			basicDoors.add(67807);
			basicDoors.add(67831);
			basicDoors.add(67832);
			log.info(MSG_BDOOR);

			for (int i = 50353; i < 50545; i++) {
				keyDoors.add(i);
			}

			for (int i = 53884; i < 53948; i++) {
				keyDoors.add(i);
			}

			for (int i = 55675; i < 55739; i++) {
				keyDoors.add(i);
			}

			for (int i = 50208; i < 50272; i++) {
				keyDoors.add(i);
			}
			log.info(MSG_KDOOR);

		}

		private void doorMsg(Tile loc, String name) {
			log.info("Door found: " + name +" at(X: " +loc.getX()+" Y: "+loc.getY()+BKT_O);
		}
		
		private  Tile[] tilesAround(Tile loc) {
			
			if (loc != null) {
			
			int x = loc.getX();
			int y = loc.getY();
			
			Tile north = new Tile(x, y+1, 0);
			Tile south = new Tile(x, y-1, 0);
			Tile west = new Tile(x-1, y, 0);
			Tile east = new Tile(x+1, y, 0);
			Tile se = new Tile(x+1, y-1, 0);
			Tile sw = new Tile(x-1, y-1, 0);
			Tile ne = new Tile(x+1, y+1, 0);
			Tile nw = new Tile(x-1, y+1, 0);
			
			return new Tile[] { north, south, west, east, se, sw, ne, nw};
			}
			return new Tile[] {};
		}
		
		private DGRoom generateRoom() {
			
			
			ConcurrentLinkedQueue<Tile> frontier = new ConcurrentLinkedQueue<Tile>();
			ConcurrentHashMap<Tile, Tile> checked = new ConcurrentHashMap<Tile, Tile>();
			Tile polled = null;

			Area roomArea = new Area();

			if (polled == null) {
				Tile loc = Players.getLocal().getLocation();
				polled = loc;
				frontier.add(polled);
			}

			for (int i = 0; i < (DGDungeon.WIDTH * DGDungeon.HEIGHT); i++) {
				Tile[] tiles = tilesAround(frontier.poll());
				
				for (Tile tile : tiles) {
					
					boolean reach = tile.canReach();
					boolean checkd = checked.contains(tile);
					
					if (reach && !checkd) {
						frontier.add(tile);
						checked.put(tile, tile);
					}
					
					if (!reach) {
						roomArea.addTile(tile);
					}
					
				}
			}
			
			Rectangle r = roomArea.getBounds();
			r.setSize(r.width+1, r.height+1);
			
			log.info("Generated new room.");
			
			ArrayList<DGDoor> doorz = scanForDoors(r);
			
			DGRoom room = new DGRoom(doorz, r, checked, DGDungeon.getRoomCount());
			
			DungeonObjective doe = null;

			for (DGDoor door : doorz) {
				if (door instanceof KeyDoor) {
					doe = new KeyObjective(door, room);
				}

				if (door instanceof GuardianDoor) {
					doe = new GuardianObjective(door, room);
				}

				if (door instanceof SkillDoor) {
					doe = new SkillObjective(door, room);
				}

				if (door instanceof BossDoor) {
					doe = new BossObjective(door, room);
				}

				if (doe != null && !DGDungeon.hasObjective(doe)) {
					DGDungeon.addObjective(doe);
					log.info("Added objective: " + doe.getDoor().getName());
				}
			}
			
			return room;
		}
		
		
		private ArrayList<DGDoor> scanForDoors(Rectangle r) {
			
			ArrayList<DGDoor> d00rs = new ArrayList<DGDoor>();
			
			SceneObject[] doors = SceneEntities.getLoaded(newDoors);
			if (doors != null) {
				
				for (int i = 0; i < doors.length; i++) {
					int id = doors[i].getId();
					String d = db.getObject(id);
					Tile loc = doors[i].getLocation();
					if (!doorsOnMap.containsKey(loc) && r.contains(loc.getX(), loc.getY()) && !sideDoors.contains(id)) {
						if (keyDoors.contains(id)) {
							String key = d.replace(CST_DOOR, CST_KEY);
							int keyID = db.getItem(key);
							
							ArrayList<Integer> ds = db.getObjects(d);
							
							DGKey dk = new DGKey(keyID, key, ds);
							KeyDoor kd = new KeyDoor(id, d, loc, dk, "Unlock");
							
							d00rs.add(kd);
							doorsOnMap.put(loc,  kd);
							log.info(MSG_KEYF + d + BKT_I + key + BKT_O);
							
						}
						
						else if (skillDoors.contains(id)) {
							SkillDoor sd = new SkillDoor(id, d, loc, "Open");
							
							d00rs.add(sd);
							doorsOnMap.put(loc, sd);
						}
						
						else if (basicDoors.contains(id)) {
							BasicDoor bd = new BasicDoor(id, d, loc, "Open");
							
							d00rs.add(bd);
							doorsOnMap.put(loc, bd);
						}
						
						else if (bossDoors.contains(id)) {
							BossDoor bd = new BossDoor(id, d, loc, "Enter");

							d00rs.add(bd);
							doorsOnMap.put(loc, bd);
						}
						else if (guardianDoors.contains(id)) {
							GuardianDoor gd = new GuardianDoor(id, d, loc, "Enter");
							
							d00rs.add(gd);
							doorsOnMap.put(loc, gd);
						}
						
						else {
							UnknownDoor ud = new UnknownDoor(id, d, loc, "Open");
							
							d00rs.add(ud);
							doorsOnMap.put(loc, ud);
							doorMsg(loc, "Unknown");
						}
						
						doorMsg(loc, d);
					}
				}
			}
			return d00rs;
		}


		private boolean inANewRoom() {
			Collection<DGRoom> rooms = DGDungeon.getRooms();
			for (DGRoom room : rooms) {
				if (room.containsTile(Players.getLocal().getLocation())) {
					DGDungeon.setCurrentRoom(room);
					return false;
				}
			}
			return true;
		}

		boolean pathSet = false;
		
		@Override
		public void run() {
			if (inANewRoom()) {
				DGRoom room = generateRoom();
				if (!DGDungeon.getRooms().contains(room)) {
					currentDungeon.addRoom(room);
				} 
			} 
		}

		@Override
		public boolean validate() {
			return inADungeon();
		}
		
	}

	class ObjectiveSolver extends Strategy implements Task {

		
		private DungeonObjective doe = null;
		
		private final String COMPL_OBJ = "Completing objective.";
		private final String OBT_REQ   = "Obtaining objective reqs.";
		private final String RESET_OBJ = "Finding new objective.";
		
		@Override
		public void run() {
			
			Tile dLoc = null;
			String dName = null;
			
			if (doe != null) {
				dLoc = doe.getDoor().getLocation();
				dName = doe.getDoor().getName();
			}
			
			if (doe == null) {
				objective = "Initializing objectives.";
				doe = DGDungeon.getNextObjective();
			}
			
			if (doe.isComplete()) {
				log.info("Complete objective: " + dName+dLoc);
				doe = DGDungeon.getNextObjective();
				objective = dName+dLoc;
			}
			
			if (!doe.getRoom().isOpen(doe.getDoor())) {
				if (doe != null) {
					if (doe.hasRequirements()) {
						status = COMPL_OBJ;
						doe.completeObjective();
					} else {
						if (doe.canObtainRequirements()) {
							status = OBT_REQ;
							doe.obtainRequirements();
						} else {
							status = RESET_OBJ;
							DGDungeon.addObjective(doe);
							log.info("Could not complete objective " +dName+dLoc);
							doe = DGDungeon.getNextObjective();
						}
					}
				}
			} else {
				DungeonObjective nxt = DGDungeon.getNextObjective();
				if (nxt != null) {
					doe = nxt;
					log.info("Hurrdapurpda: " + doe.getDoor().getName());
				}
			}
		}

		@Override
		public boolean validate() {
			return DGDungeon.getObjectiveCount() > 0;
		}
	}

	class StrategyFour extends Strategy implements Task {

		@Override
		public void run() {

		}

		@Override
		public boolean validate() {
			return false;

		}

	}

	@SuppressWarnings("serial")
	class SpiffyGUI extends JFrame {

		public SpiffyGUI() {
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("SpiffyAlcherV1.0");
			setBounds(new Rectangle(300, 300));

			JPanel jp = new JPanel();
			jp.setLayout(null);

			class SwingAction extends AbstractAction {

				public SwingAction() {
					putValue(NAME, "Start!");
				}

				@Override
				public void actionPerformed(ActionEvent arg0) {

					MoveBox mb = new MoveBox();
					Strategy mbs = new Strategy(mb, mb);
					provide(mbs);

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

	private final Color TXT_CLR = Color.black;
	private final int SPCING = 10;
	private int PAINT_X = 8;
	private int PAINT_Y = 393;
	private int RECT_HEIGHT = 128;
	private int RECT_WIDTH = 510;
	private int MOVEBOX_X;
	private int MOVEBOX_Y;
	private int HIDEBOX_X;
	private int HIDEBOX_Y;
	private int PBAR_HEIGHT = 20;
	private int PBAR_WIDTH = 300;
	private int PBAR_OVAL = 5;
	private final int DUNGEON_MAP_X = -40;
	private final int DUNGEON_MAP_Y = 200;
	private final int DUNGEON_MAP_THICK = 4;
	
	
	private boolean HIDEBOX = false;
	private boolean MOVEBOX = false;

	public void drawTotal(Graphics2D g, String total, int x, int y) {
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

	public void drawProgress(Graphics2D g, double percent, int x, int y, Color c) {
		g.setColor(c);
		g.drawRoundRect(x, y, PBAR_WIDTH, PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
		g.fillRoundRect(x, y, (int) (PBAR_WIDTH * (percent / 100)),
				PBAR_HEIGHT, PBAR_OVAL, PBAR_OVAL);
		g.setColor(Color.black);
		g.drawString("Smithing: " + percent + "%", x + 55, y + 15);
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

	public String perHour(int number) {
		double seconds = (double) (((System.currentTimeMillis() - RUN_TIME_MS) / 1000));
		if (seconds != 0) {
			float gpPerHour = (float) (number / seconds);

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

			}
			DGRoom startRoom = currentDungeon.getStartRoom();
			drawDungeon(g2d, Color.blue, startRoom, DUNGEON_MAP_X, DUNGEON_MAP_Y, 300, 20);
			drawStatus(g, TXT_CLR, status);
			drawObjective(g, TXT_CLR, objective);
			drawNumberOfRooms(g, TXT_CLR, DGDungeon.getRoomCount());
			drawNumberOfDoorsOpen(g, TXT_CLR, getNumberOfDoorsOpen());
			
			drawMoveBox(g, x - 30, y + 50);
			drawHideBox(g, x - 30, y + 50, Color.red);
			drawHide(g);
			drawMove(g);
			
			roomsPainted.clear();
		}
	}
	
	public int getNumberOfDoorsOpen() {
		int t = 0;
		Collection<DGRoom> rooms = DGDungeon.getRooms();
		rooms = Collections.synchronizedCollection(rooms);
		
		for (DGRoom room : rooms) {
			if (room.hasNorthRoom()) {
				t++;
			}
			
			if (room.hasSouthRoom()) {
				t++;
			}
			
			if (room.hasEastRoom()) {
				t++;
			}
			
			if (room.hasWestRoom()) {
				t++;
			}
		}
		return t/2;
	}
	
	
	public void drawObjective(Graphics2D g, Color c, String obj) {
		g.setColor(c);
		g.drawString("Objective: " +obj, PAINT_X+45, PAINT_Y+4*SPCING);
	}
	
	public void drawNumberOfDoorsOpen(Graphics2D g, Color c, int n) {
		g.setColor(c);
		g.drawString("#DoorsOpen: "+ n, PAINT_X+45, PAINT_Y+5*SPCING);
	}
	
	public void drawNumberOfRooms(Graphics2D g, Color c, int n) {
		g.setColor(c);
		g.drawString("#Rooms: " + n, PAINT_X+45, PAINT_Y+6*SPCING);
	}

	public void drawNumberOfDoorsFound(Graphics2D g, Color c, int n) {
		g.setColor(c);
	}
	
	public void drawRoomsComplete(Graphics2D g, Color c, int n) {
		g.setColor(c);
	}
	
	public void drawNumberOfNPCsKilled(Graphics2D g, Color c, int n) {
		g.setColor(c);
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

	private void drawStatus(Graphics2D g, Color c, String statusz) {
		g.setColor(c);
		g.drawString("Status : " + statusz, PAINT_X + 45, PAINT_Y + 30);
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
		g2d = (Graphics2D) g;
		if (GUI_DONE) {
			drawObjective(g2d, Color.yellow);
			drawChecked();
			
			
			drawMouseCursor(g2d, Color.black);
			movePaintBox(g2d, PAINT_X, PAINT_Y);
		}
	}
	
	private final int DGROOM_HEIGHT = 30;
	private final int DGROOM_WIDTH = DGROOM_HEIGHT;
	private final int CONNECTOR_LENGTH = 10;
	
	private ConcurrentLinkedQueue<DGRoom> roomsPainted = new ConcurrentLinkedQueue<DGRoom>();
	
	public void drawDungeon(Graphics2D g, Color c, DGRoom rm, int x, int y, int horizontalOffset, int verticalOffset) {
		
		if (rm != null) {
			if (rm.isComplete()) {
				c = Color.green; g.setColor(c);
				g.drawString("C", (x+horizontalOffset+DGROOM_WIDTH/2)-3, (y+verticalOffset+DGROOM_HEIGHT/2)+2);
			} else {
				c = Color.blue; g.setColor(c);
				g.drawString("I", (x+horizontalOffset+DGROOM_WIDTH/2)-3,  (y+verticalOffset+DGROOM_HEIGHT/2)+2);
			}
			
		
			Tile pLoc = Players.getLocal().getLocation();
			if (pLoc != null) {
				if (rm.containsTile(pLoc)) { c = Color.red; g.setColor(c);}
			}
			g.setStroke(new BasicStroke(DUNGEON_MAP_THICK));
			g.drawRect(x+horizontalOffset, y+verticalOffset, DGROOM_WIDTH, DGROOM_HEIGHT);
			roomsPainted.add(rm);
			c = Color.blue;
			g.setColor(c);
			
			
			if (rm.hasEastRoom()) {
				DGRoom east = rm.getEastRoom();
				if (!roomsPainted.contains(east)) {
					roomsPainted.add(east);
					drawHorizontalConnector(g, x,y, c, horizontalOffset+(DGROOM_WIDTH), verticalOffset+(DGROOM_HEIGHT)/2);
					drawDungeon(g,c, east, x+(CONNECTOR_LENGTH+DGROOM_WIDTH), y, horizontalOffset, verticalOffset);
				}
			}
			if (rm.hasWestRoom()) {
				
				DGRoom west = rm.getWestRoom();
				if (!roomsPainted.contains(west)) {
					roomsPainted.add(west);
					drawHorizontalConnector(g, x, y, c, horizontalOffset-CONNECTOR_LENGTH, verticalOffset+(DGROOM_HEIGHT)/2);
					drawDungeon(g, c, west, x-(CONNECTOR_LENGTH+DGROOM_WIDTH), y, horizontalOffset, verticalOffset);
				}
			}
			if (rm.hasSouthRoom()) {
				
				DGRoom south = rm.getSouthRoom();
				if (!roomsPainted.contains(south)) {
					roomsPainted.add(south);
					drawVerticalConnector(g, x, y, c, horizontalOffset+(DGROOM_WIDTH)/2, verticalOffset+(DGROOM_HEIGHT));
					drawDungeon(g,c, south, x, y+(CONNECTOR_LENGTH+DGROOM_WIDTH), horizontalOffset, verticalOffset);
				}
			}
			if (rm.hasNorthRoom()) {
				
				DGRoom north = rm.getNorthRoom();
				if (!roomsPainted.contains(north)) {
					roomsPainted.add(north);
					drawVerticalConnector(g, x, y, c, horizontalOffset+(DGROOM_WIDTH)/2, verticalOffset-(CONNECTOR_LENGTH));
					drawDungeon(g, c, north, x, y-(CONNECTOR_LENGTH+DGROOM_WIDTH), horizontalOffset, verticalOffset);
				}
			}
			
		}
	}
	
	private void drawVerticalConnector(Graphics2D g, int x, int y, Color c, int horizontalOffset, int verticalOffset) {
		g.setColor(c);
		
		x = x+horizontalOffset;
		y = y+verticalOffset;
		g.drawLine(x, y, x, y+10);
	}
	
	private void drawHorizontalConnector(Graphics2D g, int x, int y, Color c, int horizontalOffset, int verticalOffset) {
		g.setColor(c);
		
		x = x+horizontalOffset;
		y = y+verticalOffset;
		g.drawLine(x, y, x+10, y);
	}
	
	public void drawObjective(Graphics2D g, Color c) {
		DungeonObjective doe = DGDungeon.getCurrentObjective();
		if (doe != null) {
			DGDoor door = doe.getDoor();
			if (door != null) {
				Tile l = door.getLocation();

				Tile d = Players.getLocal().getLocation();

				g2d.fill(d.getBounds()[0]);
				if (l.isOnScreen()) {
					g2d.fill(l.getBounds()[0]);
				}
			}
		}
	}
	
	public void drawChecked() {
		g2d.setColor(Color.blue);
		DGRoom rm = DGDungeon.getCurrentRoom();
		if (rm != null) {
			Enumeration<Tile> checkd = rm.getRoomTiles();
			if (checkd != null) {
				while (checkd.hasMoreElements()) {
					checkd.nextElement().draw(g2d);
				}
			}
		}
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
