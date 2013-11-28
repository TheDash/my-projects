package libs;



public class PrayerInfo {

	
	
	private static final PRAYER[] prayers = { PRAYER.THICK_SKIN,
			PRAYER.BURST_OF_STRENGTH, PRAYER.SHARP_EYE, PRAYER.MYSTIC_WILL,
			PRAYER.ROCK_SKIN, PRAYER.SUPERHUMAN_STRENGTH,
			PRAYER.IMPROVED_REFLEXES, PRAYER.RAPID_RESTORE, PRAYER.RAPID_HEAL,
			PRAYER.PROTECT_ITEM, PRAYER.HAWK_EYE, PRAYER.MYSTIC_LORE,
			PRAYER.STEEL_SKIN, PRAYER.ULTIMATE_STRENGTH,
			PRAYER.INCREDIBLE_REFLEXES, PRAYER.PROTECT_MAGIC,
			PRAYER.PROTECT_MISSILES, PRAYER.PROTECT_MELEE, PRAYER.EAGLE_EYE,
			PRAYER.MYSTIC_MIGHT, PRAYER.CLARITY_OF_THOUGHT };

	public final static PRAYER[] getPrayers() {
		return prayers;
	}

	public final PrayerSelect getPrayer(PRAYER p) {
		switch (p) {
		case THICK_SKIN:
			return new PrayerSelect(0x1, 0);
		case BURST_OF_STRENGTH:
			return new PrayerSelect(0x2, 1);
		case SHARP_EYE:
			return new PrayerSelect(0x40000, 3);
		case MYSTIC_WILL:
			return new PrayerSelect(0x80000, 4);
		case ROCK_SKIN:
			return new PrayerSelect(0x10, 6);
		case SUPERHUMAN_STRENGTH:
			return new PrayerSelect(0x20, 7);
		case IMPROVED_REFLEXES:
			return new PrayerSelect(0x40, 8);
		case RAPID_RESTORE:
			return new PrayerSelect(0x80, 9);
		case RAPID_HEAL:
			return new PrayerSelect(0x100, 10);
		case PROTECT_ITEM:
			return new PrayerSelect(0x100000, 11);
		case HAWK_EYE:
			return new PrayerSelect(0x200000, 12);
		case MYSTIC_LORE:
			return new PrayerSelect(0x200, 13);
		case STEEL_SKIN:
			return new PrayerSelect(0x400, 14);
		case ULTIMATE_STRENGTH:
			return new PrayerSelect(0x800, 15);
		case INCREDIBLE_REFLEXES:
			return new PrayerSelect(0x1000, 17);
		case PROTECT_MAGIC:
			return new PrayerSelect(0x2000, 18);
		case PROTECT_MISSILES:
			return new PrayerSelect(0x4000, 19);
		case PROTECT_MELEE:
			return new PrayerSelect(0x400000, 20);
		case EAGLE_EYE:
			return new PrayerSelect(0x800000, 21);
		case MYSTIC_MIGHT:
			return new PrayerSelect(0x800000, 21);
		case CLARITY_OF_THOUGHT:
			return new PrayerSelect(0x4, 2);
		}
		return null;
	}
}
