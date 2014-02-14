package cf.spaceybird.screens;

import cf.spaceybird.LevelManager;
import cf.spaceybird.actors.Obstacle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen extends ScreenTemplate {
	
	private Game game;
	
	public GameScreen(Game g) {
		// TODO Auto-generated constructor stub
		this.game = g;
	}

	@Override
	public void draw() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 0, 0, 1));
        for (Obstacle o : LevelManager.getObstacles()) {
        	debugRenderer.circle(o.getPosition().x, o.getPosition().y, o.getRadius(), 1200);
        }
        debugRenderer.end();
	}

	@Override
	public void update() {
		LevelManager.setLevel(1);
		
	}

}
