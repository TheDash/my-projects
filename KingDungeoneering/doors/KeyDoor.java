package doors;

import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.bot.event.MessageEvent;


public class KeyDoor extends DGDoor {

	
	private final String UNLK = "Unlock-door";
	
	private DGKey key;
	
	public KeyDoor(int doorID, String doorName, Tile doorLoc, DGKey key, String open) {
		super(doorID, doorName, doorLoc);
		
		this.key = key;
	}
	
	@Override
	public boolean isNorth() {
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		Tile unreachable1 = new Tile(x, y+2, 0);
		Tile reachable = new Tile(x, y-1, 0);
		
		Tile unreachable2 = new Tile(x, y+1, 0);
		
		return (!unreachable1.canReach() || !unreachable2.canReach()) && reachable.canReach();
	}
	
	@Override
	public boolean isSouth() {
		
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		Tile unreachable1 = new Tile(x, y-2, 0);
		Tile reachable = new Tile(x, y+1, 0);
		
		Tile unreachable2 = new Tile(x, y-1, 0);
		
		return (!unreachable1.canReach() || !unreachable2.canReach()) && reachable.canReach();
	}
	
	@Override
	public boolean isWest() {
		
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		Tile unreachable1 = new Tile(x-2, y, 0);
		Tile reachable    = new Tile(x+1, y, 0);
		
		Tile unreachable2 = new Tile(x-1, y, 0);
		
		return (!unreachable1.canReach() || !unreachable2.canReach()) && reachable.canReach();
	}
	
	@Override
	public boolean isEast() {
		int x = this.doorLoc.getX();
		int y = this.doorLoc.getY();
		
		
		Tile unreachable1 = new Tile(x+2, y, 0);
		Tile reachable = new Tile(x-1, y, 0);
		
		Tile unreachable2 = new Tile(x+1, y, 0);
		
		return (!unreachable1.canReach() || !unreachable2.canReach()) && reachable.canReach();
	}
	
	public DGKey getKey() {
		return this.key;
	}
	
	@Override
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
	
	@Override
	public void messageReceived(MessageEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
