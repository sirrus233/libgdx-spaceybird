package cf.spaceybird;

import cf.spaceybird.screens.AbstractScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class Input implements InputProcessor {
	//Constants for buttons
	public static final char LEFT = 0;
	public static final char RIGHT = 1;
	public static final char CENTER = 2;
	
	//Unicode char constants, for comparison in keyTyped method
	public static final char ESC = '\u001B';
	//public static final char O = '\u006F';
	//public static final char P = '\u0070';
	//public static final char R = '\u0072';
	//public static final char S = '\u0073';
	
	//Mouse vectors
	private static Vector2 mouse = new Vector2();
	private static Vector2 mouseDelta = new Vector2();
	private static Vector2 mouseNorm = new Vector2();
	private static Vector2 mouseDeltaNorm = new Vector2();
	
	//Array of inputs, available for rest of program to query
	public static boolean[] keys = new boolean[128];
	
	public static boolean[] buttonsDown = new boolean[8];
	public static boolean[] buttonsClicked = new boolean[8];
	
	public static Vector2 getMouse() {
		return new Vector2(mouse);
	}
	
	public static Vector2 getMouseDelta() {
		return new Vector2(mouseDelta);
	}
	
	public static Vector2 getMouseNorm() {
		return new Vector2(mouseNorm);
	}
	
	public static Vector2 getMouseDeltaNorm() {
		return new Vector2(mouseDeltaNorm);
	}
	
	public static void clear() {
		//mouseDelta vectors need to be reset on clear, since this is the only way to "auto-clear" them
		//in the even that no input is recieved
		mouseDelta.set(0, 0);
		mouseDeltaNorm.set(0, 0);
		
		for (int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
		
		for (int i = 0; i < buttonsClicked.length; i++) {
			buttonsClicked[i] = false;
		}
	}
	
	public void updateMouse(int screenX, int screenY) {
		//mouseDelta is based on the previous mouse position, so must be calculated before
		//the mouse vector is changed
		mouseDelta.set(screenX - getMouse().x, Gdx.graphics.getHeight() - screenY - getMouse().y);
		mouse.set(screenX, Gdx.graphics.getHeight() - screenY);
						
		//mouse vectors, normalized to work in terms of screen units instead of pixels
		mouseDeltaNorm.set(getMouseDelta().scl(1/AbstractScreen.ppuX,1/AbstractScreen.ppuY));
		mouseNorm.set(getMouse().scl(1/AbstractScreen.ppuX,1/AbstractScreen.ppuY));
	}
	
	@Override
	public boolean keyTyped(char character) {		
		keys[character] = true;
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		buttonsDown[button] = true;		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//If the mouse button is currently down, set it as released, and set the bit in 
		//the buttonsClicked array to show we have registered a click
		if (buttonsDown[button]) {
			buttonsClicked[button] = true;
			buttonsDown[button] = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		updateMouse(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		updateMouse(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

}
