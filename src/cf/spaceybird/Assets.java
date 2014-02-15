package cf.spaceybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture gameTextures;
	public static TextureRegion background;
	public static TextureRegion spaceyBird;
	public static TextureRegion planetLarge;
	public static TextureRegion planetMedium;
	public static TextureRegion planetSmall;
	public static TextureRegion satellite;
	public static TextureRegion asteroid;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		gameTextures = loadTexture("assets/spaceybird.png");
		background = new TextureRegion(background, 910, 30, 1600, 1200);
		background = new TextureRegion(spaceyBird, 840, 1290, 640, 350);
		background = new TextureRegion(planetLarge, 110, 530, 670, 590);
		background = new TextureRegion(planetMedium, 1540, 1260, 590, 570);
		background = new TextureRegion(planetSmall, 420, 1300, 320, 310);
		background = new TextureRegion(satellite, 660, 110, 190, 380);
		background = new TextureRegion(asteroid, 2220, 1350, 170, 190);
	}
}
