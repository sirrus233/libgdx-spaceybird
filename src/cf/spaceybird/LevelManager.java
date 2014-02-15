package cf.spaceybird;

import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelManager {
    
	private static Player player = new Player(new Vector2());
	private static Vector2 startPos = new Vector2();
	private static Array<Obstacle> obstacles = new Array<Obstacle>(false, 16);
	private static int currentLevel = 1;
	
	public static void setLevel(int level) {
		obstacles.clear();
		switch(level) {
		case 1:
			initPlayer(3,3);		
			obstacles.add(new Obstacle(new Vector2(8,8), 0.5f));
			break;
		case 2:
			initPlayer(4,4);
			obstacles.add(new Obstacle(new Vector2(8,8), 0.5f));
			obstacles.add(new Obstacle(new Vector2(2,5), 1.5f));
			break;	
		default:
			initPlayer(3,3);		
			obstacles.add(new Obstacle(new Vector2(8,8), 0.5f));
		}
	}
	
	private static void initPlayer(float x, float y) {
		startPos.set(x,y);
		player.setPosition(startPos);
	}
	
	//Returns the player object
	public static Player getPlayer() {
		return player;
	}
	
	//Returns an array of the level's obstacles
	public static Array<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public static int getCurrentLevel() {
		return currentLevel;
	}
	
	public static Vector2 getStartPos() {
		return new Vector2(startPos);
	}
}
