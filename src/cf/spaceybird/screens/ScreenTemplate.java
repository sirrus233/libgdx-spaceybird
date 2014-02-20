package cf.spaceybird.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class ScreenTemplate implements Screen {
	final boolean DEBUG = false;
	
	static OrthographicCamera gameCam;
	static OrthographicCamera fontCam;
	static SpriteBatch batch;
	static ShapeRenderer debugRenderer;
	
	static float unitsX;
	static float unitsY;
	static float ppuX; //pixels per unit on the X axis
	static float ppuY; //pixels per unit on the Y axis
	
	public static void init() {
		unitsX = 16;
		unitsY = 9;
		
		ppuX = Gdx.graphics.getWidth()/unitsX;
		ppuY = Gdx.graphics.getHeight()/unitsY;
		
		//A new OrthographicCamera is created, with relative aspect ratio defined in its parameters
		//The camera view is then shifted to point at only the first quadrant of the graph
		gameCam = new OrthographicCamera(unitsX, unitsY);
    	gameCam.position.set(unitsX/2, unitsY/2, 0);
    	gameCam.update();
    	
    	fontCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	fontCam.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
    	fontCam.update();
    	
    	batch = new SpriteBatch();
    	debugRenderer = new ShapeRenderer();
	}
	
	public abstract void draw();
	
	public abstract void update(float delta);
	
	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	@Override
	public void resize(int width, int height) {
		ppuX = Gdx.graphics.getWidth()/unitsX;
		ppuY = Gdx.graphics.getHeight()/unitsY;	
		
		fontCam.setToOrtho(false, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
		fontCam.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
    	fontCam.update();
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
