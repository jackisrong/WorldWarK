package worldwark;

public class Biplane extends Enemy {

    private int health;
    private int damage;

    public Biplane(int xPos, int yPos, int health, int damage) {
	super(xPos, yPos);
	this.health = health;
	this.damage = damage;
    }
}
