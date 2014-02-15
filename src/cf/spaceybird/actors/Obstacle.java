package cf.spaceybird.actors;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
	
	private static final float DEFAULT_MASS = 1;
	
	private Vector2 position = new Vector2();
	private Circle bounds = new Circle();
	private float mass = DEFAULT_MASS;
	
	//Constructs an obstacle at the specified position vector with the specified mass and radius.
	public Obstacle(Vector2 position, float radius, float mass) {
		this.position = position;
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.radius = radius;
		this.mass = mass;
	}
	
	//Constructs an obstacle at the specified position vector with specified radius and the
	//default mass.
	public Obstacle(Vector2 position, float radius) {
		this.position = position;
		this.bounds.x = position.x;
		this.bounds.y = position.y;
		this.bounds.radius = radius;
	}
	
	public Circle getBounds() {
		return bounds;
	}
	
	public float getMass() {
		return this.mass;
	}
	
	public Vector2 getPosition() {
		return new Vector2(this.position);
	}
	
	public void updatePosition(float deltaX, float deltaY) {
		this.position.x += deltaX;
		this.position.y += deltaY;
		this.bounds.setPosition(this.position);
	}
}
