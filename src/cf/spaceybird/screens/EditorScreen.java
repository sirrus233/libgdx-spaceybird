package cf.spaceybird.screens;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cf.spaceybird.Assets;
import cf.spaceybird.Input;
import cf.spaceybird.LevelManager;
import cf.spaceybird.PhysicsEngine;
import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;


public class EditorScreen extends GameScreen {
	private final int PATH_LENGTH = 1024*2;
	private final int MAX_PATHS = 10;
	
	private enum EditorState {
		WAITING, PLACING_OBSTACLE, PLACING_PLAYER
	}
	
	private Game game;
	private EditorState state;
	private Player playerPredict;
	private ArrayList<ArrayList<Vector2>> pathHistory;
	private ArrayList<Vector2> pathTrace;
	private float[][][] pathColor; //TODO
	private ArrayList<Vector2> predictPath;	
	
	public EditorScreen(Game g) {
		super(g);
		this.game = g;
		this.state = EditorState.WAITING;
		this.playerPredict = LevelManager.getPlayerPredict();
		this.pathHistory = new ArrayList<ArrayList<Vector2>>(MAX_PATHS);
		this.newPathTrace(); //PT		
		this.pathHistory.add(pathTrace);
		this.pathColor = new float[MAX_PATHS][MAX_PATHS][MAX_PATHS];
		this.predictPath = new ArrayList<Vector2>(1024);
		
		LevelManager.setLevel(0);
	}

	@Override
	public void draw() {
		super.draw();
		
		if (DEBUG) {
			debugRenderer.setProjectionMatrix(gameCam.combined);
	        debugRenderer.begin(ShapeType.Line);
	       
	        //Draw historical paths
	        debugRenderer.setColor(0, 0.7f, 0.3f, 1);
	        for (ArrayList<Vector2> path : this.pathHistory){	        	
	        	for (int i = 1; i < path.size(); i++) {
	        		debugRenderer.line(path.get(i-1), path.get(i));
	        	}   
	        }
	        
	        //Draw predictive paths
	        debugRenderer.setColor(0.8f, 0.7f, 0, 1);
        	for (int i = 1; i < predictPath.size(); i++) {
        		debugRenderer.line(predictPath.get(i-1), predictPath.get(i));
        	}   
	        
	        debugRenderer.end();
		}
		
		switch (state) {	
		case PLACING_OBSTACLE:
			batch.setProjectionMatrix(fontCam.combined);
			batch.begin();
			Assets.font.draw(batch, "Placing Obstacle", Gdx.graphics.getWidth()-15*ppuX, Gdx.graphics.getHeight()-0.3f*ppuY);
			batch.end();
			break;
			
		case PLACING_PLAYER:
			batch.setProjectionMatrix(fontCam.combined);
			batch.begin();
			Assets.font.draw(batch, "Placing Player", Gdx.graphics.getWidth()-15*ppuX, Gdx.graphics.getHeight()-0.3f*ppuY);
			batch.end();
			break;
		
		default:
			batch.setProjectionMatrix(fontCam.combined);
			batch.begin();
			Assets.font.draw(batch, "Press P or O", Gdx.graphics.getWidth()-15*ppuX, Gdx.graphics.getHeight()-0.3f*ppuY);
			batch.end();
			break;
		}
	}

	public void update(float delta) {				
		if (Input.keys[Input.ESC]) { this.game.setScreen(new MenuScreen(this.game)); }
		if (Input.keys['r']) { resetBoard(); }
		
		editUpdate();
		
		if (this.state == EditorState.WAITING) { 
			gameUpdate(delta);	
		}
	}
	
	private void editUpdate() {
		switch (this.state) {
		case WAITING:
			break;
			
		case PLACING_PLAYER:
			super.getPlayer().setPosition(Input.getMouseNorm());
			if (Input.buttonsClicked[Input.LEFT]){
				LevelManager.setStartPos(Input.getMouseNorm());
				this.state = EditorState.WAITING;
			}
			break;
			
		case PLACING_OBSTACLE:
			if (Input.keys['1']) {				
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 0.5f, 1));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['2']) {
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 0.5f, 2));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['3']) {
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 1f, 3));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['4']) {
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 1f, 4));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['5']) {
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 2f, 5));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['6']) {
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 2f, 6));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['7']) {
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 1f, 7));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['8']) {
				super.getObstacles().add(new Obstacle(new Vector2(Input.getMouseNorm()), 0.5f, 8));
				this.state = EditorState.WAITING;
			}
			else if (Input.keys['0']) {
				super.getObstacles().clear();
				this.state = EditorState.WAITING;
			}
			
			break;
			
		default:
			System.out.println("Error: Invalid state accessed from EditorScreen!");
		}
	}
	
	private void gameUpdate(float delta) {
		super.update(delta);
		
		switch (super.getGameState()) {
		case WAITING:
			if (Input.keys['o']) {
				this.state = EditorState.PLACING_OBSTACLE;
			} 
			else if (Input.keys['p']) {
				this.state = EditorState.PLACING_PLAYER;
			}
			else if (Input.keys['s']) {				
				saveLevel();
			}
			break;
			
		case AIMING:
			//Begin path prediction calculations
			this.predictPath.clear();
			this.playerPredict.setPosition(super.getPlayer().getPosition());
			
			Vector2 launch = new Vector2(LevelManager.getStartPos()).sub(super.getPlayer().getPosition());
			this.playerPredict.setVelocity(launch.scl(LAUNCH_FORCE_SCALE));
							
			while (!isDead(this.playerPredict)) {
				Vector2 gravForce = new Vector2();
				for (Obstacle o : super.getObstacles()) {						
					gravForce.add(PhysicsEngine.getGravForce(this.playerPredict, o));
				}
				
				this.playerPredict.setAcceleration(PhysicsEngine.getAcceleration(this.playerPredict.getMass(), gravForce));
				this.playerPredict.setVelocity(PhysicsEngine.getVelocity(this.playerPredict.getVelocity(), this.playerPredict.getAcceleration(), .015f));
				//XXX Why is this scaling factor necessary?
				predictPath.add(this.playerPredict.updatePosition(this.playerPredict.getVelocity().scl(.015f)));
				
				//This handles the corner case where if the player has no velocity after acceleration is applied, then there 
				//is an infinite loop condition because the player will never die. This generally happens in levels with no
				//planets.
				if (playerPredict.getVelocity().isZero()) { break; }
			}				
			break;
		
		case LAUNCHED:
			break;
			
		case VICTORY:
			newPathTrace();
			break;
			
		default:
			System.out.println("Error: Invalid state accessed from EditorScreen!");
		}
	}
	
	private void resetBoard() {
		newPathTrace();
		pathHistory.clear();
		predictPath.clear();
	}
	
	private void newPathTrace() { //PT
		if (pathHistory.size() >= MAX_PATHS ){
			pathHistory.remove(1);	
		}
		
		pathTrace = new ArrayList<Vector2>(PATH_LENGTH);
		pathHistory.add(pathTrace);
	}
	
	//FIXME
	private void saveLevel() {
		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy-HH:mm:ss");
		Date date = new Date();
		String myDate = dateFormat.format(date);
		File f = new File("D:\\Mason\\SBSaves\\SpaceyBirdLevel-" + myDate + ".txt");;
		
		try {
		    FileOutputStream os = new FileOutputStream(f);
		    OutputStreamWriter osw = new OutputStreamWriter(os);
		    Writer w = new BufferedWriter(osw);
			//Writer writer = new BufferedWriter(new OutputStreamWriter(
		     //     new FileOutputStream("SpaceyBirdLevel-" + myDate + ".txt")));
		    for (Obstacle o : super.getObstacles()) {
		    	w.write(o.getBounds().toString() +"|"+ o.getMass() + "$");				
			}
		    w.write(super.getPlayer().getPosition().toString());
		    w.flush();w.close();
		    System.out.println("Should print");
		} catch (IOException ex) {
			System.out.println("PROBLEMS");
			System.out.println(f.toString());
			System.out.println(ex.getMessage());
		}		
	}

}
