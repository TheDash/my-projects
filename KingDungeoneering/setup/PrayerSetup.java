package setup;

import enums.PRAYER;

import java.util.HashMap;

import libs.PrayerSelect;

public class PrayerSetup {

	private HashMap<PRAYER, PrayerSelect> prayers = new HashMap<PRAYER, PrayerSelect>();
	private String setupName = "Default";
	
	public void setName(String name) {
		setupName = name;
	}
	
	public boolean isActive(PRAYER p) {
		return prayers.get(p).isActivated();
	}
	
	public void setPrayer(PRAYER p, boolean b) {
		prayers.get(p).setPrayer(b);
	}
	
	public PrayerSelect getPrayer(PRAYER p) {
		return prayers.get(p);
	}
	
	public void addPrayer(PRAYER p) {
		switch (p) {
		case THICK_SKIN:
			prayers.put(p, new PrayerSelect(0x1, 0));
			break;
		case BURST_OF_STRENGTH:
			prayers.put(p,new PrayerSelect(0x2, 1));
			break;
		case SHARP_EYE:
			prayers.put(p, new PrayerSelect(0x40000, 3));
			break;
		case MYSTIC_WILL:
			prayers.put(p, new PrayerSelect(0x80000, 4));
			break;
		case ROCK_SKIN:
			prayers.put(p, new PrayerSelect(0x10, 6));
			break;
		case SUPERHUMAN_STRENGTH:
			prayers.put(p, new PrayerSelect(0x20, 7));
			break;
		case IMPROVED_REFLEXES:
			prayers.put(p, new PrayerSelect(0x40, 8));
			break; 
		case RAPID_RESTORE:
			prayers.put(p, new PrayerSelect(0x80, 9));
			break; 
		case RAPID_HEAL:
			prayers.put(p, new PrayerSelect(0x100, 10));
			break; 
		case PROTECT_ITEM:
			prayers.put(p, new PrayerSelect(0x100000, 11));
			break; 
		case HAWK_EYE:
			prayers.put(p, new PrayerSelect(0x200000, 12));
			break; 
		case MYSTIC_LORE:
			prayers.put(p, new PrayerSelect(0x200, 13));
			break; 
		case STEEL_SKIN:
			prayers.put(p, new PrayerSelect(0x400, 14));
			break; 
		case ULTIMATE_STRENGTH:
			prayers.put(p, new PrayerSelect(0x800, 15));
			break; 
		case INCREDIBLE_REFLEXES:
			prayers.put(p, new PrayerSelect(0x1000, 17));
			break; 
		case PROTECT_MAGIC:
			prayers.put(p,  new PrayerSelect(0x2000, 18));
			break; 
		case PROTECT_MISSILES:
			prayers.put(p, new PrayerSelect(0x4000, 19));
			break; 
		case PROTECT_MELEE:
			prayers.put(p, new PrayerSelect(0x400000, 20));
			break; 
		case EAGLE_EYE:
			prayers.put(p, new PrayerSelect(0x800000, 21));
			break; 
		case MYSTIC_MIGHT:
			prayers.put(p, new PrayerSelect(0x800000, 21));
			break; 
		case CLARITY_OF_THOUGHT:
			prayers.put(p, new PrayerSelect(0x4, 2));
			break; 
		}
	}
	
}
