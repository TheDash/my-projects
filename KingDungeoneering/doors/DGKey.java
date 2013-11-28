package doors;
import java.util.ArrayList;


public class DGKey {

	private ArrayList<Integer> doorIDs;
	private String keyName;
	private int keyID;
	
	public DGKey(int keyID, String keyName, ArrayList<Integer> doorIDs) {
		this.doorIDs = doorIDs;
		this.keyID = keyID;
		this.keyName = keyName;
	}
	
	public String getName() {
		return keyName;
	}
	
	public int getID() {
		return keyID;
	}
	
	public ArrayList<Integer> getDoors() {
		return doorIDs;
	}
	
}
