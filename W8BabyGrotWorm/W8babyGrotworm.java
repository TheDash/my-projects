import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;
import org.powerbot.game.bot.event.listener.PaintListener;

@Manifest(authors = { "W8baby" }, name = "W8babyGrotworm", description = "Kills mature grotworms, 50 agility required.", version = 1.1)
public class W8babyGrotworm extends ActiveScript implements MessageListener, PaintListener, MouseListener 
{
	 int expStart = 0;

	 boolean setup = true;
	 static String status = "";
	 static int profit = 0;
	    
	 public long startTime = System.currentTimeMillis();
	 public long millis = 0;
	 public long hours = 0;
	 public long minutes = 0;
	 public long seconds = 0;
	
	
	@Override
	protected void setup() 
	{
		provide(new launchGUI());
		
		InventTab tab = new InventTab();
        Strategy Tab = new Strategy(tab, tab);
        provide(Tab);
		
        Potter pot = new Potter();
        Strategy pots = new Strategy(pot, pot);
        provide(pots);
        
        Looter loot = new Looter();
        Strategy Loot = new Strategy(loot, loot);
        provide(Loot);
        
        Fighter combat = new Fighter();
        Strategy combats = new Strategy(combat, combat);
        provide(combats);
        
        Eater eat = new Eater();
        Strategy eats = new Strategy(eat, eat);
        provide(eats);
        
        Banker bank = new Banker();
        Strategy Bank = new Strategy(bank, bank);
        provide(Bank);
        
        Walker walk = new Walker();
        Strategy Walk = new Strategy(walk, walk);
        provide(Walk);

        expStart = Skills.getExperiences()[1] + Skills.getExperiences()[2] + Skills.getExperiences()[3] + Skills.getExperiences()[0];
	}
	
	//Since GUIs are fucked, you will need to edit stuff here
	private class launchGUI extends Strategy implements Task 
	{
        public void run() 
        {
        	status = "Starting up";
        	
        	//Use super or extreme potions
        	//If you want to use super potions, remove the slashes in front of Potter.useSuper); 
        	//And add two slashes in front of Potter.useExtreme;
        	
        	Potter.useSuper = true;
        	//Potter.useExtreme = true;
        	
        	//How much food to bring
        	//28 to withdraw all
        	Banker.amtFood = 28;
        	
        	//Use quick prayers
        	//Fighter.usePrayer = true;
        	
            Looter.addTables();
            setup =	false;
        }
    

        public boolean validate() {
        		return setup;
            }
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		millis = System.currentTimeMillis() - startTime;
 		hours = millis / (1000 * 60 * 60);
 		millis -= hours * (1000 * 60 * 60);
 		minutes = millis / (1000 * 60);
 		millis -= minutes * (1000 * 60);
 		seconds = millis / 1000;
 		
		int expGained = (Skills.getExperiences()[1] + Skills.getExperiences()[2] + Skills.getExperiences()[3] + Skills.getExperiences()[0] - expStart);
 		
		try{
	 		if(Walker.atGrotworm())
	 		if(Fighter.getNPC() != null){
	 			highlightNPC(g, Fighter.getNPC());
	 		}
	 		}
	 		catch(Exception e)
	 		{}
		
		g.setColor(Color.black);
		g.drawRect(240, 345, 257, 113);	
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(240, 345, 257, 113);	
		g.setColor(Color.WHITE);
		g.drawString("v1.2", 450, 370);
		g.drawString("Run time: " + hours + ":" + minutes + ":" + seconds, 250, 390);
		g.drawString("Status: " + status, 10, 335);
		
		g.setColor(Color.red);
		g.drawString("Experience gained: " + (expGained) , 250, 405);
 		g.drawString("Experience/hr: " +(int)(expGained * (3600000D / (System.currentTimeMillis() - startTime))) , 250, 420);
 		
 		g.setColor(Color.green);
 		g.drawString("Profit: " + Looter.profit, 250, 435);
 		g.drawString("Profit/hr: " + (int)(Looter.profit * (3600000D / (System.currentTimeMillis() - startTime))), 250, 450);
 		
 		drawMouse(g);
 		
 		g.setFont(new Font("Arial", 0, 16));
 		g.drawString("W8babyGrotworm", 250, 370);

	}
	  
	private void drawMouse(Graphics2D g) {
		int Ran = Random.nextInt(1, 7);
        
        if (Ran == 1) {
        	g.setColor(Color.RED);
        }
        else if (Ran == 2) {
        	g.setColor(Color.ORANGE);      
        }
        else if (Ran == 3) {
            g.setColor(Color.YELLOW);  
        }
        else if (Ran == 4) {
            g.setColor(Color.GREEN);   
        }
        else if (Ran == 5) {
            g.setColor(Color.BLUE);    
        }
        else if (Ran == 6) {
            g.setColor(Color.MAGENTA);     
        }
        else if (Ran == 7) {
            g.setColor(Color.CYAN);     
        }
        g.drawOval((int) Mouse.getLocation().getX() - 3, (int)Mouse.getLocation().getY() - 3, 6, 6);
    }
	
	public void highlightNPC(final Graphics g, final Filter<NPC> npcs) 
	{
		int px = 0;
		int py = 0;
		
		for(NPC npc: NPCs.getLoaded(npcs))
		
        for (Polygon poly : npc.getLocation().getBounds())
        {
            boolean drawThisOne = true;
            
            for (int i = 0; i < poly.npoints; i++) 
            {
                Point p = new Point(poly.xpoints[i], poly.ypoints[i]);
                px = poly.xpoints[i]; py = poly.ypoints[i];
                if (!Calculations.isOnScreen(p)) {
                            drawThisOne = false;
                    }
            }
            
            if (drawThisOne) {

            	g.setColor(Color.yellow);
            	g.drawPolygon(poly);
            	g.setColor(new Color(255, 175, 175, 50));
            	g.fillPolygon(poly);
            	g.setColor(Color.green);
            	g.drawString(npc.getName(), px, py - 20);
            	if(npc.getInteracting() != null)
            	g.drawString("Interacting with player", px, py - 5);
            }
        }
	}
	
	@Override
	public void messageReceived(MessageEvent e) {
		String msg = e.getMessage();
        if (msg.contains("coins")) 
        {
        	try{
        		Looter.profit += (Integer.parseInt(Looter.formatter(msg.substring(0, 5))));
        	}
        	catch(Exception x){}
        }
        
        if(msg.contains("bot"))
        {
        	System.out.println("Someone mentioned botting. " + msg);
        }
	}
}

class Banker extends Strategy implements Task
{       
        final int WIDGET_BANK = 762;
        final int WIDGET_CHILD_DEPOSIT_ALL = 34;
        final int WIDGET_CHILD_CLOSE = 45;
        final int WIDGET_BANK_SLOTS = 95;
        final int bankIDs[] = {11758};
        
        final static int strPotion = 2440,
        atkPotion = 2436,
        exStr = 15312,
        exAtk = 15308,
        prayPot = 2434,
        fallyTab = 8009,
        houseTab = 8013;
        
        static int amtFood = 0;
        
        private final Area bankArea = new Area(new Tile[] {new Tile(2949, 3368, 0), new Tile(2943, 3368, 0), new Tile(2943, 3373, 0),
            	new Tile(2949, 3373, 0)});
        
        
        public void run(){
        	
        	W8babyGrotworm.status = ("Banking");
        		
        	if((Inventory.getCount(Eater.food) > 0 || amtFood == 0) && Inventory.getCount(fallyTab) > 0 && Inventory.getCount(houseTab) > 0)
            {
        		W8babyGrotworm.status = ("Teleporting");
            	try
            	{
            		Inventory.getItem(8013).getWidgetChild().interact("Break");
            	}
            	
				catch(Exception e) {}	
            	
            	Time.sleep(5000);
            }
        	
                if(!isOpen()){
                        open();
                        Time.sleep(Random.nextInt(1500,2000));
                }

                if(isOpen()){
                        depositAll();
                        Time.sleep(Random.nextInt(1000,1500));
                        
                        withdraw(fallyTab, 1);
                        withdraw(houseTab, 1);
                        
                        if(Potter.useSuper){
                		withdraw(strPotion, 1);
                		withdraw(atkPotion, 1);}
                        
                        if(Potter.useExtreme){
                        withdraw(exStr, 1);
                    	withdraw(exAtk, 1);}
                        
                        if(Fighter.usePrayer){
                        withdraw(prayPot, 5);
                        }
                        
                        if(amtFood > 0){
                		withdraw(Eater.food, amtFood);
                		}
                		
                		close();
                        Time.sleep(1000);
                }
        }

                public boolean isOpen() {
                        return Widgets.get(WIDGET_BANK, 1).isOnScreen();
                }

                public boolean open() {
                        SceneObject booths = SceneEntities.getNearest(bankIDs);
                        return booths != null && booths.interact("Bank");
                }

                public boolean depositAll() {
                        return isOpen() && Widgets.get(WIDGET_BANK).getChild(WIDGET_CHILD_DEPOSIT_ALL).click(true);
                }

                public boolean close() {
                        return isOpen() && Widgets.get(WIDGET_BANK).getChild(WIDGET_CHILD_CLOSE).click(true);
                }

                public boolean withdraw(int id, int amount)
                {
                        WidgetChild bankSlots = Widgets.get(WIDGET_BANK, WIDGET_BANK_SLOTS);
                        for (WidgetChild bankSlot : bankSlots.getChildren())
                        {
                                if (bankSlot != null && bankSlot.getChildId() == id)
                                {
                                        int bankSlotX = bankSlots.getRelativeX() + bankSlot.getRelativeX();
                                        int bankSlotY = bankSlots.getRelativeY() + bankSlot.getRelativeY();
                                        
                                        if(amount == 1)
                                    	{
                                        	Mouse.click(bankSlotX + Random.nextInt(5 ,bankSlot.getWidth()), bankSlotY + Random.nextInt(5 ,bankSlot.getHeight()), true);
                                        	break;
                                    	}
                                        
                                        Mouse.click(bankSlotX + Random.nextInt(5 ,bankSlot.getWidth()), bankSlotY + Random.nextInt(5 ,bankSlot.getHeight()), false);

                                        Time.sleep(Random.nextInt(500, 1000));

                                        if(Menu.isOpen()){
                                        	if(amount == 5)
                                        	{
                                        		Menu.select("Withdraw-5");
                                        		break;
                                        	}
                                        	if(amount == 10)
                                        	{
                                        		Menu.select("Withdraw-10");
                                        		break;
                                        	}
                                        	if(amount >= 28)
                                        	{
                                        		Menu.select("Withdraw-All");
                                        		break;
                                        	}
                                        	
                                        	else
                                        	Menu.select("Withdraw-X");
                                        	Time.sleep(2000);
                                        	Keyboard.sendText(Integer.toString(amount), true);
                                        	Time.sleep(2000);
                                        
                                         if(Inventory.getCount(id) > 0) return true;
                                        }
                                }
                        }
                        return false;
                }

                public boolean validate() {
                        return bankArea.contains(Players.getLocal().getLocation());
                }
}

class Eater extends Strategy implements Task, Condition
{	
	static int eatAt = 500;
	static int food = 7946;
	
	public void run(){
		if(Inventory.getCount(food) < 1)
		{
			teleport();
		}
		
		eat();
	}	
	
	public static void teleport()
	{
		W8babyGrotworm.status = ("Teleporting");
		
		try
    	{
    		Inventory.getItem(8009).getWidgetChild().interact("Break");
    	}
    	
		catch(Exception e) {}	
    	Time.sleep(5000);
	}
	
	public static void eat()
	{	
		
		W8babyGrotworm.status = ("Eating");
		if(Inventory.getCount(food) > 0){
			for (Item i : Inventory.getItems()){
				if (i.getId() == food){
					i.getWidgetChild().interact("Eat");
					Time.sleep(500);
					break;
                }
           	}
		}
	}
	public boolean validate() {
		//If health is lower than what we eat at or the inventory needs space, returns true
        return Fighter.getLp() < eatAt || (Inventory.getCount() > 27 && Inventory.getCount(food) > 0);
    }
}

class Fighter extends Strategy implements Task, Condition {
	
	final static int grotworm = 15463;
	static boolean usePrayer = false;
	
	public static int getLp(){
    	if(Widgets.get(748, 8) != null)
    		return (Integer.parseInt(Widgets.get(748, 8).getText()));
    
    	return -1;
    }
	
    public static boolean isPrayerActivated()
    {
    	if(Widgets.get(749, 0) != null)
    		return Widgets.get(749, 0).getTextureId() == 782;
    
    	return false;
    }
    
    public static boolean activatePrayer()
    {
    	if(Widgets.get(749, 0) != null)
    		return Widgets.get(749, 0).click(true);
    
    	return false;
    }
	
	public static Filter<NPC> getNPC()
	{
		return grotworms; 
	}
	
	static Filter<NPC> grotworms = new Filter<NPC>(){
		public boolean accept(NPC n){
			if ((n.getInteracting() == null || n.getInteracting().equals(Players.getLocal())) && n.getHpPercent() > 0 && 
					Walker.grotArea.contains(n.getLocation())) {
					if (n.getId() == grotworm) return true;
			}
			return false;
	}};

	@Override
	public void run() 
	{	
		//Searching for grotworm
		if(Players.getLocal().getInteracting() == null)
		{
			W8babyGrotworm.status = ("Looking for grotworm");
		
			if(!Walking.isRunEnabled()){
				if(Walking.getEnergy() > 50){
					Walking.setRun(true);
				}
			}	
		
			NPC npc = NPCs.getNearest(grotworms);
		
			if (!Players.getLocal().isMoving()) 
			{
				while(npc.getInteracting() == Players.getLocal() ||
						(npc != null && !Players.getLocal().isInCombat() && !npc.isInCombat()))
				{
					W8babyGrotworm.status = ("Interacting with grotworm");
					Walking.walk(npc.getLocation());
					npc.interact("Attack");
					
					Timer tm = new Timer(500);
		            while(tm.isRunning()){
		            if(Players.getLocal().isMoving()){
		            tm.reset();
		            }
		            if(Players.getLocal().isInCombat()) break;
		            
		            	Time.sleep(100);
		            }
					break;
				}
			} 
		
			else if(npc != null && !Players.getLocal().isMoving() && Players.getLocal().getInteracting() == null && !Players.getLocal().isInCombat())
			{
				Walking.walk(npc.getLocation());
				Timer tm = new Timer(500);
			
				while(tm.isRunning()){
					if(Players.getLocal().isMoving() && !npc.isOnScreen()){
						tm.reset();
					}
					
					if(Players.getLocal().isInCombat()) break;

					Time.sleep(100);
				}
			}
		}
		
		//Fighting grotworm
		if(Players.getLocal().getInteracting() != null || Players.getLocal().isInCombat())
		{
			W8babyGrotworm.status = ("Fighting grotworm");
			org.powerbot.game.api.wrappers.interactive.Character worm = Players.getLocal().getInteracting();

			if(!isPrayerActivated() && usePrayer)
			{
				activatePrayer();
			}
			
			
			if(worm != null && worm.getAnimation() == 16790)
			{
				if(isPrayerActivated() && usePrayer)
				{
					activatePrayer();
				}
				
				W8babyGrotworm.status = ("Waiting for death");
				Time.sleep(4000);
			}
		}
	}

	public boolean validate() {
		return (getLp() > Eater.eatAt && Walker.atGrotworm());
	}

}

class InventTab extends Strategy implements Task, Condition
{
	public void run()
	{
		
	}
	
	@Override
	public boolean validate()
	{
		if(Tabs.getCurrent() != Tabs.INVENTORY)
		{
			return true && Tabs.INVENTORY.open();
		}
		return false;
	}
}

class Looter extends Strategy implements Task, Condition {
	
	static final int[] junk = {532, 229, 14664};
	static HashMap<Integer, Integer> prices = new HashMap<Integer, Integer>();
	static int[] rareTable = {7937, 24154, 1515, 5289, 5315, 5316, 5300, 6686, 270, 3001, 2999, 258, 2364, 2362, 450, 452, 454, 384, 372, 20667, 574, 570, 1392, 9342, 1149, 2366, 1215, 18778};
	static int[] grotDrop = {1319, 1213, 1303, 1432, 24315, 1113, 1147, 1185, 565, 561, 6689, 3028, 2363, 451, 1780, 995};
	static int profit = 0;
	static boolean activeLoot = false;

    public static void addTables()
    {
    	for(int x = 0; x < rareTable.length; x++)
		{
			prices.put(rareTable[x], 0);
		}
    	
    	for(int x = 0; x < grotDrop.length; x++)
		{
			prices.put(grotDrop[x], lookup(grotDrop[x]));
		}
    }
    
    public static int lookup(int itemID) {
        try {
        	final URL url = new URL("http://services.runescape.com/m=itemdb_rs/viewitem.ws?obj=" + itemID);
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String input;
            while ((input = in.readLine()) != null) {
                  if(input.contains("Current guide price:")) {
                	  input = in.readLine();
                      return Integer.parseInt(formatter(input.substring(4, input.lastIndexOf('<'))));
                }
            }
        }
            catch(Exception e) {}                  
        return 0;
    }

    public static String formatter(String num) {
        try {
                return num.replaceAll("\\.","").replaceAll("m", "00000").replaceAll("k", "00").replaceAll(",", "");
        } catch (Exception e) {}
        return "0";
    }
	
    
    public void run()
    {	  	
    	GroundItem loot = GroundItems.getNearest(new Filter <GroundItem>(){
        public boolean accept(GroundItem i){
        	if(prices.containsKey(i.getGroundItem().getId()) && Walker.grotArea.contains(i.getLocation())){
        		return true;
        	}
            return false;
        	}});	
    		
        	if(loot != null && loot.isOnScreen())
        	{
        		int lootId = loot.getGroundItem().getId(); 
        		
        		for (int x: junk)
        		{
        			if(Inventory.getCount(x) > 0)
        			{
        				Inventory.getItem(x).getWidgetChild().interact("Drop");
        			}
        		}
        		
        		/*if the item is not stackable or not in the inventory already, clears space by eating
        		 Count of stacked items in inventory will usually never be equal to the inventory count*/
        		if((Inventory.getCount(lootId) != Inventory.getCount(true, lootId)) && Inventory.getCount() == 28)
        			Eater.eat();
        		
        		W8babyGrotworm.status = ("Looting " + loot.getGroundItem().getName());
        		
                int oldInventoryCount = Inventory.getCount(true, lootId);
                int stack = Math.abs(loot.getGroundItem().getStackSize());
                
                //getName is broken
                loot.interact("Take");
                //loot.interact("Take", loot.getGroundItem().getName());
                
                Timer tm = new Timer(2000);
                
                while(tm.isRunning()){
                if(Players.getLocal().isMoving()){
                	tm.reset();}
                    	Time.sleep(100);
                    }
                
                //If there is more of the item than before, adds the difference to the profit
            	if(oldInventoryCount < Inventory.getCount(true, lootId)){
            		profit += (Integer)(prices.get(lootId)) * stack;
                }
        	}
        	
        	if(activeLoot){
    			if(!loot.isOnScreen() && loot != null){
    			Walking.walk(loot.getLocation());
    				
    			Timer tm = new Timer(1000);
            
    			while(tm.isRunning()){
    				if(Players.getLocal().isMoving()){
    					tm.reset();}
                	Time.sleep(100);
                }
            }}
    	}
    
    public boolean validate()
	{
    	//If inventory has empty space or we have the ability to clear inventory space by eating, returns true
		return Inventory.getCount() <= 27 || (Inventory.getCount(Eater.food) > 0 && Walker.atGrotworm());
	}
}
class Potter extends Strategy implements Task, Condition {

	final int[] superStrengthPots = {161, 159, 157, 2440};
    final int[] extremeStrengthPots = {15315, 15314, 15313, 15312};
	final int[] superAttackPots = {149, 147, 145, 2436};
    final int[] extremeAttackPots = {15311, 15310, 15309, 15308};
    final int[] prayerPots = {143, 141, 139, 2434};
    static boolean useSuper = false;
    static boolean useExtreme = false;

    public static int getPrayer()
    {
    	if(Widgets.get(749, 6) != null)
    		return (Integer.parseInt(Widgets.get(749, 6).getText()));
    
    	return -1;
    }
    
    public static boolean contains(int[] items)
    {
    	for (int i: items) 
    	{
            if (Inventory.getCount(i) > 0) return true;
        }
    	return false;
    }
    
	@Override
	public void run() 
	{
		if(Fighter.usePrayer)
		{
			if(getPrayer() < 500 && contains(prayerPots))
				drinkPot(prayerPots);
			
			else if(getPrayer() < 500)
				Eater.teleport();
		}
		
		if (useExtreme)
		{
			if(Skills.getRealLevel(Skills.ATTACK)+10 >= Skills.getLevel(Skills.ATTACK))
			drinkPot(extremeAttackPots);
		
			if(Skills.getRealLevel(Skills.STRENGTH)+10 >= Skills.getLevel(Skills.STRENGTH)) 
			drinkPot(extremeStrengthPots);
		}
		
		else if (useSuper)
		{
			if(Skills.getRealLevel(Skills.ATTACK)+10 >= Skills.getLevel(Skills.ATTACK))
			drinkPot(superAttackPots);
			
			if(Skills.getRealLevel(Skills.STRENGTH)+10 >= Skills.getLevel(Skills.STRENGTH))
			drinkPot(superStrengthPots);
		}
	}
	
	private void drinkPot(int[] pots) {
        for (int i: pots) {
            if (Inventory.getCount(i) > 0) {
            	W8babyGrotworm.status = ("Drinking potion");
                Inventory.getItem(i).getWidgetChild().interact("Drink");
                Time.sleep(1700);
				break;
            }
        }
    }
	
	public boolean validate()
    {
		return Walker.atGrotworm() && (useSuper || useExtreme || Fighter.usePrayer);
    }

}

class Walker extends Strategy implements Task, Condition
{
    final int entrance = 70792, shortcut = 25636;

	//Areas
    final static Area 
    	grotArea = new Area(new Tile[] {new Tile(1177, 6512, 0), new Tile(1207, 6512, 0), new Tile(1207, 6486, 0),
            new Tile(1177, 6486, 0)}),
        grotArea2 = new Area(new Tile[] {new Tile(1135, 6512, 0), new Tile(1173, 6512, 0), new Tile(1173, 6485, 0),
            new Tile(1135, 6485, 0)}),
        rimmington = new Area(new Tile[] {new Tile(2940, 3250, 0), new Tile(3000, 3250, 0), new Tile(3000, 3195, 0),
            new Tile(2940, 3195, 0)}), 
        bankArea = new Area(new Tile[] {new Tile(2949, 3368, 0), new Tile(2943, 3368, 0), new Tile(2943, 3373, 0),
        	new Tile(2949, 3373, 0)}),
        falador = new Area(new Tile[] {new Tile(2937, 3392, 0), new Tile(3063, 3392, 0), new Tile(3063, 3331, 0),
            new Tile(2937, 3331, 0)});
    
    private enum currentArea
    {
    	BANK, RIMMINGTON, FALADOR, GROTWORM, ENTRANCE, UNKNOWN
    }
    
    public currentArea getCurrentArea()
    {
    	if(bankArea.contains(Players.getLocal().getLocation())) return currentArea.BANK;
    	if(falador.contains(Players.getLocal().getLocation()) && !bankArea.contains(Players.getLocal().getLocation())) return currentArea.FALADOR;
    	if(rimmington.contains(Players.getLocal().getLocation())) return currentArea.RIMMINGTON;
    	if(grotArea.contains(Players.getLocal().getLocation())) return currentArea.GROTWORM;
    	if(Players.getLocal().getLocation().getY() > 6000 && Players.getLocal().getLocation().getY() < 6400) return currentArea.ENTRANCE;
    	
    	return currentArea.UNKNOWN;
    }
    
    public static boolean atGrotworm()
    {
    	return grotArea.contains(Players.getLocal().getLocation());
    }

	//Walking
	public static float distanceTo(Tile tile){
		return (float) Calculations.distance(Players.getLocal().getLocation(), tile);
	}
	
	public static int angleTo(Tile tile) {
        double ydif = tile.getY() - Players.getLocal().getLocation().getY();
        double xdif = tile.getX() - Players.getLocal().getLocation().getX();
        return (int) (Math.atan2(ydif, xdif) * 180 / Math.PI);
        }
	
	public static boolean walkTileMM(Tile tile, int rnd) {
        double angle = angleTo(tile) - Camera.getAngleTo(0);
        double distance = distanceTo(tile);
        if(distance > 15){
        	distance = 15;}
        angle = angle * Math.PI / 180;
        int x = 627, y = 85;
        int dx = (int) (4 *(distance + Random.nextGaussian(0, rnd, 1)) * Math
        .cos(angle));
        int dy = (int) (4 * (distance + Random.nextGaussian(0, rnd, 1)) * Math
        .sin(angle));
        return Mouse.click(x + dx, y - dy, true);
    }
	
	 public boolean inventoryIsFull() {
     	return (Inventory.getCount() == 28);
     }
	
	@Override
	public void run()
	{
		W8babyGrotworm.status = ("Walking");
		
		if(!Walking.isRunEnabled()){
			if(Walking.getEnergy() > 50){
			Walking.setRun(true);
			}
		}
		
		//Status after teleporting to falador
		if(getCurrentArea() == currentArea.FALADOR)
		{
			//turns off prayer
			if(Fighter.isPrayerActivated())
			{
				Fighter.activatePrayer();
			}
			
			if(Players.getLocal().getLocation().getX() > 2960)
			{
				walkTileMM(new Tile(2955, 3381, 0), 0);
			}
			
			else
			walkTileMM(new Tile(2946, 3368, 0), 0);
		}
		
		//Status after teleporting to house
		else if(getCurrentArea() == currentArea.RIMMINGTON)
		{	
			//Temporary fix for illogical walking
			if(Players.getLocal().getLocation().getX() < 2975)
			{
				walkTileMM(new Tile(2980, 3222, 0), 1);
			}
			
			else
			walkTileMM(new Tile(2991, 3240, 0), 0);
			SceneObject theEntrance = SceneEntities.getNearest(entrance);
            if(theEntrance != null) theEntrance.interact("Enter"); 
		}
		
		//Status after entering dungeon
		else if(getCurrentArea() == currentArea.ENTRANCE)
		{	
			//Temporary fix for illogical walking
			if(Players.getLocal().getLocation().getX() > 1195)
			{
				walkTileMM(new Tile(1189, 6370, 0), 0);
			}
			
			else
			walkTileMM(new Tile(1178, 6356, 0), 0);
			SceneObject theShortcut = SceneEntities.getNearest(shortcut);
            if(theShortcut != null) theShortcut.interact("Slide down"); 
		}
		
		//Walks to the middle so the script can fight
		else if(getCurrentArea() == currentArea.GROTWORM)
		{	
			walkTileMM(new Tile(1190, 6500, 0), 2);
		}
		
		while(Players.getLocal().isMoving())
			Time.sleep(100);

	}

	public boolean validate()
    {
		//If the player isn't at the bank or killing worms, then he is walking.
		return !bankArea.contains(Players.getLocal().getLocation()) && !grotArea.contains(Players.getLocal().getLocation());
    }

}

