package worldwark;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

public class KeyboardControls implements KeyListener {
   
    private WorldWarK w;
    
    public KeyboardControls(WorldWarK w) {
        this.w = w;
    }
    @Override
    public void keyPressed(KeyEvent event) {
        // Keyboard controls
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                w.getPlayer().keyboardMoveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                w.getPlayer().keyboardMoveRight();
                break;
            case KeyEvent.VK_SPACE:
                if (w.getRun() == false && w.getClickedStartScreenButton() == null && w.getGameOver() == false) {
                    w.start();
                } else {
                    w.shootBullet();
                }
                break;
            case KeyEvent.VK_B:
                w.launchBomb();
                break;
            case KeyEvent.VK_R:
                if (w.getRun() == false && w.getGameOver() == true) {
                    w.setGameOver(false);
                    w.start();
                }
                break;
            case KeyEvent.VK_T:
                if (w.getRun() == false && w.getGameOver() == true) {
                    w.setGameOver(false);
                    w.repaint();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if (w.getRun() == true && w.getGamePaused() == false) {
                    w.setGamePaused(true);
                    w.setRun(false);
                } else if (w.getRun() == false && w.getGamePaused() == true) {
                    w.start();
                    w.setGamePaused(false);
                    w.repaint();
                } else if (w.getRun() == false && w.getGamePaused() == false && w.getClickedStartScreenButton() != null) {
                    w.setClickedStartScreenButton(new Rectangle2D.Double(370, 80, 80, 30));
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