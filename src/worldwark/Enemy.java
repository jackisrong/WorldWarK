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

    /*    public void spawnEnemy(WorldWarK panel) { // Just the basis of the spawn enemy algorithm shit fuck
        int xPos;
        int ySpeed;
        int health;
        int xSpeed;

         if (score <= 2500) { //First Tier of enemies
            switch (rnd) {
                case 1:
                    if (spawnTimer >= 2000) {
                        xPos = 150;
                        ySpeed = 20;
                        health = 50;
                        xSpeed = 0;
                        Enemy enemy = new Enemy(xPos, 0, 64, 64, xSpeed, ySpeed, health, 0);
                        objects.add(enemy);
                        xPos = 350;
                        ySpeed = 20;
                        health = 50;
                        xSpeed = 0;
                        Enemy enemy = new Enemy(xPos, 0, 64, 64, xSpeed, ySpeed, health, 0);
                        objects.add(enemy);
                        spawnTimer = 0;

                    }
                    // Check for collision, draw objects and sleep
                    for (GameObject i : objects) {
                        i.update(this);
                    }
                    // Removes objects from list so there wont be a wack exception!!
                    for (GameObject i : finishedObjects) {
                        objects.remove(i);
                    }
                    repaint();
                    try {
                        Thread.sleep(17);
                        spawnTimer += 17;
                    } catch (InterruptedException e) {
                        System.out.println("ERROR: Thread.sleep(17) has been interrupted.");
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        } else if (score > 2500 && score <= 5000) { //Second Tier of Enemies
            switch (rnd) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        } else if (score > 5000 && score <= 7500) { // Third Tier of Enemies
            switch (rnd) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        } else if (score >= 10000) { // Boss summoned as well as the fifth tier of enemies
            switch (rnd) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
    }
     */
    public void update(WorldWarK panel) {
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

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
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
