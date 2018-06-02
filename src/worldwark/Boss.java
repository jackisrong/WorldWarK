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
    private int updateCounter;
    private boolean horizontalMove;
    private int moveSpeed;
    private boolean firstStop;

    public Boss(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int typeOfEnemy, int points, int shoot) {
	super(xPos, yPos, width, height, xSpeed, ySpeed, health, typeOfEnemy, points, shoot);
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
        updateCounter = 0;
        horizontalMove = false;
        moveSpeed = 0;
        firstStop = false;
    }
    
    @Override
    public void update(WorldWarK panel) {
        if (updateCounter == 0) {
            Random rand = new Random();
            int direction = rand.nextInt(3);
            switch(direction) {
                // Case 0 is left
                // Case 1 is right
                // Case 2 is down
                case 0: 
                    horizontalMove = true;
                    moveSpeed = 2;
                    break;
                case 1:
                    horizontalMove = true;
                    moveSpeed = -2;
                    break;
                case 2:
                    horizontalMove = false;
                    moveSpeed = 2;
                    break;
                default: 
                    moveSpeed = 0;
                    break;
            }
        }
        updateCounter++;
        if (updateCounter == 200) {
            xSpeed = horizontalMove? moveSpeed : xSpeed;
            ySpeed = !horizontalMove? moveSpeed : ySpeed;
        } else if (updateCounter == 267) {
            xSpeed = horizontalMove? 0 : xSpeed;
            ySpeed = !horizontalMove? 0 : ySpeed;
        } else if (updateCounter == 467) {
            xSpeed = horizontalMove? -moveSpeed : xSpeed;
            ySpeed = !horizontalMove? -moveSpeed : ySpeed;
        } else if (updateCounter == 534) {
            xSpeed = horizontalMove? 0 : xSpeed;
            ySpeed = !horizontalMove? 0 : ySpeed;
            updateCounter = 0;
        }
        
        if (yPos > 100 && !firstStop) {
            firstStop = true;
            ySpeed = 0;
        }
        
        yPos += ySpeed;

	if (xPos > 0 && xPos < xPos + width) {
	    xPos += xSpeed;
	}  
    }

    public void setFiringRate(int rate) {
	firingRate = rate;
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
