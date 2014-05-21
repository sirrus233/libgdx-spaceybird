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
		this(position, radius);
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
	
	public Obstacle(Obstacle o) {
		this(o.getPosition(), o.getBounds().radius, o.getMass());
	}
	
	public Circle getBounds() {
		return bounds;
	}
	
	public float getMass() {
		return this.mass;
	}
	
	//Get the current position vector of the obstacle
	public Vector2 getPosition() {
		return new Vector2(this.position);
	}
	
	public void setPosition(float x, float y) {
		this.position.set(x,y);
		this.bounds.setPosition(this.position);
	}
	
	public void setPosition(Vector2 p) {
		this.setPosition(p.x, p.y);
	}
	
	public void updatePosition(float deltaX, float deltaY) {
		this.position.x += deltaX;
		this.position.y += deltaY;
		this.bounds.setPosition(this.position);
	}
}
