package cf.spaceybird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class ScreenTemplate implements Screen {
	
	static OrthographicCamera cam;
	static ShapeRenderer debugRenderer;
	
	static float aspectX;
	static float aspectY;
	static float ppuX; //pixels per unit on the X axis
	static float ppuY; //pixels per unit on the Y axis
	
	public static void init() {
		aspectX = 16;
		aspectY = 9;
		
		ppuX = Gdx.graphics.getWidth()/aspectX;
		ppuY = Gdx.graphics.getHeight()/aspectY;
		
		//A new OrthographicCamera is created, with relative aspect ratio defined in its parameters
		//The camera view is then shifted to point at only the first quadrant of the graph
		cam = new OrthographicCamera(aspectX, aspectY);
    	cam.position.set(aspectX/2, aspectY/2, 0);
    	cam.update();
    	
    	debugRenderer = new ShapeRenderer();
	}
	
	@Override
	public void render(float delta) {
		update();
		draw();
	}
	
	public abstract void draw();
	
	public abstract void update();

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
