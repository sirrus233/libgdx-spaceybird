package cf.spaceybird;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelManager {
    
	private Player player;
	private Array<Obstacle> obstacles = new Array<Obstacle>(false, 16);
	
	//Constructor that creates the demo level
	public LevelManager() {
		createDemoLevel();
	}
	
	private void createDemoLevel() {
		player = new Player(new Vector2(3,3));		
		obstacles.add(new Obstacle(new Vector2(8,8)));
	}
    
	//Returns the player object
	public Player getPlayer() {
		return this.player;
	}
	
	//Returns an array of the level's obstacles
	public Array<Obstacle> getObstacles() {
		return this.obstacles;
	}
}
