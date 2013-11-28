package doors;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.bot.event.MessageEvent;


public class BossDoor extends DGDoor {

	
	public BossDoor(int doorID, String doorName, Tile doorLoc, String open) {
		super(doorID, doorName, doorLoc);
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
