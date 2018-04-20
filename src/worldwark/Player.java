package worldwark;

public abstract class Player extends GameObject {

    private int health;

    public Player(int xPos, int yPos, int health) {
	super(xPos, yPos);
	this.health = health;
    }
}
