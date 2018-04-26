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

    private int xSpeed;
    private int ySpeed;
    private int health;
    private int numberOfBombs;
    private Rectangle2D rectangle;
    private int typeOfEnemy;
    private int rnd;
    private int score;

    public Enemy(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int typeOfEnemy) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;
	this.health = health;
	this.typeOfEnemy = typeOfEnemy;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public Rectangle2D getRectangle() {
	return rectangle;
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

    public void revereDirection(int definedPosition, int definedSpeed) {
	if (this.getXSpeed() == 0) {
	    if (this.getYPos() == definedPosition) {
		this.setYSpeed(definedSpeed);
	    }
	} else if (this.getYSpeed() == 0) {
	    if (this.getXPos() == definedPosition) {
		this.setYSpeed(definedSpeed);
	    }

	}
    }
    
    public void update(WorldWarK panel) {
	panel.checkEnemyCollision(this);

	// Left wall impact
	if (xPos == -201) {
	    panel.deleteObject(this);
	} // Right wall impact
	else if (xPos == 701) {
	    panel.deleteObject(this);
	} else {
	    xPos += xSpeed;
	}

	// Deletes the object once it has passed the bottom of the frame
	if (yPos > panel.getHeight() - height - 1) {
	    panel.deleteObject(this);
	} else {
	    yPos += ySpeed;
	}
    }

    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw enemy rectangle/hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.fill(rectangle);
	g2.draw(rectangle);

	// Puts appropriate enemy image on the enemy
	BufferedImage enemyImage;
	String fileName;
	if (typeOfEnemy == 0) {
	    fileName = "helicopter";
	} else {
	    System.out.println("ERROR: Specified enemy type doesn't have an image.");
	    fileName = null;
	}

	if (fileName != null) {
	    try {
		enemyImage = ImageIO.read(new File("assets/img/" + fileName + ".png"));
	    } catch (IOException e) {
		System.out.println("ERROR: " + fileName + ".png cannot be read.");
		enemyImage = null;
	    }
	    g2.setClip(rectangle);
	    g2.drawImage(enemyImage, xPos, yPos, null);
	}
    }
}
