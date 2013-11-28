package dungeon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import objectives.BossObjective;
import objectives.DoorObjective;
import objectives.DungeonObjective;
import objectives.GuardianObjective;
import objectives.KeyObjective;
import objectives.SkillObjective;

import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

import rooms.DGRoom;

import doors.BasicDoor;
import doors.BossDoor;
import doors.DGDoor;
import doors.GuardianDoor;
import doors.KeyDoor;
import doors.SkillDoor;


public class DGDungeon {

	public static final int HEIGHT = 13;
	public static final int WIDTH = HEIGHT;
	
	private static int DUNGEON_HEIGHT = 4;
	private static int DUNGEON_WIDTH = 4;
	
	private int complexity = 0;
	private int floor      = 0;
	private static int roomCount  = 0;
	
	private static DGRoom currentRoom;
	private static DGRoom startRoom = null;
	
	private static final HashMap<Rectangle, DGRoom> rooms = new HashMap<Rectangle, DGRoom>();
	
	private static DungeonObjective currentObjective;
	
	private boolean isComplete = false;
	
	
	private static ConcurrentLinkedQueue<DungeonObjective> objectiveQueue = new ConcurrentLinkedQueue<DungeonObjective>();
	
	public DGDungeon() {
		
	}
	
	public static int getObjectiveCount() {
		return objectiveQueue.size();
	}
	
	public static boolean hasObjective(DungeonObjective doe) {
		return objectiveQueue.contains(doe);
	}
	
	public static void addObjective(DungeonObjective doe) {
		objectiveQueue.offer(doe);
	}
	
	public static DungeonObjective getNextObjective() {
		System.out.println("#Objectives: "+getObjectiveCount());
		return objectiveQueue.poll();
	}
	
	public DGRoom getStartRoom() {
		return startRoom;
	}

	public static DGRoom locateRoom(Tile t) {
		Set<Rectangle> recs = rooms.keySet();
		int x = t.getX();
		int y = t.getY();
		
		for (Rectangle rec : recs) {
			if (rec.contains(x, y)) {
				return rooms.get(rec);
			}
		}
		return null;
	}
	
	public void setObjective(DungeonObjective doe) {
		currentObjective = doe;
	}
	
	public static DungeonObjective getCurrentObjective() {
		return currentObjective;
	}
	
	public boolean containsRoom(DGRoom r) {
		return rooms.containsKey(r.getRoom());
	}
	
	public boolean containsArea(Area a) {
		return rooms.containsKey(a);
	}
	
	public int setFloor(int c) {
		return floor;
	}
	
	public void setComplexity(int c) {
		this.complexity = c;
	}
	
	public static int getRoomCount() {
		return roomCount;
	}
	
	public int getFloor() {
		return floor;
	}
	
	public static Collection<DGRoom> getRooms() {
		return Collections.synchronizedCollection(rooms.values());
	}
	
	public int getComplexity() {
		return complexity;
	}
	
	public void addRoom(DGRoom rm) {
		if (startRoom == null) {
			startRoom = rm;
		} else {
			Collection<DGRoom> roomz = rooms.values();
			for (DGRoom room : roomz) {
				
				if (rm.isEastOf(room)) {
					room.setEastRoom(rm);
					rm.setWestRoom(room);
				} else if (rm.isWestOf(room)) {
					room.setWestRoom(rm);
					rm.setEastRoom(room);
				} else if (rm.isNorthOf(room)) {
					rm.setSouthRoom(room);
					room.setNorthRoom(rm);
				} else if (rm.isSouthOf(room)) {
					rm.setNorthRoom(room);
					room.setSouthRoom(rm);
				}
			}
		}
		
		DGDungeon.rooms.put(rm.getRoom(), rm);
		roomCount++;
	}
	
	public static DGRoom getCurrentRoom() {
		return currentRoom;
	}
	
	public static void setCurrentRoom(DGRoom dgRoom) {
		DGDungeon.currentRoom = dgRoom;
	}
	
	public boolean isComplete() {
		if (!isComplete) {
			for (DGRoom rm : rooms.values()) {
				if (!rm.isComplete()) {
					return false;
				}
			}
		}
		return true;
	}

	static Stack<DGRoom> path = null;
	static DGRoom nextRoom = null;
	
	public static boolean traversePath(Tile goal) {

		
		Stack<DGRoom> pathTmp = getDungeonPath(goal);
		if (pathTmp != path) {
			System.out.println("Different path.");
			path = pathTmp;
		}

		Tile pLoc = Players.getLocal().getLocation();

		if (!nextRoom.containsTile(pLoc)) {
			System.out.println("We arent in the next room yet.");
			DGDoor nextDoor = getCurrentRoom().getConnectedDoor(nextRoom);
			if (nextDoor != null) {
				System.out.println("next door to open is : "
						+ nextDoor.getName());
				nextDoor.openDoor();
				Time.sleep(2000);
			}
		} else if (!path.empty() && nextRoom.containsTile(pLoc)) {
			System.out
					.println("we're in the next room, setting our next room.");
			nextRoom = path.pop();
		}

		if (pLoc != goal && path.empty()) {
			System.out.println("trying to walk to our gooal.");
			Walking.walk(goal);
		}
		
		if (pLoc == goal) {
			
		}
		
		return true;
	}
	
	private static Stack<DGRoom> reconstructPath(DGRoom room) {
		
		Stack<DGRoom> roomPath = new Stack<DGRoom>();
		DGRoom cameFrom = null;
		
		roomPath.push(room);
		
		while ((cameFrom = room.getCameFrom()) != currentRoom) {
			roomPath.push(cameFrom);
			room = cameFrom;
		}
		
		roomPath.push(cameFrom);
		
		return roomPath;
	}
	
	private static Stack<DGRoom> getDungeonPath(Tile goal) {
	
		ConcurrentLinkedQueue<DGRoom> frontier = new ConcurrentLinkedQueue<DGRoom>();
		ConcurrentLinkedQueue<DGRoom> checked = new ConcurrentLinkedQueue<DGRoom>();
		
		DGRoom polled = null;
		
		if (polled == null) {
			polled = DGDungeon.currentRoom;
			frontier.add(polled);
		}
		
		for (int i = 0; i < DUNGEON_HEIGHT*DUNGEON_WIDTH; i++) {
			DGRoom previous = frontier.poll();
			
			if (previous.containsTile(goal)) {
				return reconstructPath(previous);
			}
			
			Collection<DGRoom> roomsAround = previous.getConnectedRooms();
			
			for (DGRoom room : roomsAround) {
				
				if (!checked.contains(room)) {
					room.setCameFrom(previous);
					frontier.add(room);
					checked.add(room);
				}
			}
		}
		
		return null;
	}
	
}
