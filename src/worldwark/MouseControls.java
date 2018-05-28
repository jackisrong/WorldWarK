package worldwark;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

public class MouseControls implements MouseListener, MouseMotionListener {

    private final WorldWarK w;

    public MouseControls(WorldWarK w) {
	this.w = w;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
	// Mouse click controls
	if (event.getButton() == MouseEvent.BUTTON1) {
	    if (w.run == false) {
		if (w.gamePaused == true) {
		    if (new Rectangle2D.Double(370, 80, 80, 30).contains(event.getPoint())) {
			// Resume game
			w.start();
			w.gamePaused = false;
			w.repaint();
		    }
		} else {
		    // Check if x and y coords of mouse click is within a button area
		    for (Rectangle2D i : w.startScreenButtons) {
			if (i.contains(event.getPoint()) && (w.clickedStartScreenButton == null || i.equals(new Rectangle2D.Double(370, 80, 80, 30)))) {
			    w.clickedStartScreenButton = i;
			    w.repaint();
			}
		    }
		}
	    } else {
		if (new Rectangle2D.Double(420, 0, 80, 30).contains(event.getPoint())) {
		    // Pause game
		    w.run = false;
		    w.gamePaused = true;
		    w.repaint();
		}

		if (w.gamePaused == false) {
		    if (w.shootTimer >= w.player.getWeaponCooldown()) {
			w.shootBullet();
			w.shootTimer = 0;
		    }
		}
	    }
	} else if (event.getButton() == MouseEvent.BUTTON3) {
	    if (w.run == true) {
		w.launchBomb();
	    }
	}
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mouseMoved(MouseEvent event) {
	// Sets x position of player when mouse is moved
	if (w.run == true && w.gamePaused == false) {
	    w.player.setXPosition(event.getX());
	}
    }

    @Override
    public void mouseDragged(MouseEvent event) {
	// Sets x position of player when mouse is clicked and dragged
	if (w.run == true && w.gamePaused == false) {
	    w.player.setXPosition(event.getX());
	}
    }
}
