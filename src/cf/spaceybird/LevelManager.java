package cf.spaceybird;

import java.util.HashMap;
import java.util.Map;

import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelManager {

	private static Player player = new Player(new Vector2());
	//private static PlayerPredict playerPredict = new PlayerPredict(new Vector2());
	private static Player playerPredict = new Player(new Vector2());
	private static Vector2 startPos = new Vector2();
	private static Array<Obstacle> obstacles = new Array<Obstacle>(false, 16);
	private static Circle goal = new Circle(0, 0, 0.3f);
	private static int currentLevel = 1;
	public static Map<Integer, Integer> scores = new HashMap<Integer, Integer>();
	
	public static void setLevel(int level) {
		currentLevel = level;
		obstacles.clear();
		switch(level) {
		case 0:
			initPlayer(2,2);
			//obstacles.add(new Obstacle(new Vector2(8,8), 0.5f));
			goal.setPosition(14,8);
			break;
		case 1:
			initPlayer(3,3);
			obstacles.add(new Obstacle(new Vector2(8,8), 0.5f));
			goal.setPosition(10,3);
			break;
		case 2:
			initPlayer(6,4);
			obstacles.add(new Obstacle(new Vector2(8,6), 0.5f));
			obstacles.add(new Obstacle(new Vector2(2,5), 1.5f));
			goal.setPosition(10,3);
			break;	
		default:
			System.out.println("Error: Invalid level accessed from LevelManager!");
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
	
	//Returns the playerPredict object
		public static Player getPlayerPredict() {
			return playerPredict;
		}
	
	//Returns an array of the level's obstacles
	public static Array<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public static void nextLevel() {
		if (currentLevel > 0) { setLevel(++currentLevel); }
	}
	
	public static Vector2 getStartPos() {
		return new Vector2(startPos);
	}
	
	public static void setStartPos(Vector2 newPos) {
		startPos = new Vector2(newPos);
	}
	
	public static Circle getGoal() {
		return goal;
	}
	
	public static void loadScores(){
		//TODO Loads in scores from gamesave file.
	}
	
	public static void setScore(int score){
		scores.put(currentLevel, score);
		//TODO Write score to gamesave file.
	}
}
