package WorldEditor.GameObjects.InanimateObjects;

import java.awt.image.BufferedImage;

import WorldEditor.Layer;
import WorldEditor.Room;
import WorldEditor.GameObjects.GameObject;
import renderer.Bitmap;

public class Floor extends GameObject {

	private boolean inBossRoom;
	public final Room room;

	public Floor(Room room) {
		// TODO Auto-generated constructor stub
		this.room = room;
	}
	
	@Override
	public String toString() {
		return "_";

	}
	
	public void setInBossRoom(boolean b) {
		inBossRoom = b;
	}
	
	public boolean isBossRoom() {
		// TODO Auto-generated method stub
		return inBossRoom;
	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.FLOOR;

	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 0;
	}
}
