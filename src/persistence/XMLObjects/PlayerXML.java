package persistence.XMLObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;import WorldEditor.Location;
import WorldEditor.GameObjects.Boss;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Player;

public class PlayerXML {
	public LocationXML location;
	public List<Class<? extends Item>> itemsInInventory = new ArrayList<>();
	public double money;
	public double currentHealth;
	public static PlayerXML toXML(Player player) {
		PlayerXML playerXML = new PlayerXML();
		playerXML.location = LocationXML.toXML(player.getExactLocation());
		Item[] items = player.invertory.getAllItems();
		
		for(int i =0; i<items.length; i++) {
			if(items[i] == null) continue;
			playerXML.itemsInInventory.add(items[i].getClass());
		}
		playerXML.money = player.getMoney();
		playerXML.currentHealth = player.statemanager.getCurrentHealth();
		return playerXML;
	}
	
	public Player XMLto() {
		// TODO Auto-generated method stub
		Player player = new Player();
		player.statemanager.setCurrentHealth((int)currentHealth);
		player.setExactLocation(location.XMLTo());
		itemsInInventory.forEach(i->{
			try {
				player.invertory.addItem(i.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		player.plusMoney(money);
		return player;
	}
}
