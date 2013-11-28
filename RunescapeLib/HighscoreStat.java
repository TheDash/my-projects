package libs;

enum SKILL {
	ATTACK, DEFENCE, STRENGTH, CONSTITUTION, RANGED, PRAYER, MAGIC, COOKING, WOODCUTTING, FLETCHING, FISHING, FIREMAKING, 
	CRAFTING, SMITHING, MINING, HERBLORE, AGILITY, THIEVING, SLAYER, FARMING, RUNECRAFTING, HUNTER, CONSTRUCTION, SUMMONING,
	DUNGEONEERING;
}

public class HighscoreStat {

	private SKILL skill;
	private int xp;
	private int level;
	
	public HighscoreStat(SKILL skill, int xp, int level) {
		this.skill = skill;
		this.xp = xp;
		this.level = level;
	}
	
	public SKILL getSkill() {
		return skill;
	}
	
	public int getXP() {
		return xp;
	}
	
	public int getLevel() {
		return level;
	}
	
}
