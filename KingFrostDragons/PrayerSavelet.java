package savelets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import enums.PRAYER;

@SuppressWarnings("serial")
public class PrayerSavelet implements Serializable{

	private String saveName = "";
	
	private boolean isNormalBook = true;
	private PRAYER[] prayerSelection = null;
	
	public PrayerSavelet(String name) {
		
	}
	
	public void setNormalBook(boolean b) {
		this.isNormalBook = b;
	}
	
	public void setPrayerSelection(PRAYER[] p) {
		this.prayerSelection = p;
	}
	
	public void saveSavelet() {
		try {
			FileOutputStream fos = new FileOutputStream(saveName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			System.out.println("Successfully saved PrayerSavelet to " + saveName);
			oos.close();
			fos.close();
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
			
			if (obj instanceof PrayerSavelet) {
				
				PrayerSavelet ps = (PrayerSavelet) obj;
				this.saveName = ps.saveName;
				this.prayerSelection = ps.prayerSelection;
				this.isNormalBook = ps.isNormalBook;
				
				System.out.println("Successully loaded PrayerSavelet from " + name);
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
