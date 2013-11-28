package rooms;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import objectives.DungeonObjective;

import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import doors.DGDoor;
import doors.KeyDoor;


public class DGRoom {
	
	private Rectangle room = null;
	
	private int roomNumber;
	
	private ArrayList<DGDoor> doors = new ArrayList<DGDoor>();
	private ConcurrentHashMap<Tile, Tile> tilesChecked;
	
	private DGDoor northDoor;
	private DGDoor southDoor;
	private DGDoor eastDoor;
	private DGDoor westDoor;
	
	private DGRoom northRoom;
	private DGRoom southRoom;
	private DGRoom eastRoom;
	private DGRoom westRoom;
	
	private boolean isComplete = false;
	
	private DGRoom cameFrom;
	private DGDoor pathDoor;
	
	public DGRoom(ArrayList<DGDoor> doors,Rectangle roomArea, ConcurrentHashMap<Tile, Tile> chkd, int roomNumber) {
		this.doors = doors;
		this.room = roomArea;
		this.tilesChecked = chkd;
		this.roomNumber = roomNumber;
		positionDoors(doors);
	}
	
	public int getRoomNumber() {
		return this.roomNumber;
	}
	
	public DGRoom getCameFrom() {
		return cameFrom;
	}
	
	
	public boolean isOpen(DGDoor door) {
		
		if (door.equals(this.northDoor)) {
			return hasNorthRoom();
		}
		
		if (door.equals(this.eastDoor)) {
			return hasEastRoom();
		}
		
		if (door.equals(this.westDoor)) {
			return hasWestRoom();
		}
		
		if (door.equals(this.southDoor)) {
			return hasSouthRoom();
		}
		
		return false;
	}
	
	public DGDoor getConnectedDoor(DGRoom room) {
		
		if (room.equals(this.northRoom)) {
			return this.northDoor;
		}
		
		if (room.equals(this.southRoom)) {
			return this.southDoor;
		}
		
		if (room.equals(this.eastRoom)) {
			return this.eastDoor;
		}
		
		if (room.equals(this.westRoom)) {
			return this.westDoor;
		}
		
		return null;
	}
	
	public void setCameFrom(DGRoom came) {
		this.cameFrom = came;
	}
	
	public void setPathDoor(DGDoor door) {
		this.pathDoor = door;
	}
	
	public DGDoor getPathDoor() {
		return pathDoor;
	}
	
	
	public boolean isNorthOf(DGRoom rm) {
		DGDoor north = rm.getNorthDoor();
		if (southDoor != null && north != null) {
			Tile northLoc = north.getLocation();
			Tile southLoc = southDoor.getLocation();
			
			Tile belowSouth1 = new Tile(southLoc.getX(), southLoc.getY()-1, 0);
			Tile belowSouth2 = new Tile(southLoc.getX()-1, southLoc.getY()-1, 0);
			Tile belowSouth3 = new Tile(southLoc.getX()+1, southLoc.getY()-1, 0);
			
			return (belowSouth1.equals(northLoc) || belowSouth2.equals(northLoc) || belowSouth3.equals(northLoc));
			
		}
		return false;
	}
	
	public boolean isSouthOf(DGRoom rm) {
		DGDoor south = rm.getSouthDoor();
		if (northDoor != null && south != null) {
			Tile northLoc = northDoor.getLocation();
			Tile southLoc = south.getLocation();
			
			Tile aboveNorth1 = new Tile(northLoc.getX(), northLoc.getY()+1, 0);
			Tile aboveNorth2 = new Tile(northLoc.getX()+1, northLoc.getY()+1, 0);
			Tile aboveNorth3 = new Tile(northLoc.getX()-1, northLoc.getY()+1, 0);
			
			return (southLoc.equals(aboveNorth1) || southLoc.equals(aboveNorth2) || southLoc.equals(aboveNorth3));
			
		}
		return false;
	}
	
	public boolean isEastOf(DGRoom rm) {
		DGDoor east = rm.getEastDoor();
		if (westDoor != null && east != null) {
			
			Tile eastLoc = east.getLocation();
			Tile westLoc = westDoor.getLocation();
			
			Tile sideEast1 = new Tile(westLoc.getX()-1, westLoc.getY(), 0);
			Tile sideEast2 = new Tile(westLoc.getX()-1, westLoc.getY()+1, 0);
			Tile sideEast3 = new Tile(westLoc.getX()-1, westLoc.getY()-1, 0);
			
			return (eastLoc.equals(sideEast1) || eastLoc.equals(sideEast2) || eastLoc.equals(sideEast3));
		}
		return false;
	}
	
	public boolean isWestOf(DGRoom rm) {
		DGDoor west = rm.getWestDoor();
		if (eastDoor != null && west != null) {
			
			Tile westLoc = west.getLocation();
			Tile eastLoc = eastDoor.getLocation();
			
			Tile sideWest1 = new Tile(eastLoc.getX()+1, eastLoc.getY(), 0);
			Tile sideWest2 = new Tile(eastLoc.getX()+1, eastLoc.getY()+1, 0);
			Tile sideWest3 = new Tile(eastLoc.getX()+1, eastLoc.getY()-1, 0);
			
			return (westLoc.equals(sideWest1) || westLoc.equals(sideWest2) || westLoc.equals(sideWest3));
		}
		return false;
	}
	
	public Collection<DGRoom> getConnectedRooms() {
		
		Collection<DGRoom> cd = new ArrayList<DGRoom>();
		
		if (hasNorthRoom()) {
			cd.add(northRoom);
		}
		
		if (hasSouthRoom()) {
			cd.add(southRoom);
		}
		
		if (hasEastRoom()) {
			cd.add(eastRoom);
		}
		
		if (hasWestRoom()) {
			cd.add(westRoom);
		}
		
		return cd;
	}
	
	public boolean hasNorthRoom() {
		return this.northRoom != null;
	}
	
	public boolean hasSouthRoom() {
		return this.southRoom != null;
	}
	
	public boolean hasEastRoom() {
		return this.eastRoom != null;
	}
	
	public boolean hasWestRoom() {
		return this.westRoom != null;
	}
	
	public DGRoom getNorthRoom() {
		return this.northRoom;
	}
	
	public DGRoom getSouthRoom() {
		return this.southRoom;
	}
	
	public DGRoom getEastRoom() {
		return this.eastRoom;
	}
	
	public DGRoom getWestRoom() {
		return this.westRoom;
	}
	
	public void setNorthRoom(DGRoom rm) {
		this.northRoom = rm;
	}
	
	public void setSouthRoom(DGRoom rm) {
		this.southRoom = rm;
	}
	
	public void setEastRoom(DGRoom rm) {
		this.eastRoom = rm;
	}
	
	public void setWestRoom(DGRoom rm) {
		this.westRoom = rm;
	}
	
	public void setRoom(Rectangle a) {
		this.room = a;
	}
	
	public Enumeration<Tile> getRoomTiles() {
		return tilesChecked.keys();
	}
	
	public ArrayList<DGDoor> getDoors() {
		
		final ArrayList<DGDoor> doo = new ArrayList<DGDoor>();
		
		if (this.northDoor != null) {
			doo.add(northDoor);
		}
		
		if (this.southDoor != null) {
			doo.add(southDoor);
		}
		
		if (this.eastDoor != null) {
			doo.add(eastDoor);
		}
		
		if (this.westDoor != null) {
			doo.add(westDoor);
		}
	
		return doo;
	}
	
	public boolean containsTile(Tile t) {
		if ( t != null) {
			return room.contains(t.getX(), t.getY());
		}
		return false;
	}
	
	public boolean hasNorthDoor() {
		return this.northDoor != null;
	}

	public boolean hasSouthDoor() {
		return this.southDoor != null;
	}
	
	public boolean hasEastDoor() {
		return this.eastDoor != null;
	}
	
	public boolean hasWestDoor() {
		return this.westDoor != null;
	}
	
	public DGDoor getNorthDoor() {
		return this.northDoor;
	}
	
	public DGDoor getSouthDoor() {
		return this.southDoor;
	}
	
	public DGDoor getWestDoor() {
		return this.westDoor;
	}
	
	public DGDoor getEastDoor() {
		return this.eastDoor;
	}
	
	public boolean containsDoor(DGDoor door) {
		return doors.contains(door);
	}
	
	private void positionDoors(ArrayList<DGDoor> doors) {
		for (DGDoor door : doors) {
			if (door instanceof KeyDoor) {
				KeyDoor keyDoor = (KeyDoor) door;
				
				if (keyDoor.isNorth()) {
					this.northDoor = door;
				}
				
				if (keyDoor.isSouth()) {
					this.southDoor = door;
				}
				
				if (keyDoor.isEast()) {
					this.eastDoor = door;
				}
				
				if (keyDoor.isWest()) {
					this.westDoor = keyDoor;
				}
				
			} else {
			
			if (door.isNorth()) {
				this.northDoor = door;
			}
			else if (door.isSouth()) {
				this.southDoor = door;
			}
			else if (door.isWest()) {
				this.westDoor = door;
			}
			
			else if (door.isEast()) {
				this.eastDoor = door;
			}
			}
		}
	}
	
	public boolean northDoorOpened() {
		return hasNorthDoor() && hasNorthRoom();
	}
	
	public boolean southDoorOpened() {
		return hasSouthDoor() && hasSouthRoom();
	}
	
	public boolean westDoorOpened() {
		return hasWestDoor() && hasWestRoom();
	}
	
	public boolean eastDoorOpened() {
		return hasEastDoor() && hasEastRoom();
	}
	
	public boolean isComplete() {
		
		ArrayList<DGDoor> dooz = getDoors();
		
		for (DGDoor door : dooz) {
			if (door == this.northDoor) {
				if (!northDoorOpened()) {
					return false;
				}
			}
			
			if (door == this.southDoor) {
				if (!southDoorOpened()) {
					return false;
				}
			}
			
			if (door == this.eastDoor) {
				if (!eastDoorOpened()) {
					return false;
				}
			}
			
			if (door == this.westDoor) {
				if (!westDoorOpened()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void addDoors(DGDoor[] doors) {
		for (int i = 0; i < doors.length ; i++) {
			this.doors.add(doors[i]);
		}
	}
	
	public Rectangle getRoom() {
		return room;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((doors == null) ? 0 : doors.hashCode());
		result = prime * result + (isComplete ? 1231 : 1237);
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DGRoom) {
			DGRoom room = (DGRoom) obj;
			return 
					this.room.equals(room.getRoom());
		}
		return false;
	}
	

}
