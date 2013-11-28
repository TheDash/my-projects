package libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;



public class Highscores {
	
	private static SKILL[] skills = {
		SKILL.ATTACK,
		SKILL.DEFENCE,
		SKILL.STRENGTH,
		SKILL.CONSTITUTION,
		SKILL.RANGED,
		SKILL.PRAYER,
		SKILL.MAGIC,
		SKILL.COOKING,
		SKILL.WOODCUTTING,
		SKILL.FLETCHING,
		SKILL.FISHING,
		SKILL.FIREMAKING,
		SKILL.CRAFTING,
		SKILL.SMITHING,
		SKILL.MINING,
		SKILL.HERBLORE,
		SKILL.AGILITY,
		SKILL.THIEVING,
		SKILL.SLAYER,
		SKILL.FARMING,
		SKILL.RUNECRAFTING,
		SKILL.HUNTER,
		SKILL.CONSTRUCTION,
		SKILL.SUMMONING,
		SKILL.DUNGEONEERING
	};
	
	private static final HashMap<SKILL, HighscoreStat> highscores = new HashMap<SKILL, HighscoreStat>();
	private static final String HS_URL = "http://services.runescape.com/m=hiscore/index_lite.ws?player=";
	private static String currentHighScores = "";
	
	public static void main(String args[]) {
		setHighScores("skilld0ntkil");
		System.out.println(currentHighScores);
	}
	
	public static void setHighScores(String username) {
	try {
			int lc = 0;
			
			currentHighScores = "";
			
			URL url = new URL(HS_URL+username);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = br.readLine();
			
			
			while ((line = br.readLine()) != null) {
			if (lc < skills.length) {
					SKILL lz = skills[lc];
					
					int currXP = 0;
					int currLVL = 0;
					int fir = line.indexOf(',');
					int sec = line.lastIndexOf(',');
					currXP = Integer.parseInt(line.substring(0, fir));
					currLVL = Integer.parseInt(line.substring(fir+1, sec));
					highscores.put(lz, new HighscoreStat(lz, currXP, currLVL));
				lc++;
			}
			}
			System.out.println("Successfully read the highscores.");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HighscoreStat getHighscore(final SKILL skill) {
		return highscores.get(skill);
	}
	
	
}
