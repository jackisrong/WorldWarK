package worldwark;

public abstract class AttackHelicopter extends Enemy {

    private int health;
    private int damage;
    
    public AttackHelicopter(int xPos, int yPos, int width, int height, int health, int damage) {
	super(xPos, yPos, width, height);
	this.health = health;
	this.damage = damage;
    }
}
