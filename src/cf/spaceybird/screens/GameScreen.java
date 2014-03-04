package cf.spaceybird.screens;

import cf.spaceybird.Assets;
import cf.spaceybird.Input;
import cf.spaceybird.LevelManager;
import cf.spaceybird.PhysicsEngine;
import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends ScreenTemplate {
	private final float LAUNCH_FORCE_SCALE = 8;
	
	private enum State {
		WAITING, AIMING, LAUNCHED, VICTORY
	}
	
	private Game game;
	private Player player;
	private Array<Obstacle> obstacles;
	private Circle goal;
	private State state;
	private int score;
	
	public GameScreen(Game g) {
		this.game = g;
		this.state = State.WAITING;
		this.player = LevelManager.getPlayer();
		this.obstacles = LevelManager.getObstacles();
		this.goal = LevelManager.getGoal();
		this.score = 0;
		
		LevelManager.setLevel(1);
	}

	@Override
	public void draw() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(gameCam.combined);
		batch.begin();
		batch.disableBlending();
		batch.draw(Assets.background, 0, 0, unitsX, unitsY);
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
		batch.draw(Assets.satellite, this.goal.x - this.goal.radius, this.goal.y - this.goal.radius, 
				2*this.goal.radius, 2*this.goal.radius);
		batch.end();
		
		batch.setProjectionMatrix(fontCam.combined);
		batch.begin();
		Assets.font.draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-3*ppuX, Gdx.graphics.getHeight()-0.3f*ppuY);
		batch.end();
		
		if (DEBUG) {
			debugRenderer.setProjectionMatrix(gameCam.combined);
	        debugRenderer.begin(ShapeType.Line);
	        debugRenderer.setColor(new Color(1, 0, 0, 1));
	        debugRenderer.circle(this.player.getBounds().x, this.player.getBounds().y, this.player.getBounds().radius, 1200);
	        for (Obstacle o : this.obstacles) {
	        	debugRenderer.circle(o.getBounds().x, o.getBounds().y, o.getBounds().radius, 1200);
	        }
	        debugRenderer.end();
		}
	}

	@Override
	public void update(float delta) {
		if (Input.keys[Input.ESC]) {
			this.game.setScreen(new MenuScreen(this.game));
		}
		
		switch(state) {
		case WAITING:
			if (Input.buttonsDown[Input.LEFT] && this.player.getBounds().contains(Input.getMouseNorm())) {
				this.state = State.AIMING;
			}
			break;
			
		case AIMING:
			if (Input.getMouseNorm().dst(LevelManager.getStartPos()) < 2) {
				this.player.updatePosition(Input.getMouseDeltaNorm());
			} else {
				Vector2 newDirection = Input.getMouseNorm().sub(LevelManager.getStartPos()).nor().scl(2);
				Vector2 newPosition = LevelManager.getStartPos().add(newDirection);
				this.player.setPosition(newPosition);
			}
			if (Input.buttonsClicked[Input.LEFT]) {
				Vector2 launch = LevelManager.getStartPos().sub(this.player.getPosition());
				this.player.setVelocity(launch.scl(LAUNCH_FORCE_SCALE));
				this.state = State.LAUNCHED;
			}
			break;
			
		case LAUNCHED:
			Vector2 gravForce = new Vector2();
			boolean hitObstacle = false;
			for (Obstacle o : this.obstacles) {
				if (o.getBounds().overlaps(this.player.getBounds())) {
					hitObstacle = true;
					break;
				}
				gravForce.add(PhysicsEngine.getGravForce(this.player, o));
			}
			
			if (hitObstacle) {
				resetPlayer();
			} else if (this.goal.overlaps(this.player.getBounds())) {
				this.state = State.VICTORY;
			} else if (this.player.getBounds().x > unitsX + this.player.getBounds().radius ||
					this.player.getBounds().x < -this.player.getBounds().radius ||
					this.player.getBounds().y > unitsY + this.player.getBounds().radius ||
					this.player.getBounds().y < -this.player.getBounds().radius) {
				resetPlayer();
			} else {
				Vector2 oldPosition = this.player.getPosition();
				this.player.setAcceleration(PhysicsEngine.getAcceleration(this.player.getMass(), gravForce));
				this.player.setVelocity(PhysicsEngine.getVelocity(this.player.getVelocity(), this.player.getAcceleration(), delta));
				this.player.updatePosition(this.player.getVelocity().scl(delta));
				this.score += this.player.getPosition().sub(oldPosition).len() *100;
			}
			break;
			
		case VICTORY:			
			LevelManager.nextLevel();
			resetPlayer();
			break;
			
		default:
			System.out.println("Error: Invalid state accessed from GameScreen!");
		}
	}
	
	private void resetPlayer() {
		this.score = 0;
		this.player.setPosition(LevelManager.getStartPos());
		this.state = State.WAITING;
	}

}
