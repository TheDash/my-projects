package doors;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.bot.event.MessageEvent;


public class BasicDoor extends DGDoor {

	public BasicDoor(int doorID, String doorName, Tile loc, String open) {
		super(doorID, doorName, loc);
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void openDoor() {
		SceneObject d = SceneEntities.getNearest(doorFilter);
		if (d != null) {
			Tile pLoc = Players.getLocal().getLocation();
			
			if (pLoc.distance(d) >= 3) {
				System.out.println("Walking to door.");
				Walking.walk(d.getLocation());
				if (!d.isOnScreen()) {
					Camera.turnTo(d);
				}
			} 
			
			if (pLoc.distance(d) <= 3) {
				d.interact("Enter");
			}
		}
	}

}
