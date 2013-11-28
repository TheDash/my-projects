package objectives;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;

import rooms.DGRoom;
import doors.DGDoor;
import doors.GuardianDoor;
import dungeon.DGDungeon;

public class GuardianObjective extends DungeonObjective {

	public GuardianObjective(DGDoor door, DGRoom room) {
		super(door, room);
		this.door = door;
	}

	private NPC currentNPC;

	
	private void setCurrentNPC() {
		NPC[] npcs = NPCs.getLoaded();
		for (int i = 0; i < npcs.length; i++) {
			NPC np = npcs[i];
			Tile t = np.getLocation();
			int x = t.getX();
			int y = t.getY();

			Rectangle currRoom = DGDungeon.getCurrentRoom().getRoom();
			if (currRoom.contains(x, y)) {
				currentNPC = np;
				return;
			}
		}
	}

	@Override
	public void completeObjective() {
		if (!guardianRoomComplete()) {
			if (currentNPC == null || !Players.getLocal().isInCombat()) {
				setCurrentNPC();
				if (currentNPC != null) {
					currentNPC.interact("Attack");
				}
			}
		} else {
			this.door.openDoor();
		}
	}

	private boolean guardianRoomComplete() {
		NPC[] npcs = NPCs.getLoaded();
		int c = 0;
		for (int i = 0; i < npcs.length; i++) {
			NPC n = npcs[i];
			if (n != null) {
				Tile t = n.getLocation();
				int x = t.getX();
				int y = t.getY();
				if (room != null) {
					Rectangle r = room.getRoom();
					if (r != null) {
						if (r.contains(x, y) && hasOption(n, "Attack")) {
							c++;
						}
					}
				}
			}
		}

		return c == 0;
	}

	private boolean hasOption(NPC n, String s) {
		String[] acs = n.getActions();
		for (int i = 0; i < acs.length; i++) {
			if (acs[i] != null) {
				if (acs[i].equals(s)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasRequirements() {
		if (room != null) {
			Tile pLoc = Players.getLocal().getLocation();
			Rectangle r = room.getRoom();
			
			int x = pLoc.getX();
			int y = pLoc.getY();
			
			return r.contains(x, y);
		}
		return false;
	}

	@Override
	public boolean canObtainRequirements() {
		Collection<DGRoom> rooms = DGDungeon.getRooms();
		for (DGRoom room : rooms) {
			ArrayList<DGDoor> doors = room.getDoors();
			for (DGDoor door : doors) {
				if (door.getName().equals(this.door.getName()) && door.getLocation().equals(this.door.getLocation())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void obtainRequirements() {
		if (!this.room.containsTile(Players.getLocal().getLocation())) {
			System.out.println("Trying to find a path to : " + this.getDoor().getName());
			DGDungeon.traversePath(this.door.getLocation());
		}
 	}

	@Override
	public boolean isComplete() {
		if (this.door.isNorth()) {
			return this.room.hasNorthRoom();
		}
		
		if (this.door.isEast()) {
			return this.room.hasEastRoom();
		}
		
		if (this.door.isWest()) {
			return this.room.hasWestRoom();
		}
		
		if (this.door.isSouth()) {
			return this.room.hasSouthRoom();
		}
		
		return false;
	}
	

}
