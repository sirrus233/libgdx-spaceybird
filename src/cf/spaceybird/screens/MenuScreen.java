package cf.spaceybird.screens;

import cf.spaceybird.Assets;
import cf.spaceybird.Input;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class MenuScreen extends AbstractScreen {
	private Game game;
	private Rectangle startButton;
	private Rectangle soundButton;
	private Rectangle levEdButton;	
	
	public MenuScreen(Game g) {
		this.game = g;
			
		this.startButton = new Rectangle(4,6,8,1);
		this.soundButton = new Rectangle(4,4,8,1);
		this.levEdButton = new Rectangle(4,2,8,1);
	}
	
	@Override
	public void draw() {
		Gdx.gl.glClearColor(0.20f, 0.18f, 0.54f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(gameCam.combined);
		batch.begin();
		//TODO button assets go here
		batch.end();		

		batch.setProjectionMatrix(fontCam.combined);
		batch.begin();
		Assets.font.draw(batch, "SPACEY BIRD!", 5.8f*ppuX, 8f*ppuY);
		Assets.font.draw(batch, "START", 7f*ppuX, 6.7f*ppuY);
		Assets.font.draw(batch, "SOUND", 7f*ppuX, 4.7f*ppuY);
		Assets.font.draw(batch, "LevEd", 7f*ppuX, 2.7f*ppuY);
		batch.end();
		
		if (debug) {
			debugRenderer.setProjectionMatrix(gameCam.combined);
			debugRenderer.begin(ShapeType.Line);
			debugRenderer.setColor(1, 0, 0, 1);
			debugRenderer.rect(this.startButton.x, this.startButton.y, this.startButton.width, this.startButton.height);
			debugRenderer.rect(this.soundButton.x, this.soundButton.y, this.soundButton.width, this.soundButton.height);
			debugRenderer.rect(this.levEdButton.x, this.levEdButton.y, this.levEdButton.width, this.levEdButton.height);
			debugRenderer.end();
		}
	}

	@Override
	public void update(float delta) {
		if (Input.keys[Input.ESC]) {
			Gdx.app.exit();
		}
		
		if (Input.buttonsClicked[Input.LEFT]) {
			if (this.startButton.contains(Input.getMouseNorm())) {
				this.game.setScreen(new GameScreen(this.game));

				
			} else if (this.levEdButton.contains(Input.getMouseNorm())) {
					this.game.setScreen(new EditorScreen(this.game));
					
			} else if (this.soundButton.contains(Input.getMouseNorm())) {
				if (Assets.music.isPlaying()) {
					Assets.music.stop();
				} else {
					Assets.music.play();
				}
			}
		}
	}

}
