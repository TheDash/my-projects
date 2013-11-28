package savelets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SummoningSavelet {

	private String saveName = "";
	private String summon = "Bull ant";
	private int numOfSums = 0;
	private boolean usePakYakBanking = false;
	
	public SummoningSavelet(String name) {
		this.saveName = name;
	}
	
	public void setSummon(String monster) {
		this.summon = monster;
	}

	public void setNumber(int n) {
		this.numOfSums = n;
	}
	
	public void setPakYakBanking(boolean b) {
		this.usePakYakBanking = b;
	}
	
	public void saveSavelet() {
		try {
			FileOutputStream fos = new FileOutputStream(saveName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			System.out.println("Successfully saved SummoningSavelet to " + saveName);
			
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
			FileInputStream fin = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fin);
			
			Object obj = ois.readObject();
			
			if (obj instanceof SummoningSavelet) {
				SummoningSavelet s = (SummoningSavelet) obj;
				this.saveName = s.saveName;
				this.usePakYakBanking = s.usePakYakBanking;
				this.summon = s.summon;
				this.numOfSums = s.numOfSums;
				
				System.out.println("Successfully loaded SummoningSavelet from " + name);
			}
			
			ois.close();
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
