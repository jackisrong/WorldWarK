package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class WorldWarK extends JPanel implements Runnable {

    public static WorldWarK panel;
    private static int spawnTimer = 0;
    private Player player;
    private ArrayList<GameObject> objects = new ArrayList<>();
    private ArrayList<GameObject> finishedObjects = new ArrayList<>();
    private boolean run = false;
    private Image img = Toolkit.getDefaultToolkit().createImage("background.jpg");

    public WorldWarK() {
	JFrame frame = new JFrame("World War K");
	setBackground(Color.black);
	setPreferredSize(new Dimension(500, 800));
	addKeyListener(new KeyboardControls());
	addMouseListener(new MouseControls());
	addMouseMotionListener(new MouseControls());
	setFocusable(true);
	frame.setSize(500, 800);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	frame.add(this);
	frame.pack();

	player = new Player(this.getWidth() / 2, this.getHeight() - 200, 64, 64, 5, 100, 3);
	objects.add(player);
	start();
    }

    public void deleteObject(GameObject gameObject) {
	finishedObjects.add(gameObject);
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
	    // Spawns enemies every 2 seconds at random speeds, health, and positions
	    // If you want, you can set your own set health but i just made it random for now
	    if (spawnTimer >= 2000) {
		Random rand = new Random();
		int xPos = rand.nextInt(this.getWidth()) + 1;
		int ySpeed = rand.nextInt(10) + 1;
		int health = rand.nextInt(60) + 40;
		int xSpeed = rand.nextInt(20) - 10;
		xSpeed = xSpeed == 0 ? 1 : xSpeed;
		Enemy enemy = new Enemy(xPos, 0, 15, 15, ySpeed, xSpeed, health, 0);
		objects.add(enemy);
		spawnTimer = 0;
	    }
	    
	    // Check for collision, draw objects and sleep
	    for (GameObject i : objects) {
		i.update(this);
	    }
	    
	    // Removes objects from list so there wont be a wack exception!!
	    for (GameObject i : finishedObjects) {
		objects.remove(i);
	    }
	    repaint();
	    try {
		Thread.sleep(17);
		spawnTimer += 17;
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

    public void playSound(int sound) {
	String file = "";
	
	// Choose sound to play based on parameter
	switch (sound) {
	    case 0:
		file = "GoGoGo";
		break;
	    case 1:
		file = "RockAndRoll";
		break;
	    case 2:
		file = "Death";
		break;
	}
	
	// Play sound file
	try {
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("assets/sounds/" + file + ".wav"));
	    Clip clip = AudioSystem.getClip();
	    clip.open(audioIn);
	    clip.start();
	} catch (Exception e) {
	    System.out.println("ERROR: " + file + ".wav cannot be played.");
	}
    }

    public static void main(String[] args) {
	panel = new WorldWarK();
    }

    private class KeyboardControls implements KeyListener {

	public void keyPressed(KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.VK_LEFT) {
		//System.out.println("LEFT");
		player.moveLeft();
	    } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
		//System.out.println("RIGHT");
		player.moveRight();
	    } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
		System.out.println("SPACE");
		//playSound(0);
	    } else if (event.getKeyCode() == KeyEvent.VK_B) {
		System.out.println("B");
	    }
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {
	}
    }

    private class MouseControls implements MouseListener, MouseMotionListener {

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

	// Sets x position of player when mouse is moved
	public void mouseMoved(MouseEvent event) {
	    ///System.out.println("Trigged");
	    player.setXPosition(event.getX());
	}

	// Sets x position of player when mouse is clicked and dragged
	// Don't know if this is necessary but just in case
	public void mouseDragged(MouseEvent event) {
	    //System.out.println("Trigged");
	    player.setXPosition(event.getX());
	}
    }
}
