package objectives;

import org.powerbot.game.api.methods.interactive.Players;

import rooms.DGRoom;
import doors.DGDoor;
import dungeon.DGDungeon;

public class DoorObjective extends DungeonObjective {

	public DoorObjective(DGDoor door, DGRoom room) {
		super(door, room);
	}

	@Override
	public void completeObjective() {
		this.door.openDoor();
	}

	@Override
	public boolean hasRequirements() {
		return room.containsTile(Players.getLocal().getLocation());
	}

	@Override
	public boolean canObtainRequirements() {
		return room.containsTile(this.door.getLocation());
	}

	@Override
	public void obtainRequirements() {
		if (!room.containsTile(Players.getLocal().getLocation())) {
			DGDungeon.traversePath(this.door.getLocation());
		}
	}

	@Override
	public boolean isComplete() {
		return room.isOpen(this.door);
	}


}
