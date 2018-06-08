package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EnemyBullet extends GameObject {

    private int xSpeed;
    private int ySpeed;
    private int damage;
    private BufferedImage bulletImage;

    public EnemyBullet(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int damage) {
	super(xPos, yPos, width, height);
	this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;
	this.damage = damage;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
	try {
	    bulletImage = ImageIO.read(new File("assets/img/enemybullet.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: bomb.png cannot be read.");
	}
    }

    public int getDamage() {
	return damage;
    }

    @Override
    public void update(WorldWarK panel) {
	// Removes damage upon bullet collision on player
	if (rectangle.intersects(panel.player.getXPos(), panel.player.getYPos(), panel.player.getWidth(), panel.player.getHeight())) {
	    panel.deleteObject(this);
	    panel.player.miniExplosion(true);
	    panel.player.loseHealth(10);
	    panel.playSound(4);
	}

	// Update bullet location
	yPos -= ySpeed;
	xPos -= xSpeed;
	if (isOutsideScreen()) {
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
	g2.drawImage(bulletImage, xPos, yPos, null);
    }
}
