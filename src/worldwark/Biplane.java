package worldwark;

public abstract class Biplane extends Enemy {

    private int health;
    private int damage;
    private int xSpeed;
    private int ySpeed;

    public Biplane(int xPos, int yPos, int width, int height, int xSpeed, int ySpeed, int health, int damage) {
	super(xPos, yPos, width, height, xSpeed, ySpeed, health);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
	this.health = health;
	this.damage = damage;
    }
}
