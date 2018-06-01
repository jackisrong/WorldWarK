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

    protected int xSpeed;
    protected int ySpeed;
    protected int health;
    protected int typeOfEnemy;
    protected int points;
    protected int shoot;
    private boolean reverse;
    private int reverseTimer;
    private int shootTimer;
    protected int initialHealth;
    protected int firingRate;
    protected int initialSpeed = 0;
    private static Random rand = new Random();

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
        firingRate = 1000;
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

    public int getPoints() {
        return points;
    }

    public int getFiringRate() {
        return firingRate;
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
        // Check enemy collision
        if (this.getRectangle().intersects(panel.player.getXPos(), panel.player.getYPos(), panel.player.getWidth(), panel.player.getHeight())) {
            panel.deleteObject(this);
            panel.player.loseHealth(10);
            if (panel.player.getHealth() <= 0) {
                try {
                    panel.gameOver();
                } catch (IOException e) {
                    System.out.println("ERROR: IOException when updating EnemyBullet");
                }
            }
        }
        // X reverse
        if (reverse && readyToTurnAtX(panel.getWidth() / 2) && this.getType() < 6) {
            if (reverseTimer == 0) {
                initialSpeed = this.getXSpeed();
            }
            xSpeed = 0;
            reverseTimer += 15;
            if (reverseTimer >= 1000) {
                reverse = false;
                xSpeed = -initialSpeed;
                reverseTimer = 0;
            }
        }
        if (reverse && readyToTurnAtY(panel.getHeight() / 2) && this.getType() < 6) { // Horizontal Zig Zag
            if (reverseTimer == 0) {
                initialSpeed = this.getYSpeed();
            }
            ySpeed = 0;
            reverseTimer += 15;
            if (reverseTimer >= 1000) {
                reverse = false;
                ySpeed = -initialSpeed;
                reverseTimer = 0;
            }
        }

        if (reverse && this.getYSpeed() != 0 && this.getType() == 6) { // Horizontal Zig Zag
            reverseTimer += 20;
            if (reverseTimer == 500) {
                ySpeed = -this.getYSpeed();
            }
            if (reverseTimer == 1000) {
                ySpeed = -this.getYSpeed();
                reverseTimer = 0;
            }

        }

        if (reverse && this.getXSpeed() != 0 && this.getType() == 8) {
            reverseTimer += 20;
            if (reverseTimer == 500) {
                xSpeed = -this.getXSpeed();
            }
            if (reverseTimer == 1000) {
                xSpeed = -this.getXSpeed();
                reverseTimer = 0;
            }
        }
        if (reverse && readyToTurnAtX(panel.getWidth() / 2) && this.getType() == 10) {
            if (reverseTimer == 0) {
                initialSpeed = this.getXSpeed();
            }
            xSpeed = 0;
            reverseTimer += 15;
            if (reverseTimer >= 1000) {
                reverse = false;
                xSpeed = initialSpeed;
                ySpeed = rand.nextInt(15) + 5;
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
                fileName = "helicopterLow1";
                break;
            case 1:
            case 6: {
                fileName = "helicopterMed1";
                break;
            }
            case 2:
            case 7:
            case 10: {
                fileName = "helicopterHard1";
                break;
            }
            case 3:
                fileName = "fighterPlaneLow";
                break;
            case 4:
            case 8: {
                fileName = "fighterPlaneMed";
                break;
            }
            case 5:
            case 9: {
                fileName = "fighterPlaneHard";
                break;
            }
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
