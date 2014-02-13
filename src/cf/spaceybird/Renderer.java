package cf.spaceybird;

import cf.spaceybird.actors.Obstacle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Renderer {
    
	public static float aspectX = 16;
	public static float aspectY = 9;
	
	private static OrthographicCamera cam;
	private static ShapeRenderer debugRenderer = new ShapeRenderer();
	private static float ppuX; //pixels per unit on the X axis
	private static float ppuY; //pixels per unit on the Y axis
	
	//This method primarily handles the initialization of the Renderer's camera
	public static void init() {
		//A new OrthographicCamera is created, with relative aspect ratio defined in its parameters
		//The camera view is then shifted to point at only the first quadrant of the graph
		cam = new OrthographicCamera(aspectX, aspectY);
    	cam.position.set(aspectX/2, aspectY/2, 0);
    	cam.update();
    }
	
	public static void render() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 0, 0, 1));
        for (Obstacle o : LevelManager.getObstacles()) {
        	debugRenderer.circle(o.getPosition().x, o.getPosition().y, o.getRadius(), 1200);
        }
        debugRenderer.end();
	}
}
