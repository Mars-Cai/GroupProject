package WorldEditor.GameObjects;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import WorldEditor.Layer;
import WorldEditor.Square;
import renderer.Bitmap;

public class MeleeEnemy extends Enemy {

	public MeleeEnemy() {
		statemanager.setRunSpeed(0.02);
		statemanager.setAttackRange(0.5);
		statemanager.setMaxHealth(80);
		statemanager.setDefualtAttackDamage(10);
	}



	@Override
	public String toString() {
		return "E";

	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.MOVEMENTBLOCKING;
	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 0.15;
	}
}
