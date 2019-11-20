package gameWorld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import WorldEditor.Location;
import WorldEditor.Square;

public class TestFrame extends JFrame {

	public static int FRAMEWIDTH = 800;
	public static int FRAMEHEIGHT = 800;
	public BufferedImage imgBuf;
	private JPanel imgPanel;
	public TestFrame() {
		// TODO Auto-generated constructor stub
		createWidnow();
		
	}
	
	public void createWidnow() {
		this.setMinimumSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		this.setVisible(true);
		this.setFocusableWindowState(false);
		this.setFocusable(false);
		this.toFront();
		this.setLocation(1000, 0);
		imgPanel = new JPanel();
		imgPanel.setFocusable(false);
		imgBuf = new BufferedImage(FRAMEWIDTH, FRAMEHEIGHT, BufferedImage.TYPE_INT_RGB);
		this.getContentPane().add(imgPanel);
		this.pack();
	}
	
	public void paint(Graphics g) {
		g.drawImage(imgBuf, 0, 0, imgPanel);
	}
	
	public void line(Location from, Location to, Color c) {
		Graphics2D g = (Graphics2D)imgBuf.getGraphics();
		g.setColor(c);
		Location scFrom = Location.scale(from, (int) Square.PIXELSPERSQUARE/*40*/);
		Location scTo = Location.scale(to, (int)Square.PIXELSPERSQUARE /*40*/);
		Line2D.Double line = new Line2D.Double(scFrom.x, scFrom.y, scTo.x, scTo.y);
		g.draw(line);
		this.repaint();
	}
	
	
}
