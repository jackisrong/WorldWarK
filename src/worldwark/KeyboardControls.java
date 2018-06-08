package worldwark;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

public class KeyboardControls implements KeyListener {

    private final WorldWarK w;
    private boolean[] keys = new boolean[256];

    public KeyboardControls(WorldWarK w) {
	this.w = w;
    }

    public boolean isKey(int keyCode) {
	return keys[keyCode];
    }

    @Override
    public void keyPressed(KeyEvent e) {
	keys[e.getKeyCode()] = true;

	// Keyboard controls
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_SPACE:
		if (w.run == false && w.clickedStartScreenButton == null && w.gameOver == false && w.gamePaused == false) {
		    keys[e.getKeyCode()] = false;
		    w.start();
		}
		break;
	    case KeyEvent.VK_B:
		w.launchBomb();
		break;
	    case KeyEvent.VK_R:
		if (w.run == false && w.gameOver == true) {
		    w.gameOver = false;
		    w.start();
		}
		break;
	    case KeyEvent.VK_T:
		if (w.run == false && w.gameOver == true) {
		    w.gameOver = false;
		    w.repaint();
		}
		break;
	    case KeyEvent.VK_ESCAPE:
		if (w.run == true && w.gamePaused == false) {
		    w.gamePaused = true;
		    w.run = false;
		    w.repaint();
		} else if (w.run == false && w.gamePaused == true) {
		    w.start();
		    w.gamePaused = false;
		    w.repaint();
		} else if (w.run == false && w.gamePaused == false && w.clickedStartScreenButton != null) {
		    w.clickedStartScreenButton = new Rectangle2D.Double(370, 80, 80, 30);
		    w.repaint();
		}
		break;
	    default:
		break;
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	keys[e.getKeyCode()] = false;

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
