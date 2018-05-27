package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PowerUp extends GameObject {

    private final int ySpeed = 4;
    private int powerUpType;

    public PowerUp(int xPos, int yPos, int width, int height, int powerUpType) {
	super(xPos, yPos, width, height);
	this.powerUpType = powerUpType;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    @Override
    public void update(WorldWarK panel) {
	// Check power up pick up
	if (this.getRectangle().intersects(panel.player.getXPos(), panel.player.getYPos(), panel.player.getWidth(), panel.player.getHeight())) {
	    if (powerUpType == 0) {
		panel.player.pickUpBomb();
	    } else if (powerUpType == 1) {
		panel.player.upgradeWeapon();
		panel.shootTimer = panel.player.getWeaponCooldown();
	    }
	    panel.deleteObject(this);
	    panel.playSound(3);
	}

	// Update power up location
	yPos += ySpeed;
	if (yPos > panel.getHeight()) {
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
	BufferedImage powerUpImage;
	String fileName = null;
	if (powerUpType == 0) {
	    fileName = "bombPowerUp";
	} else if (powerUpType == 1) {
	    fileName = "weaponPowerUp";
	}

	try {
	    powerUpImage = ImageIO.read(new File("assets/img/" + fileName + ".png"));
	    g2.setClip(rectangle);
	    g2.drawImage(powerUpImage, xPos, yPos, null);
	} catch (IOException e) {
	    System.out.println("ERROR: " + fileName + ".png cannot be read.");
	}
    }
}
