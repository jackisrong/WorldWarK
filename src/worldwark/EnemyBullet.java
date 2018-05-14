package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class EnemyBullet extends GameObject {
    
    private int xSpeed;
    private int ySpeed;
    
    public EnemyBullet(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed) {
        super(xPos, yPos, width, height);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    @Override
    public void update(WorldWarK panel) {
        try {
            panel.checkEnemyBulletHit(this);
            yPos -= ySpeed;
            xPos -= xSpeed;
            if (yPos > panel.getHeight() - height - 1) {
                panel.deleteObject(this);
            }
            if (xPos <= -201 || xPos >= 701) {
                panel.deleteObject(this);
            }
        } catch (IOException ex) {
            System.out.println("ERROR: Enemy bullet hit update");
        }
    }

    @Override
    public void paintComponent(Graphics2D g2) {
        rectangle.setFrame(xPos, yPos, width, height);

        // Draw hitbox
        Color transparentColor = new Color(0, 0, 0, 0);
        g2.setColor(transparentColor);
        g2.fill(rectangle);
        g2.draw(rectangle);

        // Draw image
        BufferedImage bulletImage;
        try {
            bulletImage = ImageIO.read(new File("assets/img/bomb.png"));
            g2.setClip(rectangle);
            g2.drawImage(bulletImage, xPos, yPos, null);
        } catch (IOException e) {
            System.out.println("ERROR: bomb.png cannot be read.");
        }
    }
}
