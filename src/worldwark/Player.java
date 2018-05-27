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
		return 50;
	    case 2:
		return 75;
	    case 3:
		return 100;
	    case 4:
		return 125;
	    case 5:
		return 150;
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

    @Override
    public void update(WorldWarK panel) {
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw player hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.fill(rectangle);
	g2.draw(rectangle);

	// Draw player health bar
	g2.setColor(Color.RED);
	g2.fillRect(xPos - 16, yPos + height, 100, 3);
	g2.setColor(Color.GREEN);
	g2.fillRect(xPos - 16, yPos + height, (int) ((double) health / (double) initialHealth * 100.0), 3);

	// Draw image
	BufferedImage playerImage;
	try {
	    playerImage = ImageIO.read(new File("assets/img/player.png"));
	    g2.setClip(rectangle);
	    g2.drawImage(playerImage, xPos, yPos, null);
	} catch (IOException e) {
	    System.out.println("ERROR: player.png cannot be read.");
	}
    }
}
