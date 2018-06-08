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
    private int imageTimer = 0;

    public Bomb(int xPos, int yPos, int width, int height) {
	super(xPos, yPos, width, height);
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public Ellipse2D getExplosionEllipse() {
	return explosion;
    }

    @Override
    public void update(WorldWarK panel) {
	// Update image animation timer
	if (!armed) {
	    if (imageTimer == 17) {
		imageTimer = 0;
	    } else {
		imageTimer++;
	    }
	}

	if (!armed && yPos > panel.getHeight() / 2) {
	    yPos -= 2;
	    // Check bomb collision
	    for (GameObject i : panel.objects) {
		if (i instanceof Enemy) {
		    if (rectangle.intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
			armed = true;
		    }
		}
	    }
	} else {
	    armed = true;
	}

	if (armed) {
	    explosionTimer++;
	}

	if (explosionTimer == 1) {
	    explosion = new Ellipse2D.Double(xPos - 150, yPos - 150, 300, 300);
	    panel.playSound(1);
	    // Check bomb explosion hit
	    for (GameObject i : panel.objects) {
		if (i instanceof Enemy) {
		    if (this.getExplosionEllipse().intersects(i.getXPos(), i.getYPos(), i.getWidth(), i.getHeight())) {
			Enemy q = (Enemy) i;
			q.loseHealth(200);
			panel.score += q.getPoints();
			panel.dropPowerUp(i);
			if (q.getHealth() <= 0) {
			    panel.deleteObject(i);
			}
		    }
		}
	    }
	} else if (explosionTimer == 30) {
	    panel.deleteObject(this);
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);

	if (!armed) {
	    // Draw hitbox
	    g2.draw(rectangle);

	    // Draw image
	    BufferedImage bombImage = null;
	    try {
		if ((imageTimer >= 0 && imageTimer < 3)) {
		    bombImage = ImageIO.read(new File("assets/img/bomb1.png"));
		} else if ((imageTimer >= 3 && imageTimer < 6)) {
		    bombImage = ImageIO.read(new File("assets/img/bomb2.png"));
		} else if ((imageTimer >= 6 && imageTimer < 9)) {
		    bombImage = ImageIO.read(new File("assets/img/bomb3.png"));
		} else if (imageTimer >= 9 && imageTimer < 18) {
		    bombImage = ImageIO.read(new File("assets/img/bomb4.png"));
		}
		g2.drawImage(bombImage, xPos, yPos, null);
	    } catch (IOException e) {
		System.out.println("ERROR: bomb.png cannot be read.");
	    }
	} else {
	    // Draw explosion radius
	    g2.draw(explosion);

	    // Draw image
	    BufferedImage explosionImage = null;
	    try {
		if ((explosionTimer >= 0 && explosionTimer < 3) || (explosionTimer >= 24 && explosionTimer < 27)) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion1.png"));
		} else if ((explosionTimer >= 3 && explosionTimer < 6) || (explosionTimer >= 21 && explosionTimer < 24)) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion2.png"));
		} else if ((explosionTimer >= 6 && explosionTimer < 9) || (explosionTimer >= 18 && explosionTimer < 21)) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion3.png"));
		} else if (explosionTimer >= 9 && explosionTimer < 18) {
		    explosionImage = ImageIO.read(new File("assets/img/explosion4.png"));
		}
		g2.drawImage(explosionImage, xPos - 150, yPos - 150, null);
	    } catch (IOException e) {
		System.out.println("ERROR: explosion.png cannot be read.");
	    }
	}
    }
}
