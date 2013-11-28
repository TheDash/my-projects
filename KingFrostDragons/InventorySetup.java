package setup;

import java.util.Collection;
import java.util.HashMap;

import libs.ItemInfo;

import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.node.Item;

public class InventorySetup {

	private final int MAX_INV = 28;
	private final int MIN_INV = 0;
	
	// Static pre-game referencing
	private HashMap<String, ItemInfo> selectedPotions = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo> selectedPouches = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo>    selectedFood = new HashMap<String, ItemInfo>();
	private HashMap<String, ItemInfo>    selectedLoot = new HashMap<String, ItemInfo>();
	
	private Filter<Item> potions = new Filter<Item>() {

		@Override
		public boolean accept(Item arg0) {
			return selectedPotions.containsKey(arg0.getName());
		}
		
	};
	
	private Filter<Item> food = new Filter<Item>() {

		@Override
		public boolean accept(Item arg0) {
			return selectedFood.containsKey(arg0.getName());
		}
		
	};
	
	private Filter<Item> loot = new Filter<Item>() {

		@Override
		public boolean accept(Item arg0) {
			return selectedLoot.containsKey(arg0.getName());
		}
		
	};
	
	private Filter<Item> pouches = new Filter<Item>() {

		@Override
		public boolean accept(Item arg0) {
			return selectedPouches.containsKey(arg0.getName());
		}
		
	};
	
	public void addLoot(ItemInfo i) {
		selectedLoot.put(i.getName(), i);
	}
	
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
		return Inventory.getCount(potions) > MIN_INV;
	}
	
	public boolean hasFood() {
		return Inventory.getCount(food) > MIN_INV;
	}
	
	public boolean hasPouches() {
		return Inventory.getCount(pouches) > MIN_INV;
	}
	
	public boolean hasLoot() {
		return Inventory.getCount(loot) > MIN_INV;
	}
	
	public boolean contains(String item) {
		return hasLoot() || hasFood() || hasPouches() || hasPotions();
	}

	public void depositInventory() {
		Item[] pots = Inventory.getItems(potions);
		Item[] foods = Inventory.getItems(food);
		Item[] pouchs = Inventory.getItems(pouches);
		Item[] loots = Inventory.getItems(loot);
		
		deposit(loots);
		deposit(pots);
		deposit(pouchs);
		deposit(foods);
	}
	
	private void deposit(Item[] its) {
		for (int i = 0; i < its.length; i++) {
			Bank.deposit(its[i].getWidgetChild().getId(), Inventory.getCount());
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
