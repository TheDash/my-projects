package savelets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PotionsSavelet implements Serializable {

	
	private String saveName = "";
	private int percentToDrink = 0;
	private int numPPots = 0;
	private String[] potions = null;
	
	public PotionsSavelet(String name) {
		this.saveName = name;
	}
	
	public void setPercentToDrink(int p) {
		this.percentToDrink = p;
	}
	
	public void setNumPrayerPots(int p) {
		this.numPPots = p;
	}
	
	public void setPrayerSelection(String[] selected) {
		this.potions = selected;
	}
	
	
	public void saveSavelet() {
		try {
			FileOutputStream fos = new FileOutputStream(saveName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(this);
			
			System.out.println("Successfully saved PotionsSavelet to " + saveName);
			oos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadSavelet(String name) {
		ObjectInputStream ois;
		try {
			FileInputStream fis = new FileInputStream(name);
			ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			
			if (obj instanceof PotionsSavelet) {
				PotionsSavelet ps = (PotionsSavelet) obj;
				
				this.percentToDrink = ps.percentToDrink;
				this.numPPots = ps.numPPots;
				this.potions = ps.potions;
				this.saveName = ps.saveName;
				
				System.out.println("Succecssfully loaded PotionsSavelet from " + name);
			}
			
			ois.close();
			fis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
