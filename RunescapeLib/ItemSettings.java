package libs;

import java.util.HashMap;

import libs.FOOD;
import libs.LOOT;
import libs.POTIONS;
import libs.POUCHES;
import libs.TELEPORTS;

public class ItemSettings {
	
	static POUCHES[] summonEnums = {
		POUCHES.SPIRIT_TERRORBIRD, POUCHES. BULL_ANT, POUCHES. WAR_TORTOISE, POUCHES. UNICORN_STALLION, POUCHES. PACK_YAK, POUCHES. WINTER_STORAGE_SCROLL
	};

	static LOOT[] lootEnums = { LOOT.FROSTDRAGON_BONES, LOOT.BLUE_CHARM,
			LOOT.CRIMSON_CHARM, LOOT.GOLD_CHARM, LOOT.GREEN_CHARM,
			LOOT.BLOOD_RUNES, LOOT.DEATH_RUNES, LOOT.COINS,
			LOOT.CLUE_SCROLL, LOOT.RUNE_LONGSWORD, LOOT.WATER_ORB,
			LOOT.STARVED_ANCIENT_EFFIGY, LOOT.PURE_ESSENCE,
			LOOT.DRACONIC_VISAGE, LOOT.GRIMY_RANARR, LOOT.GRIMY_AVANTOE,
			LOOT.GRIMY_IRIT, LOOT.GRIMY_CADANTINE, LOOT.SHARK,
			LOOT.CLEAN_RANARR, LOOT.CLEAN_TOADFLAX, LOOT.CLEAN_TORSTOL,
			LOOT.CLEAN_SNAPDRAGON, LOOT.MAGIC_SEED, LOOT.TORSTOL_SEED,
			LOOT.YEW_SEED, LOOT.UNCUT_DIAMOND, LOOT.UNCUT_RUBY,
			LOOT.RAW_SHARK, LOOT.RUNE_BAR, LOOT.YEW_LOGS, LOOT.COAL,
			LOOT.ADAMANTITE_ORE, LOOT.RUNITE_ORE, LOOT.DRAGON_DAGGER,
			LOOT.DRAGON_HELM, LOOT.BATTLESTAFF, LOOT.RUNE_ARROW,
			LOOT.SHIELD_LEFT_HALF, LOOT.ONYX_BOLTS, LOOT.LOOP_HALF,
			LOOT.TOOTH_HALF, LOOT.VECNA_SKULL, LOOT.SARADOMIN_BREW };
	
	
	static FOOD[] foodEnums = { FOOD.TROUT, FOOD.SALMON, FOOD.TUNA,
			FOOD.LOBSTER, FOOD.SWORDFISH, FOOD.PIKE, FOOD.COOKED_CHICKEN,
			FOOD.BASS, FOOD.POTATO_WITH_CHEESE, FOOD.SHARK, FOOD.CAVEFISH,
			FOOD.SEA_TURTLE, FOOD.MANTA_RAY, FOOD.TUNA_POTATO,
			FOOD.ROCKTAIL, FOOD.BARON_SHARK, FOOD.JUJU_GUMBO };

	static POTIONS[] potionEnums = { POTIONS.ATTACK_1, POTIONS.ATTACK_2,
			POTIONS.ATTACK_3, POTIONS.ATTACK_4, POTIONS.STRENGTH_1,
			POTIONS.STRENGTH_2, POTIONS.STRENGTH_3, POTIONS.STRENGTH_4,
			POTIONS.DEFENSE_1, POTIONS.DEFENSE_2, POTIONS.DEFENSE_3,
			POTIONS.DEFENSE_4, POTIONS.COMBAT_1, POTIONS.COMBAT_2,
			POTIONS.COMBAT_3, POTIONS.COMBAT_4, POTIONS.PRAYER_1,
			POTIONS.PRAYER_2, POTIONS.PRAYER_3, POTIONS.PRAYER_4,
			POTIONS.SUPER_ATTACK_1, POTIONS.SUPER_ATTACK_2,
			POTIONS.SUPER_ATTACK_3, POTIONS.SUPER_ATTACK_4,
			POTIONS.SUPER_DEFENSE_1, POTIONS.SUPER_DEFENSE_2,
			POTIONS.SUPER_DEFENSE_3, POTIONS.SUPER_DEFENSE_4,
			POTIONS.SUPER_STRENGTH_1, POTIONS.SUPER_STRENGTH_2,
			POTIONS.SUPER_STRENGTH_3, POTIONS.SUPER_STRENGTH_4,
			POTIONS.ANTIFIRE_1, POTIONS.ANTIFIRE_2, POTIONS.ANTIFIRE_3,
			POTIONS.ANTIFIRE_4, POTIONS.RANGING_1, POTIONS.RANGING_2,
			POTIONS.RANGING_3, POTIONS.RANGING_4, POTIONS.SARADOMIN_BREW_1,
			POTIONS.SARADOMIN_BREW_2, POTIONS.SARADOMIN_BREW_3,
			POTIONS.SARADOMIN_BREW_4, POTIONS.SUPER_ANTIFIRE_1,
			POTIONS.SUPER_ANTIFIRE_2, POTIONS.SUPER_ANTIFIRE_3,
			POTIONS.SUPER_ANTIFIRE_4, POTIONS.EXTREME_ATTACK_1,
			POTIONS.EXTREME_ATTACK_2, POTIONS.EXTREME_ATTACK_3,
			POTIONS.EXTREME_ATTACK_4, POTIONS.EXTREME_DEFENCE_1,
			POTIONS.EXTREME_DEFENCE_2, POTIONS.EXTREME_DEFENCE_3,
			POTIONS.EXTREME_DEFENCE_4, POTIONS.EXTREME_STRENGTH_1,
			POTIONS.EXTREME_STRENGTH_2, POTIONS.EXTREME_STRENGTH_3,
			POTIONS.EXTREME_STRENGTH_4, POTIONS.EXTREME_RANGING_1,
			POTIONS.EXTREME_RANGING_2, POTIONS.EXTREME_RANGING_3,
			POTIONS.EXTREME_RANGING_4, POTIONS.SUPER_PRAYER_1,
			POTIONS.SUPER_PRAYER_2, POTIONS.SUPER_PRAYER_3,
			POTIONS.SUPER_PRAYER_4, POTIONS.PRAYER_RENEWAL_1,
			POTIONS.PRAYER_RENEWAL_2, POTIONS.PRAYER_RENEWAL_3,
			POTIONS.PRAYER_RENEWAL_4, POTIONS.OVERLOAD_1,
			POTIONS.OVERLOAD_2, POTIONS.OVERLOAD_3, POTIONS.OVERLOAD_4 };

	static HashMap<POTIONS, ItemInfo> potions;
	static HashMap<LOOT, ItemInfo> loot;
	static HashMap<POUCHES, ItemInfo> pouches;
	static HashMap<FOOD, ItemInfo> food;
	static HashMap<TELEPORTS, ItemInfo> teleportItems;
	static HashMap<String, ItemInfo> depositItems;
	static HashMap<String, ItemInfo> withdrawlItems;
	static HashMap<String, ItemInfo> droppables;

	public static ItemInfo getFoodInfo(FOOD food) {
		switch (food) {
		case LOBSTER:
			return new ItemInfo("Lobster", 179);
		case TUNA:
			return new ItemInfo("Tuna", 361);
		case TROUT:
			return new ItemInfo("Trout", 333);
		case JUJU_GUMBO:
			return new ItemInfo("Juju gumbo", 19949);
		case SALMON:
			return new ItemInfo("Salmon", 329);
		case SWORDFISH:
			return new ItemInfo("Swordfish", 373);
		case PIKE:
			return new ItemInfo("Pike", 351);
		case COOKED_CHICKEN:
			return new ItemInfo("Cooked chicken", 2140);
		case BASS:
			return new ItemInfo("Bass", 365);
		case POTATO_WITH_CHEESE:
			return new ItemInfo("Potato with cheese", 6705);
		case SHARK:
			return new ItemInfo("Shark", 385);
		case CAVEFISH:
			return new ItemInfo("Cavefish", 15266);
		case SEA_TURTLE:
			return new ItemInfo("Sea turtle", 397);
		case MANTA_RAY:
			return new ItemInfo("Manta ray", 391);
		case TUNA_POTATO:
			return new ItemInfo("Tuna potato", 7060);
		case ROCKTAIL:
			return new ItemInfo("Rocktail", 15272);
		case BARON_SHARK:
			return new ItemInfo("Baron shark", 19948);
		default:
			return null;
		}
	}

	public static ItemInfo getPotionInfo(POTIONS potion) {
		switch (potion) {
		case ATTACK_1:
			return new ItemInfo("Attack potion (1)", 125);
		case ATTACK_2:
			return new ItemInfo("Attack potion (2)", 123);
		case ATTACK_3:
			return new ItemInfo("Attack potion (3)", 121);
		case ATTACK_4:
			return new ItemInfo("Attack potion (4)", 2429);
		case STRENGTH_1:
			return new ItemInfo("Strength potion (1)", 119);
		case STRENGTH_2:
			return new ItemInfo("Strength potion (2)", 117);
		case STRENGTH_3:
			return new ItemInfo("Strength potion (3)", 115);
		case STRENGTH_4:
			return new ItemInfo("Strength potion (4)", 113);
		case DEFENSE_1:
			return new ItemInfo("Defence potion (1)", 137);
		case DEFENSE_2:
			return new ItemInfo("Defence potion (2)", 135);
		case DEFENSE_3:
			return new ItemInfo("Defence potion (3)", 133);
		case DEFENSE_4:
			return new ItemInfo("Defence potion (4)", 2432);
		case COMBAT_1:
			return new ItemInfo("Combat potion (1)", 9745);
		case COMBAT_2:
			return new ItemInfo("Combat potion (2)", 9743);
		case COMBAT_3:
			return new ItemInfo("Combat potion (3)", 9741);
		case COMBAT_4:
			return new ItemInfo("Combat potion (4)", 9739);
		case PRAYER_1:
			return new ItemInfo("Prayer potion (1)", 143);
		case PRAYER_2:
			return new ItemInfo("Prayer potion (2)", 141);
		case PRAYER_3:
			return new ItemInfo("Prayer potion (3)", 139);
		case PRAYER_4:
			return new ItemInfo("Prayer potion (4)", 2434);
		case SUPER_ATTACK_1:
			return new ItemInfo("Super attack (1)", 149);
		case SUPER_ATTACK_2:
			return new ItemInfo("Super attack (2)", 147);
		case SUPER_ATTACK_3:
			return new ItemInfo("Super attack (3)", 145);
		case SUPER_ATTACK_4:
			return new ItemInfo("Super attack (4)", 2436);
		case SUPER_DEFENSE_1:
			return new ItemInfo("Super defence (1)", 167);
		case SUPER_DEFENSE_2:
			return new ItemInfo("Super defence (2)", 165);
		case SUPER_DEFENSE_3:
			return new ItemInfo("Super defence (3)", 163);
		case SUPER_DEFENSE_4:
			return new ItemInfo("Super defence (4)", 2442);
		case SUPER_STRENGTH_1:
			return new ItemInfo("Super strength (1)", 161);
		case SUPER_STRENGTH_2:
			return new ItemInfo("Super strength (2)", 159);
		case SUPER_STRENGTH_3:
			return new ItemInfo("Super strength (3)", 157);
		case SUPER_STRENGTH_4:
			return new ItemInfo("Super strength (4)", 2440);
		case ANTIFIRE_1:
			return new ItemInfo("Antifire (1)", 2458);
		case ANTIFIRE_2:
			return new ItemInfo("Antifire (2)", 2456);
		case ANTIFIRE_3:
			return new ItemInfo("Antifire (3)", 2454);
		case ANTIFIRE_4:
			return new ItemInfo("Antifire (4)", 2452);
		case RANGING_1:
			return new ItemInfo("Ranging potion (1)", 173);
		case RANGING_2:
			return new ItemInfo("Ranging potion (2)", 171);
		case RANGING_3:
			return new ItemInfo("Ranging potion (3)", 169);
		case RANGING_4:
			return new ItemInfo("Ranging potion (4)", 2444);
		case SARADOMIN_BREW_1:
			return new ItemInfo("Saradomin brew (1)", 6691);
		case SARADOMIN_BREW_2:
			return new ItemInfo("Saradomin brew (2)", 6689);
		case SARADOMIN_BREW_3:
			return new ItemInfo("Saradomin brew (3)", 6687);
		case SARADOMIN_BREW_4:
			return new ItemInfo("Saradomin brew (4)", 6685);
		case SUPER_ANTIFIRE_1:
			return new ItemInfo("Super antifire (1)", 15307);
		case SUPER_ANTIFIRE_2:
			return new ItemInfo("Super antifire (2)", 15306);
		case SUPER_ANTIFIRE_3:
			return new ItemInfo("Super antifire (3)", 15305);
		case SUPER_ANTIFIRE_4:
			return new ItemInfo("Super antifire (4)", 15304);
		case EXTREME_ATTACK_1:
			return new ItemInfo("Extreme attack (1)", 15311);
		case EXTREME_ATTACK_2:
			return new ItemInfo("Extreme attack (2)", 15310);
		case EXTREME_ATTACK_3:
			return new ItemInfo("Extreme attack (3)", 15309);
		case EXTREME_ATTACK_4:
			return new ItemInfo("Extreme attack (4)", 15308);
		case EXTREME_DEFENCE_1:
			return new ItemInfo("Extreme defence (1)", 15319);
		case EXTREME_DEFENCE_2:
			return new ItemInfo("Extreme defence (2)", 15318);
		case EXTREME_DEFENCE_3:
			return new ItemInfo("Extreme defence (3)", 15317);
		case EXTREME_DEFENCE_4:
			return new ItemInfo("Extreme defence (4)", 15316);
		case EXTREME_STRENGTH_1:
			return new ItemInfo("Extreme strength (1)", 15315);
		case EXTREME_STRENGTH_2:
			return new ItemInfo("Extreme strength (2)", 15314);
		case EXTREME_STRENGTH_3:
			return new ItemInfo("Extreme strength (3)", 15313);
		case EXTREME_STRENGTH_4:
			return new ItemInfo("Extreme strength (4)", 15312);
		case EXTREME_RANGING_1:
			return new ItemInfo("Extreme ranging (1)", 15327);
		case EXTREME_RANGING_2:
			return new ItemInfo("Extreme ranging (2)", 15326);
		case EXTREME_RANGING_3:
			return new ItemInfo("Extreme ranging (3)", 15325);
		case EXTREME_RANGING_4:
			return new ItemInfo("Extreme ranging (4)", 15324);
		case SUPER_PRAYER_1:
			return new ItemInfo("Super prayer (1)", 15331);
		case SUPER_PRAYER_2:
			return new ItemInfo("Super prayer (2)", 15330);
		case SUPER_PRAYER_3:
			return new ItemInfo("Super prayer (3)", 15329);
		case SUPER_PRAYER_4:
			return new ItemInfo("Super prayer (4)", 15328);
		case PRAYER_RENEWAL_1:
			return new ItemInfo("Prayer renewal (1)", 21637);
		case PRAYER_RENEWAL_2:
			return new ItemInfo("Prayer renewal (2)", 21636);
		case PRAYER_RENEWAL_3:
			return new ItemInfo("Prayer renewal (3)", 21635);
		case PRAYER_RENEWAL_4:
			return new ItemInfo("Prayer renewal (4)", 21634);
		case OVERLOAD_1:
			return new ItemInfo("Overload (1)", 15335);
		case OVERLOAD_2:
			return new ItemInfo("Overload (2)", 15334);
		case OVERLOAD_3:
			return new ItemInfo("Overload (3)", 15333);
		case OVERLOAD_4:
			return new ItemInfo("Overload (4)", 15332);
		}
		return null;

	}

	public static void addDeposit(ItemInfo ii) {
		depositItems.put(ii.getName(), ii);
	}

	public static void addWithdraw(ItemInfo ii) {
		withdrawlItems.put(ii.getName(), ii);
	}
}