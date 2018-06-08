package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends GameObject {

    private int xSpeed;
    private int health;
    private int weapon;
    private int numberOfBombs;
    private int initialHealth;
    private int imageTimer;
    private boolean miniExplosionState = false;
    private int explosionTimer = 0;

    public Player(int xPos, int yPos, int width, int height, int xSpeed, int health, int weapon, int numberOfBombs) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.health = health;
	this.weapon = weapon;
	this.numberOfBombs = numberOfBombs;
	initialHealth = health;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public void keyboardMoveLeft() {
	if (xPos > -width / 2) {
	    xPos -= 10;
	    if (xPos < -width / 2) {
		xPos = -width / 2;
	    }
	}
    }

    public void keyboardMoveRight() {
	if (xPos < 500 - width / 2) {
	    xPos += 10;
	    if (xPos > 500 - width / 2) {
		xPos = 500 - width / 2;
	    }
	}
    }

    public void setXPosition(int xPos) {
	this.xPos = xPos - width / 2;
    }

    public int getWeaponLevel() {
	return weapon;
    }

    public int getInitialHealth() {
	return initialHealth;
    }

    public void upgradeWeapon() {
	if (weapon < 5) {
	    weapon++;
	}
    }

    public int getWeaponCooldown() {
	switch (weapon) {
	    case 1:
		return 300;
	    case 2:
		return 200;
	    case 3:
		return 500;
	    case 4:
		return 400;
	    case 5:
		return 100;
	    default:
		return 0;
	}
    }

    public int getWeaponDamage() {
	switch (weapon) {
	    case 1:
		return 70;
	    case 2:
		return 90;
	    case 3:
		return 70;
	    case 4:
		return 90;
	    case 5:
		return 100;
	    default:
		return 0;
	}
    }

    public int getNumberOfBombs() {
	return numberOfBombs;
    }

    public int useBomb() {
	if (numberOfBombs > 0) {
	    return numberOfBombs--;
	}
	return numberOfBombs;
    }

    public int pickUpBomb() {
	return numberOfBombs++;
    }

    public int getHealth() {
	return health;
    }

    public void setHealth(int health) {
	this.health = health;
    }

    public void loseHealth(int healthLost) {
	health -= healthLost;
    }

    public void miniExplosion(boolean state) {
	miniExplosionState = state;
    }

    @Override
    public void update(WorldWarK panel) {
	// Increase image animation timer
	if (imageTimer == 8) {
	    imageTimer = 0;
	} else {
	    imageTimer++;
	}

	// Update explosion timer
	if (miniExplosionState) {
	    if (explosionTimer < 9) {
		explosionTimer++;
	    } else {
		if (health <= 0) {
		    try {
			panel.gameOver();
		    } catch (IOException e) {
			System.out.println("ERROR: IOException when updating EnemyBullet");
		    }
		} else {
		    miniExplosionState = false;
		    explosionTimer = 0;
		}
	    }
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw player hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.draw(rectangle);

	// Draw player health bar
	g2.setColor(Color.RED);
	g2.fillRect(xPos - 16, yPos + height, 100, 3);
	g2.setColor(Color.GREEN);
	g2.fillRect(xPos - 16, yPos + height, (int) ((double) health / (double) initialHealth * 100.0), 3);

	// Draw player image
	String fileName = "";
	try {
	    if (imageTimer >= 0 && imageTimer < 3) {
		fileName = "player1";
	    } else if (imageTimer >= 3 && imageTimer < 6) {
		fileName = "player2";
	    } else {
		fileName = "player3";
	    }
	    g2.drawImage(ImageIO.read(new File("assets/img/" + fileName + ".png")), xPos, yPos, null);
	} catch (IOException e) {
	    System.out.println("ERROR: " + fileName + ".png cannot be read.");
	}

	if (miniExplosionState) {
	    // Draw image of explosion for player hit
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
