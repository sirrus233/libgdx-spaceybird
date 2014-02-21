package cf.spaceybird.screens;

import java.util.ArrayList;

import cf.spaceybird.Assets;
import cf.spaceybird.LevelManager;
import cf.spaceybird.PhysicsEngine;
import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;
//import cf.spaceybird.screens.GameScreen.State;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LevelEditor extends ScreenTemplate {
private final float LAUNCH_FORCE_SCALE = 8;
private final int PATH_LENGTH = 1024;
private final int MAX_PATHS = 10;
	
	private enum State {
		WAITING, AIMING, LAUNCHED, VICTORY, PLACING
	}
	
	private Game game;
	private Player player;
	private Array<Obstacle> obstacles;
	private Circle goal;
	private State state;
	private Vector2 mouse;
	private Vector2 mouseDelta;
	private Vector2 mouseNorm;
	private Vector2 mouseDeltaNorm;
	private Vector2 oldPosition;
	ArrayList<Vector2> pathTrace;
	ArrayList<ArrayList<Vector2>> pathHistory;
	
	private int score;
	
	public LevelEditor(Game g) {
		// TODO Auto-generated constructor stub
		this.game = g;
		this.state = State.WAITING;
		this.player = LevelManager.getPlayer();
		this.obstacles = LevelManager.getObstacles();
		this.goal = LevelManager.getGoal();
		this.mouse = new Vector2();
		this.mouseDelta = new Vector2();
		this.mouseNorm = new Vector2();
		this.mouseDeltaNorm = new Vector2();
		this.score = 0;
		this.oldPosition = new Vector2(); //PT
		pathTrace = new ArrayList<Vector2>(PATH_LENGTH); //PT
		pathHistory = new ArrayList<ArrayList<Vector2>>(MAX_PATHS);
		pathHistory.add(pathTrace);
		
		LevelManager.setLevel(0);
	}

	@Override
	public void draw() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gameBatch.setProjectionMatrix(gameCam.combined);
		gameBatch.begin();
		gameBatch.disableBlending();
		gameBatch.draw(Assets.background, 0, 0, unitsX, unitsY);
		gameBatch.enableBlending();
		gameBatch.draw(Assets.spaceyBird, this.player.getBounds().x - this.player.getBounds().radius, 
				this.player.getBounds().y - this.player.getBounds().radius, 
				2*this.player.getBounds().radius, 2*this.player.getBounds().radius);
		for (Obstacle o : this.obstacles) {
			if (o.getBounds().radius >= 1.5) {
				gameBatch.draw(Assets.planetLarge, o.getBounds().x - o.getBounds().radius, 
						o.getBounds().y - o.getBounds().radius, 2*o.getBounds().radius, 2*o.getBounds().radius);	
		
			} else if (o.getBounds().radius >= 1.0) {
				gameBatch.draw(Assets.planetMedium, o.getBounds().x - o.getBounds().radius, 
						o.getBounds().y - o.getBounds().radius, 2*o.getBounds().radius, 2*o.getBounds().radius);
			} else {
				gameBatch.draw(Assets.planetSmall, o.getBounds().x - o.getBounds().radius, 
						o.getBounds().y - o.getBounds().radius, 2*o.getBounds().radius, 2*o.getBounds().radius);				
			}
		}
		gameBatch.draw(Assets.satellite, this.goal.x - this.goal.radius, this.goal.y - this.goal.radius, 
				2*this.goal.radius, 2*this.goal.radius);
		gameBatch.end();
		
		fontBatch.setProjectionMatrix(fontCam.combined);
		fontBatch.begin();
		Assets.font.draw(fontBatch, "Score:"+this.score, Gdx.graphics.getWidth()-3*ppuX, Gdx.graphics.getHeight()-0.3f*ppuY);
		fontBatch.end();
		
		if (DEBUG) {
			debugRenderer.setProjectionMatrix(gameCam.combined);
	        debugRenderer.begin(ShapeType.Line);
	        debugRenderer.setColor(new Color(1, 0, 0, 1));
	        debugRenderer.circle(this.player.getBounds().x, this.player.getBounds().y, this.player.getBounds().radius, 1200);
	        for (Obstacle o : this.obstacles) {
	        	debugRenderer.circle(o.getBounds().x, o.getBounds().y, o.getBounds().radius, 1200);
	        }
	        
	        for (ArrayList<Vector2> path : this.pathHistory){	        	
	        	for (int i = 1; i < path.size(); i++) {
	        		debugRenderer.line(path.get(i-1), path.get(i));
	        	}   
	        }	        
	        
	        debugRenderer.end();
		}
	}

	public void update(float delta) {
		this.mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		this.mouseDelta.set(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
		this.mouseNorm.set(new Vector2(mouse).div(ppuX,ppuY));
		this.mouseDeltaNorm.set(new Vector2(mouseDelta).div(ppuX,ppuY));
		
		switch(state) {
		case WAITING:
			System.out.println(obstacles.size);
			oldPosition = player.getPosition();
			
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && this.player.getBounds().contains(this.mouseNorm)) {
				this.state = State.AIMING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
				this.state = State.PLACING;
			}
			break;
			
		case AIMING:
			if (mouseNorm.dst(LevelManager.getStartPos()) < 2) {
				this.player.updatePosition(this.mouseDeltaNorm);
			} else {
				Vector2 newDirection = new Vector2(mouseNorm).sub(LevelManager.getStartPos()).nor().scl(2);
				Vector2 newPosition = new Vector2(LevelManager.getStartPos()).add(newDirection);
				this.player.setPosition(newPosition);
			}
			if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				Vector2 launch = new Vector2(LevelManager.getStartPos()).sub(this.player.getPosition());
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
			} else if (this.player.getBounds().x > unitsX*1.5 + this.player.getBounds().radius ||
					this.player.getBounds().x < -this.player.getBounds().radius - unitsX*1.5 ||
					this.player.getBounds().y > unitsY*1.5 + this.player.getBounds().radius ||
					this.player.getBounds().y < -this.player.getBounds().radius - unitsY*1.5) {
				resetPlayer();
			} else {
				incrementPath();
				this.player.setAcceleration(PhysicsEngine.getAcceleration(this.player.getMass(), gravForce));
				this.player.setVelocity(PhysicsEngine.getVelocity(this.player.getVelocity(), this.player.getAcceleration(), delta));
				this.player.updatePosition(this.player.getVelocity().scl(delta));
				this.score += this.player.getPosition().sub(oldPosition).len() *100;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.R)) {				
				resetPlayer();
				this.state = State.WAITING;
			}
			break;
			
		case VICTORY:
			resetPlayer();
			break;
			
		case PLACING:
			
			 if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {				
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 0.5f, 1));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 0.5f, 2));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 1f, 3));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 1f, 4));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 2f, 5));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 2f, 6));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 1f, 7));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
				obstacles.add(new Obstacle(new Vector2(this.mouseNorm), 0.5f, 8));
				this.state = State.WAITING;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
				obstacles.clear();
				this.state = State.WAITING;
			}
			
			break;
			
		default:
			System.out.println("Error: Invalid state accessed from GameScreen!");
		}
	}
	
	private void resetPlayer() {
		this.score = 0;
		newPathTrace();
		this.player.setPosition(LevelManager.getStartPos());
		this.state = State.WAITING;
	}
	
	private void newPathTrace() { //PT
		if (pathHistory.size() >= MAX_PATHS ){
			pathHistory.remove(1);	
		}		
		pathHistory.add(pathTrace);		
		pathTrace = new ArrayList<Vector2>(PATH_LENGTH);
		pathHistory.add(pathTrace);
	}
	
	private void incrementPath() {
		oldPosition = this.player.getPosition();
		if (pathTrace.size() < PATH_LENGTH ){
			pathTrace.add(oldPosition);	
		}
	}

}
