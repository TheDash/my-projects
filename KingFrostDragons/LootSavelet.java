package savelets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LootSavelet implements Serializable {

	
	private String saveName = "";
	private String[] loot = null;
	
	public LootSavelet(String name) {
		this.saveName = name;
	}
	
	public void setLoot(String[] loot) {
		this.loot = loot;
	}
	
	public void saveSavelet() {
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(saveName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(this);
			
			System.out.println("Successfully saved LootSavelet to " + saveName);
			fos.close();
			oos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadSavelet(String name) {
		FileInputStream fis;
		try {
			
			fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Object obj = ois.readObject();
			
			if (obj instanceof LootSavelet) {
				
				LootSavelet l = (LootSavelet) obj;
				
				this.loot = l.loot;
				this.saveName = l.saveName;
				
				System.out.println("Successfully loaded LootSavelet from " + name);
			}
			
			ois.close();
			fis.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
