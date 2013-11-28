package objectives;

import rooms.DGRoom;
import doors.DGDoor;
import dungeon.DGDungeon;

public class BossObjective extends DungeonObjective {

	public BossObjective(DGDoor door, DGRoom room) {
		super(door, room);
	}

	@Override
	public void completeObjective() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasRequirements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canObtainRequirements() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void obtainRequirements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

}
