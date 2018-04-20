package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

public class WorldWarK extends JPanel implements Runnable {

    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private boolean run = false;

    public WorldWarK() {
	JFrame frame = new JFrame("World War K");
	objects.add(new Player(450, 350, 30, 20, 5, 100, 3));

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
	start();
    }

    public void start() {
	Thread thread = new Thread(this);
	run = true;
	thread.start();
    }

    public void stop() {
	run = false;
    }

    public void run() {
	while (run) {
	    // Check for collision, draw objects and sleep
	    for (GameObject i : objects) {
		i.update(this);
	    }
	    repaint();
	    try {
		Thread.sleep(17);
	    } catch (Exception e) {
	    }
	}
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	for (GameObject i : objects) {
	    i.paintComponent(g2);
	}
    }

    public static void main(String[] args) {
	WorldWarK panel = new WorldWarK();
    }

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
}
