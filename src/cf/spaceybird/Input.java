package cf.spaceybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class Input implements InputProcessor {
	//Unicode char constants, for comparison in keyTyped method
	private final char ESC = '\u001B';
	
	//Mouse vectors
	private Vector2 mouse = new Vector2();
	private Vector2 mouseDelta = new Vector2();
	
	//Array of keyboard inputs
	public static boolean[] inputs = new boolean[128];
	
	public Vector2 getMouse() {
		return new Vector2(this.mouse);
	}
	
	public Vector2 getMouseDelta() {
		return new Vector2(this.mouseDelta);
	}
	
	public static void clear() {
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = false;
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {		
		if (character == ESC) {
			inputs[ESC] = true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		this.mouse.set(screenX, Gdx.graphics.getHeight() - screenY);
		this.mouseDelta.set(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
