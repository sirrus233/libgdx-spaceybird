package cf.spaceybird.screens;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cf.spaceybird.Assets;
import cf.spaceybird.Input;
import cf.spaceybird.LevelManager;
import cf.spaceybird.actors.Obstacle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class EditorScreen extends GameScreen {
	private final float DEFAULT_ICON_RADIUS = 0.5f;
	private final float DEFAULT_ICON_MASS = 1f;
	
	public enum EditorState {
		WAITING, PLACING_OBSTACLE, PLACING_PLAYER
	}
	
	private EditorState state;
	private Obstacle obstacleIcon;
	
	public EditorScreen(Game g) {
		super(g);
		this.state = EditorState.WAITING;		
		this.obstacleIcon = new Obstacle(new Vector2(Input.getMouseNorm()), DEFAULT_ICON_RADIUS, DEFAULT_ICON_MASS);
		LevelManager.setLevel(0);
	}

	@Override
	public void draw() {
		super.draw();
		
		switch (state) {	
		case PLACING_OBSTACLE:
			batch.setProjectionMatrix(gameCam.combined);
			batch.begin();
			if (obstacleIcon.getBounds().radius >= 1.5) {
				batch.draw(Assets.planetLarge, this.obstacleIcon.getBounds().x - this.obstacleIcon.getBounds().radius, 
						this.obstacleIcon.getBounds().y - this.obstacleIcon.getBounds().radius, 
						2*this.obstacleIcon.getBounds().radius, 2*this.obstacleIcon.getBounds().radius);
			} else if (obstacleIcon.getBounds().radius >= 1.0) {
				batch.draw(Assets.planetMedium, this.obstacleIcon.getBounds().x - this.obstacleIcon.getBounds().radius, 
						this.obstacleIcon.getBounds().y - this.obstacleIcon.getBounds().radius, 
						2*this.obstacleIcon.getBounds().radius, 2*this.obstacleIcon.getBounds().radius);
			} else {
				batch.draw(Assets.planetSmall, this.obstacleIcon.getBounds().x - this.obstacleIcon.getBounds().radius, 
						this.obstacleIcon.getBounds().y - this.obstacleIcon.getBounds().radius, 
						2*this.obstacleIcon.getBounds().radius, 2*this.obstacleIcon.getBounds().radius);			
			}
			batch.end();
			
			batch.setProjectionMatrix(fontCam.combined);
			batch.begin();
			Assets.font.draw(batch, "Placing Obstacle", Gdx.graphics.getWidth()-15*ppuX, Gdx.graphics.getHeight()-0.3f*ppuY);
			batch.end();
			
			if (debug) {
				//Draw bounding boxes
				debugRenderer.setProjectionMatrix(gameCam.combined);
		        debugRenderer.begin(ShapeType.Line);
		        debugRenderer.setColor(1, 0, 0, 1);
		        debugRenderer.circle(this.obstacleIcon.getBounds().x, this.obstacleIcon.getBounds().y, this.obstacleIcon.getBounds().radius, 1200);  	
		        debugRenderer.end();
			}
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
		if (this.state == EditorState.WAITING) { 
			super.update(delta);	
		}
		
		if (super.getGameState() == GameState.WAITING) {
			if (Input.keys['o']) {
				this.state = EditorState.PLACING_OBSTACLE;
			} 
			else if (Input.keys['p']) {
				this.state = EditorState.PLACING_PLAYER;
			}
			else if (Input.keys['s']) {				
				saveLevel();
			}
		}
		
		switch (this.state) {
		case WAITING:
			break;
			
		case PLACING_PLAYER:
			super.getPlayer().setPosition(Input.getMouseNorm());
			if (Input.buttonsClicked[Input.LEFT]) {
				LevelManager.setStartPos(Input.getMouseNorm());
				this.state = EditorState.WAITING;
			}
			if (Input.keys[Input.ESC]) {
				super.getPlayer().setPosition(LevelManager.getStartPos());
				this.state = EditorState.WAITING;
			}
			break;
			
		case PLACING_OBSTACLE:
			this.obstacleIcon.setPosition(Input.getMouseNorm());
			
			if (Input.mouseScrollDirection > 0) {
				this.obstacleIcon.getBounds().radius -= 0.1f;
			} else if (Input.mouseScrollDirection < 0) {
				this.obstacleIcon.getBounds().radius += 0.1f;	
			}
			
			if (Input.buttonsClicked[Input.LEFT]) {
				super.getObstacles().add(new Obstacle(obstacleIcon));
			}
			
			if (Input.keys['0']) {
				super.getObstacles().clear();
				this.state = EditorState.WAITING;
			}
			
			if (Input.keys[Input.ESC]) {
				this.obstacleIcon.getBounds().radius = DEFAULT_ICON_RADIUS;
				this.state = EditorState.WAITING;
			}
			break;
			
		default:
			System.out.println("Error: Invalid state accessed from EditorScreen!");
		}
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
