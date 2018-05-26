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
import java.util.Scanner;
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
    private static int fireTimer = 0;
    private static int shootTimer;
    private Player player;
    private ArrayList<Rectangle2D> startScreenButtons = new ArrayList<>();
    private CopyOnWriteArrayList<GameObject> objects = new CopyOnWriteArrayList<>();
    private Rectangle2D clickedStartScreenButton;
    private boolean run = false;
    private boolean gamePaused = false;
    private boolean gameOver = false;
    private int score;
    private Clip clip;
    private float volume;
    private int b = 1;
    private FloatControl audioControl;
    private int previousHighScore = 0;
    private int highScore = 0;

    public WorldWarK() {
	JFrame frame = new JFrame("World War K");
	setBackground(Color.black);
	setPreferredSize(new Dimension(500, 800));
	addKeyListener(new KeyboardControls(this));
	addMouseListener(new MouseControls(this));
	addMouseMotionListener(new MouseControls(this));
	setFocusable(true);
	frame.setSize(500, 800);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	frame.add(this);
	frame.pack();

	// Get saved volume
	BufferedReader inputStream = null;
	//Scanner sc;
	try {
	    inputStream = new BufferedReader(new FileReader("assets/data/volume.txt"));
	    //sc = new Scanner(new File("assets/data/volume.txt"));
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("assets/music/myjam.wav"));
	    clip = AudioSystem.getClip();
	    clip.open(audioIn);
	    volume = Float.parseFloat(inputStream.readLine());
	    audioControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    float range = audioControl.getMaximum() - audioControl.getMinimum();
	    float gain = (range * volume) + audioControl.getMinimum();
	    audioControl.setValue(gain);
	    clip.start();
	} catch (Exception e) {
	    System.out.println("ERROR: myjam.wav cannot be played.");
	} finally {
	    try {
		inputStream.close();
	    } catch (IOException e) {
		System.out.println("ERROR: Cannot close inputStream");
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
    }

    public void deleteObject(GameObject gameObject) {
	objects.remove(gameObject);
    }

    public Player getPlayer() {
	return player;
    }

    public boolean getRun() {
	return run;
    }

    public Rectangle2D getClickedStartScreenButton() {
	return clickedStartScreenButton;
    }

    public void setClickedStartScreenButton(Rectangle2D rect) {
	clickedStartScreenButton = rect;
    }

    public boolean getGameOver() {
	return gameOver;
    }

    public boolean getGamePaused() {
	return gamePaused;
    }

    public ArrayList<Rectangle2D> getStartScreenButtons() {
	return startScreenButtons;
    }

    public void setGamePaused(boolean paused) {
	gamePaused = paused;
    }

    public void setGameOver(boolean over) {
	gameOver = over;
    }

    public void setRun(boolean run) {
	this.run = run;
    }

    public void start() {
	Thread thread = new Thread(this);
	if (gamePaused == false) {
	    player = new Player(this.getWidth() / 2, this.getHeight() - 200, 64, 64, 5, 100, 1, 3);
	    objects.add(player);
	    score = 0;
	    shootTimer = player.getWeaponCooldown();
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
	    if (spawnTimer >= 3000 && score <= 2500) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 2000 && score > 2500 && score <= 5000) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 1000 && score > 5000 && score <= 7500) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 5000 && score >= 10000) {
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
		spawnTimer += 15;
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
            Random rand = new Random();
            int dX = boss.getXPos() - player.getXPos();
            int dY = boss.getYPos() - player.getYPos();
            EnemyBullet bullet = new EnemyBullet(boss.getXPos() + 24, boss.getYPos(), 10, 10, dX / 67, dY / 67, 10);
            objects.add(bullet);
            playSound(0);
            if (boss.getHealth() <= 500 && boss.getHealth() > 100) {
                boss.setFiringRate(750);
                int randomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
                int nextRandomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
                // position of boss fire is off due to size of boss; change position
                EnemyBullet bullet2 = new EnemyBullet(boss.getXPos() + 24, boss.getYPos(), 10, 10, randomDX / 67, dY / 67, 15);
                EnemyBullet bullet3 = new EnemyBullet(boss.getXPos() + 24, boss.getYPos(), 10, 10, nextRandomDX / 67, dY / 67, 15);
                objects.add(bullet2);
                objects.add(bullet3);
            } else if (boss.getHealth() <= 100) {
                boss.setFiringRate(500);
                int randomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
                int nextRandomDX = boss.getXPos() - rand.nextInt(panel.getWidth() - 5) + 5;
                // position of boss fire is off due to size of boss; change position
                EnemyBullet bullet2 = new EnemyBullet(boss.getXPos() + 24, boss.getYPos(), 10, 10, randomDX / 67, dY / 67, 20);
                EnemyBullet bullet3 = new EnemyBullet(boss.getXPos() + 24, boss.getYPos(), 10, 10, nextRandomDX / 67, dY / 67, 20);
                objects.add(bullet2);
                objects.add(bullet3);
            }
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
                for(int i = 0; i < enemies.size(); i++ ) {
                    chosenEnemies.add(i);
                }
            }
            for (int i = 0; i < chosenEnemies.size(); i++ ) {
                Enemy selectedEnemy = enemies.get(chosenEnemies.get(i));
                int dX = selectedEnemy.getXPos() - player.getXPos();
                int dY = selectedEnemy.getYPos() - player.getYPos();
                int bulletXSpeed = dX / 67;
                int bulletYSpeed = dY / 67;
                if (selectedEnemy.getYSpeed() < bulletYSpeed) {
                    double bulletSpeedMultiplier = (double)selectedEnemy.getYSpeed() / bulletYSpeed;
                    bulletXSpeed *= Math.pow(bulletSpeedMultiplier, 2);
                    bulletYSpeed *= Math.pow(bulletSpeedMultiplier, 2);
                }
                if (dY <= -100 ) {
                    EnemyBullet bullet = new EnemyBullet(
                            selectedEnemy.getXPos() + 24,
                            selectedEnemy.getYPos(),
                            10,
                            10,
                            bulletXSpeed,
                            bulletYSpeed,
                            10);
                    objects.add(bullet);
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
		g2.drawString("YOUR SCORE: " + Integer.toString(score), 60, 430);
		g2.drawString("HIGH SCORE: " + highScore, 60, 470);
		if (highScore > previousHighScore) {
		    g2.drawString("NEW HIGH SCORE!", 80, 180);
		}

		// Paint play again info
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

	    // Paint score and high score
	    Font scoreHeading = null;
	    try {
		scoreHeading = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/CabinRegular.ttf")).deriveFont(20f);
	    } catch (Exception e) {
		System.out.println("ERROR: Font file CabinRegular.ttf cannot be opened.");
	    }
	    g2.setFont(scoreHeading);
	    g2.setColor(Color.PINK);
	    g2.drawString("SCORE: " + Integer.toString(score), 10, 25);
	    g2.drawString("HIGH SCORE: " + highScore, 180, 25);

	    // Paint number of bombs and weapon level
	    g2.drawString("BOMBS: " + player.getNumberOfBombs(), 10, 790);
	    g2.drawString("WEAPON: LEVEL " + player.getWeaponLevel() + "/5", 300, 790);

	    // Draw pause button
	    g2.setColor(Color.RED);
	    g2.fillRect(420, 0, 80, 30);
	    g2.setColor(Color.WHITE);
	    g2.drawString("PAUSE", 427, 22);

	    // Paint GameObjects
	    for (GameObject i : objects) {
		i.paintComponent(g2);
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

	// Paint start screen content
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

	// Paint power ups button
	g2.setColor(Color.RED);
	Rectangle2D powerUpsButton = new Rectangle2D.Double(80, 600, 145, 40);
	startScreenButtons.add(powerUpsButton);
	g2.draw(powerUpsButton);
	g2.drawString("POWER UPS", 96, 628);

	// Paint enemies button
	g2.setColor(Color.RED);
	Rectangle2D enemiesButton = new Rectangle2D.Double(280, 600, 116, 40);
	startScreenButtons.add(enemiesButton);
	g2.draw(enemiesButton);
	g2.drawString("ENEMIES", 296, 628);

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
		drawMusic(g2);
		audioControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = audioControl.getMaximum() - audioControl.getMinimum();
		volume = (audioControl.getValue() - audioControl.getMinimum()) / range;
		if (clickedStartScreenButton.equals(new Rectangle2D.Double(100, 278, 100, 30))) {
		    volume -= 0.1;
		    volume = Math.max(volume, 0);
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(215, 278, 100, 30))) {
		    volume = (float) 0.9;
		} else if (clickedStartScreenButton.equals(new Rectangle2D.Double(330, 278, 100, 30))) {
		    volume += 0.1;
		    volume = Math.min(volume, 1);
		}
		float gain = (range * volume) + audioControl.getMinimum();
		audioControl.setValue(gain);
		clip.start();

		// Save volume setting to file
		try {
		    FileWriter outputStream = null;
		    try {
			outputStream = new FileWriter("assets/data/volume.txt");
			outputStream.write(volume + "\r\n");
		    } catch (FileNotFoundException e) {
			System.out.println("ERROR: Cannot write to volume.txt");
		    } finally {
			if (outputStream != null) {
			    outputStream.close();
			}
		    }
		} catch (IOException e) {
		    System.out.println("ERROR: IOException when saving volume setting to file");
		}
		System.out.println("VOLUME AFTER: " + volume);
	    }
	}
    }

    public void drawMusic(Graphics2D g2) {
	// Draw low volume button
	g2.setColor(Color.WHITE);
	Rectangle2D lowButton = new Rectangle2D.Double(100, 278, 100, 30);
	g2.draw(lowButton);
	startScreenButtons.add(lowButton);
	//g2.setColor(Color.BLACK);
	g2.drawString("LOW", 130, 300);

	// Draw normal volume button
	g2.setColor(Color.WHITE);
	Rectangle2D medButton = new Rectangle2D.Double(215, 278, 100, 30);
	g2.draw(medButton);
	startScreenButtons.add(medButton);
	//g2.setColor(Color.BLACK);
	g2.drawString("NORMAL", 225, 300);

	// Draw high volume button
	g2.setColor(Color.WHITE);
	Rectangle2D highButton = new Rectangle2D.Double(330, 278, 100, 30);
	g2.draw(highButton);
	startScreenButtons.add(highButton);
	//g2.setColor(Color.BLACK);
	g2.drawString("HIGH", 355, 300);

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
	spawnTimer = 0;
        objects.addAll(EnemyFactory.makeEnemies(score));
    }

    public void shootBullet() {
	Bullet bullet = new Bullet(player.getXPos() + 24, player.getYPos(), 10, 10);
	objects.add(bullet);
	playSound(0);
    }

    public void launchBomb() {
	if (player.getNumberOfBombs() > 0) {
	    Bomb bomb = new Bomb(player.getXPos() + 32, player.getYPos(), 10, 10);
	    objects.add(bomb);
	    player.useBomb();
	}
    }

    public void dropPowerUp(GameObject enemy) {
	Random rng = new Random();

	// Chance for a power up to drop
	if (rng.nextInt(50) == 0) {
	    // Choose which power up to drop
	    if (rng.nextInt(2) == 0) {
		// Drop bomb power up
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
	    if (i instanceof Enemy) {
		if (bullet.getRectangle().intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
		    deleteObject(bullet);
		    Enemy q = (Enemy) i;
		    q.loseHealth(player.getWeaponDamage());
		    if (q.getHealth() <= 0) {
			deleteObject(i);
			score += i.getPoints();
		    }

		    if (i instanceof Boss == false) {
			dropPowerUp(i);
		    }
		}
	    }
	}
    }

    public void checkEnemyBulletHit(EnemyBullet bullet) throws IOException {
	if (bullet.getRectangle().intersects(player.getXPos(), player.getYPos(), player.getWidth(), player.getHeight())) {
	    deleteObject(bullet);
	    player.loseHealth(10);
	    if (player.getHealth() <= 0) {
		gameOver();
	    }
	}
    }

    public void checkEnemyCollision(Enemy enemy) throws IOException {
	if (enemy.getRectangle().intersects(player.getXPos(), player.getYPos(), player.getWidth(), player.getHeight())) {
	    deleteObject(enemy);
	    player.loseHealth(10);
	    if (player.getHealth() <= 0) {
		gameOver();
	    }
	}
    }

    public void checkBombCollision(Bomb bomb) {
	for (GameObject i : objects) {
	    if (i instanceof Enemy) {
		if (bomb.getRectangle().intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
		    bomb.setArmed(true);
		}
	    }
	}
    }

    public void checkBombExplosionHit(Bomb bomb) {
	for (GameObject i : objects) {
	    if (i instanceof Enemy) {
		if (bomb.getExplosionEllipse().intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
		    deleteObject(i);
		    score += i.getPoints();
		    dropPowerUp(i);
		}
	    }
	}
    }

    public void checkPowerUpPickUp(PowerUp powerUp) {
	if (powerUp.getRectangle().intersects(player.getXPos(), player.getYPos(), player.getWidth(), player.getHeight())) {
	    if (powerUp.getType() == 0) {
		player.pickUpBomb();
	    } else if (powerUp.getType() == 1) {
		player.upgradeWeapon();
		shootTimer = player.getWeaponCooldown();
	    }
	    deleteObject(powerUp);
	    playSound(3);
	}
    }

    public void gameOver() throws IOException {
	run = false;
	gameOver = true;
	objects.clear();
	repaint();

	if (score > highScore) {
	    previousHighScore = highScore;
	    highScore = score;
	}

	FileWriter outputStream = null;
	try {
	    outputStream = new FileWriter("assets/data/volume.txt");
	    outputStream.write("" + volume);
	} catch (FileNotFoundException exception) {
	    System.out.println("Error opening file");
	} finally {
	    if (outputStream != null) {
		outputStream.close();
	    }
	}

	// Save high score to file
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
