package cf.spaceybird;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Renderer {
    
	private static OrthographicCamera cam;
	private static ShapeRenderer debugRenderer = new ShapeRenderer();

	public static void init() {
    	cam = new OrthographicCamera(10, 7);
    	cam.position.set(5, 3.5f, 0);
    	cam.update();
    }
	
	public static void render() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        debugRenderer.setColor(new Color(1, 0, 0, 1));
        debugRenderer.rect(1, 2, 3, 5);
        debugRenderer.end();
	}
}
