package savelets;

import java.io.Serializable;

abstract class Savelet implements Serializable {
	
	private static String saveName = "";
	
	public Savelet(String name) {
		this.saveName = name;
	}
	
}
