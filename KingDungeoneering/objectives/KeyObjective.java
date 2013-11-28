package objectives;

import main.KeyChain;

import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.SceneObject;

import rooms.DGRoom;

import doors.DGDoor;
import doors.DGKey;
import doors.KeyDoor;
import dungeon.DGDungeon;

public class KeyObjective extends DungeonObjective {

	
	private DGKey key;
	private KeyDoor keyDoor;
	
	public KeyObjective(DGDoor doord, DGRoom room) {
		super(doord, room);
		this.door = doord;
		if (door instanceof KeyDoor) {
			this.keyDoor = (KeyDoor)doord;
			this.key = this.keyDoor.getKey();
		}
	}

	@Override
	public void completeObjective() {
		SceneObject door = SceneEntities.getNearest(keyDoor.getID());
		if (door != null) {
			if (!door.isOnScreen()) {
				DGDungeon.traversePath(door.getLocation());
			} else {
				door.interact("Unlock");
				Time.sleep(2000);
			}
		}
	}

	@Override
	public boolean hasRequirements() {
		if (this.door instanceof KeyDoor) {
			DGKey key = ((KeyDoor)(this.door)).getKey();
			return KeyChain.contains(key.getName());
		}
		return false;
	}

	@Override
	public boolean canObtainRequirements() {
		GroundItem gi = GroundItems.getNearest(key.getID());
		if (gi != null) {
			return true;
		}
		
		String key = this.door.getName().replace("door", "key");
		
		if (!KeyChain.contains(key) && gi == null) {
			return false;
		}
		
		return true;
	}

	@Override
	public void obtainRequirements() {
		GroundItem gi = GroundItems.getNearest(key.getID());
		if (gi != null) {
			DGDungeon.traversePath(gi.getLocation());
			if (gi.isOnScreen()) {
				gi.interact("Take");
				Time.sleep(500);
			}
		}
	}

	@Override
	public boolean isComplete() {
		return room.isOpen(this.door);
	}


}
