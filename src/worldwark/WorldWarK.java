package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javafx.scene.input.MouseEvent;
import javax.swing.JPanel;

public class WorldWarK extends JPanel {

    private abstract class ControlPlayer implements KeyListener, MouseListener {

	public void keyPressed(KeyEvent event) {
	    switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		    System.out.println("LEFT");
		    break;
		case KeyEvent.VK_RIGHT:
		    System.out.println("RIGHT");
		    break;
	    }
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {
	}

	public void mouseClicked(MouseEvent event) {
	    System.out.println("MOUSE CLICK");
	    //balls.add(new Ball(event.getX(), event.getY(), 40, Color.WHITE, 0, 0, 1, 1, 15, .2));
	}

	public void mousePressed(MouseEvent event) {
	}

	public void mouseReleased(MouseEvent event) {
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}
    }

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("World War K");
        JPanel panel = new JPanel();
        JLabel label = new JLabel("YEEEEEEEEEEEEEEEEEEEEEEEEEEET");
        JMenuBar menu = new JMenuBar();
        
        panel.setSize(3000, 3000);
        panel.add(label);
        panel.add(menu);
        
        frame.add(panel);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

    }
}
