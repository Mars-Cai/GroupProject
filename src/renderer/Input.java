package renderer;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import WorldEditor.TabMap;

public class Input implements MouseListener, KeyListener, FocusListener, MouseMotionListener, MouseWheelListener {

	public boolean keys[];
	public boolean mouseButtons[];
	public int mouseX;
	public int mouseY;
	public boolean tabPressed;
	public boolean vendorPressed;
	public boolean bagPressed;

	public final static int LEFTCLICK = MouseEvent.BUTTON1;
	public final static int RIGHTCLICK = MouseEvent.BUTTON3;
	public final static int[] playerMoveKeys = { VK_A, VK_D, VK_E, VK_Q, VK_S, VK_W };

	public Input() {

		keys = new boolean[65535];
		mouseButtons = new boolean[65535];

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseX = arg0.getX();
		mouseY = arg0.getY();

	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent arg0) {
		for (int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = true;

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = false;
		if (arg0.getKeyCode() == KeyEvent.VK_1) {
			tabPressed = !tabPressed;
		}
		if (arg0.getKeyCode() == KeyEvent.VK_V) {
          vendorPressed = !vendorPressed;
      }
		if (arg0.getKeyCode() == KeyEvent.VK_B) {
        bagPressed = !bagPressed;
      }
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		if (mouseUp) {
//			mouseButtons[e.getButton()] = true;
//			mouseUp = false;
////			mouseDown = true;
//		} else {
//			mouseButtons[e.getButton()] = false;
//		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	private boolean mouseDown;

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (mouseUp) {
			mouseButtons[e.getButton()] = true;
			mouseUp = false;
//			mouseDown = true;
		} else {
			mouseButtons[e.getButton()] = false;
		}

	}

	private boolean mouseUp = true;


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseButtons[e.getButton()] = false;
		mouseUp = true;
	}

	public boolean moveKeysPressed() {
		for (int i = 0; i < playerMoveKeys.length; i++) {
			if (keys[playerMoveKeys[i]])
				return true;
		}
		return false;
	}

}
