package renderer;

public class Bitmap {

	public int width;
	public int height;
	public int[] pixels;

	public Bitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	public void render(Bitmap b, int xOffs, int yOffs) {
		for (int y = 0; y < b.height; y++) {
			int yPixel = y + yOffs;
			
			// Continue if off screen
			if (yPixel < 0 || yPixel >= height) 
				continue;
			
			for (int x = 0; x < b.width; x++) {
				int xPixel = x + xOffs;
				
				// Continue if off screen
				if (xPixel < 0 || xPixel >= width) 
					continue;
				
				pixels[xPixel + yPixel * width] = b.pixels[x + y * b.width];
			}
		}
	}

	/**
	 * Clears pixels array.
	 */
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
}
