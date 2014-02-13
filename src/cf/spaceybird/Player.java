package cf.spaceybird;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Player {

	static final float SPEED = 1f;
	static final float SIZE = 1f;
	
	Vector2 position = new Vector2();
	Vector2 velocity = new Vector2();
	Vector2 acceleration = new Vector2();
	Circle bounds = new Circle();
	
	//Constructor for a new player object at the input position vector. Initial velocity
	//and acceleration are both 0.
	public Player(Vector2 position) {
		this.position = position;
		this.bounds.radius = SIZE;
	}

}
