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
    private int imageTimer = 0;
    private int deadTimer = 0;
    private int r = 0;

    public Boss(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int typeOfEnemy, int points, int shoot) {
	super(xPos, yPos, width, height, xSpeed, ySpeed, health, typeOfEnemy, points, shoot);
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
	initialHealth = health;
	updateCounter = 0;
	horizontalMove = false;
	moveSpeed = 0;
	firstStop = false;
    }

    public void setFiringRate(int rate) {
	firingRate = rate;
    }

    @Override
    public void update(WorldWarK panel) {
	// Update image animation timer
	if (imageTimer == 79) {
	    imageTimer = 0;
	} else {
	    imageTimer++;
	}

	// Update death timer
	if (health <= 0 && deadTimer < 26) {
	    deadTimer++;
	} else if (deadTimer == 26) {
	    panel.deleteObject(this);
	}

	// Updates direction of boss
	if (updateCounter == 0) {
	    Random rand = new Random();
	    int direction = rand.nextInt(3);
	    switch (direction) {
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

	// Set speeds for boss
	switch (updateCounter) {
	    case 200:
		xSpeed = horizontalMove ? moveSpeed : xSpeed;
		ySpeed = !horizontalMove ? moveSpeed : ySpeed;
		break;
	    case 267:
		xSpeed = horizontalMove ? 0 : xSpeed;
		ySpeed = !horizontalMove ? 0 : ySpeed;
		break;
	    case 467:
		xSpeed = horizontalMove ? -moveSpeed : xSpeed;
		ySpeed = !horizontalMove ? -moveSpeed : ySpeed;
		break;
	    case 534:
		xSpeed = horizontalMove ? 0 : xSpeed;
		ySpeed = !horizontalMove ? 0 : ySpeed;
		updateCounter = 0;
		break;
	    default:
		break;
	}

	// Update boss motion
	if (yPos > 100 && !firstStop) {
	    firstStop = true;
	    ySpeed = 0;
	}
	yPos += ySpeed;
	if (xPos > 0 && xPos < xPos + width) {
	    xPos += xSpeed;
	}

	// Adds difficulty for boss
	if (health <= 0 && r == 0) {
	    panel.score += points;
	    panel.difficulty += 1;
	    panel.snapShot = panel.score;
	    r = 1;
	    panel.b = 0;
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
	g2.fillRect(xPos - 16, yPos + height, 245, 3);
	g2.setColor(Color.GREEN);
	g2.fillRect(xPos - 16, yPos + height, (int) ((double) health / (double) initialHealth * 245.0), 3);

	// Draw appropriate image
	BufferedImage enemyImage;
	String fileName = null;
	BufferedImage explosionImage = null;
	try {
	    if (health > initialHealth / 2) {
		if (imageTimer >= 0 && imageTimer < 20) {
		    fileName = "boss1";
		} else if (imageTimer >= 20 && imageTimer < 40) {
		    fileName = "boss2";
		} else if (imageTimer >= 40 && imageTimer < 60) {
		    fileName = "boss3";
		} else if (imageTimer >= 60 && imageTimer < 80) {
		    fileName = "boss4";
		}
	    } else if (health <= initialHealth / 2 && health > 100) {
		if (imageTimer >= 0 && imageTimer < 20) {
		    fileName = "halfboss1";
		} else if (imageTimer >= 20 && imageTimer < 40) {
		    fileName = "halfboss2";
		} else if (imageTimer >= 40 && imageTimer < 60) {
		    fileName = "halfboss3";
		} else if (imageTimer >= 60 && imageTimer < 80) {
		    fileName = "halfboss4";
		}
	    } else if (health <= 100) {
		fileName = "bossdead";
	    }
	    enemyImage = ImageIO.read(new File("assets/img/" + fileName + ".png"));

	    // Adds animations for boss explosion 
	    if (health <= 0) {
		if ((deadTimer >= 0 && deadTimer < 3) || (deadTimer >= 24 && deadTimer < 27)) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion1.png"));
		} else if ((deadTimer >= 3 && deadTimer < 6) || (deadTimer >= 21 && deadTimer < 24)) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion2.png"));
		} else if ((deadTimer >= 6 && deadTimer < 9) || (deadTimer >= 18 && deadTimer < 21)) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion3.png"));
		} else if (deadTimer >= 9 && deadTimer < 18) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion4.png"));
		}
	    }
	    g2.drawImage(enemyImage, xPos, yPos, null);
	    g2.drawImage(explosionImage, xPos - 48, yPos - 16, null);
	} catch (IOException e) {
	    System.out.println("ERROR: " + fileName + ".png cannot be read.");
	}
    }
}
