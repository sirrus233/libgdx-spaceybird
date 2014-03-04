package cf.spaceybird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import cf.spaceybird.screens.MenuScreen;
import cf.spaceybird.screens.ScreenTemplate;

public class SpaceyBird extends Game {

	@Override
	public void create() {
		ScreenTemplate.init();
		Assets.load();
		Gdx.input.setInputProcessor(new Input());
		setScreen(new MenuScreen(this));
	}
}
