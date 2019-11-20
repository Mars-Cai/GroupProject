package WorldEditor.GameObjects.InanimateObjects;

import java.util.HashMap;
import java.util.Map;

import WorldEditor.Layer;
import WorldEditor.Square;
import WorldEditor.GameObjects.Coin;
import WorldEditor.GameObjects.Fuel;
import WorldEditor.GameObjects.HealthPotion;
import WorldEditor.GameObjects.InteractableObject;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.Player;
import renderer.Bitmap;

public class Vendor extends InteractableObject {
	private String vendorID;

	Map<Item, Integer> allItems = new HashMap<>();// Integer type means price of each item

	Player player;
	public Vendor(Key key, Player player) {
		this.player = player;
		setupShop(key);
	}

	private void setupShop(Key key) {
		allItems.put(new HealthPotion(), 10);
		allItems.put(key, 1000);
		allItems.put(new Fuel(), 20);
		allItems.put(new Upgrade(player), 20);
	}

	public String getVendorID() {
		return vendorID;
	}

	private int removeItem(Item item) {
		if (item instanceof HealthPotion) {
			return allItems.get(item);
		} else {
			return allItems.remove(item);
		}

	}

	public boolean hasItem(Item item) {
		return allItems.containsKey(item);
	}


	public void sellToPlayer(Item i) {
		if (player.getMoney() >= this.allItems.get(i)) {
			if (i instanceof Upgrade) {
			  player.minusMoney(allItems.get(i));
			} else {
				if (player.invertory.hasEmptySpace()) {
					int soldFor = removeItem(i);
					player.invertory.addItem(i);
					player.minusMoney(soldFor);
					GUI.Frame.appendText("You bought " + i.toString() + "! For " + soldFor);

				} else {
					GUI.Frame.appendText("Your bag is full! Can not buy more items!");
				}
			}
		}else {
		GUI.Frame.appendText("Your money is not enough to buy this item!");
		}
	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.MOVEMENTBLOCKING;

	}

	@Override
	protected void setHitBoxAndRadius() {
		hitBoxRadius = 0.2;
	}

	public Map<Item, Integer> getVendorStock() {
		return allItems;
	}

}
