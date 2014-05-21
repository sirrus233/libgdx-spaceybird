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
import com.badlogic.gdx.math.Vector2;

public class EditorScreen extends GameScreen {
	
	public enum EditorState {
		WAITING, PLACING_OBSTACLE, PLACING_PLAYER
	}
	
	private EditorState state;
	
	public EditorScreen(Game g) {
		super(g);
		this.state = EditorState.WAITING;		
		
		LevelManager.setLevel(0);
	}

	@Override
	public void draw() {
		super.draw();
		
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
			if (Input.buttonsClicked[Input.LEFT]){
				LevelManager.setStartPos(Input.getMouseNorm());
				this.state = EditorState.WAITING;
			}
			if (Input.keys[Input.ESC]) {
				super.getPlayer().setPosition(LevelManager.getStartPos());
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
