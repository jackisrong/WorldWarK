package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends GameObject {

    protected int xSpeed;
    protected int ySpeed;
    protected int health;
    protected int typeOfEnemy;
    protected int shoot;
    private boolean reverse;
    private int reverseTimer;
    private int shootTimer;
    protected int initialHealth;

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
	shootTimer = 0;
	initialHealth = health;
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

    public int getType() {
	return typeOfEnemy;
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

    @Override
    public void update(WorldWarK panel) {
	try {
	    panel.checkEnemyCollision(this);
	} catch (IOException e) {
	    System.out.println("ERROR: IOException at checkEnemyCollision");
	}

	// Y reverse
	/* if (reverse && readyToTurnAtY(panel.getHeight() / 2)) {
            ySpeed = 0;
            reverseTimer += 15;
            if (reverseTimer >= 1000) {
                reverse = false;
                ySpeed = -5;
            }
        }
        // X reverse
        if (reverse && readyToTurnAtX(panel.getWidth() / 2)) {
            xSpeed = 0;
            reverseTimer += 15;
            if (reverseTimer >= 1000) {
                reverse = false;
                xSpeed = -5;
            }
        }
	 */
	if (reverse && this.getYSpeed() != 0 && this.getType() < 4) { // Horizontal Zig Zag
	    reverseTimer += 20;
	    if (reverseTimer == 500) {
		ySpeed = -this.getYSpeed();
	    }
	    System.out.println(reverseTimer);
	    if (reverseTimer == 1000) {
		ySpeed = -this.getYSpeed();
		reverseTimer = 0;
	    }

	}
	if (reverse && this.getXSpeed() != 0) {
	    reverseTimer += 20;
	    if (reverseTimer == 500) {
		xSpeed = -this.getXSpeed();
	    }
	    System.out.println(reverseTimer);
	    if (reverseTimer == 1000) {
		xSpeed = -this.getXSpeed();
		reverseTimer = 0;
	    }
	}
	/* if (reverse && readyToTurnAtX(panel.getWidth() - 50) && this.getType() > 4) {
	    reverseTimer += 15;
	    if (reverseTimer >= 300) {
		xSpeed = -this.getXSpeed();
	    } else if (reverseTimer >= 300 && readyToTurnAtX(panel.getWidth() / 4)) {
		xSpeed = -this.getXSpeed();
	    }
	} */
	if (shootTimer > this.getShoot()) {

	}

	// Exits Left/Right wall's threshold
	if (xPos <= -201 || xPos >= 701) {
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
	g2.fillRect(xPos - 8, yPos + height, 70, 3);
	g2.setColor(Color.GREEN);
	g2.fillRect(xPos - 8, yPos + height, (int) ((double) health / (double) initialHealth * 70.0), 3);

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
