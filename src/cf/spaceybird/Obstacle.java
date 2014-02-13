package cf.spaceybird;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
	
	private static final float SIZE = 1f;
	private static final float DEFAULT_MASS = 1f;
	
	private Vector2 position = new Vector2();
	private Circle bounds = new Circle();
	private float mass = DEFAULT_MASS;
	
	//Constructs an obstacle at the specified position vector with the specified mass.
	public Obstacle(Vector2 position, float mass) {
		this.position = position;
		this.bounds.radius = SIZE;
		this.mass = mass;
	}
	
	//Constructs an obstacle at the specified position vector with the default mass.
	public Obstacle(Vector2 position) {
		this.position = position;
		this.bounds.radius = SIZE;
	}

}
