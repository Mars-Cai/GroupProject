package WorldEditor.Spawners;

import java.util.ArrayList;
import java.util.List;

import WorldEditor.Rand;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Coin;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.HealthPotion;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.InanimateObjects.BreakableObject;
import renderer.Bitmap;

public class ItemSpawner {

	private Bitmap[] itemImages = null;
	private BreakableObject breakableObject;
	private GameObject objectTiedToo;
	private Item garenteedSpawn;

	public ItemSpawner(GameObject e) {
		this.objectTiedToo = e;

	}


	public void spawn() {
		// TODO Auto-generated method stub
		if (objectTiedToo != null) {
			if(garenteedSpawn != null) {
				garenteedSpawn.setExactLocation(objectTiedToo.getExactLocation());
				objectTiedToo.getSquare().addGameObject(garenteedSpawn);
				return;
			}
			List<Item> itemsDropped = new ArrayList<>();
			double spawnVar = Rand.ranDouble(1, 0);
			if (Coin.CHANCETOSPAWN >= spawnVar) {
				Coin coin = new Coin(Rand.ranDouble(70, 10));
				itemsDropped.add(coin);
			}

			if (HealthPotion.CHANCETOSPAWN >= spawnVar) {
				HealthPotion healthPotion = new HealthPotion();
				itemsDropped.add(healthPotion);
			}
			itemsDropped.forEach(i -> {
				i.setExactLocation(objectTiedToo.getExactLocation());
				objectTiedToo.getSquare().addGameObject(i);
			});
		}

	}

	public void forceSpawn(Item i) {
		// TODO Auto-generated method stub
		this.garenteedSpawn = i;

	}

}
