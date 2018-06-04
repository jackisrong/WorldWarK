package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BossLaser extends GameObject {

    private int damage;
    private int laserTimer = 0;

    public BossLaser(int xPos, int yPos, int width, int height, int damage) {
	super(xPos, yPos, width, height);
	this.damage = damage;
	rectangle = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    @Override
    public void update(WorldWarK panel) {
	// Have laser do damage every 40 ticks on laserTimer
	if (laserTimer % 40 == 0) {
	    if (rectangle.intersects(panel.player.getXPos(), panel.player.getYPos(), panel.player.getWidth(), panel.player.getHeight())) {
		panel.player.miniExplosion(true);
		panel.player.loseHealth(damage);
		panel.playSound(4);
	    }
	}

	// Update laser motion
	if (laserTimer < 500) {
	    laserTimer++;
	    if (height < 800) {
		height += 10;
	    }
	} else {
	    height -= 10;
	    if (height <= 0) {
		panel.deleteObject(this);
	    }
	}
    }

    @Override
    public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, width, height);

	// Draw hitbox
	g2.setClip(null);
	g2.setColor(Color.RED);
	g2.fill(rectangle);
	g2.draw(rectangle);
    }
}
