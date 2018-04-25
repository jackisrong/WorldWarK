package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bomb extends PowerUp {

    private Rectangle2D bombBox;

    public Bomb(int xPos, int yPos, int width, int height) {
	super(xPos, yPos, width, height);
	bombBox = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public void update(WorldWarK panel) {

    }

    public void paintComponent(Graphics2D g2) {
	bombBox.setFrame(xPos, yPos, width, height);

	// Draw bullet bulletBox/hitbox
	//Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(Color.WHITE);
	g2.fill(bombBox);
	g2.draw(bombBox);

	// Puts the bullet image on the player
	BufferedImage playerImage;
	try {
	    playerImage = ImageIO.read(new File("assets/img/bomb.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: bomb.png cannot be read.");
	    playerImage = null;
	}
	g2.setClip(bombBox);
	g2.drawImage(playerImage, xPos, yPos, null);
    }
}
