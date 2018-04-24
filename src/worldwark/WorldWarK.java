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
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
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
    private ArrayList<Rectangle2D> startScreenButtons = new ArrayList<>();
    private ArrayList<GameObject> objects = new ArrayList<>();
    private ArrayList<GameObject> finishedObjects = new ArrayList<>();
    private boolean run = false;
    private int score;
    private Rectangle2D clickedStartScreenButton;

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

    @Override
    public void run() {
	while (run) {
	    // Spawns enemies every 2 seconds at random speeds, health, and positions
	    if (spawnTimer >= 2000) {
		Random rand = new Random();
		int xPos = 50;
		int ySpeed = rand.nextInt(10) + 1;
		int health = rand.nextInt(60) + 40;
		int xSpeed = 0;
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
	    // Paint start screen background
	    BufferedImage image;
	    try {
		image = ImageIO.read(new File("assets/img/background.jpg"));
	    } catch (IOException e) {
		System.out.println("ERROR: background.jpg cannot be read.");
		image = null;
	    }
	    g2.drawImage(image, 0, 0, null);

	    // Paint start instructions
	    Font font1 = new Font("Comic Sans MS", Font.PLAIN, 20);
	    g2.setColor(new Color(255, 215, 0));
	    g2.setFont(font1);
	    g2.drawString("World War K", 25, 100);
	    g2.drawString("Press SPACE to start", 25, 525);

	    // Paint credits button
	    g2.setColor(Color.RED);
	    Rectangle2D creditsBox = new Rectangle2D.Double(380, 750, 100, 40);
	    startScreenButtons.add(creditsBox);
	    g2.draw(creditsBox);
	    g2.drawString("CREDITS", 386, 778);

	    if (clickedStartScreenButton != null) {
		if (clickedStartScreenButton.equals(creditsBox)) {
		    // Draw window background rectangle
		    g2.setColor(new Color(0, 0, 0, 250));
		    g2.fillRect(50, 80, 400, 700);

		    // Draw close button
		    g2.setColor(Color.RED);
		    Rectangle2D closeButton = new Rectangle2D.Double(370, 80, 80, 30);
		    g2.fill(closeButton);
		    startScreenButtons.add(closeButton);
		    g2.setColor(Color.WHITE);
		    g2.drawString("CLOSE", 378, 102);

		    // Draw text
		    Font font2 = new Font("Comic Sans MS", Font.BOLD, 40);
		    g2.setColor(Color.CYAN);
		    g2.setFont(font2);
		    g2.drawString("CREDITS", 150, 150);
		    g2.setFont(font1);
		    g2.drawString("In loving memory of JPlays.", 120, 180);
		    g2.setColor(Color.PINK);
		    g2.drawString("Lead Programmer: LORD Omar Qayum", 70, 230);
		    g2.drawString("Programming Team: Jack Rong", 70, 260);
		    g2.drawString("Justin Tran", 260, 280);
		    g2.drawString("Krista Tian", 260, 300);
		    g2.drawString("Artistic Envisionist: Justin Tran", 70, 330);
		    g2.drawString("Original Artwork: Justin Tran", 70, 360);
		    g2.drawString("Testing Team: Brian Wu", 70, 390);
		    g2.drawString("Justin Reiter", 205, 410);
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(370, 80, 80, 30))) {
		    // Clear credits window if close button is pressed
		    clickedStartScreenButton = null;
		    startScreenButtons.clear();
		    repaint();
		}
	    }
	} else {
	    // Paint game background image
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
		    //System.out.println("SPACE");
		    if (run == false && clickedStartScreenButton == null) {
			start();
		    } else {
			// shoot
			playSound(0);
		    }
		    break;
		case KeyEvent.VK_B:
		    // launch bomb
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
		if (run == false) {
		    // check x and y coords
		    // see if it is contained in a button area
		    for (Rectangle2D i : startScreenButtons) {
			if (i.contains(event.getPoint())) {
			    //System.out.println("Clicked a " + i);
			    clickedStartScreenButton = i;
			    repaint();
			}
		    }
		} else {
		    // shoot
		}
	    } else if (event.getButton() == MouseEvent.BUTTON3) {
		// launch bomb
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
