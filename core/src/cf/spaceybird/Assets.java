package cf.spaceybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	
	public static BitmapFont font;
	
	public static Music music;
		
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		gameTextures = loadTexture("spaceybird.png");
		background = new TextureRegion(gameTextures, 910, 30, 1600, 1200);
		spaceyBird = new TextureRegion(gameTextures, 840, 1290, 640, 350);
		planetLarge = new TextureRegion(gameTextures, 5, 490, 758, 666);
		planetMedium = new TextureRegion(gameTextures, 1540, 1260, 590, 570);
		planetSmall = new TextureRegion(gameTextures, 420, 1300, 320, 310);
		satellite = new TextureRegion(gameTextures, 660, 110, 190, 380);
		asteroid = new TextureRegion(gameTextures, 2220, 1350, 170, 190);
		
		font = new BitmapFont(Gdx.files.internal("cartoon_font.fnt"));
		
		music = Gdx.audio.newMusic(Gdx.files.internal("spaceybird.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		music.play();
	}
}
