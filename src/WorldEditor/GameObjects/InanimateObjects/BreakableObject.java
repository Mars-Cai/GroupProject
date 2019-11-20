package WorldEditor.GameObjects.InanimateObjects;

import WorldEditor.AbstractWorld;
import WorldEditor.Layer;
import WorldEditor.World;
import WorldEditor.GameObjects.InteractableObject;
import WorldEditor.GameObjects.Item;
import WorldEditor.Spawners.ItemSpawner;
import renderer.Bitmap;

public class BreakableObject extends InteractableObject{

	ItemSpawner itemSpawner;
	public BreakableObject() {
		itemSpawner = new ItemSpawner(this);
		// TODO Auto-generated constructor stub
	}
	
	public void breakObject() {
		itemSpawner.spawn();
	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.MOVEMENTBLOCKING;
	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 0.5;
	}
	
	

}
