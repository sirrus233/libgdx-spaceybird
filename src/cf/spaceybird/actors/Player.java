package cf.spaceybird.actors;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private static final float SPEED = 1f;
	private static final float SIZE = 1f;
	
	private Vector2 position = new Vector2();
	private Vector2 velocity = new Vector2();
	private Vector2 acceleration = new Vector2();
	private Circle bounds = new Circle();
	private float mass = 1f;
	
	//Constructor for a new player object at the input position vector, with the specified mass.
	//Initial velocityand acceleration are both 0. Mass is left default.
	public Player(Vector2 position, float mass) {
		this.position = position;
		this.bounds.radius = SIZE;
		this.mass = mass;
	}
	
	//Constructor for a new player object at the input position vector. Initial velocity
	//and acceleration are both 0. Mass is left default.
	public Player(Vector2 position) {
		this.position = position;
		this.bounds.radius = SIZE;
	}
	
	public Circle getBounds() {
		return bounds;
	}
	
	public float getMass() {
		return this.mass;
	}
	
	//Sets the position of the player
	public void setPosition(float x, float y) {
		this.position.set(x,y);
		this.bounds.setPosition(this.position);
	}
	
	public void updatePosition(float deltaX, float deltaY) {
		this.position.x += deltaX;
		this.position.y += deltaY;
		this.bounds.setPosition(this.position);
	}
}
