package libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ItemDatabase {

	private static final String getByNameURL = "http://tip.it/runescape/index.php?gec=&action=search&itemsearch=";
	
	public static void main(String args[]) {
		Long start = System.currentTimeMillis();
		ItemInfo ii = ItemDatabase.getItem("Staff of fire");
		System.out.println("Time taken: " + (float)(System.currentTimeMillis() - start)/1000+" seconds");
	}
	
	public static ItemInfo getItem(String name) {
		URL url;
		
		int id = 0;
		
		name = name.replace(" ", "+");
		name = name.replace("'", "%27");
		String urlName = getByNameURL+name;
		
		try {
			url = new URL(urlName);
			System.out.println("URL :"+urlName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				if (line.contains("<a href=\"?gec&itemid=")) {
					int ids = line.indexOf("<a href=\"?gec&itemid=");
					int pos = ids + 21;
					String op = line.substring(pos, pos+5);
					op = op.replaceAll("\"", "");
					op = op.replaceAll(">", "");
					id = Integer.parseInt(op);
				}
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		name = name.replace("+", " ");
		return new ItemInfo(name, id);
		
	}
	
}
