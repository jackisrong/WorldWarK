package worldwark;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

public class KeyboardControls implements KeyListener {

    private final WorldWarK w;

    public KeyboardControls(WorldWarK w) {
	this.w = w;
    }

    @Override
    public void keyPressed(KeyEvent event) {
	// Keyboard controls
	switch (event.getKeyCode()) {
	    case KeyEvent.VK_LEFT:
		w.player.keyboardMoveLeft();
		break;
	    case KeyEvent.VK_RIGHT:
		w.player.keyboardMoveRight();
		break;
	    case KeyEvent.VK_SPACE:
		if (w.run == false && w.clickedStartScreenButton == null && w.gameOver == false && w.gamePaused == false) {
		    w.start();
		} else {
		    if (w.shootTimer >= w.player.getWeaponCooldown()) {
			w.shootBullet();
			w.shootTimer = 0;
		    }
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
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }
}
