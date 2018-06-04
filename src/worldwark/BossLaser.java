package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

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
	if (laserTimer < 100) {
	    laserTimer++;
	    if (height < 500) {
		height += 100;
	    }
	} else {
	    height -= 100;
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
