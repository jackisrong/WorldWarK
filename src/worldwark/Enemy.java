package worldwark;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Enemy extends GameObject {
    
    private int xSpeed;
    private int ySpeed;
    private int health;
    private int numberOfBombs;
    private Rectangle2D rectangle;

    public Enemy(int xPos, int yPos, int width, int height, int ySpeed, int xSpeed, int health) {
	super(xPos, yPos, width, height);
        this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;
	this.health = health;
	rectangle = new Rectangle2D.Double(xPos, yPos, 15, 10);
    }
    
     public void update(WorldWarK panel) {
	// Left wall impact
        if (xPos + xSpeed < 0) {
	    xPos = 0;
	    panel.deleteObject(this);
	} // Right wall impact
	else if (xPos + xSpeed > panel.getWidth() - width - 1) {
	    xPos = panel.getWidth() - width - 1;
	    panel.deleteObject(this);
	} else {
            xPos += xSpeed;
        }
        
        // Deletes the object once it has passed the bottom of the frame
        if (yPos > panel.getHeight() - height - 1) {
            panel.deleteObject(this);
        } else {
            yPos += ySpeed;
        }
    }
     
     // Paints the enemy
     public void paintComponent(Graphics2D g2) {
	rectangle.setFrame(xPos, yPos, 15, 10);
	g2.setColor(Color.WHITE);
	g2.fill(rectangle);
	g2.draw(rectangle);
    }
}
