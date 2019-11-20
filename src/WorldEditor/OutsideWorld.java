package WorldEditor;

import renderer.Bitmap;
import renderer.Core;

public class OutsideWorld extends AbstractWorld{

	private Room outsideArea;
	
	public OutsideWorld() {
		initialise();
		generateOutsideArea();
	}
	
	@Override
	protected void initialise() {
		// TODO Auto-generated method stub
		roomHeightBoundry = new Count(50, 50);
		roomWidthBoundry = new Count(50, 50);
		initialiseSquareGrid();
		testImage = Core.loadTexture("TestFloorSWEN.jpg");
		floorImages = new Bitmap[]{ Core.loadTexture("TestFloorSWEN.jpg") };
		wallImages =new Bitmap[] { Core.loadTexture("TestWallSWEN.jpg") };
		doorImages = new Bitmap[]{ testImage };
		enemyImages = new Bitmap[]{ testImage };
		
	}
	
	
	private void generateOutsideArea() {
		outsideArea = new Room(new Location(0, 0), new Location(roomWidthBoundry.max, roomHeightBoundry.max));
		
	}

	
}
