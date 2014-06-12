package cf.spaceybird.screens;

import java.util.ArrayList;

import cf.spaceybird.Assets;
import cf.spaceybird.Input;
import cf.spaceybird.LevelManager;
import cf.spaceybird.PhysicsEngine;
import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends AbstractScreen {
	public final float LAUNCH_FORCE_SCALE = 8;
	public final int MAX_PATHS = 10;
	public final float SPACE_OFFSET_X = 0.5f * unitsX;
	public final float SPACE_OFFSET_Y = 0.5f * unitsY;
	public final int PREDICT_CAP = 4096;
	
	public enum GameState {
		WAITING, AIMING, LAUNCHED, VICTORY
	}
	
	private Game game;
	private Player player;
	private Player playerPredict;
	private ArrayList<Vector2> predictPath;
	private ArrayList<ArrayList<Vector2>> pathHistory;
	private Array<Obstacle> obstacles;
	private Circle goal;
	private int score;
	private GameState state;
	
	public GameScreen(Game g) {
		this.game = g;	
		this.player = LevelManager.getPlayer();
		this.playerPredict = LevelManager.getPlayerPredict();
		this.predictPath = new ArrayList<Vector2>();
		this.pathHistory = new ArrayList<ArrayList<Vector2>>(MAX_PATHS);
		this.obstacles = LevelManager.getObstacles();
		this.goal = LevelManager.getGoal();
		this.score = 0;
		this.state = GameState.WAITING;
		
		LevelManager.setLevel(1);
	}
	
	@Override
	public void draw() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Draw background
		batch.setProjectionMatrix(gameCam.combined);
		batch.begin();
		batch.disableBlending();
		batch.draw(Assets.background, 0, 0, unitsX, unitsY);
		batch.enableBlending();
		batch.end();
		
		//Draw historical paths
		debugRenderer.setProjectionMatrix(gameCam.combined);
        debugRenderer.begin(ShapeType.Line);
        
        debugRenderer.setColor(0, 0.7f, 0.3f, 1);
        for (ArrayList<Vector2> path : this.pathHistory){	        	
        	for (int i = 1; i < path.size(); i++) {
        		debugRenderer.line(path.get(i-1), path.get(i));
        	}   
        }
        
        //Draw player, obstacles, and goal
        batch.setProjectionMatrix(gameCam.combined);
		batch.begin();
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
		
		//Draw text
		batch.setProjectionMatrix(fontCam.combined);
		batch.begin();
		Assets.font.draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-3*ppuX, Gdx.graphics.getHeight()-0.3f*ppuY);
		batch.end();
		
        
        
        debugRenderer.end();
        
		if (debug) {
			//Draw bounding boxes
			debugRenderer.setProjectionMatrix(gameCam.combined);
	        debugRenderer.begin(ShapeType.Line);
	        debugRenderer.setColor(1, 0, 0, 1);
	        debugRenderer.circle(this.player.getBounds().x, this.player.getBounds().y, this.player.getBounds().radius, 1200);
	        for (Obstacle o : this.obstacles) {
	        	debugRenderer.circle(o.getBounds().x, o.getBounds().y, o.getBounds().radius, 1200);
	        }
	        
	        //Draw predictive paths
	        debugRenderer.setColor(0.8f, 0.7f, 0, 1);
        	for (int i = 1; i < predictPath.size(); i++) {
        		debugRenderer.line(predictPath.get(i-1), predictPath.get(i));
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
				this.state = GameState.AIMING;
			}
			break;
			
		case AIMING:
			aimingPathPrediction();
			
			if (Input.getMouseNorm().dst(LevelManager.getStartPos()) < 2) {
				this.player.setPosition(Input.getMouseNorm());
			} else {
				Vector2 newDirection = Input.getMouseNorm().sub(LevelManager.getStartPos()).nor().scl(2);
				Vector2 newPosition = LevelManager.getStartPos().add(newDirection);
				this.player.setPosition(newPosition);
			}
			if (Input.buttonsClicked[Input.LEFT]) {
				if (this.pathHistory.size() >= MAX_PATHS ){
					this.pathHistory.remove(0);
				}
				this.pathHistory.add(new ArrayList<Vector2>());
				
				Vector2 launch = LevelManager.getStartPos().sub(this.player.getPosition());
				this.player.setVelocity(launch.scl(LAUNCH_FORCE_SCALE));
				this.state = GameState.LAUNCHED;
			}
			break;
			
		case LAUNCHED:
			if (this.goal.overlaps(this.player.getBounds())) {
				this.state = GameState.VICTORY;
			}
			
			Vector2 gravForce = new Vector2();
			for (Obstacle o : this.obstacles) {
				gravForce.add(PhysicsEngine.getGravForce(this.player, o));
			}
			
			if (isDead(this.player)) {
				resetPlayer();
			} else {
				Vector2 oldPosition = this.player.getPosition();
				
				this.player.setAcceleration(PhysicsEngine.getAcceleration(this.player.getMass(), gravForce));
				this.player.setVelocity(PhysicsEngine.getVelocity(this.player.getVelocity(), this.player.getAcceleration(), delta));
				this.player.updatePosition(this.player.getVelocity().scl(delta));
				this.score += this.player.getPosition().sub(oldPosition).len() *100;
				
				int lastIndex = this.pathHistory.size() - 1;
				ArrayList<Vector2> travelPath = this.pathHistory.get(lastIndex);
				travelPath.add(new Vector2(this.player.getPosition()));
			}
			break;
			
		case VICTORY:
			this.predictPath.clear();
			this.pathHistory.clear();
			LevelManager.nextLevel();
			resetPlayer();
			break;
			
		default:
			System.out.println("Error: Invalid state accessed from GameScreen!");
		}
	}
	
	private void aimingPathPrediction() {
		this.predictPath.clear();
		this.playerPredict.setPosition(this.player.getPosition());
		
		Vector2 launch = new Vector2(LevelManager.getStartPos()).sub(this.player.getPosition());
		this.playerPredict.setVelocity(launch.scl(LAUNCH_FORCE_SCALE));
						
		while (!isDead(this.playerPredict) && predictPath.size() < PREDICT_CAP) {
			Vector2 gravForce = new Vector2();
			for (Obstacle o : this.obstacles) {						
				gravForce.add(PhysicsEngine.getGravForce(this.playerPredict, o));
			}
			
			this.playerPredict.setAcceleration(PhysicsEngine.getAcceleration(this.playerPredict.getMass(), gravForce));
			this.playerPredict.setVelocity(PhysicsEngine.getVelocity(this.playerPredict.getVelocity(), this.playerPredict.getAcceleration(), .015f));
			this.predictPath.add(this.playerPredict.updatePosition(this.playerPredict.getVelocity().scl(.015f)));
			
			//This handles the corner case where if the player has no velocity after acceleration is applied, then there 
			//is an infinite loop condition because the player will never die. This generally happens in levels with no
			//planets.
			if (playerPredict.getVelocity().isZero()) { break; }
		}
	}
	
	private boolean isDead(Player p) {
		for (Obstacle o : this.obstacles) {
			if (o.getBounds().overlaps(p.getBounds())) {
				return true;
			}
		}
				
		boolean offRight = p.getBounds().x > unitsX + p.getBounds().radius + SPACE_OFFSET_X;
		boolean offLeft = p.getBounds().x < -p.getBounds().radius - SPACE_OFFSET_X;
		boolean offTop = p.getBounds().y > unitsY + p.getBounds().radius + SPACE_OFFSET_Y;
		boolean offBottom = p.getBounds().y < -p.getBounds().radius - SPACE_OFFSET_Y;
		
		return  offRight || offLeft || offTop || offBottom;
	}
	
	private void resetPlayer() {
		this.score = 0;
		this.player.setPosition(LevelManager.getStartPos());
		this.state = GameState.WAITING;
	}
	
	public GameState getGameState() {
		return this.state;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Array<Obstacle> getObstacles() {
		return obstacles;
	}
}
