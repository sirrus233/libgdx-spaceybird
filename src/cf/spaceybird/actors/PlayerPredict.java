package cf.spaceybird.actors;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class PlayerPredict{

	private static final float SIZE = 0.3f;
	private static final float DEFAULT_MASS = 1;
	
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;
	private Circle bounds = new Circle();
	private float mass = DEFAULT_MASS;
	
	public PlayerPredict(Vector2 position) {		
		this.position = position;		
		this.bounds.radius = SIZE;
		this.acceleration = new Vector2();			
	}
	
	//Returns the mass of the player
	public float getMass() {
		return this.mass;
	}
	
	public Vector2 getVelocity() {
		return new Vector2(this.velocity);
	}
	
	public void setVelocity(Vector2 v) {
		this.velocity = v;
	}
	
	public Vector2 getAcceleration() {
		return new Vector2(this.acceleration);
	}
	
	public void setAcceleration(Vector2 a) {
		this.acceleration = a;
	}
	
	public Vector2 getPosition() {
		return new Vector2(this.position);
	}
	
	//Sets the position of the player
	public void setPosition(float x, float y) {
		this.position.set(x,y);
		this.bounds.setPosition(this.position);
	}
	
	public void setPosition(Vector2 p) {
		this.setPosition(p.x, p.y);
	}
	
	public Vector2 updatePosition(float deltaX, float deltaY) {
		this.position.x += deltaX;
		this.position.y += deltaY;
		this.bounds.setPosition(this.position);
		return this.getPosition();
	}
	
	public Vector2 updatePosition(Vector2 deltaP) {
		this.updatePosition(deltaP.x, deltaP.y);
		return this.getPosition();
	}
	
	public void ready(Vector2 position){
		this.position = position;
		velocity = new Vector2();
		acceleration = new Vector2();
	}
}