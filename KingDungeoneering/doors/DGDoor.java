package doors;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.bot.event.listener.MessageListener;


public abstract class DGDoor implements MessageListener {

	
	private DGDoor parentDoor;
	
	protected Tile doorLoc;
	protected int doorID;
	protected String doorName;
	
	protected boolean isOpen = false;
	
	
	protected Filter<SceneObject> doorFilter = new Filter<SceneObject>() {

		@Override
		public boolean accept(SceneObject arg0) {
			return arg0.getId() == doorID && arg0.getLocation().distance(doorLoc) <= 1;
		}
		
	};
	
	public DGDoor(int doorID, String doorName, Tile loc) {
		this.doorName = doorName;
		this.doorID = doorID;
		this.doorLoc = loc;
	}
	
	public void setParent(DGDoor parent) {
		this.parentDoor = parent;
	}
	
	public DGDoor getParent() {
		return parentDoor;
	}
	
	public String getName() {
		return doorName;
	}
	
	public Tile getLocation() {
		return doorLoc;
	}
	
	public int getID() {
		return doorID;
	}
	
	public void openDoor() {
		SceneObject d = SceneEntities.getNearest(doorFilter);
		if (d != null) {
			if (Players.getLocal().getLocation().distance(d.getLocation()) >= 3) {
				Walking.walk(d.getLocation());
				if (!d.isOnScreen()) {
					Camera.turnTo(d);
				}
			} else {
				d.interact("Open");
			}
		} 
	}
	
	
	
	public boolean isNorth() {
		
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		Tile reachable = new Tile(x, y-1, 0);
		Tile reachable2 = new Tile(x, y-2, 0);
		
		return reachable.canReach() || reachable2.canReach();
	}

	public boolean isSouth() {
		
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		Tile reachable    = new Tile(x, y+1, 0);
		Tile reachable2 = new Tile(x, y+2, 0);
		
		return reachable.canReach() || reachable2.canReach();
	}

	public boolean isWest() {
		
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		Tile reachable = new Tile(x+1, y, 0);
		Tile reachable2 = new Tile(x+2, y, 0);
		
		return reachable.canReach() || reachable2.canReach();
	}

	public boolean isEast() {
		
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		Tile reachable = new Tile(x-1, y, 0);
		Tile reachable2 = new Tile(x-2, y, 0);
		
		return reachable.canReach() || reachable2.canReach();
	}
	
	public boolean isOpen() {
		
			int x = this.doorLoc.getX();
			int y = this.doorLoc.getY();

			if (this.isEast()) {
				
				Tile reachable = new Tile(x+3,y, 0);
				Tile reachable2 = new Tile(x+2,y, 0);
				
				System.out.println("1: " +reachable.canReach());
				System.out.println("2: "+reachable2.canReach());
				
				return reachable.canReach() || reachable2.canReach();

			} else if (this.isWest()) {
				
				Tile reachable = new Tile(x-3, y, 0);
				Tile reachable2 = new Tile(x-2, y, 0);
				
				return reachable.canReach() || reachable2.canReach();
				
			} else if (this.isSouth()) {
				
				Tile reachable = new Tile(x, y-3, 0);
				Tile reachable2= new Tile(x, y-2, 0);
				
				return reachable.canReach() || reachable2.canReach();
				
			} else if (this.isNorth()) {
				
				Tile reachable = new Tile(x, y+3, 0);
				Tile reachable2= new Tile(x, y+2, 0);
				
				return reachable.canReach() || reachable2.canReach();
			}
			
			return false;
	}
}
