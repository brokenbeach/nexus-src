package ResourceStores;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteStore {
	
	//Singleton
	private static SpriteStore store = new SpriteStore();
	//Map from reference to sprite
	private HashMap<String, BufferedImage> reference = new HashMap<String, BufferedImage>();

	SpriteStore() {	
		try {	
			// tilesets
			reference.put("tileset0", ImageIO.read( getClass().getResource("/res/img/tileset0.png")) );
			reference.put("tileset1", ImageIO.read( getClass().getResource("/res/img/tileset1.png")) );
			reference.put("tileset2", ImageIO.read( getClass().getResource("/res/img/tileset2.png")) );
			reference.put("tileset3", ImageIO.read( getClass().getResource("/res/img/tileset3.png")) );
			reference.put("tileset4", ImageIO.read( getClass().getResource("/res/img/tileset4.png")) );

			// sprites
			reference.put("player", ImageIO.read( getClass().getResource("/res/img/playersheet.png")) );
			reference.put("playerstill", ImageIO.read( getClass().getResource("/res/img/player.png")) );
			reference.put("flyingenemy0", ImageIO.read( getClass().getResource("/res/img/flyingenemy0.png")) );
			reference.put("flyingenemy0hit", ImageIO.read( getClass().getResource("/res/img/flyingenemy0hit.png")) );
			reference.put("flyingenemy1", ImageIO.read( getClass().getResource("/res/img/flyingenemy1.png")) );
			reference.put("flyingenemy1hit", ImageIO.read( getClass().getResource("/res/img/flyingenemy1hit.png")) );
			reference.put("flyingenemy2", ImageIO.read( getClass().getResource("/res/img/flyingenemy2.png")) );
			reference.put("flyingenemy2hit", ImageIO.read( getClass().getResource("/res/img/flyingenemy2hit.png")) );
			reference.put("health", ImageIO.read( getClass().getResource("/res/img/healthsheet.png")) );
			reference.put("gun", ImageIO.read( getClass().getResource("/res/img/gun.png")) );
			reference.put("boss", ImageIO.read( getClass().getResource("/res/img/boss.png")) );
			reference.put("bosshit", ImageIO.read( getClass().getResource("/res/img/bosshit.png")) );

			// backgrounds
			reference.put("bg0", ImageIO.read( getClass().getResource("/res/bg/bg0.png")) );
			reference.put("bg1", ImageIO.read( getClass().getResource("/res/bg/bg1.png")) );
			reference.put("bg2", ImageIO.read( getClass().getResource("/res/bg/bg2.png")) );
			reference.put("bg3", ImageIO.read( getClass().getResource("/res/bg/bg3.png")) );
			reference.put("bg4", ImageIO.read( getClass().getResource("/res/bg/bg4.png")) );
			reference.put("bg5", ImageIO.read( getClass().getResource("/res/bg/bg5.png")) );
			reference.put("bg6", ImageIO.read( getClass().getResource("/res/bg/bg6.png")) );

			reference.put("tree0", ImageIO.read( getClass().getResource("/res/img/tree0.png")) );

			// misc entities
			reference.put("bullet", ImageIO.read( getClass().getResource("/res/img/bullet.png")) );
			reference.put("enemybullet", ImageIO.read( getClass().getResource("/res/img/enemybullet.png")) );
			reference.put("missile", ImageIO.read( getClass().getResource("/res/img/missile.png")) );
			reference.put("artifact", ImageIO.read( getClass().getResource("/res/img/artifact.png")) );
			// planets
			reference.put("planet0", ImageIO.read( getClass().getResource("/res/img/planet0.png")) );
			reference.put("planet1", ImageIO.read( getClass().getResource("/res/img/planet1.png")) );
			reference.put("planet2", ImageIO.read( getClass().getResource("/res/img/planet2.png")) );
			reference.put("planet3", ImageIO.read( getClass().getResource("/res/img/planet3.png")) );
			reference.put("planet4", ImageIO.read( getClass().getResource("/res/img/planet4.png")) );
			reference.put("finalPlanet", ImageIO.read( getClass().getResource("/res/img/finalPlanet.png")) );
			reference.put("clouds", ImageIO.read( getClass().getResource("/res/img/clouds.png")) );
			// misc other
			reference.put("title", ImageIO.read( getClass().getResource("/res/img/title.png")) );
			reference.put("cursor", ImageIO.read( getClass().getResource("/res/img/cursor.png")) );
			reference.put("timebar", ImageIO.read( getClass().getResource("/res/img/timebar.png")) );
			reference.put("excavated", ImageIO.read( getClass().getResource("/res/img/excavated.png")) );
			reference.put("move", ImageIO.read( getClass().getResource("/res/img/move.png")) );
			reference.put("beamup", ImageIO.read( getClass().getResource("/res/img/beamup.png")) );
			reference.put("shoot", ImageIO.read( getClass().getResource("/res/img/shoot.png")) );
			reference.put("uncover", ImageIO.read( getClass().getResource("/res/img/uncover.png")) );
			reference.put("unable", ImageIO.read( getClass().getResource("/res/img/unable.png")) );
			reference.put("thankyou", ImageIO.read( getClass().getResource("/res/img/thankyou.png")) );
			reference.put("nexus", ImageIO.read( getClass().getResource("/res/img/nexus.png")) );
		} 
		catch ( IOException e ) {
			System.err.println(e);
		}	
	}
	
	public static SpriteStore get()	{
		return store;
	}

	public BufferedImage getImage( String ref ) {
		return reference.get( ref );
	}	
}