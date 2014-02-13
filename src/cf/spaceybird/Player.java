package cf.spaceybird;

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
	
	//Returns the players mass
	public float getMass() {
		return this.mass;
	}

}