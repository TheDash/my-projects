package doors;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.bot.event.MessageEvent;


public class UnknownDoor extends DGDoor {

	public UnknownDoor(int doorID, String doorName, Tile loc, String open) {
		super(doorID, doorName, loc);
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		
	}

}
