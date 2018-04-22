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
    private int numberOfBombs;
    private Rectangle2D rectangle;

    public Player(int xPos, int yPos, int width, int height, int xSpeed, int health, int numberOfBombs) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.health = health;
	this.numberOfBombs = numberOfBombs;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public void moveLeft() {
	if (xPos > 0) {
	    xPos -= 10;
	}	
    }

    public void setXPosition(int xPos) {
	this.xPos = xPos;
    }

    public int useBomb() {
	return numberOfBombs--;
    }

    public int pickUpBomb() {
	return numberOfBombs++;
    }

    public void update(WorldWarK panel) {
	// Left wall impact
    }

    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// This accidently adds the background image LOL
	BufferedImage image;
	try {
	    image = ImageIO.read(new File("background.jpg"));
	} catch (IOException e) {
	    image = null;
	}
	g2.drawImage(image, 0, 0, null);

	// Draw player rectangle/hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.fill(rectangle);
	g2.draw(rectangle);

	// Puts the player image on the player
	BufferedImage playerImage;
	try {
	    playerImage = ImageIO.read(new File("player.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: player.png cannot be read.");
	    playerImage = null;
	}
	g2.setClip(rectangle);
	g2.drawImage(playerImage, xPos, yPos, null);
    }
}
