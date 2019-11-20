package WorldEditor.GameObjects;

import java.util.List;
import java.util.Set;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Room;
import WorldEditor.World;
import WorldEditor.Spawners.RoomPopulator;
import gameWorld.Hit;
import gameWorld.Raycast2D;
import renderer.Bitmap;

public class Boss extends Enemy {
	public Boss() {
		// TODO Auto-generated constructor stub
		statemanager.setRunSpeed(0.02);
		statemanager.setDefualtAttackRange(1);
		statemanager.setMaxHealth(500);
		statemanager.setDefualtAttackDamage(20);
	}


	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.MOVEMENTBLOCKING;
	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 1;
	}

}
