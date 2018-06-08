package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Enemy extends GameObject {

    protected int xSpeed;
    protected int ySpeed;
    protected int health;
    protected int typeOfEnemy;
    protected int points;
    protected int shoot;
    private boolean reverse;
    private int reverseTimer;
    protected int initialHealth;
    protected int firingRate;
    protected int initialSpeed = 0;
    private Random rand = new Random();
    private int imageTimer = 0;
    private boolean miniExplosionState = false;
    private int explosionTimer = 0;

    public Enemy(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int typeOfEnemy, int points, int shoot) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;
	this.health = health;
	this.typeOfEnemy = typeOfEnemy;
	this.points = points;
	this.shoot = shoot;
	reverse = false;
	reverseTimer = 0;
	initialHealth = health;
	firingRate = 1000;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public int getXSpeed() {
	return xSpeed;
    }

    public int getYSpeed() {
	return ySpeed;
    }

    public void setXSpeed(int speed) {
	xSpeed = speed;
    }

    public void setYSpeed(int speed) {
	ySpeed = speed;
    }

    public void loseHealth(int health) {
	this.health -= health;
    }

    public int getHealth() {
	return health;
    }

    public int getInitialHealth() {
	return initialHealth;
    }

    public int getType() {
	return typeOfEnemy;
    }

    public int getPoints() {
	return points;
    }

    public int getFiringRate() {
	return firingRate;
    }

    public void setReverse(boolean reverse) {
	this.reverse = reverse;
    }

    public boolean readyToTurnAtY(int reverseYPosition) {
	return (Math.abs(yPos - reverseYPosition) <= 3);
    }

    public boolean readyToTurnAtX(int reverseXPosition) {// hi
	return (Math.abs(xPos - reverseXPosition) <= 3);
    }

    public int getShoot() {
	return shoot;
    }

    public void setYPos(int yPos) {
	this.yPos = yPos;
    }

    public void setXPos(int xPos) {
	this.xPos = xPos;
    }

    public void miniExplosion(boolean state) {
	miniExplosionState = state;
    }

    @Override
    public void update(WorldWarK panel) {
	// Check enemy collision
	if (!miniExplosionState && rectangle.intersects(panel.player.getXPos(), panel.player.getYPos(), panel.player.getWidth(), panel.player.getHeight())) {
	    health = 0;
	    miniExplosionState = true;
	    panel.player.miniExplosion(true);
	    panel.player.loseHealth(10);
	    panel.playSound(4);
	}

	// Update explosion timer
	if (miniExplosionState) {
	    if (explosionTimer < 9) {
		explosionTimer++;
	    } else {
		if (health <= 0) {
		    panel.deleteObject(this);
		}
	    }
	}

	// Increase image animation timer
	if (imageTimer == 5) {
	    imageTimer = 0;
	} else {
	    imageTimer++;
	}

	// X reverse
	if (reverse && readyToTurnAtX(panel.getWidth() / 2) && typeOfEnemy < 6) {
	    if (reverseTimer == 0) {
		initialSpeed = xSpeed;
	    }
	    xSpeed = 0;
	    reverseTimer += 15;
	    if (reverseTimer >= 1000) {
		reverse = false;
		xSpeed = -initialSpeed;
		reverseTimer = 0;
	    }
	}

	// Horizontal reverse
	if (reverse && readyToTurnAtY(panel.getHeight() / 2) && typeOfEnemy < 6) {
	    if (reverseTimer == 0) {
		initialSpeed = ySpeed;
	    }
	    ySpeed = 0;
	    reverseTimer += 15;
	    if (reverseTimer >= 1000) {
		reverse = false;
		ySpeed = -initialSpeed;
		reverseTimer = 0;
	    }
	}

	// Horizontal zig zag
	if (reverse && ySpeed != 0 && typeOfEnemy == 6) {
	    reverseTimer += 20;
	    if (reverseTimer == 500) {
		ySpeed = -xSpeed;
	    }
	    if (reverseTimer == 1000) {
		ySpeed = -ySpeed;
		reverseTimer = 0;
	    }
	}

	// Vertical zig zag
	if (reverse && xSpeed != 0 && typeOfEnemy == 8) {
	    reverseTimer += 20;
	    if (reverseTimer == 500) {
		xSpeed = -xSpeed;
	    }
	    if (reverseTimer == 1000) {
		xSpeed = -xSpeed;
		reverseTimer = 0;
	    }
	}

	// Vertical reverse
	if (reverse && readyToTurnAtX(panel.getWidth() / 2) && typeOfEnemy == 10) {
	    if (reverseTimer == 0) {
		initialSpeed = xSpeed;
	    }
	    xSpeed = 0;
	    reverseTimer += 15;
	    if (reverseTimer >= 1000) {
		reverse = false;
		xSpeed = initialSpeed;
		ySpeed = rand.nextInt(15) + 5;
		reverseTimer = 0;
	    }
	}

	// Exits left/right wall threshold
	if (xPos <= -201 || xPos >= 701) {
	    panel.deleteObject(this);
	} else {
	    xPos += xSpeed;
	}

	// Deletes the object once it passes bottom of the frame
	if (yPos > panel.getHeight() - height - 1) {
	    panel.deleteObject(this);
	} else {
	    yPos += ySpeed;
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.draw(rectangle);

	// Draw health bar
	g2.setColor(Color.RED);
	g2.fillRect(xPos - 8, yPos + height, 70, 3);
	g2.setColor(Color.GREEN);
	g2.fillRect(xPos - 8, yPos + height, (int) ((double) health / (double) initialHealth * 70.0), 3);

	// Draw appropriate enemy image on the enemy
	BufferedImage enemyImage;
	String fileName = null;
	switch (typeOfEnemy) {
	    case 0:
		if (imageTimer >= 0 && imageTimer < 3) {
		    fileName = "helicopterLow1";
		} else {
		    fileName = "helicopterLow2";
		}
		break;
	    case 1:
	    case 6: {
		if (imageTimer >= 0 && imageTimer < 3) {
		    fileName = "helicopterMed1";
		} else {
		    fileName = "helicopterMed2";
		}
		break;
	    }
	    case 2:
	    case 7:
	    case 10: {
		if (imageTimer >= 0 && imageTimer < 3) {
		    fileName = "helicopterHard1";
		} else {
		    fileName = "helicopterHard2";
		}
		break;
	    }
	    case 3:
		fileName = "fighterPlaneLow";
		break;
	    case 4:
	    case 8: {
		fileName = "fighterPlaneMed";
		break;
	    }
	    case 5:
	    case 9: {
		fileName = "fighterPlaneHard";
		break;
	    }
	    default:
		break;
	}

	try {
	    enemyImage = ImageIO.read(new File("assets/img/" + fileName + ".png"));
	    g2.drawImage(enemyImage, xPos, yPos, null);
	} catch (IOException e) {
	    System.out.println("ERROR: " + fileName + ".png cannot be read.");
	}

	if (miniExplosionState) {
	    // Draw image
	    BufferedImage explosionImage = null;
	    try {
		if ((explosionTimer >= 0 && explosionTimer < 3) || (explosionTimer >= 6 && explosionTimer < 9)) {
		    explosionImage = ImageIO.read(new File("assets/img/miniExplosion1.png"));
		} else if ((explosionTimer >= 3 && explosionTimer < 6)) {
		    explosionImage = ImageIO.read(new File("assets/img/miniExplosion2.png"));
		}
		g2.drawImage(explosionImage, xPos, yPos, null);
	    } catch (IOException e) {
		System.out.println("ERROR: miniExplosion.png cannot be read.");
	    }
	}
    }
}
