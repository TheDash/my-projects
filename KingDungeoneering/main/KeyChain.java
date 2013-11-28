package main;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;

import rooms.DGRoom;

import doors.DGDoor;
import doors.DGKey;
import dungeon.DGDungeon;

public class KeyChain implements MessageListener {

	private final static String KEY_MSG = "Your party found a key:";
	private static ConcurrentHashMap<String, DGKey> keyChain = new ConcurrentHashMap<String, DGKey>();
	
	
	private DGDungeon currentDungeon;
	
	public KeyChain(DGDungeon dungeon) {
		this.currentDungeon = dungeon;
	}
	
	@Override
	public void messageReceived(MessageEvent arg0) {
		
		String msg = arg0.getMessage();
		System.out.println("LOOK HJERE AT OUR MESSAGE : " + msg);
		if (msg.contains(KEY_MSG)) {
			String key = parseKey(msg);
			int id = SpiffyDungeons.db.getItem(key);
			String door = key.replace("key", "door");
			ArrayList<Integer> doors = SpiffyDungeons.db.getObjects(door);
			
			DGKey dk = new DGKey(id, key, doors);
			System.out.println("Added " + key + " to keychain.");
			keyChain.put(key, dk);
			
//			if (currentDungeon != null) {
//				
//				
//				System.out.println("We can now open the door " + cDoor.getName());
//				
////				Collection<DGRoom> rooms = currentDungeon.getRooms();
////				if (rooms != null) {
////					for (DGRoom room : rooms) {
////						if (room != null) {
////							ArrayList<DGDoor> doors1 = room.getDoors();
////							for (DGDoor dor : doors1) {
////								if (dor != null) {
////									if (dor.getName().equals(door)) {
////										System.out.println("We can now open the door: " + dor.getName());
////										dor.setCanComplete(true);
////									}
////								}
////							}
////						}
////					}
////				}
//			}
		}
	}
	
	public static Set<String> getKeys() {
		return keyChain.keySet();
	}
	
	private static String parseKey(String msg) {
		return msg = msg.replace(KEY_MSG, "");
	}
	
	public  static  DGKey getKey(String key) {
		return keyChain.get(key);
	}
	
	public static boolean contains(String key) {
		return keyChain.containsKey(key);
	}
	
	

}
