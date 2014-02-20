package cf.spaceybird;

import com.badlogic.gdx.Game;

import cf.spaceybird.screens.MenuScreen;

public class SpaceyBird extends Game {

	public SpaceyBird() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		Setup.init();
		Assets.load();
		setScreen(new MenuScreen(this));
	}
}
