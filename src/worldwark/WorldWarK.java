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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
	    // Spawn enemies every 2 seconds
	    if (spawnTimer >= 2000 && score <= 2500) { // Controls the spawn rates of the stages depending on the score
		spawnEnemy(this);
	    } else if (spawnTimer >= 4000 && score > 2500 && score <= 5000) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 4000 && score > 5000 && score <= 7500) {
		spawnEnemy(this);
	    }

	    // Update objects' motion
	    for (GameObject i : objects) {
		i.update(this);
	    }

	    // Remove finished objects from object list
	    for (GameObject i : finishedObjects) {
		objects.remove(i);
	    }
	    repaint();

	    // Sleep the thread
	    try {
		Thread.sleep(15);
		spawnTimer += 5;
	    } catch (InterruptedException e) {
		System.out.println("ERROR: Thread.sleep(15) has been interrupted.");
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
	    Font gameTitleFont = null;
	    try {
		gameTitleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf")).deriveFont(70f);
	    } catch (Exception e) {
		System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
	    }
	    g2.setColor(new Color(255, 215, 0));
	    g2.setFont(gameTitleFont);
	    g2.drawString("World", 25, 100);
	    g2.drawString("War", 50, 200);
	    try {
		gameTitleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf")).deriveFont(120f);
	    } catch (Exception e) {
		System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
	    }
	    g2.setFont(gameTitleFont);
	    g2.drawString("K", 280, 240);

	    Font font1 = null;
	    try {
		font1 = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(20f);
	    } catch (Exception e) {
		System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
	    }
	    g2.setColor(new Color(255, 215, 0));
	    g2.setFont(font1);
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
		    g2.drawString("CLOSE", 377, 102);

		    // Read credits.txt file
		    ArrayList<String> credits = new ArrayList<>();
		    BufferedReader inputStream = null;
		    String line;
		    try {
			inputStream = new BufferedReader(new FileReader("assets/txt/credits.txt"));
			do {
			    line = inputStream.readLine();
			    if (line != null) {
				credits.add(line);
			    }
			} while (line != null);
		    } catch (IOException e) {
			System.out.println("ERROR: Cannot open credits.txt.");
		    } finally {
			if (inputStream != null) {
			    try {
				inputStream.close();
			    } catch (IOException e) {
				System.out.println("ERROR: Cannot close inputStream.");
			    }
			}
		    }

		    // Print credits heading
		    Font creditsTitleFont = null;
		    try {
			creditsTitleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf")).deriveFont(50f);
		    } catch (Exception e) {
			System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
		    }
		    g2.setColor(Color.WHITE);
		    g2.setFont(creditsTitleFont);
		    g2.drawString(credits.get(0).substring(1), 100, 170);

		    // Print credits subheading
		    Font creditsRegularFont = null;
		    try {
			creditsRegularFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
		    } catch (Exception e) {
			System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
		    }
		    g2.setFont(creditsRegularFont);
		    g2.drawString(credits.get(1).substring(1), 140, 210);
		    g2.setColor(Color.PINK);

		    // Print credits
		    final int headingXPos = 90;
		    final int nameXPos = 130;
		    int creditsYPos = 260;
		    Font creditsHeadingFont = null;
		    try {
			creditsHeadingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(20f);
		    } catch (Exception e) {
			System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
		    }
		    for (int i = 2; i < credits.size(); i++) {
			if (credits.get(i).charAt(0) == '$') {
			    g2.setFont(creditsHeadingFont);
			    g2.drawString(credits.get(i).substring(1), headingXPos, creditsYPos);
			} else if (credits.get(i).charAt(0) == '%') {
			    g2.setFont(creditsRegularFont);
			    g2.drawString(credits.get(i).substring(1), nameXPos, creditsYPos);
			}

			if (i + 1 < credits.size() && credits.get(i + 1).charAt(0) == '$') {
			    creditsYPos += 10;
			}
			creditsYPos += 30;
		    }
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

    public void spawnEnemy(WorldWarK panel) {
	Enemy enemyLeft = new Enemy(0, 0, 0, 0, 0, 0, 0, 0);
	Enemy enemyRight = new Enemy(0, 0, 0, 0, 0, 0, 0, 0);
	Random rand = new Random();
	int choose = rand.nextInt(5) + 1;
	if (score <= 2500) {
	    //First tier of enemies
	    switch (choose) {
		case 1:
		    for (int i = 0; i <= 150; i++) {
			if (i == 0 || i == 50 || i == 100) {
			    enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 5, 50, 0);
			    enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 5, 50, 0);
			    objects.add(enemyLeft);
			    objects.add(enemyRight);
			}
		    }
		    break;
		case 2:
		    for (int i = 0; i <= 100; i++) {
			if (i == 0 || i == 50 || i == 100) {
			    enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 5, 0, 15, 0);
			    enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -5, 0, 15, 0);
			    objects.add(enemyLeft);
			    objects.add(enemyRight);
			}
		    }
		    break;
		case 3:
		    enemyLeft = new Enemy(250, 0, 64, 64, 0, 5, 50, 0);
		    enemyRight = new Enemy(400, 0, 64, 64, 0, 5, 50, 0);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 4:
		    enemyLeft = new Enemy(25, 0, 64, 64, 0, 5, 50, 0);
		    enemyRight = new Enemy(150, 0, 64, 64, 0, 5, 50, 0);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 5:
		    for (int i = 0; i <= 100; i++) {
			if (i == 0 || i == 50 || i == 100) {
			    enemyLeft = new Enemy(-50 - i, 0 - i, 64, 64, 3, 5, 15, 0);
			    enemyRight = new Enemy(500 + i, 0 - i, 64, 64, -3, 5, 15, 0);
			    enemyRight.setXSpeed(0);
			    enemyLeft.setYSpeed(0);
			    objects.add(enemyLeft);
			    objects.add(enemyRight);
			}
		    }
	    }
	} else if (score > 2500 && score <= 5000) {
	    //Second tier of enemies
	    switch (choose) {
		case 1:
		    break;
		case 2:
		    break;
		case 3:
		    break;
		case 4:
		    break;
		case 5:
		    break;
	    }
	} else if (score > 5000 && score <= 7500) {
	    // Third tier of enemies
	    switch (choose) {
		case 1:
		    break;
		case 2:
		    break;
		case 3:
		    break;
		case 4:
		    break;
		case 5:
		    break;
	    }
	} else if (score >= 10000) {
	    // Boss summoned as well as the fifth tier of enemies
	    switch (choose) {
		case 1:
		    break;
		case 2:
		    break;
		case 3:
		    break;
		case 4:
		    break;
		case 5:
		    break;
	    }
	}
	spawnTimer = 0;
    }

    public void shootBullet() {
	Bullet bullet = new Bullet(player.getXPos() + 24, player.getYPos(), 10, 10);
	objects.add(bullet);
	playSound(0);
    }

    public void launchBomb() {
	Bomb bomb = new Bomb(player.getXPos() + 32, player.getYPos(), 10, 10);
	objects.add(bomb);
	playSound(1);
    }

    public void checkBulletHit(Rectangle2D bulletBox) {
	for (GameObject i : objects) {
	    if (i.getClass().getName().equals("worldwark.Enemy")) {
		// If bullet intersects Enemy object, check if bulletBox intersects rectangle of the enemy
		if (bulletBox.intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
		    // either delete object or lower health of enemy but it's just deleting for now
		    deleteObject(i);
		    // increase score (based on enemy type?)
		}
	    }
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
		    if (run == false && clickedStartScreenButton == null) {
			start();
		    } else {
			shootBullet();
		    }
		    break;
		case KeyEvent.VK_B:
		    launchBomb();
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
		    // Check if x and y coords of mouse click is within a button area
		    for (Rectangle2D i : startScreenButtons) {
			if (i.contains(event.getPoint())) {
			    clickedStartScreenButton = i;
			    repaint();
			}
		    }
		} else {
		    shootBullet();
		}
	    } else if (event.getButton() == MouseEvent.BUTTON3) {
		launchBomb();
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
