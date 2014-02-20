package cf.spaceybird;

import com.badlogic.gdx.Game;

import cf.spaceybird.screens.MenuScreen;
import cf.spaceybird.screens.ScreenTemplate;

public class SpaceyBird extends Game {

	@Override
	public void create() {
		ScreenTemplate.init();
		Assets.load();
		setScreen(new MenuScreen(this));
	}
}
