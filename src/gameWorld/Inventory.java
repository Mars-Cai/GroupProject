
package gameWorld;

import java.util.ArrayList;
import java.util.List;

import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.WeaponObjects.Gun;
import WorldEditor.GameObjects.WeaponObjects.Sword;

public class Inventory {

	private int totalNumOfItems;
	private Item[] allItems;
	private int selected;
	public Gun gun;
	public Sword sword;

	// Builds a new empty inventory with the 8 storage capacity.

	public Inventory() {
		allItems = new Item[8];
		totalNumOfItems = 0;
		
//		addItem(new Gun());
//		addItem(new Sword());
	}

	// Returns whether or not this inventory has space for at least one more
	// ItemObject.

	public boolean hasEmptySpace() {
		return totalNumOfItems < 8;
	}

	public int getTotalNumOfItems() {
		return this.totalNumOfItems;
	}

	// Attempts to add an ItemObject into the next available slot
	// of this inventory.
	public boolean addItem(Item i) {
		if (!hasEmptySpace())
			return false;

		return addItem(i, nextAvailableSpace());
	}

	// Attempts to add an ItemObject into the given slot of this
	// inventory.Does nothing if the bad is full

	public boolean addItem(Item i, int index) {
		if (index >= 8 || index < 0)
			throw new IllegalArgumentException("The bag is full,can not add more ItemObject!");

		if (allItems[index] == null) {
			allItems[index] = i;
			if(i instanceof Gun) {
				gun = (Gun)i;
				
			}
			if(i instanceof Sword) {
				sword = (Sword)i;
			}
			totalNumOfItems++;
			return true;
		}
		return false;
	}

	// Attempts to remove the first instance of a given ItemObject from this
	// inventory.
	// Returns whether or not the ItemObject was found; does nothing if it wasn't
	// found.

	public boolean removeItem(Item ItemObject) {
		for (int i = 0; i < allItems.length; i++) {
			if (allItems[i] == ItemObject) {
				removeAtIndex(i);
				return true;
			}
		}
		return false;
	}

	private void removeAtIndex(int index) {
		if (index < 0 || index > allItems.length)
			throw new IllegalArgumentException(
					"Attempting to remove an ItemObject from an inventory at an invalid index.");

		allItems[index] = null;
		totalNumOfItems--;
	}

	// Returns whether or not this inventory contains the given ItemObject.

	public boolean containsItem(Item ItemObject) {
		for (int i = 0; i < allItems.length; i++) {
			if (allItems[i].equals(ItemObject))
				return true;
		}
		return false;
	}

	// The position of the given ItemObject in this inventory,
	// or -1 if it isn't contained.

	public int indexOfItem(Item ItemObject) {
		for (int i = 0; i < 8; i++) {
			if (allItems[i].equals(ItemObject))
				return i;
		}
		return -1;
	}

	public Item getItem(int index) {
		if (index < 0 || index > allItems.length)
			throw new IllegalArgumentException("Invalid index.");
		return allItems[index];
	}

	// An array of all allItems in this inventory, with null denoting no ItemObject
	// at that index.

	public Item[] getAllItems() {
		return allItems;
	}

	public Item getSelected() {
		return allItems[selected];
	}

	private int nextAvailableSpace() {
		if (!hasEmptySpace())
			return -1;

		for (int i = 0; i < allItems.length; i++)
			if (allItems[i] == null)
				return i;

		throw new IllegalStateException("No available space in inventory");
	}

	public List<Key> getKeys() {
		List<Key> keys = new ArrayList<>();
		for (int i = 0; i < allItems.length; i++)
			if (allItems[i] instanceof Key)
				keys.add((Key)allItems[i]);
		
		return keys;
	}

}
