package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
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

	player = new Player(this.getWidth() / 2, this.getHeight() - 200, 64, 64, 5, 100, 0, 3);
	objects.add(player);
	//start();
    }

    public void deleteObject(GameObject gameObject) {
	finishedObjects.add(gameObject);
    }

    public void startScreen() {
	Graphics2D g2 = null;
	g2.drawRect(0, 0, 100, 100);
	g2.setColor(Color.BLUE);
    }

    public void start() {
	Thread thread = new Thread(this);
	run = true;
	thread.start();
    }

    public void stop() {
	run = false;
    }

    @Override
    public void run() {
	while (run) {
	    // Spawns enemies every 2 seconds at random speeds, health, and positions
	    if (spawnTimer >= 2000) {
		Random rand = new Random();
		int xPos = rand.nextInt(this.getWidth()) + 1;
		int ySpeed = rand.nextInt(10) + 1;
		int health = rand.nextInt(60) + 40;
		int xSpeed = rand.nextInt(20) - 10;
		xSpeed = xSpeed == 0 ? 1 : xSpeed;
		Enemy enemy = new Enemy(xPos, 0, 64, 64, xSpeed, ySpeed, health, 0);
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
	    } catch (InterruptedException e) {
		System.out.println("ERROR: Thread.sleep(17) has been interrupted.");
	    }
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	if (run == false) {
	    BufferedImage image;
	    try {
		image = ImageIO.read(new File("assets/img/background.jpg"));
	    } catch (IOException e) {
		System.out.println("ERROR: background.jpg cannot be read.");
		image = null;
	    }
	    g2.drawImage(image, 0, 0, null);

	    Font font1 = new Font("Comic Sans MS", Font.PLAIN, 20);
	    g2.setColor(new Color(255, 215, 0));
	    g2.setFont(font1);
	    g2.drawString("World War K", 25, 100);
	    g2.drawString("Press SPACE to start", 25, 525);
	    g2.drawString("Credits", 25, 550);
	    g2.drawString("In loving memory of JPlays... RIP. Long live DapperQuokka.", 25, 575);
	    g2.drawString("Artistic Envisionist: Justin Tran", 25, 600);

	    g2.setColor(Color.RED);
	    g2.drawRect(380, 750, 100, 40);
	    g2.drawString("CREDITS", 400, 750);
	} else {
	    // Add background image
	    BufferedImage image;
	    try {
		image = ImageIO.read(new File("assets/img/background.jpg"));
	    } catch (IOException e) {
		System.out.println("ERROR: background.jpg cannot be read.");
		image = null;
	    }
	    g2.drawImage(image, 0, 0, null);

	    // Paint all GameObjects
	    for (GameObject i : objects) {
		i.paintComponent(g2);
	    }
	}
    }

    public void playSound(int sound) {
	String file;

	// Choose sound to play based on parameter
	switch (sound) {
	    case 0:
		file = "shoot";
		break;
	    case 1:
		file = "bomb";
		break;
	    case 2:
		file = "death";
		break;
	    default:
		file = null;
		break;
	}

	// Play sound file
	if (file != null) {
	    try {
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("assets/sounds/" + file + ".wav"));
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.start();
	    } catch (Exception e) {
		System.out.println("ERROR: " + file + ".wav cannot be played.");
	    }
	} else {
	    System.out.println("ERROR: Specified audio file does not exist.");
	}
    }

    public static void main(String[] args) {
	panel = new WorldWarK();
    }

    private class KeyboardControls implements KeyListener {

	public void keyPressed(KeyEvent event) {
	    // Keyboard controls
	    switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		    player.keyboardMoveLeft();
		    break;
		case KeyEvent.VK_RIGHT:
		    player.keyboardMoveRight();
		    break;
		case KeyEvent.VK_SPACE:
		    System.out.println("SPACE");
		    if (run == false) {
			start();
		    } else {
			// shoot
			playSound(0);
		    }
		    break;
		case KeyEvent.VK_B:
		    System.out.println("B");
		    break;
		default:
		    break;
	    }
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {
	}
    }

    private class MouseControls implements MouseListener, MouseMotionListener {

	public void mouseClicked(MouseEvent event) {
	    // Mouse click controls
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

	public void mouseMoved(MouseEvent event) {
	    // Sets x position of player when mouse is moved
	    player.setXPosition(event.getX());
	}

	public void mouseDragged(MouseEvent event) {
	    // Sets x position of player when mouse is clicked and dragged
	    player.setXPosition(event.getX());
	}
    }
}
