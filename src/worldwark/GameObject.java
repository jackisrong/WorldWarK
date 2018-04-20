package worldwark;

import java.awt.Graphics2D;

public abstract class GameObject {

    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;

    public GameObject(int xPos, int yPos, int width, int height) {
	this.xPos = xPos;
	this.yPos = yPos;
	this.width = width;
	this.height = height;
    }

    public abstract void update(WorldWarK panel);

    public abstract void paintComponent(Graphics2D g2);
}
