package cf.spaceybird;

import com.badlogic.gdx.Game;
import cf.spaceybird.screens.GameScreen;

public class SpaceyBird extends Game {

	public SpaceyBird() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		Renderer.init();
		setScreen(new GameScreen());

	}
}
