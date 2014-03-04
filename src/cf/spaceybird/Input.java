package cf.spaceybird;

import cf.spaceybird.screens.ScreenTemplate;

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
	
	//Mouse vectors
	private static Vector2 mouse = new Vector2();
	private static Vector2 mouseDelta = new Vector2();
	private static Vector2 mouseNorm = new Vector2();
	private static Vector2 mouseDeltaNorm = new Vector2();
	
	//Array of keyboard inputs
	public static boolean[] keys = new boolean[128];
	public static boolean[] buttons = new boolean[8];
	
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
		for (int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
	}

	@Override
	public boolean keyTyped(char character) {		
		keys[character] = true;
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		buttons[button] = true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		buttons[button] = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouse.set(screenX, Gdx.graphics.getHeight() - screenY);
		mouseDelta.set(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
		mouseNorm.set(getMouse().div(ScreenTemplate.ppuX,ScreenTemplate.ppuY));
		mouseDeltaNorm.set(getMouseDelta().div(ScreenTemplate.ppuX,ScreenTemplate.ppuY));
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
