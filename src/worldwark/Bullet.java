package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bullet extends GameObject {

    private int xSpeed;
    private int ySpeed;

    public Bullet(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    @Override
    public void update(WorldWarK panel) {
	// Check bullet hit
	for (GameObject i : panel.objects) {
	    if (i instanceof Enemy) {
		if (rectangle.intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
		    panel.deleteObject(this);
		    Enemy q = (Enemy) i;
		    q.miniExplosion(true);
		    q.loseHealth(panel.player.getWeaponDamage());
		    if (q.getHealth() <= 0) {
			//panel.deleteObject(i);
			panel.score += q.getPoints();
		    }

		    if (i instanceof Boss == false) {
			panel.dropPowerUp(i);
		    }
		}
	    }
	}

	// Update bullet location
	xPos -= xSpeed;
	yPos -= ySpeed;
	if (yPos < 0 || xPos > 500 || xPos < 0) {
	    panel.deleteObject(this);
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.draw(rectangle);

	// Draw image
	BufferedImage bulletImage;
	try {
	    bulletImage = ImageIO.read(new File("assets/img/bullet.png"));
	    g2.drawImage(bulletImage, xPos, yPos, null);
	} catch (IOException e) {
	    System.out.println("ERROR: bomb.png cannot be read.");
	}
    }
}
