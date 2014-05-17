package cf.spaceybird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import cf.spaceybird.screens.MenuScreen;
import cf.spaceybird.screens.AbstractScreen;

public class SpaceyBird extends Game {

	@Override
	public void create() {
		AbstractScreen.init();
		Assets.load();
		Gdx.input.setInputProcessor(new Input());
		setScreen(new MenuScreen(this));
	}
}
