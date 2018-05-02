package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends GameObject {

    private int xSpeed;
    private int ySpeed;
    private int health;
    private int typeOfEnemy;
    private boolean reverse;
    private int reverseTimer;

    public Enemy(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int typeOfEnemy, int points) {
        super(xPos, yPos, width, height);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.health = health;
        this.typeOfEnemy = typeOfEnemy;
        this.points = points;
        reverse = false;
        reverseTimer = 0;
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

    @Override
    public void update(WorldWarK panel) {
        panel.checkEnemyCollision(this);
        // Y reverse
        if (reverse && readyToTurnAtY(panel.getHeight() / 2)) {
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

        if (reverse && readyToTurnAtY(panel.getHeight() / 2) && this.getType() > 0 && this.getType() < 4 && this.getYSpeed() != 0) {
            int bb = 0;
            reverseTimer += 15;
            if (reverseTimer >= 300 && bb == 0) {
                ySpeed = -5;
                bb = 1;
            } else if (reverseTimer >= 300 && readyToTurnAtY(panel.getHeight() / 4) && bb == 1) {
                xSpeed = - this.getXSpeed();
                bb = 0;
            }
        }
        if (reverse && readyToTurnAtX(panel.getWidth() / 2) && this.getType() > 4) {
            reverseTimer += 15;
            if (reverseTimer >= 300) {
                xSpeed = - this.getXSpeed();
            } else if (reverseTimer >= 300 && readyToTurnAtX(panel.getWidth() / 4)) {
                xSpeed = - this.getXSpeed();
            }
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

        // Draw enemy rectangle/hitbox
        Color transparentColor = new Color(0, 0, 0, 0);
        g2.setColor(transparentColor);
        g2.fill(rectangle);
        g2.draw(rectangle);

        // Puts appropriate enemy image on the enemy
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
