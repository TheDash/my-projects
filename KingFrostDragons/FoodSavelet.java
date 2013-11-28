package savelets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FoodSavelet implements Serializable {

	private String saveName = "";
	private String[] food = null;
	
	private int pctToEat = 0;
	private int numFood = 0;
	
	public FoodSavelet(String name) {
		this.saveName = name;
	}
	
	public void setPctToEat(int n) {
		this.pctToEat = n;
	}
	
	public void setNumFood(int n) {
		this.numFood = n;
	}
	
	public void setFood(String[] food) {
		this.food = food;
	}
	
	public void saveSavelet()  {
		try {
			FileOutputStream fos = new FileOutputStream(saveName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(this);
			System.out.println("Successfully saved FoodSavelet to " + saveName);
			
			fos.close();
			oos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadSavelet(String name) {
		
		try {
			FileInputStream fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Object obj = ois.readObject();
			
			if (obj instanceof FoodSavelet) {
				
				FoodSavelet fs = (FoodSavelet) obj;
				
				this.food = fs.food;
				this.saveName = fs.saveName;
				this.pctToEat = fs.pctToEat;
				this.numFood = fs.numFood;
				System.out.println("Successfully loaded FoodSavelet from" + name);
			}
			
			fis.close();
			ois.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
			
		
	}
	
}
