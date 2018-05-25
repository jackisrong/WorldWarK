package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bomb extends GameObject {

    private Ellipse2D explosion;
    private boolean armed = false;
    private int explosionTimer = 0;

    public Bomb(int xPos, int yPos, int width, int height) {
	super(xPos, yPos, width, height);
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public Ellipse2D getExplosionEllipse() {
	return explosion;
    }

    public void setArmed(boolean armedState) {
	armed = armedState;
    }

    @Override
    public void update(WorldWarK panel) {
	if (armed == false && yPos > panel.getHeight() / 2) {
	    yPos -= 2;
	    panel.checkBombCollision(this);
	} else {
	    armed = true;
	}

	if (armed == true) {
	    explosionTimer++;
	}

	if (explosionTimer == 1) {
	    // Check explosion hit on enemies
	    explosion = new Ellipse2D.Double(xPos - 150, yPos - 150, 300, 300);
	    panel.playSound(1);
	    panel.checkBombExplosionHit(this);
	} else if (explosionTimer == 30) {
	    panel.deleteObject(this);
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.setClip(null);

	if (armed == false) {
	    // Draw hitbox
	    g2.fill(rectangle);
	    g2.draw(rectangle);

	    // Draw image
	    BufferedImage bombImage;
	    try {
		bombImage = ImageIO.read(new File("assets/img/bomb.png"));
		g2.setClip(rectangle);
		g2.drawImage(bombImage, xPos, yPos, null);
	    } catch (IOException e) {
		System.out.println("ERROR: bomb.png cannot be read.");
	    }
	} else {
	    // Draw explosion radius
	    g2.fill(explosion);
	    g2.draw(explosion);

	    // Draw image
	    BufferedImage explosionImage;
	    try {
		explosionImage = ImageIO.read(new File("assets/img/explosion.png"));
		g2.setClip(explosion);
		g2.drawImage(explosionImage, xPos - 150, yPos - 150, null);
	    } catch (IOException e) {
		System.out.println("ERROR: explosion.png cannot be read.");
	    }
	}
    }
}
