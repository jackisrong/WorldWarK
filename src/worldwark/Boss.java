package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Boss extends Enemy {

    public Boss(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int typeOfEnemy, int points, int shoot) {
	super(xPos, yPos, width, height, xSpeed, ySpeed, health, typeOfEnemy, points, shoot);
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    @Override
    public void update(WorldWarK panel) {
	if (yPos < 100) {
	    yPos += ySpeed;
	}

	if (xPos > 0 && xPos < xPos + width) {
	    Random rng = new Random();
	    int number = rng.nextInt(20);
	    if (number == 0) {
		xPos -= xSpeed;
	    } else if (number == 1) {
		xPos += xSpeed;
	    }
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);
	g2.setClip(null);

	// Draw hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.fill(rectangle);
	g2.draw(rectangle);
	
	// Draw player health bar
	g2.setColor(Color.RED);
	g2.fillRect(xPos - 16, yPos + height, 200, 3);
	g2.setColor(Color.GREEN);
	g2.fillRect(xPos - 16, yPos + height, (int) ((double) health / (double) initialHealth * 200.0), 3);

	// Draw appropriate enemy image on the enemy
	BufferedImage enemyImage;
	String fileName = null;
	switch (typeOfEnemy) {
	    case 0:
		fileName = "boss1";
		break;
	    case 1:
		fileName = "boss2";
		break;
	    case 2:
		fileName = "boss3";
		break;
	    case 3:
		fileName = "boss4";
		break;
	    case 4:
		fileName = "boss5";
		break;
	    case 5:
		fileName = "boss6";
		break;
	    default:
		break;
	}

	try {
	    enemyImage = ImageIO.read(new File("assets/img/" + fileName + ".png"));
	    g2.setClip(rectangle);
	    g2.drawImage(enemyImage, xPos, yPos, null);
	} catch (IOException e) {
	    System.out.println("ERROR: " + fileName + ".png cannot be read.");
	}
    }
}
