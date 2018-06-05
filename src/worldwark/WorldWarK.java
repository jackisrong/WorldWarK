package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    public int shootTimer;
    public Player player;
    public ArrayList<Rectangle2D> startScreenButtons = new ArrayList<>();
    public CopyOnWriteArrayList<GameObject> objects = new CopyOnWriteArrayList<>();
    public Rectangle2D clickedStartScreenButton;
    public boolean run = false;
    public boolean gamePaused = false;
    public boolean gameOver = false;
    public int score;
    private int spawnTimer;
    private int fireTimer = 0;
    private Clip clip;
    private float volume;
    private float fxVolume;
    private FloatControl audioControl;
    private int previousHighScore = 0;
    private int highScore = 0;
    private int backgroundYScroll = 0;
    private BufferedImage backgroundImage;
    private EnemyFactory e;

    public WorldWarK() {
	JFrame frame = new JFrame("World War K");
	setBackground(Color.black);
	setPreferredSize(new Dimension(500, 800));
	addKeyListener(new KeyboardControls(this));
	addMouseListener(new MouseControls(this));
	addMouseMotionListener(new MouseControls(this));
	setFocusable(true);
	frame.addKeyListener(new KeyboardControls(this));
	frame.addMouseListener(new MouseControls(this));
	frame.setSize(500, 800);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	frame.add(this);
	frame.pack();

	// Get saved volume
	BufferedReader inputStream = null;
	try {
	    inputStream = new BufferedReader(new FileReader("assets/data/volume.txt"));
	    volume = Float.parseFloat(inputStream.readLine());
	} catch (Exception e) {
	    System.out.println("ERROR: Cannot read volume.txt");
	} finally {
	    try {
		inputStream.close();
	    } catch (IOException e) {
		System.out.println("ERROR: Cannot close inputStream");
	    } catch (NullPointerException e) {
		System.out.println("ERROR: volume.txt doesn't exist, setting volume to default value of 0.5");
		volume = (float) 0.5;
	    }
	    inputStream = null;
	}

	// Play music
	try {
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("assets/music/myjam.wav"));
	    clip = AudioSystem.getClip();
	    clip.open(audioIn);
	    clip.loop(10);
	    audioControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    float range = audioControl.getMaximum() - audioControl.getMinimum();
	    float gain = (range * volume) + audioControl.getMinimum();
	    audioControl.setValue(gain);
	    clip.start();
	} catch (Exception e) {
	    System.out.println("ERROR: myjam.wav cannot be played.");
	}

	// Get saved fx volume
	try {
	    inputStream = new BufferedReader(new FileReader("assets/data/fxVolume.txt"));
	    fxVolume = Float.parseFloat(inputStream.readLine());
	} catch (Exception e) {
	    System.out.println("ERROR: Cannot read fxVolume.txt");
	} finally {
	    try {
		inputStream.close();
	    } catch (IOException e) {
		System.out.println("ERROR: Cannot close inputStream");
	    } catch (NullPointerException e) {
		System.out.println("ERROR: fxVolume.txt doesn't exist, setting volume to default value of 0.5");
		fxVolume = (float) 0.5;
	    }
	}

	// Get previous high score
	try {
	    inputStream = new BufferedReader(new FileReader("assets/data/highScore.txt"));
	    highScore = Integer.parseInt(inputStream.readLine());
	    previousHighScore = highScore;
	} catch (IOException e) {
	    System.out.println("ERROR: Cannot open highScore.txt");
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (IOException e) {
		    System.out.println("ERROR: Cannot close inputStream");
		}
	    }
	}

	// Get background image
	try {
	    backgroundImage = ImageIO.read(new File("assets/img/background.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: background.png cannot be read.");
	    backgroundImage = null;
	}
    }

    public void deleteObject(GameObject gameObject) {
	objects.remove(gameObject);
    }

    public void start() {
	Thread thread = new Thread(this);
	if (gamePaused == false) {
	    player = new Player(this.getWidth() / 2, this.getHeight() - 200, 64, 64, 5, 100, 1, 3);
	    objects.add(player);
	    score = 90000;
	    spawnTimer = 0;
	    shootTimer = player.getWeaponCooldown();
	    e = new EnemyFactory();
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
	    // Update high score
	    if (score > highScore) {
		highScore = score;
	    }

	    // Controls the spawn rates of the stages depending on the score
	    if (spawnTimer >= 7000 && score <= 2500) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 6000 && score > 2500 && score <= 5000) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 5500 && score > 5000 && score <= 7500) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 7000 && score >= 10000) {
		spawnEnemy(this);
	    }

	    if (fireTimer >= 1500) {
		enemyFire();
	    }

	    // Update objects' motion
	    for (GameObject i : objects) {
		i.update(this);
	    }
	    repaint();

	    // Sleep the thread
	    try {
		Thread.sleep(15);
		spawnTimer += 20;
		fireTimer += 15;
		if (shootTimer <= player.getWeaponCooldown()) {
		    shootTimer += 15;
		}
	    } catch (InterruptedException e) {
		System.out.println("ERROR: Thread.sleep(15) has been interrupted.");
	    }
	}
    }

    public void enemyFire() {
	ArrayList<Enemy> enemies = new ArrayList<>();
	Boss boss = null;
	for (GameObject i : objects) {
	    if (i instanceof Enemy && !i.isOutsideScreen()) {
		enemies.add((Enemy) i);
	    }
	    if (i instanceof Boss) {
		boss = (Boss) i;
	    }
	}

	if (boss != null && fireTimer % boss.getFiringRate() == 0) {
	    if (boss.getHealth() > boss.getInitialHealth() / 2) {
		//Random rand = new Random();
		//int dX = boss.getXPos() - player.getXPos();
		//int dY = boss.getYPos() - player.getYPos();
		//objects.add(new EnemyBullet(boss.getXPos() + 30, boss.getYPos(), 3, 8, dX / 67, dY / 67, 10));
		objects.add(new BossLaser(boss.getXPos() + 30, boss.getYPos() + boss.getHeight() + 3, 5, 0, 10, boss));
	    } else if (boss.getHealth() <= boss.getInitialHealth() / 2 && boss.getHealth() > boss.getInitialHealth() / 10) {
		//boss.setFiringRate(750);
		//int randomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
		//int nextRandomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
		// position of boss fire is off due to size of boss; change position
		//objects.add(new EnemyBullet(boss.getXPos() + 30, boss.getYPos(), 3, 8, randomDX / 67, dY / 67, 15));
		//objects.add(new EnemyBullet(boss.getXPos() + 30, boss.getYPos(), 3, 8, nextRandomDX / 67, dY / 67, 15));
		objects.add(new BossLaser(boss.getXPos() + 180, boss.getYPos() + boss.getHeight() + 3, 5, 0, 15, boss));
	    } else if (boss.getHealth() <= boss.getInitialHealth() / 10) {
		//boss.setFiringRate(500);
		//int randomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
		//int nextRandomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
		// position of boss fire is off due to size of boss; change position
		//objects.add(new EnemyBullet(boss.getXPos() + 30, boss.getYPos(), 3, 8, randomDX / 67, dY / 67, 20));
		//objects.add(new EnemyBullet(boss.getXPos() + 30, boss.getYPos(), 3, 8, nextRandomDX / 67, dY / 67, 20));
		objects.add(new BossLaser(boss.getXPos() + 30, boss.getYPos() + boss.getHeight() + 3, 5, 0, 10, boss));
		objects.add(new BossLaser(boss.getXPos() + 180, boss.getYPos() + boss.getHeight() + 3, 5, 0, 10, boss));
	    }
	    playSound(8);
	}

	if (enemies.size() > 0 && fireTimer % (enemies.get(0).getFiringRate()) == 0) {
	    int prevSelectedEnemy = -1;
	    ArrayList<Integer> chosenEnemies = new ArrayList<>();
	    if (enemies.size() >= 3) {
		for (int i = 0; i < 3; i++) {
		    Random rand = new Random();
		    int selectedIndex = rand.nextInt(enemies.size());
		    while (selectedIndex == prevSelectedEnemy) {
			selectedIndex = rand.nextInt(enemies.size());
		    }
		    chosenEnemies.add(selectedIndex);
		}
	    } else {
		for (int i = 0; i < enemies.size(); i++) {
		    chosenEnemies.add(i);
		}
	    }
	    for (int i = 0; i < chosenEnemies.size(); i++) {
		Enemy selectedEnemy = enemies.get(chosenEnemies.get(i));
		int dX = selectedEnemy.getXPos() - player.getXPos();
		int dY = selectedEnemy.getYPos() - player.getYPos();
		int bulletXSpeed = dX / 67;
		int bulletYSpeed = dY / 67;
		if (selectedEnemy.getYSpeed() < bulletYSpeed) {
		    double bulletSpeedMultiplier = (double) selectedEnemy.getYSpeed() / bulletYSpeed;
		    bulletXSpeed *= Math.pow(bulletSpeedMultiplier, 2);
		    bulletYSpeed *= Math.pow(bulletSpeedMultiplier, 2);
		}
		if (dY <= -100) {
		    objects.add(new EnemyBullet(selectedEnemy.getXPos() + 30, selectedEnemy.getYPos(), 3, 8, bulletXSpeed, bulletYSpeed, 10));
		    playSound(0);
		}
	    }
	    fireTimer = 0;
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	if (run == false && gamePaused == false) {
	    if (gameOver == true) {
		// Paint game over background
		BufferedImage image;
		try {
		    image = ImageIO.read(new File("assets/img/gameOverScreenBackground.png"));
		} catch (IOException e) {
		    System.out.println("ERROR: gameOverScreenBackground.png cannot be read.");
		    image = null;
		}
		g2.drawImage(image, 0, 0, null);

		// Paint final score
		Font finalScoreFont = null;
		try {
		    finalScoreFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(40f);
		} catch (Exception e) {
		    System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
		}
		g2.setFont(finalScoreFont);
		g2.setColor(Color.WHITE);
		g2.drawString("YOUR SCORE: " + Integer.toString(score), 55, 430);
		g2.drawString("HIGH SCORE: " + highScore, 55, 480);
		if (highScore > previousHighScore) {
		    g2.setColor(Color.CYAN);
		    g2.drawString("NEW HIGH SCORE!", 75, 350);
		}

		// Paint play again info
		Font playAgainFont = null;
		try {
		    playAgainFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
		} catch (Exception e) {
		    System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
		}
		g2.setFont(playAgainFont);
		g2.setColor(Color.WHITE);
		g2.drawString("Press R to play again", 220, 650);
		g2.drawString("Press T to return to title screen", 220, 680);
	    } else {
		drawStartScreen(g2);
	    }
	}

	if (run == true || (run == false && gamePaused == true)) {
	    // Paint game background image
	    g2.drawImage(backgroundImage, 0, -backgroundImage.getHeight() + 800 - backgroundYScroll, null);
	    if (backgroundYScroll == -backgroundImage.getHeight() + 800) {
		backgroundYScroll = 0;
	    } else {
		backgroundYScroll -= 2;
	    }

	    // Paint score and high score
	    Font scoreHeading = null;
	    try {
		scoreHeading = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
	    } catch (Exception e) {
		System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
	    }
	    g2.setFont(scoreHeading);
	    g2.setColor(Color.BLACK);
	    g2.drawString("SCORE: " + Integer.toString(score), 10, 25);
	    g2.drawString("HIGH SCORE: " + highScore, 180, 25);

	    // Paint number of bombs and weapon level
	    g2.drawString("BOMBS: " + player.getNumberOfBombs(), 10, 790);
	    g2.drawString("WEAPON: LEVEL " + player.getWeaponLevel() + "/5", 300, 790);

	    // Draw pause button
	    g2.setColor(Color.BLACK);
	    g2.fillRect(420, 0, 80, 30);
	    g2.setColor(Color.WHITE);
	    g2.drawString("PAUSE", 430, 22);

	    // Paint GameObjects
	    for (GameObject i : objects) {
		i.paintComponent(g2);
	    }
	}

	if (gamePaused == true) {
	    drawPausedScreen(g2);
	}

	if (run == true && gamePaused == false) {
	    g2.setClip(null);
	    Font scoreHeading = null;
	    try {
		scoreHeading = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(200f);
	    } catch (Exception e) {
		System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
	    }
	    g2.setFont(scoreHeading);
	    g2.setColor(Color.BLACK);
	    if (spawnTimer < 1000) {
		g2.drawString("3", 210, 400);
	    } else if (spawnTimer > 1000 && spawnTimer < 2000) {
		g2.drawString("2", 210, 400);
	    } else if (spawnTimer > 2000 && spawnTimer < 3000) {
		g2.drawString("1", 220, 400);
	    } else if (spawnTimer > 3000 & spawnTimer < 4000) {
		g2.drawString("GO!", 85, 400);
	    }
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
	    image = ImageIO.read(new File("assets/img/startScreenBackground.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: startScreenBackground.png cannot be read.");
	    image = null;
	}
	g2.drawImage(image, 0, 0, null);

	// Paint start screen content
	Font buttonFont = null;
	try {
	    buttonFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinBold.ttf")).deriveFont(20f);
	} catch (Exception e) {
	    System.out.println("ERROR: Font file CabinBold.ttf cannot be opened.");
	}
	g2.setFont(buttonFont);

	// Paint button rectangles
	g2.setColor(Color.BLACK);
	Rectangle2D instructionsButton = new Rectangle2D.Double(80, 550, 145, 40);
	startScreenButtons.add(instructionsButton);
	g2.fill(instructionsButton);
	g2.draw(instructionsButton);
	Rectangle2D controlsButton = new Rectangle2D.Double(280, 550, 145, 40);
	startScreenButtons.add(controlsButton);
	g2.fill(controlsButton);
	g2.draw(controlsButton);
	Rectangle2D powerUpsButton = new Rectangle2D.Double(80, 600, 145, 40);
	startScreenButtons.add(powerUpsButton);
	g2.fill(powerUpsButton);
	g2.draw(powerUpsButton);
	Rectangle2D enemiesButton = new Rectangle2D.Double(280, 600, 145, 40);
	startScreenButtons.add(enemiesButton);
	g2.fill(enemiesButton);
	g2.draw(enemiesButton);
	Rectangle2D creditsButton = new Rectangle2D.Double(380, 750, 92, 40);
	startScreenButtons.add(creditsButton);
	g2.fill(creditsButton);
	g2.draw(creditsButton);
	Rectangle2D settingsButton = new Rectangle2D.Double(30, 750, 100, 40);
	startScreenButtons.add(settingsButton);
	g2.fill(settingsButton);
	g2.draw(settingsButton);

	// Paint button text
	g2.setColor(Color.WHITE);
	g2.drawString("HOW TO PLAY", 86, 578);
	g2.drawString("CONTROLS", 300, 578);
	g2.drawString("POWER UPS", 96, 628);
	g2.drawString("ENEMIES", 312, 628);
	g2.drawString("CREDITS", 386, 778);
	g2.drawString("SETTINGS", 35, 778);

	// Check if a start screen button has been clicked
	if (clickedStartScreenButton != null) {
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

	    if (clickedStartScreenButton.equals(instructionsButton)) {
		readDrawFile(g2, "instructions", 60, 70);
	    } else if (clickedStartScreenButton.equals(controlsButton)) {
		readDrawFile(g2, "controls", 60, 100);
	    } else if (clickedStartScreenButton.equals(powerUpsButton)) {
		readDrawFile(g2, "powerUps", 45, 80);
	    } else if (clickedStartScreenButton.equals(enemiesButton)) {
		readDrawFile(g2, "enemies", 100, 45);
	    } else if (clickedStartScreenButton.equals(creditsButton)) {
		readDrawFile(g2, "credits", 100, 140);
	    } else if (clickedStartScreenButton.equals(settingsButton)) {
		drawMusic(g2);
	    } else if (clickedStartScreenButton.equals(new Rectangle2D.Double(370, 80, 80, 30))) {
		// Remove dialogue boxes if close button is pressed
		clickedStartScreenButton = null;
		startScreenButtons.clear();
		repaint();
	    } else {
		// Change volume based on volume button pressed
		System.out.println("VOLUME BEFORE: " + volume);
		System.out.println("FX VOLUME BEFORE: " + fxVolume);
		drawMusic(g2);
		audioControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = audioControl.getMaximum() - audioControl.getMinimum();
		//volume = (audioControl.getValue() - audioControl.getMinimum()) / range;
		if (clickedStartScreenButton.equals(new Rectangle2D.Double(100, 278, 100, 30))) {
		    volume -= 0.1;
		    volume = Math.max(Math.round(volume * 10) / (float) 10.0, 0);
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(330, 278, 100, 30))) {
		    volume += 0.1;
		    volume = Math.min(Math.round(volume * 10) / (float) 10.0, 1);
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(100, 378, 100, 30))) {
		    fxVolume -= 0.1;
		    fxVolume = Math.max(Math.round(fxVolume * 10) / (float) 10.0, 0);
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(330, 378, 100, 30))) {
		    fxVolume += 0.1;
		    fxVolume = Math.min(Math.round(fxVolume * 10) / (float) 10.0, 1);
		}
		clickedStartScreenButton = new Rectangle2D.Double(30, 750, 100, 40);
		float gain = (range * volume) + audioControl.getMinimum();
		audioControl.setValue(gain);
		clip.start();

		// Save volume setting to file
		FileWriter outputStream = null;
		try {
		    outputStream = new FileWriter("assets/data/volume.txt");
		    outputStream.write(volume + "\r\n");
		} catch (Exception e) {
		    System.out.println("ERROR: Cannot write to volume.txt");
		} finally {
		    if (outputStream != null) {
			try {
			    outputStream.close();
			} catch (IOException e) {
			    System.out.println("ERROR: Cannot close outputStream");
			}
		    }
		}

		// Save fx volume to file
		try {
		    outputStream = new FileWriter("assets/data/fxVolume.txt");
		    outputStream.write(fxVolume + "\r\n");
		} catch (Exception e) {
		    System.out.println("ERROR: Cannot write to fxVolume.txt");
		} finally {
		    try {
			outputStream.close();
		    } catch (IOException e) {
			System.out.println("ERROR: Cannot close outputStream");
		    }
		}

		repaint();
	    }
	    System.out.println("VOLUME AFTER: " + volume);
	    System.out.println("FX VOLUME AFTER: " + fxVolume);
	}
    }

    public void drawMusic(Graphics2D g2) {
	// Draw low volume button
	g2.setColor(Color.WHITE);
	Rectangle2D lowButton = new Rectangle2D.Double(100, 278, 100, 30);
	g2.draw(lowButton);
	startScreenButtons.add(lowButton);
	//g2.setColor(Color.BLACK);
	g2.drawString("LOWER", 115, 300);

	// Draw current volume
	g2.drawString(Float.toString(volume), 245, 300);

	// Draw high volume button
	g2.setColor(Color.WHITE);
	Rectangle2D highButton = new Rectangle2D.Double(330, 278, 100, 30);
	g2.draw(highButton);
	startScreenButtons.add(highButton);
	//g2.setColor(Color.BLACK);
	g2.drawString("HIGHER", 345, 300);

	// Draw low sound fx button
	g2.setColor(Color.WHITE);
	Rectangle2D lowFxButton = new Rectangle2D.Double(100, 378, 100, 30);
	g2.draw(lowFxButton);
	startScreenButtons.add(lowFxButton);
	//g2.setColor(Color.BLACK);
	g2.drawString("LOWER", 115, 400);

	// Draw current fx volume
	g2.drawString(Float.toString(fxVolume), 245, 400);

	// Draw high sound fx button
	g2.setColor(Color.WHITE);
	Rectangle2D highFxButton = new Rectangle2D.Double(330, 378, 100, 30);
	g2.draw(highFxButton);
	startScreenButtons.add(highFxButton);
	//g2.setColor(Color.BLACK);
	g2.drawString("HIGHER", 345, 400);

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
	g2.drawString("Sound Effects Volume", 100, 360);
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
	    case 4:
		file = "ow";
		break;
	    case 5:
		file = "enemyShoot";
		break;
	    case 6:
		file = "beatHighScore";
		break;
	    case 7:
		file = "bombThrown";
		break;
	    case 8:
		file = "laser";
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
		float range = ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).getMaximum() - ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).getMinimum();
		float gain = (range * fxVolume) + ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).getMinimum();
		((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(gain);
		clip.start();
	    } catch (Exception e) {
		System.out.println("ERROR: " + file + ".wav cannot be played.");
	    }
	} else {
	    System.out.println("ERROR: Specified audio file does not exist.");
	}
    }

    public void spawnEnemy(WorldWarK panel) {
	spawnTimer = 4000;
	objects.addAll(e.makeEnemies(score));
    }

    public void shootBullet() {
	switch (player.getWeaponLevel()) {
	    case 1:
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, 0, 10));
		break;
	    case 2:
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, 0, 10));
		break;
	    case 3:
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, 5, 10));
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, 0, 10));
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, -5, 10));
		break;
	    case 4:
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, 3, 10));
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, 0, 10));
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, -3, 10));
		break;
	    case 5:
		objects.add(new Bullet(player.getXPos() + 30, player.getYPos(), 3, 8, 0, 10));
		break;
	    default:
		break;
	}

	playSound(0);
    }

    public void launchBomb() {
	if (player.getNumberOfBombs() > 0) {
	    Bomb bomb = new Bomb(player.getXPos() + 32, player.getYPos(), 8, 16);
	    objects.add(bomb);
	    player.useBomb();
	    playSound(7);
	}
    }

    public void dropPowerUp(GameObject enemy) {
	Random rng = new Random();
	int choosePowerUp = rng.nextInt(3);

	// Chance for a power up to drop
	if (rng.nextInt(50) == 0) {
	    // Choose which power up to drop
	    switch (choosePowerUp) {
		case 0:
		    // Drop bomb power up
		    objects.add(new PowerUp(enemy.getXPos(), enemy.getYPos(), 32, 32, 0));
		    break;
		case 1:
		    // Drop weapon upgrade
		    if (player.getWeaponLevel() <= 4) {
			objects.add(new PowerUp(enemy.getXPos(), enemy.getYPos(), 32, 32, 1));
		    }
		    break;
		case 2:
		    // Drop health pack
		    objects.add(new PowerUp(enemy.getXPos(), enemy.getYPos(), 32, 32, 2));
		    break;
		default:
		    break;
	    }
	}
    }

    public void gameOver() throws IOException {
	playSound(2);
	run = false;
	gameOver = true;
	backgroundYScroll = 0;
	objects.clear();
	repaint();

	if (score > highScore) {
	    previousHighScore = highScore;
	    highScore = score;
	    playSound(6);
	}

	// Save high score to file
	FileWriter outputStream = null;
	try {
	    outputStream = new FileWriter("assets/data/highScore.txt");
	    outputStream.write(highScore + "\r\n");
	} catch (FileNotFoundException exception) {
	    System.out.println("ERROR: Cannot write to highScore.txt");
	} finally {
	    if (outputStream != null) {
		outputStream.close();
	    }
	}
    }

    public static void main(String[] args) throws IOException {
	panel = new WorldWarK();
    }
}
