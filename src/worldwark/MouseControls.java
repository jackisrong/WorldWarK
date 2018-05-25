package worldwark;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

public class MouseControls implements MouseListener, MouseMotionListener {
    
    private WorldWarK w;
    
    public MouseControls(WorldWarK w) {
        this.w = w;
    }
    
    @Override
    public void mouseClicked(MouseEvent event) {
        // Mouse click controls
        if (event.getButton() == MouseEvent.BUTTON1) {
            if (w.getRun() == false) {
                if (w.getGamePaused() == true) {
                    if (new Rectangle2D.Double(370, 80, 80, 30).contains(event.getPoint())) {
                        // Resume game
                        w.start();
                        w.setGamePaused(false);
                        w.repaint();
                    }
                } else {
                    // Check if x and y coords of mouse click is within a button area
                    for (Rectangle2D i : w.getStartScreenButtons()) {
                        if (i.contains(event.getPoint())) {
                            w.setClickedStartScreenButton(i);
                            w.repaint();
                        }
                    }
                }
            } else {
                if (new Rectangle2D.Double(420, 0, 80, 30).contains(event.getPoint())) {
                    // Pause game
                    w.setRun(false);
                    w.setGamePaused(true);
                    w.repaint();
                }

                if (w.getGamePaused() == false) {
                    w.shootBullet();
                }
            }
        } else if (event.getButton() == MouseEvent.BUTTON3) {
            w.launchBomb();
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
        if (w.getRun() == true && w.getGamePaused() == false) {
            w.getPlayer().setXPosition(event.getX());
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        // Sets x position of player when mouse is clicked and dragged
        if (w.getRun() == true && w.getGamePaused() == false) {
            w.getPlayer().setXPosition(event.getX());
        }
    }
}
