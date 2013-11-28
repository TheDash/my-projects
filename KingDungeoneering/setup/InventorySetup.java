package setup;

import java.util.Collection;
import java.util.HashMap;

import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

import libs.ItemInfo;

public class InventorySetup {

	private String setupName = "default";
	private final int MAX_INV = 28;
	private final int MIN_INV = 0;
	
	// Static pre-game referencing
	private HashMap<String, ItemInfo> selectedPotions = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo> selectedPouches = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo>    selectedFood = new HashMap<String, ItemInfo>();
	
	// Dynamic in game checking
	private HashMap<String, ItemInfo>    inventoryLoot = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo> inventoryPotions = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo> inventoryPouches = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo>    inventoryFood = new HashMap<String, ItemInfo>();
	
	
	public void addPouch(ItemInfo p) {
		selectedPouches.put(p.getName(), p);
	}
	
	public void addFood(ItemInfo f) {
		selectedFood.put(f.getName(), f);
	}
	
	public void addPotion(ItemInfo p) {
		selectedPotions.put(p.getName(), p); 
	}
	
	public int getInventoryCount() {
		return Inventory.getCount();
	}
	
	public boolean hasEmptyInventory() {
		return Inventory.getCount() == MIN_INV;
	}
	
	public boolean hasFullInventory() {
		return Inventory.getCount() == MAX_INV;
	}
	
	public boolean hasPotions() {
		return inventoryPotions.size() > 0;
	}
	
	public boolean hasFood() {
		return inventoryFood.size() > 0;
	}
	
	public boolean hasPouches() {
		return inventoryPouches.size() > 0;
	}
	
	public boolean hasLoot() {
		return inventoryLoot.size() > 0;
	}
	
	public boolean hasLoot(String loot) {
		return inventoryLoot.containsKey(loot);
	}
	
	public boolean hasPouch(String pouch) {
		return inventoryPouches.containsKey(pouch);
	}
	
	public boolean hasFood(String f) {
		return inventoryFood.containsKey(f);
	}
	
	public boolean hasPotion(String p) {
		return inventoryPotions.containsKey(p);
	}
	
	public boolean contains(String item) {
		return inventoryPotions.containsKey(item) || inventoryPouches.containsKey(item) || inventoryFood.containsKey(item);
	}

	public void depositInventory() {
		Collection<ItemInfo> loot    = inventoryLoot.values();
		Collection<ItemInfo> potions = inventoryPotions.values();
		Collection<ItemInfo> pouches = inventoryPouches.values();
		Collection<ItemInfo> food    = inventoryFood.values();
		
		deposit(loot);
		deposit(potions);
		deposit(pouches);
		deposit(food);
	}
	
	private void deposit(Collection<ItemInfo> i) {
		for (ItemInfo z : i) {
			Bank.deposit(z.getId(), z.getDepositAmount());
		}
	}
	
	private void withdraw(Collection<ItemInfo> i) {
		for (ItemInfo z : i) {
			Bank.withdraw(z.getId(), z.getWithdrawAmount());
		}
	}
	
	public void withdrawInventory() {
		Collection<ItemInfo> potions = selectedPotions.values();
		Collection<ItemInfo> pouches = selectedPouches.values();
		Collection<ItemInfo> food    = selectedFood.values();

		withdraw(potions);
		withdraw(pouches);
		withdraw(food);
	}

}
