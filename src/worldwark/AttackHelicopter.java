package worldwark;

public class AttackHelicopter extends Enemy {

    private int health;
    private int damage;
    
    public AttackHelicopter(int xPos, int yPos, int health, int damage) {
	super(xPos, yPos);
	this.health = health;
	this.damage = damage;
    }
}
