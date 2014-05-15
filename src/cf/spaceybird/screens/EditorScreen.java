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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class EditorScreen extends GameScreen {
	private final int PATH_LENGTH = 1024*2;
	private final int MAX_PATHS = 10;
	
	private enum State {
		PLACING_OBSTACLE, PLACING_PLAYER, PLACED
	}
	
	private boolean editing;
	private State state;
	private Player playerPredict;
	private ArrayList<ArrayList<Vector2>> pathHistory;
	private ArrayList<Vector2> pathTrace;
	private float[][][] pathColor; //TODO
	private ArrayList<Vector2> predictPath;	
	private long predictDelay;
	
	public EditorScreen(Game g) {
		super(g);
		
		this.editing = false;
		this.state = super.readOnlyState;
		this.playerPredict = LevelManager.getPlayerPredict();
		this.pathHistory = new ArrayList<ArrayList<Vector2>>(MAX_PATHS);
		this.newPathTrace(); //PT		
		this.pathHistory.add(pathTrace);
		this.pathColor = new float[MAX_PATHS][MAX_PATHS][MAX_PATHS];
		this.predictPath = new ArrayList<Vector2>(1024);
		this.predictDelay = 0;
		
		LevelManager.setLevel(0);
	}

	@Override
	public void draw() {
		super.draw();
		
		if (DEBUG) {
			debugRenderer.setProjectionMatrix(gameCam.combined);
	        debugRenderer.begin(ShapeType.Line);
	       
	        //Draw historical pathing
	        debugRenderer.setColor(0, 0.7f, 0.3f, 1);
	        for (ArrayList<Vector2> path : this.pathHistory){	        	
	        	for (int i = 1; i < path.size(); i++) {
	        		debugRenderer.line(path.get(i-1), path.get(i));
	        	}   
	        }
	        
	        //Draw predictive pathing
	        debugRenderer.setColor(0.8f, 0.7f, 0, 1);
        	for (int i = 1; i < predictPath.size(); i++) {
        		debugRenderer.line(predictPath.get(i-1), predictPath.get(i));
        	}   
	        
	        debugRenderer.end();
		}
		
		switch(state)
		{	
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
		super.update(delta);
		
		switch(readOnlyState) {
		case WAITING:
			this.predictDelay = System.currentTimeMillis();
			
			if (Gdx.input.isKeyPressed(Input.Keys.O)) {
				this.state = State.PLACING_OBSTACLE;
			} 
			else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
				this.state = State.PLACING_PLAYER;
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.R)) {				
				resetBoard();
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.S)) {				
				saveLevel();
				System.out.println("pressed S");
			}
			break;
			
		case AIMING:
			System.out.println("pathPredict array size: " + predictPath.size() + ", Timestep: " + delta);
			System.out.println("Time diff: " +(System.currentTimeMillis() - predictDelay));
			
			//Begin path prediction calculations			
			if((System.currentTimeMillis() - predictDelay) > 500){
				this.predictDelay = System.currentTimeMillis();				
				this.playerPredict.ready(this.player.getPosition());
				this.predictPath.clear();
				Vector2 launch = new Vector2(LevelManager.getStartPos()).sub(this.player.getPosition());
				this.playerPredict.setVelocity(launch.scl(LAUNCH_FORCE_SCALE));
								
				for (int i = 0; i <1024 ; i++)
				{
					Vector2 gravForce = new Vector2();
					for (Obstacle o : this.obstacles) {						
						gravForce.add(PhysicsEngine.getGravForce(this.playerPredict, o));
					}					
					this.playerPredict.setAcceleration(PhysicsEngine.getAcceleration(this.playerPredict.getMass(), gravForce));
					this.playerPredict.setVelocity(PhysicsEngine.getVelocity(this.playerPredict.getVelocity(), this.playerPredict.getAcceleration(), .015f));
					predictPath.add(this.playerPredict.updatePosition(this.playerPredict.getVelocity().scl(.015f)));//XXX REFRESH RATE ISSUE					
				}				
			}
			break;
			
		case LAUNCHED:			
			if (Gdx.input.isKeyPressed(Input.Keys.R)) {				
				resetBoard();
				this.state = State.WAITING;
			}
			break;
			
		case VICTORY:
			resetPlayer();
			break;
			
		case PLACING_PLAYER:
			player.setPosition(this.mouseNorm);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				LevelManager.setStartPos(this.mouseNorm);
				this.state = State.PLACED;
			}
			break;
			
		case PLACED:			
			if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)){				
				this.state = State.WAITING;
			}
			break;
			
		case PLACING_OBSTACLE:
			
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
	
	private void resetBoard() {
		this.score = 0;
		pathHistory.clear();
		newPathTrace();
		this.player.setPosition(LevelManager.getStartPos());		
		predictPath.clear();
		this.state = State.WAITING;
	}
	
	private void newPathTrace() { //PT
		if (pathHistory.size() >= MAX_PATHS ){
			pathHistory.remove(1);	
		}
		//Random rn = new Random();
		
		pathTrace = new ArrayList<Vector2>(PATH_LENGTH);
		pathHistory.add(pathTrace);
	}
	
	private void incrementPath() {
		oldPosition = this.player.getPosition();
		if (pathTrace.size() < PATH_LENGTH ){
			pathTrace.add(oldPosition);	
		}
	}
	
	//FIXME
	private void saveLevel() {
		DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy-HH:mm:ss");
		Date date = new Date();
		String myDate = dateFormat.format(date);
		File f = new File("D:\\Mason\\SBSaves\\SpaceyBirdLevel-" + myDate + ".txt");;
		
		try {
			
		    boolean created = f.createNewFile();
		    FileOutputStream os = new FileOutputStream(f);
		    OutputStreamWriter osw = new OutputStreamWriter(os);
		    Writer w = new BufferedWriter(osw);
			//Writer writer = new BufferedWriter(new OutputStreamWriter(
		     //     new FileOutputStream("SpaceyBirdLevel-" + myDate + ".txt")));
		    for (Obstacle o : this.obstacles) {
		    	w.write(o.getBounds().toString() +"|"+ o.getMass() + "$");				
			}
		    w.write(this.player.getPosition().toString());
		    w.flush();w.close();
		    System.out.println("Should print");
		} catch (IOException ex) {
			System.out.println("PROLEMS");
			System.out.println(f.toString());
			System.out.println(ex.getMessage());
		}		
	}

}
