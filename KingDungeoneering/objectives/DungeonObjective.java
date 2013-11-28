package objectives;

import rooms.DGRoom;
import doors.DGDoor;
import dungeon.DGDungeon;


public abstract class DungeonObjective implements Objective {

	protected boolean isComplete = false;
	protected DGDoor door;
	protected DGRoom room;
	
	public DungeonObjective(DGDoor door, DGRoom room) {
		this.room = room;
		this.door = door;
	}
	
	public DGDoor getDoor() {
		return door;
	}
	
	public DGRoom getRoom() {
		return room;
	}
}
