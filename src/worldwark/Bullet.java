package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bullet extends GameObject {

    private Rectangle2D bulletBox;

    public Bullet(int xPos, int yPos, int width, int height) {
	super(xPos, yPos, width, height);
	bulletBox = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public void update(WorldWarK panel) {
	yPos -= 10;
	if (yPos < 0) {
	    panel.deleteObject(this);
	}
    }

    public void paintComponent(Graphics2D g2) {
	bulletBox.setFrame(xPos, yPos, width, height);

	// Draw bullet bulletBox/hitbox
	//Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(Color.WHITE);
	g2.fill(bulletBox);
	g2.draw(bulletBox);

	// Puts the bullet image on the player
	BufferedImage playerImage;
	try {
	    playerImage = ImageIO.read(new File("assets/img/bullet.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: bullet.png cannot be read.");
	    playerImage = null;
	}
	g2.setClip(bulletBox);
	g2.drawImage(playerImage, xPos, yPos, null);
    }
}
