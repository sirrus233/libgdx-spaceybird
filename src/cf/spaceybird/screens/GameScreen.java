package cf.spaceybird.screens;

import cf.spaceybird.Assets;
import cf.spaceybird.LevelManager;
import cf.spaceybird.PhysicsEngine;
import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends ScreenTemplate {
	
	private Game game;
	private Player player;
	private Array<Obstacle> obstacles;
	private boolean grabbingPlayer;
	private Vector2 mouse;
	private Vector2 mouseDelta;
	private Vector2 mouseNorm;
	private Vector2 mouseDeltaNorm;
	
	public GameScreen(Game g) {
		// TODO Auto-generated constructor stub
		this.game = g;
		this.grabbingPlayer = false;
		LevelManager.setLevel(2);
		this.player = LevelManager.getPlayer();
		this.obstacles = LevelManager.getObstacles();
		this.mouse = new Vector2();
		this.mouseDelta = new Vector2();
		this.mouseNorm = new Vector2();
		this.mouseDeltaNorm = new Vector2();
	}

	@Override
	public void draw() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.disableBlending();
		batch.draw(Assets.background, 0, 0, aspectX, aspectY);
		batch.enableBlending();
		batch.draw(Assets.spaceyBird, this.player.getBounds().x - this.player.getBounds().radius, 
				this.player.getBounds().y - this.player.getBounds().radius, 
				2*this.player.getBounds().radius, 2*this.player.getBounds().radius);
		for (Obstacle o : this.obstacles) {
			if (o.getBounds().radius >= 1.5) {
				batch.draw(Assets.planetLarge, o.getBounds().x - o.getBounds().radius, 
						o.getBounds().y - o.getBounds().radius, 2*o.getBounds().radius, 2*o.getBounds().radius);	
		
			} else if (o.getBounds().radius >= 1.0) {
				batch.draw(Assets.planetMedium, o.getBounds().x - o.getBounds().radius, 
						o.getBounds().y - o.getBounds().radius, 2*o.getBounds().radius, 2*o.getBounds().radius);
			} else {
				batch.draw(Assets.planetSmall, o.getBounds().x - o.getBounds().radius, 
						o.getBounds().y - o.getBounds().radius, 2*o.getBounds().radius, 2*o.getBounds().radius);				
			}
		}	
		batch.end();
		if (DEBUG) {
			debugRenderer.setProjectionMatrix(cam.combined);
	        debugRenderer.begin(ShapeType.Line);
	        debugRenderer.setColor(new Color(1, 0, 0, 1));
	        debugRenderer.circle(this.player.getBounds().x, this.player.getBounds().y, this.player.getBounds().radius, 1200);
	        for (Obstacle o : this.obstacles) {
	        	debugRenderer.circle(o.getBounds().x, o.getBounds().y, o.getBounds().radius, 1200);
	        }
	        debugRenderer.end();
		}
	}

	public void update(float delta) {
		this.mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		this.mouseDelta.set(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
		this.mouseNorm.set(new Vector2(mouse).div(ppuX,ppuY));
		this.mouseDeltaNorm.set(new Vector2(mouseDelta).div(ppuX,ppuY));
		
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && this.player.getBounds().contains(this.mouseNorm)) {
			this.grabbingPlayer = true;
			this.player.stop();
		} else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			this.grabbingPlayer = false;
		}
		
		if (this.grabbingPlayer) {
			System.out.println(mouse.dst(LevelManager.getStartPos()));
			if (mouseNorm.dst(LevelManager.getStartPos()) < 2) {
				this.player.updatePosition(this.mouseDeltaNorm);
			}
		} else {
			
			Vector2 gravForce = new Vector2();
			for (Obstacle o : this.obstacles) {
				if (o.getBounds().overlaps(this.player.getBounds())) {
					this.player.setPosition(LevelManager.getStartPos());
					this.player.stop();
				}
				gravForce.add(PhysicsEngine.getGravForce(this.player, o));
			}
			this.player.setAcceleration(PhysicsEngine.getAcceleration(this.player.getMass(), gravForce));
			this.player.setVelocity(PhysicsEngine.getVelocity(this.player.getVelocity(), this.player.getAcceleration(), delta));
			this.player.updatePosition(this.player.getVelocity().scl(delta));
		}
	}

}
