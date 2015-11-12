package ResourceStores;
import java.util.HashMap;

public class SoundStore {

	//Singleton
	private static SoundStore store = new SoundStore();
	//Map from reference to sprite
	private HashMap<String, AudioPlayer> reference = new HashMap<String, AudioPlayer>();

	SoundStore() {	
		// tilesets
		reference.put("beamup", new AudioPlayer("/res/sfx/beamup.mp3"));	
		reference.put("changeSelection", new AudioPlayer("/res/sfx/changeSelection.mp3"));	
		reference.put("collection", new AudioPlayer("/res/sfx/collection.mp3"));	
		reference.put("enemyHit", new AudioPlayer("/res/sfx/enemyHit.mp3"));	
		reference.put("enemyShoot", new AudioPlayer("/res/sfx/enemyShoot.mp3"));	
		reference.put("explode", new AudioPlayer("/res/sfx/explode.mp3"));	
		reference.put("land", new AudioPlayer("/res/sfx/land.mp3"));	
		reference.put("playerHit", new AudioPlayer("/res/sfx/playerhit.mp3"));	
		reference.put("selection", new AudioPlayer("/res/sfx/selection.mp3"));	
		reference.put("shoot", new AudioPlayer("/res/sfx/shoot.mp3"));	
		reference.put("shoot0", new AudioPlayer("/res/sfx/shoot0.mp3"));	
		reference.put("shoot1", new AudioPlayer("/res/sfx/shoot1.mp3"));	
		reference.put("tileDamaged", new AudioPlayer("/res/sfx/tiledamaged.mp3"));
	}
	
	public static SoundStore get()	{
		return store;
	}

	public AudioPlayer getSound( String ref ) {
		return reference.get( ref );
	}	
}
