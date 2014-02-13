package cf.spaceybird;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelManager {
    
	private static Player player = new Player(new Vector2(0,0));
	private static Array<Obstacle> obstacles = new Array<Obstacle>(false, 16);
	private static int currentLevel = 1;
	
	public static void setLevel(int level) {
		obstacles.clear();
		switch(level) {
		case 1:
			player.setPosition(3f,3f);		
			obstacles.add(new Obstacle(new Vector2(8,8), 0.5f));
			break;
		case 2:
			player.setPosition(8f,8f);	
			obstacles.add(new Obstacle(new Vector2(100,100), 0.5f));
			break;	
		default:
			player.setPosition(100f,100f);	
			obstacles.add(new Obstacle(new Vector2(100,100), 0.5f));
		}
	}
	
	//Returns the player object
	public static Player getPlayer() {
		return player;
	}
	
	//Returns an array of the level's obstacles
	public static Array<Obstacle> getObstacles() {
		return obstacles;
	}
}
