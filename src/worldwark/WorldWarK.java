package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
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
    public int snapShot = 0;
    public int difficulty = 1;
    public int b = 0;
    private int spawnTimer;
    private int fireTimer = 0;
    public Clip clip;
    public float volume;
    public float fxVolume;
    public FloatControl audioControl;
    private int previousHighScore = 0;
    private int highScore = 0;
    private int backgroundYScroll = 0;
    private BufferedImage backgroundImage;
    private EnemyFactory e;
    private KeyboardControls keyboardControls = new KeyboardControls(this);
    private StartScreen startScreen = new StartScreen(this);
    private int bombCooldown;
    public Font cabinBold;
    public Font cabinRegular;
    public Font wartorn;
    public BufferedImage bulletImage;

    public WorldWarK() {
	JFrame frame = new JFrame("World War K");
	setBackground(Color.BLACK);
	setPreferredSize(new Dimension(500, 800));
	addKeyListener(keyboardControls);
	addMouseListener(new MouseControls(this));
	addMouseMotionListener(new MouseControls(this));
	setFocusable(true);
	frame.addKeyListener(keyboardControls);
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

	// Play audio file
	try {
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("assets/music/DefenseLine.wav"));
	    clip = AudioSystem.getClip();
	    clip.open(audioIn);
	    clip.loop(10000);
	    audioControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    float range = audioControl.getMaximum() - audioControl.getMinimum();
	    float gain = (range * volume) + audioControl.getMinimum();
	    audioControl.setValue(gain);
	    clip.start();
	} catch (Exception e) {
	    System.out.println("ERROR: DefenseLine.wav cannot be played.");
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
	    inputStream = null;
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
	if (!gamePaused) {
	    player = new Player(this.getWidth() / 2, this.getHeight() - 200, 64, 64, 5, 300, 1, 3);
	    objects.add(player);
	    score = 0;
	    spawnTimer = 0;
	    snapShot = 0;
	    bombCooldown = 5000;
	    b = 0;
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
	    // Keys that require holding without delay
	    if (keyboardControls.isKey(KeyEvent.VK_LEFT)) {
		player.keyboardMoveLeft();
	    }
	    if (keyboardControls.isKey(KeyEvent.VK_RIGHT)) {
		player.keyboardMoveRight();
	    }
	    if (keyboardControls.isKey(KeyEvent.VK_SPACE)) {
		if (gameOver == false && gamePaused == false) {
		    if (shootTimer >= player.getWeaponCooldown()) {
			shootBullet();
			shootTimer = 0;
		    }
		}
	    }

	    // Update high score
	    if (score > highScore) {
		highScore = score;
	    }

	    // Controls the spawn rates of the stages depending on the score
	    if (spawnTimer >= 7000 && score <= 2500) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 6000 && score > 2500 && score <= 7500) {
		spawnEnemy(this);
	    } else if (spawnTimer >= 5500 && score > 7500 && score <= 10000) {
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
		spawnTimer += 15;
		fireTimer += 15;
		bombCooldown += 15;
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
	    if (i instanceof Enemy && !i.isOutsideScreen() && !(i instanceof Boss)) {
		enemies.add((Enemy) i);
	    } else if (i instanceof Boss) {
		boss = (Boss) i;
	    }
	}

	// Add boss lasers
	if (boss != null && fireTimer % boss.getFiringRate() == 0) {
	    if (boss.getHealth() > boss.getInitialHealth() / 2) {
		objects.add(new BossLaser(boss.getXPos() + 30, boss.getYPos() + boss.getHeight() + 3, 5, 0, 10, boss));
	    } else if (boss.getHealth() <= boss.getInitialHealth() / 2 && boss.getHealth() > boss.getInitialHealth() / 10) {
		objects.add(new BossLaser(boss.getXPos() + 180, boss.getYPos() + boss.getHeight() + 3, 5, 0, 15, boss));
	    } else if (boss.getHealth() <= boss.getInitialHealth() / 10) {
		objects.add(new BossLaser(boss.getXPos() + 30, boss.getYPos() + boss.getHeight() + 3, 10, 0, 20, boss));
	    }
	    playSound(8);
	}

	// Stores enemies added to game to ArrayList 
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

	    // Adds enemy bullets to shoot in direction of player
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
		    playSound(5);
		}
	    }
	    fireTimer = 0;
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;

	if (!run && !gamePaused) {
	    if (gameOver) {
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
		g2.setFont(cabinBold.deriveFont(40f));
		g2.setColor(Color.WHITE);
		g2.drawString("YOUR SCORE: " + Integer.toString(score), 55, 430);
		g2.drawString("HIGH SCORE: " + highScore, 55, 480);
		if (highScore > previousHighScore) {
		    g2.setColor(Color.CYAN);
		    g2.drawString("NEW HIGH SCORE!", 75, 350);
		}

		// Paint play again info
		g2.setFont(cabinRegular.deriveFont(20f));
		g2.setColor(Color.WHITE);
		g2.drawString("Press R to play again", 220, 650);
		g2.drawString("Press T to return to title screen", 220, 680);
	    } else {
		startScreen.drawStartScreen(g2);
	    }
	}

	if (run || (!run && gamePaused)) {
	    // Paint game background image
	    g2.drawImage(backgroundImage, 0, -backgroundImage.getHeight() + 800 - backgroundYScroll, null);
	    if (backgroundYScroll == -backgroundImage.getHeight() + 800) {
		backgroundYScroll = 0;
	    } else {
		backgroundYScroll -= 2;
	    }

	    // Paint score and high score
	    g2.setFont(cabinRegular.deriveFont(20f));
	    g2.setColor(Color.BLACK);
	    g2.drawString("SCORE: " + Integer.toString(score), 10, 25);
	    g2.drawString("HIGH SCORE: " + highScore, 180, 25);

	    // Paint number of bombs and weapon level
	    String bombCountdown = "";
	    if (bombCooldown < 1000) {
		bombCountdown = "5";
	    } else if (bombCooldown >= 1000 && bombCooldown < 2000) {
		bombCountdown = "4";
	    } else if (bombCooldown >= 2000 && bombCooldown < 3000) {
		bombCountdown = "3";
	    } else if (bombCooldown >= 3000 && bombCooldown < 4000) {
		bombCountdown = "2";
	    } else if (bombCooldown >= 4000 && bombCooldown < 5000) {
		bombCountdown = "1";
	    } else if (bombCooldown >= 5000) {
		bombCountdown = "READY!";
	    }
	    g2.drawString("BOMB COOLDOWN: " + bombCountdown, 10, 760);
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

	if (gamePaused) {
	    drawPausedScreen(g2);
	}

	if (run && !gamePaused) {
	    // Paints game countdown 
	    g2.setFont(cabinBold.deriveFont(200f));
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
	g2.fillRect(50, 80, 400, 700);

	// Draw close button
	g2.setColor(Color.RED);
	Rectangle2D closeButton = new Rectangle2D.Double(370, 80, 80, 30);
	g2.fill(closeButton);
	startScreenButtons.add(closeButton);
	g2.setColor(Color.WHITE);
	g2.drawString("CLOSE", 377, 102);

	// Print heading
	g2.setColor(Color.WHITE);
	g2.setFont(wartorn.deriveFont(50f));
	g2.drawString("PAUSED", 120, 170);

	// Print subheading
	g2.setFont(cabinRegular.deriveFont(20f));
	g2.drawString("Your game is paused.", 160, 210);
    }

    public void playSound(int sound) {
	// Choose sound effect to play based on parameter
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
	objects.addAll(e.makeEnemies(score, panel));
    }

    public void shootBullet() {
	// Chooses bullet for player based on level
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
	// Player bomb firing
	if (player.getNumberOfBombs() > 0 && bombCooldown >= 5000) {
	    Bomb bomb = new Bomb(player.getXPos() + 32, player.getYPos(), 8, 16);
	    objects.add(bomb);
	    player.useBomb();
	    bombCooldown = 0;
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
