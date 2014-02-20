package cf.spaceybird.screens;

import cf.spaceybird.Assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen extends ScreenTemplate {
	private Game game;
	private Rectangle startButton;
	private Rectangle soundButton;
	private Vector2 mouse;
	private Vector2 mouseNorm;
	private boolean mouseClicked;
	
	public MenuScreen(Game g) {
		this.game = g;
		this.mouseClicked = false;
		this.mouse = new Vector2();
		this.mouseNorm = new Vector2();
		this.startButton = new Rectangle(4,4,8,1);
		this.soundButton = new Rectangle(4,2,8,1);	
	}
	
	@Override
	public void draw() {
		Gdx.gl.glClearColor(0.20f, 0.18f, 0.54f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gameBatch.setProjectionMatrix(gameCam.combined);
		gameBatch.begin();
		//TODO button assets go here
		gameBatch.end();
		
		fontBatch.setProjectionMatrix(fontCam.combined);
		fontBatch.begin();
		Assets.font.draw(fontBatch, "SPACEY BIRD!", 5.8f*ppuX, 8f*ppuY);
		Assets.font.draw(fontBatch, "START", 7f*ppuX, 4.7f*ppuY);
		Assets.font.draw(fontBatch, "SOUND", 7f*ppuX, 2.7f*ppuY);
		fontBatch.end();
		
		if (DEBUG) {
			debugRenderer.setProjectionMatrix(gameCam.combined);
			debugRenderer.begin(ShapeType.Line);
			debugRenderer.setColor(new Color(1, 0, 0, 1));
			debugRenderer.rect(this.startButton.x, this.startButton.y, this.startButton.width, this.startButton.height);
			debugRenderer.rect(this.soundButton.x, this.soundButton.y, this.soundButton.width, this.soundButton.height);
			debugRenderer.end();
		}
	}

	@Override
	public void update(float delta) {
		this.mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		this.mouseNorm.set(new Vector2(mouse).div(ppuX,ppuY));
		
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			this.mouseClicked = true;
		}
		
		if (mouseClicked && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			this.mouseClicked = false;
			if (this.startButton.contains(this.mouseNorm)) {
				this.game.setScreen(new GameScreen(this.game));
			} else if (this.soundButton.contains(this.mouseNorm)) {
				if (Assets.music.isPlaying()) {
					Assets.music.stop();
				} else {
					Assets.music.play();
				}
			}
		}
	}

}
