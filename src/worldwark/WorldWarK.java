package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class WorldWarK extends JPanel {

    private class KeyboardControls implements KeyListener {

	public void keyPressed(KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.VK_LEFT) {
		System.out.println("LEFT");
	    } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
		System.out.println("RIGHT");
	    } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
		System.out.println("SPACE");
	    } else if (event.getKeyCode() == KeyEvent.VK_B) {
		System.out.println("B");
	    }
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {
	}
    }

    private class MouseControls implements MouseListener {

	public void mouseClicked(MouseEvent event) {
	    if (event.getButton() == MouseEvent.BUTTON1) {
		System.out.println("LEFT CLICK");
	    } else if (event.getButton() == MouseEvent.BUTTON3) {
		System.out.println("RIGHT CLICK");
	    }
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

    public WorldWarK() {
	JFrame frame = new JFrame("World War K");

	setBackground(Color.black);
	setPreferredSize(new Dimension(500, 500));
	addKeyListener(new KeyboardControls());
	addMouseListener(new MouseControls());
	setFocusable(true);
	frame.setSize(500, 500);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	frame.add(this);
	frame.pack();
    }

    public static void main(String[] args) {
	WorldWarK panel = new WorldWarK();
    }
}
