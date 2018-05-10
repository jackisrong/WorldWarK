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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JPanel;

public class WorldWarK extends JPanel implements Runnable {

    public static WorldWarK panel;
    private static int spawnTimer = 0;
    private Player player;
    private ArrayList<Rectangle2D> startScreenButtons = new ArrayList<>();
    private CopyOnWriteArrayList<GameObject> objects = new CopyOnWriteArrayList<>();
    private ArrayList<GameObject> finishedObjects = new ArrayList<>();
    private Rectangle2D clickedStartScreenButton;
    private boolean run = false;
    private boolean gamePaused = false;
    private boolean gameOver = false;
    private int score;
    private Clip clip;
    private float volume;

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

	try {
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("assets/music/myjam.wav"));
	    clip = AudioSystem.getClip();
	    clip.open(audioIn);
	    volume = 0.5f;
	    clip.start();
	} catch (Exception e) {
	    System.out.println("ERROR: myjam.wav cannot be played.");
	}
    }

    public void deleteObject(GameObject gameObject) {
	finishedObjects.add(gameObject);
    }

    public void start() {
	Thread thread = new Thread(this);
	if (gamePaused == false) {
	    score = 0;
	}
	run = true;
	thread.start();
    }

    public void stop() {
	run = false;
    }

    @Override
    public void run() {
	while (run) {
	    // Controls the spawn rates of the stages depending on the score
	    if (spawnTimer >= 3000 && score <= 2500) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 2000 && score > 2500 && score <= 5000) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 1000 && score > 5000 && score <= 7500) {
		spawnEnemy(this);
	    }

	    // Update objects' motion
	    for (GameObject i : objects) {
		i.update(this);
	    }
	    repaint();

	    // Sleep the thread
	    try {
		Thread.sleep(15);
		spawnTimer += 15;
	    } catch (InterruptedException e) {
		System.out.println("ERROR: Thread.sleep(15) has been interrupted.");
	    }
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	if (run == false && gamePaused == false) {
	    if (gameOver == true) {
		// Paint game over text
		Font gameOverFont = null;
		try {
		    gameOverFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf")).deriveFont(70f);
		} catch (Exception e) {
		    System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
		}
		g2.setColor(Color.RED);
		g2.setFont(gameOverFont);
		g2.drawString("GAME", 100, 270);
		g2.drawString("OVER", 100, 350);

		// Paint final score
		Font finalScoreFont = null;
		try {
		    finalScoreFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(30f);
		} catch (Exception e) {
		    System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
		}
		g2.setFont(finalScoreFont);
		g2.setColor(Color.PINK);
		g2.drawString("YOUR FINAL SCORE: " + Integer.toString(score), 60, 430);
		g2.setFont(finalScoreFont.deriveFont(20f));
		g2.drawString("Press R to play again", 60, 580);
		g2.drawString("Press T to return to title screen", 60, 610);
	    } else {
		drawStartScreen(g2);
	    }
	}

	if (run == true || (run == false && gamePaused == true)) {
	    // Paint game background image
	    BufferedImage image;
	    try {
		image = ImageIO.read(new File("assets/img/background.jpg"));
	    } catch (IOException e) {
		System.out.println("ERROR: background.jpg cannot be read.");
		image = null;
	    }
	    g2.drawImage(image, 0, 0, null);

	    // Paint score
	    Font scoreHeading = null;
	    try {
		scoreHeading = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
	    } catch (Exception e) {
		System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
	    }
	    g2.setFont(scoreHeading);
	    g2.setColor(Color.PINK);
	    g2.drawString("SCORE: " + Integer.toString(score), 10, 25);

	    // Paint high score
	    BufferedReader inputStream = null;
	    String previousHighScore = "0";
	    try {
		inputStream = new BufferedReader(new FileReader("assets/data/highScore.txt"));
		previousHighScore = inputStream.readLine();
	    } catch (IOException e) {
		System.out.println("ERROR: Cannot open highScore.txt");
	    } finally {
		if (inputStream != null) {
		    try {
			inputStream.close();
		    } catch (IOException e) {
			System.out.println("ERROR: Cannot close inputStream.");
		    }
		}
	    }
	    g2.drawString("HIGH SCORE: " + previousHighScore, 180, 25);

	    // Draw pause button
	    g2.setColor(Color.RED);
	    g2.fillRect(420, 0, 80, 30);
	    g2.setColor(Color.WHITE);
	    g2.drawString("PAUSE", 427, 22);

	    // Remove finished objects and paint remaining GameObjects
	    for (GameObject i : objects) {
		if (finishedObjects.contains(i)) {
		    objects.remove(i);
		} else {
		    i.paintComponent(g2);
		}
	    }
	}

	if (gamePaused == true) {
	    drawPausedScreen(g2);
	}
    }

    public void drawPausedScreen(Graphics2D g2) {
	// Draw window background rectangle
	g2.setColor(new Color(0, 0, 0, 250));
	g2.setClip(new Rectangle2D.Double(50, 80, 400, 700));
	g2.fillRect(50, 80, 400, 700);

	// Draw close button
	g2.setColor(Color.RED);
	Rectangle2D closeButton = new Rectangle2D.Double(370, 80, 80, 30);
	g2.fill(closeButton);
	startScreenButtons.add(closeButton);
	g2.setColor(Color.WHITE);
	g2.drawString("CLOSE", 377, 102);

	// Print heading
	Font titleFont = null;
	try {
	    titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf")).deriveFont(50f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
	}
	g2.setColor(Color.WHITE);
	g2.setFont(titleFont);
	g2.drawString("PAUSED", 120, 170);

	// Print subheading
	Font contentFont = null;
	try {
	    contentFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
	}
	g2.setFont(contentFont);
	g2.drawString("Your game is paused.", 160, 210);
    }

    public void drawStartScreen(Graphics2D g2) {
	// Paint start screen background
	BufferedImage image;
	try {
	    image = ImageIO.read(new File("assets/img/startScreenBackground.jpg"));
	} catch (IOException e) {
	    System.out.println("ERROR: background.jpg cannot be read.");
	    image = null;
	}
	g2.drawImage(image, 0, 0, null);

	// Paint start screen
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
	if (gameTitleFont != null) {
	    gameTitleFont = gameTitleFont.deriveFont(120f);
	}
	g2.setFont(gameTitleFont);
	g2.drawString("K", 280, 240);

	Font spaceToStartFont = null;
	try {
	    spaceToStartFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(40f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
	}
	g2.setColor(new Color(255, 215, 0));
	g2.setFont(spaceToStartFont);
	g2.drawString("Press SPACEBAR to play", 40, 525);

	Font buttonFont = null;
	if (spaceToStartFont != null) {
	    buttonFont = spaceToStartFont.deriveFont(20f);
	}
	g2.setFont(buttonFont);

	// Paint how to play button
	g2.setColor(Color.RED);
	Rectangle2D instructionsButton = new Rectangle2D.Double(80, 550, 145, 40);
	startScreenButtons.add(instructionsButton);
	g2.draw(instructionsButton);
	g2.drawString("HOW TO PLAY", 86, 578);

	// Paint controls button
	g2.setColor(Color.RED);
	Rectangle2D controlsButton = new Rectangle2D.Double(280, 550, 116, 40);
	startScreenButtons.add(controlsButton);
	g2.draw(controlsButton);
	g2.drawString("CONTROLS", 286, 578);

	// Paint credits button
	g2.setColor(Color.RED);
	Rectangle2D creditsButton = new Rectangle2D.Double(380, 750, 92, 40);
	startScreenButtons.add(creditsButton);
	g2.draw(creditsButton);
	g2.drawString("CREDITS", 386, 778);

	// Paint settings button
	g2.setColor(Color.RED);
	Rectangle2D settingsButton = new Rectangle2D.Double(30, 750, 100, 40);
	startScreenButtons.add(settingsButton);
	g2.draw(settingsButton);
	g2.drawString("SETTINGS", 35, 778);

	// Check if a start screen button has been clicked
	if (clickedStartScreenButton != null) {
	    if (clickedStartScreenButton.equals(instructionsButton)) {
		drawInstructions(g2);
	    } else if (clickedStartScreenButton.equals(controlsButton)) {
		drawControls(g2);
	    } else if (clickedStartScreenButton.equals(creditsButton)) {
		drawCredits(g2);
	    } else if (clickedStartScreenButton.equals(settingsButton)) {
		drawMusic(g2);
	    } else if (clickedStartScreenButton.equals(new Rectangle2D.Double(370, 80, 80, 30))) {
		// Go back to start screen if a close button is pressed
		clickedStartScreenButton = null;
		startScreenButtons.clear();
		repaint();
	    } else {
		FloatControl audioControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = audioControl.getMaximum() - audioControl.getMinimum();
		volume = (audioControl.getValue() - audioControl.getMinimum()) / range;
		if (clickedStartScreenButton.equals(new Rectangle2D.Double(98, 278, 50, 30))) {
		    volume -= 0.1;
		    volume = Math.max(volume, 0);
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(198, 278, 100, 30))) {
		    volume = (float) 0.7;
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(348, 278, 60, 30))) {
		    volume += 0.1;
		    volume = Math.min(volume, 1);
		}
		float gain = (range * volume) + audioControl.getMinimum();
		audioControl.setValue(gain);
		clip.start();
	    }
	}
    }

    public void drawInstructions(Graphics2D g2) {
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

	readDrawFile(g2, "instructions", 60, 70);
    }

    public void drawControls(Graphics2D g2) {
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

	readDrawFile(g2, "controls", 60, 170);
    }

    public void drawCredits(Graphics2D g2) {
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

	readDrawFile(g2, "credits", 100, 140);
    }

    public void drawMusic(Graphics2D g2) {
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

	// Draw close button
	g2.setColor(Color.WHITE);
	Rectangle2D lowButtom = new Rectangle2D.Double(98, 278, 50, 30);
	g2.fill(lowButtom);
	startScreenButtons.add(lowButtom);
	g2.setColor(Color.BLACK);
	g2.drawString("LOW", 100, 300);

	// Draw close button
	g2.setColor(Color.WHITE);
	Rectangle2D medButton = new Rectangle2D.Double(198, 278, 100, 30);
	g2.fill(medButton);
	startScreenButtons.add(medButton);
	g2.setColor(Color.BLACK);
	g2.drawString("NORMAL", 200, 300);

	// Draw close button
	g2.setColor(Color.WHITE);
	Rectangle2D highButton = new Rectangle2D.Double(348, 278, 60, 30);
	g2.fill(highButton);
	startScreenButtons.add(highButton);
	g2.setColor(Color.BLACK);
	g2.drawString("HIGH", 350, 300);

	// Print heading
	Font titleFont = null;
	try {
	    titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf")).deriveFont(50f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
	}
	g2.setColor(Color.WHITE);
	g2.setFont(titleFont);
	g2.drawString("SETTINGS", 80, 170);

	// Print subheading
	Font contentFont = null;
	try {
	    contentFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
	}
	g2.setFont(contentFont);
	g2.drawString("Your democratic freedom", 140, 210);

	//final int headingXPos = 90;
	//final int nameXPos = 130;
	//int textYPos = 260; + 30 before headings, +10 after each line
	Font headingFont = null;
	try {
	    headingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(20f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
	}
	g2.setColor(Color.PINK);
	g2.setFont(headingFont);
	g2.drawString("Music Volume", 100, 260);
    }

    public void readDrawFile(Graphics2D g2, String file, int titleXPos, int subtitleXPos) {
	// Read file
	ArrayList<String> content = new ArrayList<>();
	BufferedReader inputStream = null;
	String line;
	try {
	    inputStream = new BufferedReader(new FileReader("assets/txt/" + file + ".txt"));
	    do {
		line = inputStream.readLine();
		if (line != null) {
		    content.add(line);
		}
	    } while (line != null);
	} catch (IOException e) {
	    System.out.println("ERROR: Cannot open " + file + ".txt.");
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (IOException e) {
		    System.out.println("ERROR: Cannot close inputStream.");
		}
	    }
	}

	// Print heading
	Font titleFont = null;
	try {
	    titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Wartorn.ttf")).deriveFont(50f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file Warton.ttf cannot be opened.");
	}
	g2.setColor(Color.WHITE);
	g2.setFont(titleFont);
	if (content.get(0).charAt(0) == '^') {
	    g2.drawString(content.get(0).substring(1), titleXPos, 170);
	}

	// Print subheading
	Font contentFont = null;
	try {
	    contentFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
	}
	g2.setFont(contentFont);
	if (content.get(1).charAt(0) == '*') {
	    g2.drawString(content.get(1).substring(1), subtitleXPos, 210);
	}

	// Print content
	final int headingXPos = 90;
	final int nameXPos = 130;
	int textYPos = 260;
	Font headingFont = null;
	try {
	    headingFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(20f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
	}
	g2.setColor(Color.PINK);
	for (int i = 2; i < content.size(); i++) {
	    if (content.get(i).charAt(0) == '$') {
		g2.setFont(headingFont);
		g2.drawString(content.get(i).substring(1), headingXPos, textYPos);
	    } else if (content.get(i).charAt(0) == '%') {
		g2.setFont(contentFont);
		g2.drawString(content.get(i).substring(1), nameXPos, textYPos);
	    }

	    if (i + 1 < content.size() && content.get(i + 1).charAt(0) == '$') {
		textYPos += 10;
	    }
	    textYPos += 30;
	}
    }

    public void playSound(int sound) {
	// Choose sound to play based on parameter
	String file;
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
	    case 3:
		file = "powerUpPickUp";
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
	Enemy enemyLeft;
	Enemy enemyRight;
	Random rand = new Random();
	int choose = 0;

	// Spawns enemies dependeing on the score
	if (score <= 2500) {
	    choose = rand.nextInt(1) + 1;
	} else if (score > 2500 && score <= 5000) {
	    choose = rand.nextInt(1) + 8;
	} else if (score > 5000 && score <= 7500) {
	    choose = rand.nextInt(6) + 1;
	}
	System.out.println(choose);
	if (score <= 2500) {
	    // First tier of enemies
	    objects.add(new Boss(120, 0, 256, 256, 3, 3, 1000, 0, 300));
	    switch (choose) {
		case 1:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 5, 50, 3, 50);
			enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 5, 50, 3, 50);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;
		case 2:
		    for (int i = 0; i <= 100; i++) {
			if (i == 0 || i == 50 || i == 100) {
			    enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 5, 0, 15, 0, 50);
			    enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -5, 0, 15, 0, 50);
			    objects.add(enemyLeft);
			    objects.add(enemyRight);
			}
		    }
		    break;
		case 3:
		    enemyLeft = new Enemy(300, 0, 64, 64, 0, 5, 50, 0, 50);
		    enemyRight = new Enemy(400, 0, 64, 64, 0, 5, 50, 0, 50);
		    enemyLeft.setReverse(true);
		    enemyRight.setReverse(true);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 4:
		    enemyLeft = new Enemy(0, 100, 64, 64, 5, 0, 50, 0, 50);
		    enemyRight = new Enemy(0, 300, 64, 64, 5, 0, 50, 0, 50);
		    enemyLeft.setReverse(true);
		    enemyRight.setReverse(true);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 5:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 3, 0, 15, 0, 50);
			enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -3, 0, 15, 0, 50);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		case 6:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(-200 + i, -i, 64, 64, 4, 5, 15, 0, 50);
			enemyRight = new Enemy(700 - i, -i, 64, 64, -4, 5, 15, 0, 50);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
	    }
	} else if (score > 2500 && score <= 5000) {
	    // Second tier of enemies
	    switch (choose) {
		case 1:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 7, 50, 4, 75);
			enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 7, 50, 4, 75);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;
		case 2:
		    for (int i = 0; i <= 100; i++) {
			if (i == 0 || i == 50 || i == 100) {
			    enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 7, 0, 15, 1, 75);
			    enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -7, 0, 15, 1, 75);
			    objects.add(enemyLeft);
			    objects.add(enemyRight);
			}
		    }
		    break;
		case 3:
		    enemyLeft = new Enemy(300, 0, 64, 64, 0, 7, 50, 1, 75);
		    enemyRight = new Enemy(400, 0, 64, 64, 0, 7, 50, 1, 75);
		    enemyLeft.setReverse(true);
		    enemyRight.setReverse(true);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 4:
		    enemyLeft = new Enemy(0, 100, 64, 64, 7, 0, 50, 1, 75);
		    enemyRight = new Enemy(0, 300, 64, 64, 7, 0, 50, 1, 75);
		    enemyLeft.setReverse(true);
		    enemyRight.setReverse(true);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 5:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 5, 0, 15, 1, 75);
			enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -5, 0, 15, 1, 75);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;
		case 6:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(-200 + i, -i, 64, 64, 5, 7, 15, 1, 75);
			enemyRight = new Enemy(700 - i, -i, 64, 64, -5, 7, 15, 1, 75);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;
		case 7:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(0 - i * 2, 100 + i / 2, 64, 64, 3, 3, 50, 1, 75);
			enemyRight = new Enemy(450 + i * 2, 300 - i / 2, 64, 64, -3, 3, 50, 1, 75);
			enemyLeft.setReverse(true);
			enemyRight.setReverse(true);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;
		case 8:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, -3, 7, 50, 4, 75);
			enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 3, 7, 50, 4, 75);
			enemyLeft.setReverse(true);
			enemyRight.setReverse(true);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;

	    }
	} else if (score > 5000 && score <= 7500) {
	    // Third tier of enemies
	    switch (choose) {
		case 1:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(50 + i, 0 - i, 64, 64, 0, 10, 50, 5, 100);
			enemyRight = new Enemy(385 - i, 0 - i, 64, 64, 0, 10, 50, 5, 100);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;
		case 2:
		    for (int i = 0; i <= 100; i++) {
			if (i == 0 || i == 50 || i == 100) {
			    enemyLeft = new Enemy(50 - i * 2, 250, 64, 64, 10, 0, 15, 2, 100);
			    enemyRight = new Enemy(450 + i * 2, 150, 64, 64, -10, 0, 15, 2, 100);
			    objects.add(enemyLeft);
			    objects.add(enemyRight);
			}
		    }
		    break;
		case 3:
		    enemyLeft = new Enemy(300, 0, 64, 64, 0, 10, 50, 2, 100);
		    enemyRight = new Enemy(400, 0, 64, 64, 0, 10, 50, 2, 100);
		    enemyLeft.setReverse(true);
		    enemyRight.setReverse(true);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 4:
		    enemyLeft = new Enemy(0, 100, 64, 64, 10, 0, 50, 2, 100);
		    enemyRight = new Enemy(0, 300, 64, 64, 10, 0, 50, 2, 100);
		    enemyLeft.setReverse(true);
		    enemyRight.setReverse(true);
		    objects.add(enemyLeft);
		    objects.add(enemyRight);
		    break;
		case 5:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(-200 + i, 32 + i, 64, 64, 7, 0, 15, 2, 100);
			enemyRight = new Enemy(700 - i, 296 - i, 64, 64, -7, 0, 15, 2, 100);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
		    break;
		case 6:
		    for (int i = 0; i < 150; i += 50) {
			enemyLeft = new Enemy(-200 + i, -i, 64, 64, 7, 10, 15, 2, 50);
			enemyRight = new Enemy(700 - i, -i, 64, 64, -7, 10, 15, 2, 50);
			objects.add(enemyLeft);
			objects.add(enemyRight);
		    }
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

    public void dropPowerUp(GameObject enemy) {
	Random rng = new Random();

	// Chance for a power up to drop
	if (rng.nextInt(50) == 0) {
	    // Choose which power up to drop
	    if (rng.nextInt(2) == 0) {
		// Drop bomb
		PowerUp powerUp = new PowerUp(enemy.getXPos(), enemy.getYPos(), 32, 32, 0);
		objects.add(powerUp);
	    } else {
		// Drop weapon upgrade
		PowerUp powerUp = new PowerUp(enemy.getXPos(), enemy.getYPos(), 32, 32, 1);
		objects.add(powerUp);
	    }
	}
    }

    public void checkBulletHit(Bullet bullet) {
	for (GameObject i : objects) {
	    if (i.getClass().getName().equals("worldwark.Enemy")) {
		if (bullet.getRectangle().intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
		    // If bulletBox intersects rectangle of the enemy, kill the enemy
		    dropPowerUp(i);
		    deleteObject(i);
		    deleteObject(bullet);
		    // Increases score (based on enemy type in the future?)
		    score += i.getPoints();
		}
	    }
	}
    }

    public void checkEnemyCollision(Enemy enemy) throws IOException {
	if (enemy.getRectangle().intersects(player.getXPos(), player.getYPos(), player.getWidth(), player.getHeight())) {
	    // Deletes enemy upon collision and player loses health
	    deleteObject(enemy);
	    player.loseHealth(10);
	    if (player.getHealth() <= 0) {
		// If player loses all of their health, reset game
		gameOver();
	    }
	}
    }

    public void checkPowerUpPickUp(PowerUp powerUp) {
	if (powerUp.getRectangle().intersects(player.getXPos(), player.getYPos(), player.getWidth(), player.getHeight())) {
	    if (powerUp.getType() == 0) {
		player.pickUpBomb();
	    } else if (powerUp.getType() == 1) {
		player.upgradeWeapon();
	    }
	    deleteObject(powerUp);
	    playSound(3);
	}
    }

    public void gameOver() throws IOException {
	run = false;
	gameOver = true;
	player.setHealth(100);
	objects.clear();
	finishedObjects.clear();
	objects.add(player);
	repaint();

	BufferedReader inputStream = null;
	String previousHighScore = "0";
	try {
	    inputStream = new BufferedReader(new FileReader("assets/data/highScore.txt"));
	    previousHighScore = inputStream.readLine();
	} catch (IOException e) {
	    System.out.println("ERROR: Cannot open highScore.txt");
	} finally {
	    if (inputStream != null) {
		inputStream.close();
	    }
	}

	FileWriter outputStream = null;
	try {
	    outputStream = new FileWriter("assets/data/highScore.txt");
	    if (score > Integer.parseInt(previousHighScore)) {
		outputStream.write(score + "\r\n");
	    } else {
		outputStream.write(previousHighScore + "\r\n");
	    }
	} catch (FileNotFoundException exception) {
	    System.out.println("ERROR: Cannot write to highScore.txt");
	} finally {
	    if (outputStream != null) {
		outputStream.close();
	    }
	}
    }

    public static void main(String[] args) {
	panel = new WorldWarK();

    }

    private class KeyboardControls implements KeyListener {

	@Override
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
		    if (run == false && clickedStartScreenButton == null && gameOver == false) {
			start();
		    } else {
			shootBullet();
		    }
		    break;
		case KeyEvent.VK_B:
		    launchBomb();
		    break;
		case KeyEvent.VK_R:
		    if (run == false && gameOver == true) {
			gameOver = false;
			start();
		    }
		    break;
		case KeyEvent.VK_T:
		    if (run == false && gameOver == true) {
			gameOver = false;
			repaint();
		    }
		    break;
		case KeyEvent.VK_ESCAPE:
		    if (run == true && gamePaused == false) {
			gamePaused = true;
			run = false;
		    } else if (run == false && gamePaused == true) {
			start();
			gamePaused = false;
			repaint();
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

    private class MouseControls implements MouseListener, MouseMotionListener {

	@Override
	public void mouseClicked(MouseEvent event) {
	    // Mouse click controls
	    if (event.getButton() == MouseEvent.BUTTON1) {
		if (run == false) {
		    if (gamePaused == true) {
			if (new Rectangle2D.Double(370, 80, 80, 30).contains(event.getPoint())) {
			    start();
			    gamePaused = false;
			    repaint();
			}
		    } else {
			// Check if x and y coords of mouse click is within a button area
			for (Rectangle2D i : startScreenButtons) {
			    if (i.contains(event.getPoint())) {
				clickedStartScreenButton = i;
				repaint();
			    }
			}
		    }
		} else {
		    if (new Rectangle2D.Double(420, 0, 80, 30).contains(event.getPoint())) {
			run = false;
			gamePaused = true;
			repaint();
		    }

		    if (gamePaused == false) {
			shootBullet();
		    }
		}
	    } else if (event.getButton() == MouseEvent.BUTTON3) {
		launchBomb();
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
	    player.setXPosition(event.getX());
	}

	@Override
	public void mouseDragged(MouseEvent event) {
	    // Sets x position of player when mouse is clicked and dragged
	    player.setXPosition(event.getX());
	}
    }
}
