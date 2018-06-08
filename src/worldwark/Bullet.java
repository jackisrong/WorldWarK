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
    private BufferedImage bulletImage;

    public Bullet(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
	try {
	    bulletImage = ImageIO.read(new File("assets/img/bullet.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: bullet.png cannot be read.");
	}
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
		    panel.score += 20;
		    if (q.getHealth() <= 0) {
			panel.score += q.getPoints();
		    }

		    if (!(i instanceof Boss)) {
			panel.dropPowerUp(i);
		    }
		}
	    }
	}

	// Update bullet location
	xPos -= xSpeed;
	yPos -= ySpeed;
	if (isOutsideScreen()) {
	    panel.deleteObject(this);
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw hitbox and image
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.draw(rectangle);
	g2.drawImage(bulletImage, xPos, yPos, null);
    }
}
