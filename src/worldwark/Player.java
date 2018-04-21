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
        rectangle = new Rectangle2D.Double(xPos, yPos, 15, 10);
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
        rectangle.setFrame(xPos, yPos, 15, 10);
        BufferedImage image;
        //Adds the background image in LMAO accident :p
        try {
            image = ImageIO.read(new File("background.jpg"));
        } catch (IOException e) {
            image = null;
        }
        g2.drawImage(image, 0, 0, null);
        g2.setColor(Color.BLUE);
        g2.fill(rectangle);
        g2.draw(rectangle);
    }
}
