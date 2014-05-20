package cf.spaceybird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import cf.spaceybird.SpaceyBird;

public class DesktopLauncher {
	public static final int APP_WIDTH = 1280;
	public static final int APP_HEIGHT = 720;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = APP_WIDTH;
		config.height = APP_HEIGHT;
		new LwjglApplication(new SpaceyBird(), config);
	}
}