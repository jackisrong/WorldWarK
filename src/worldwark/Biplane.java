package worldwark;

public abstract class Biplane extends Enemy {

    private int health;
    private int damage;

    public Biplane(int xPos, int yPos, int width, int height, int health, int damage) {
	super(xPos, yPos, width, height);
	this.health = health;
	this.damage = damage;
    }
}
