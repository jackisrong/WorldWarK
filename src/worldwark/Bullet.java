package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bullet extends GameObject {

    public Bullet(int xPos, int yPos, int width, int height) {
	super(xPos, yPos, width, height);
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    @Override
    public void update(WorldWarK panel) {
	panel.checkBulletHit(this);

	yPos -= 10;
	if (yPos < 0) {
	    panel.deleteObject(this);
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
