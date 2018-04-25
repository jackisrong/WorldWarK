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
    private Rectangle2D playerBox;

    public Player(int xPos, int yPos, int width, int height, int xSpeed, int health, int weapon, int numberOfBombs) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.health = health;
	this.weapon = weapon;
	this.numberOfBombs = numberOfBombs;
	playerBox = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public int getXPos() {
	return xPos;
    }

    public int getYPos() {
	return yPos;
    }

    public int getWeaponLevel() {
	return weapon;
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

    public void weaponUpgraded() {
	if (weapon < 5) {
	    weapon++;
	}
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

    public void update(WorldWarK panel) {
    }

    public void paintComponent(Graphics2D g2) {
	playerBox.setFrame(xPos, yPos, width, height);

	// Draw player playerBox/hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.fill(playerBox);
	g2.draw(playerBox);

	// Puts the player image on the player
	BufferedImage playerImage;
	try {
	    playerImage = ImageIO.read(new File("assets/img/player.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: player.png cannot be read.");
	    playerImage = null;
	}
	g2.setClip(playerBox);
	g2.drawImage(playerImage, xPos, yPos, null);
    }
}
