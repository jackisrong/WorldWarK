package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Boss extends Enemy {

    public Boss(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int typeOfEnemy, int points) {
	super(xPos, yPos, width, height, xSpeed, ySpeed, health, typeOfEnemy, points);
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }
    
    @Override
    public void update(WorldWarK panel) {
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.fill(rectangle);
	g2.draw(rectangle);

	// Draw appropriate enemy image on the enemy
	BufferedImage enemyImage;
	String fileName = null;
	switch (typeOfEnemy) {
	    case 0:
		fileName = "helicopterLow";
		break;
	    case 1:
		fileName = "helicopterMed";
		break;
	    case 2:
		fileName = "helicopterHard";
		break;
	    case 3:
		fileName = "fighterPlaneLow";
		break;
	    case 4:
		fileName = "fighterPlaneMed";
		break;
	    case 5:
		fileName = "fighterPlaneHard";
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
