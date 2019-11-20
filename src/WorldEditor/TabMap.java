package WorldEditor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.print.FlavorException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.xml.stream.events.EndDocument;

import WorldEditor.GameObjects.Boss;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Floor;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import WorldEditor.GameObjects.InanimateObjects.Wall;
import renderer.Bitmap;
import renderer.Core;

public class TabMap extends JPanel {

	private World world;
	private JFrame frame;
	private TabMapImage tabMapImage = new TabMapImage();
	// private Image mapImage;
	// private BufferedImage mapImgBuf;
	// private Image topLayerImage;
	// private BufferedImage topLayerImageBuf;

	private static final int HEIGHT = 800;
	private static final int WIDTH = 800;
	private boolean mapShowing;

	public TabMap(World world) {
		this.world = world;
		setFocusable(false);

	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform old = g2.getTransform();
		g2.translate(0, getHeight() - 1);
		g2.scale(1, -1);
		Map<Location, Color> locToColr = drawTabMap(g2);
		g.drawImage(tabMapImage.getBottomImageBuf(), 0, 0, this);
		locToColr.forEach((l, c) -> ovalDouble(l, c, g2));
		// locToColr.clear();
		// g.drawImage(tabMapImage.getTopImageBuf(), 0, 0, this);
		g2.setTransform(old);
	}

	public void toggleMap(boolean tabPressed) {
		if (tabPressed) {
			if (mapShowing) {
				this.repaint();
			} else {
				createWindow();
				mapShowing = true;
			}
		} else {
			if (frame != null)
				frame.dispose();
			mapShowing = false;
		}

	}

	private void createWindowTesting() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		JSplitPane mainContainer = setupJSplitPane();
		mainContainer.setTopComponent(this);
		mainContainer.setBottomComponent(setupTestIngPane());
		frame = new JFrame();
		frame.setFocusable(false);
		frame.setFocusableWindowState(false);
		frame.setMinimumSize(new Dimension(WIDTH + 100, HEIGHT + 200));
		frame.toFront();
		frame.getContentPane().add(mainContainer);
		frame.pack();
		frame.setVisible(true);
		frame.repaint();
	}

	private void createWindow() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame = new JFrame();
		frame.setFocusable(false);
		frame.setFocusableWindowState(false);
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.toFront();
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
		frame.repaint();
	}

	private JSplitPane setupJSplitPane() {
		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		pane.setDividerLocation(HEIGHT);
		return pane;
	}

	private JPanel setupTestIngPane() {
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(10, 10));
		JButton newMap = new JButton("New Map");
		newMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				world = new World();
				frame.repaint();
			}
		});
		container.add(newMap);
		return container;
	}

	private Map<Location, Color> drawTabMap(Graphics topG) {
		this.tabMapImage.setBottomImageBuf(new BufferedImage(WIDTH, WIDTH, BufferedImage.TYPE_INT_RGB));
		this.tabMapImage.setTopImageBuf(new BufferedImage(WIDTH, WIDTH, BufferedImage.TYPE_INT_RGB));
		Graphics baseG = this.tabMapImage.getBottomImageBuf().getGraphics();
		// Graphics topG = this.tabMapImage.getTopImageBuf().getGraphics();
		return drawFromSquares(baseG, topG);
	}

	private Map<Location, Color> drawFromSquares(Graphics baseG, Graphics topG) {
		Map<Location, Color> map = new HashMap<>();
		for (int x = 0; x < World.MAXBOARDWIDTH; x++) {
			for (int y = 0; y < World.MAXBOARDHEIGHT; y++) {
				Square s = world.world[x][y];
				if (!s.isUsed())
					continue;

				if (s.isDiscovered()) {
					s.getGameObjects().forEach(o -> {
						map.putAll(drawTopObject(o, topG));
						drawBaseObject(o, baseG);
					});
				}
			}
		}
		return map;
	}

	private void drawBaseObject(GameObject o, Graphics baseG) {

		if (o instanceof Floor) {
			Floor f = (Floor) o;
			if (f.isBossRoom()) {
				rect(f.getSquareLocation(), Color.yellow, baseG);
			}
			rect(o.getSquareLocation(), Color.darkGray, baseG);
		}
		if (o instanceof Wall) {
			// rect(o.getSquareLocation(), Color.white, baseG);
			rectWire(o.getHitBox(), Color.white, (Graphics2D) baseG);
		}
		if (o instanceof Chest) {
			oval(o.getSquareLocation(), Color.CYAN, baseG);
		}
		if (o instanceof Vendor) {
			oval(o.getSquareLocation(), Color.blue, baseG);
		}
		if (o instanceof Door) {
			if (o.isHitBoxActive()) {
				rectWire(o.getHitBox(), Color.BLUE, (Graphics2D) baseG);
			}
		}

	}

	private Map<Location, Color> drawTopObject(GameObject o, Graphics topG) {
		Map<Location, Color> map = new HashMap<>();
		if (o instanceof Player) {
			map.put(o.getExactLocation(), Color.pink);

		} else if (o instanceof Enemy) {
			if (o instanceof Boss) {
				map.put(o.getExactLocation(), Color.GREEN);
			} else {
				map.put(o.getExactLocation(), Color.red);
			}
		}
		return map;
	}

	private void oval(Location loc, Color c, Graphics g) {
		g.setColor(c);
		Location scaledLocStart = Location.scale(loc, (int) Square.PIXELSPERSQUARE);
		g.fillOval((int) scaledLocStart.x, (int) scaledLocStart.y, (int) Square.PIXELSPERSQUARE / 2,
				(int) Square.PIXELSPERSQUARE / 2);

	}

	private void ovalDouble(Location loc, Color c, Graphics2D g) {
		g.setColor(c);
		Location scaledLocStart = Location.scale(loc, Square.PIXELSPERSQUARE);
		// g.fill(new Line2D.Double(scaledLocStart.x+4, scaledLocStart.y+4,
		// scaledLocStart.x+8,scaledLocStart.y+4));
		// g.fill(new Line2D.Double(scaledLocStart.x+4, scaledLocStart.y+4,
		// scaledLocStart.x+4,scaledLocStart.y+8));
		g.fill(new Ellipse2D.Double(scaledLocStart.x, scaledLocStart.y, Square.PIXELSPERSQUARE / 2,
				Square.PIXELSPERSQUARE / 2));
		// g.fill(new RoundRectangle2D.Double(0.1,0.1,scaledLocStart.x,
		// scaledLocStart.y, Square.PIXELSPERSQUARE * .7,
		// Square.PIXELSPERSQUARE *.7));

	}

	private void rect(Location loc, Color c, Graphics g) {
		g.setColor(c);
		Location scaledLocStart = Location.scale(loc, (int) Square.PIXELSPERSQUARE);
		g.fillRect((int) scaledLocStart.x, (int) scaledLocStart.y, (int) Square.PIXELSPERSQUARE,
				(int) Square.PIXELSPERSQUARE);
	}

	private void rectWire(Rectangle2D.Double rect, Color c, Graphics2D g) {
		g.setColor(c);
		// Location scaledLocStart = Location.scale(new Location(rect.x, rect.y), (int)
		// Square.PIXELSPERSQUARE);
		// g.fillRect((int) scaledLocStart.x, (int) scaledLocStart.y, (int)
		// (rect.getWidth() * Square.PIXELSPERSQUARE),
		// (int) (rect.getHeight() * Square.PIXELSPERSQUARE));
		Rectangle2D.Double scaledRect = scaledRect(rect);
		g.fill(scaledRect);
	}

	private Rectangle2D.Double scaledRect(Rectangle2D.Double rect) {
		return new Rectangle2D.Double(rect.getX() * Square.PIXELSPERSQUARE, rect.y * Square.PIXELSPERSQUARE,
				rect.getWidth() * Square.PIXELSPERSQUARE, rect.getHeight() * Square.PIXELSPERSQUARE);
	}

	public Bitmap getBitmapMapImg() {
		return Core.loadFromBufferedImage(tabMapImage.getBottomImageBuf());
	}

}

class TabMapImage {
	private Image bottomImage;
	private BufferedImage bottomImageBuf;
	private Image topImage;
	private BufferedImage topImageBuf;

	public TabMapImage() {
		// TODO Auto-generated constructor stub
		this.bottomImageBuf = bottomImageBuf;
		this.topImage = topImage;
		this.topImageBuf = topImageBuf;
		this.bottomImage = bottomImage;
	}

	public Image getBottomImage() {
		return bottomImage;
	}

	public BufferedImage getBottomImageBuf() {
		return bottomImageBuf;
	}

	public Image getTopImage() {
		return topImage;
	}

	public BufferedImage getTopImageBuf() {
		return topImageBuf;
	}

	public void setBottomImage(Image bottomImage) {
		this.bottomImage = bottomImage;
	}

	public void setBottomImageBuf(BufferedImage bottomIamgeBuf) {
		this.bottomImageBuf = bottomIamgeBuf;
	}

	public void setTopImage(Image topImage) {
		this.topImage = topImage;
	}

	public void setTopImageBuf(BufferedImage topImageBuf) {
		this.topImageBuf = topImageBuf;
	}

}
