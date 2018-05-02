package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PowerUp extends GameObject {

    private int powerUpType;
    
    public PowerUp(int xPos, int yPos, int width, int height, int powerUpType) {
	super(xPos, yPos, width, height);
	this.powerUpType = powerUpType;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }
    
    @Override
    public void update(WorldWarK panel) {
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw power up hitbox
	Color transparentColor = new Color(0, 0, 0, 0);
	g2.setColor(transparentColor);
	g2.fill(rectangle);
	g2.draw(rectangle);

	// Draw power up image
	BufferedImage powerUpImage;
	try {
	    powerUpImage = ImageIO.read(new File("assets/img/powerUp.png"));
	} catch (IOException e) {
	    System.out.println("ERROR: powerUp.png cannot be read.");
	    powerUpImage = null;
	}
	g2.setClip(rectangle);
	g2.drawImage(powerUpImage, xPos, yPos, null);
    }
}
