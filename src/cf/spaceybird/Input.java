package cf.spaceybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class Input implements InputProcessor {
	
	private Vector2 mouse = new Vector2();
	private Vector2 mouseDelta = new Vector2();
	
	public Vector2 getMouse() {
		return new Vector2(this.mouse);
	}
	
	public Vector2 getMouseDelta() {
		return new Vector2(this.mouseDelta);
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
		//TODO implement what to do if ESC is pressed
		//if (character == '\u001B')
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
