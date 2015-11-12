package StateManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entities.Artifact;
import Entities.FlyingEnemy;
import Entities.FlyingEnemyMissiles;
import Entities.Player;
import Main.GamePanel;
import ResourceStores.SpriteStore;
import TileMap.Tile;
import TileMap.TileMap;

public class Level0State extends LevelState {

	BufferedImage tree0;
	
	public Level0State(StateManager sm) {
		super(sm);
		tilemap = new TileMap();
		tilemap.loadTiles("tileset0");
		tilemap.loadMap("/res/maps/map0.csv");
		player = new Player(this, 56, 8, 0, 0, 0.5, 0.2, tilemap);
		player.setFacingRight(true);
		artifact = new Artifact(this, 20.5 * Tile.TILESIZE, 13 * Tile.TILESIZE, 0, 0, 0, 0, tilemap);
		darkFlash(1.0f);
		
		counter = 20;
		currentCount = 0;
		
		tree0 = SpriteStore.get().getImage("tree0");
	}

	@Override
	public void init() {
		
	}
	
	public boolean tick() {
		if( won ) {
			sm.win(0);
		}
		super.tick();
		if( player.artifactCollected ) {
			if( flyingEnemyDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - flyingEnemyStartTime) / 1000000;
			if(elapsed > flyingEnemyDelay) {
				int rand = (int) (Math.random() * 3);
				switch(rand) {
				case 0:
					flyingEnemies.add( new FlyingEnemyMissiles(this, Math.random() * GamePanel.WIDTH, -16, 0, 0, 0, 0, tilemap) );
					break;
				case 1:
				case 2:
					flyingEnemies.add( new FlyingEnemy(this, Math.random() * GamePanel.WIDTH, -16, 0, 0, 0, 0, tilemap) );
					break;
				}
				flyingEnemyDelay = 500;
				flyingEnemyDelay += (int) ( 1000 * (Math.random()));
				flyingEnemyStartTime = System.nanoTime();
			}
		}
		return false;
	}
	
	public void render( Graphics2D g) {

		g.drawImage(tree0, 414, 232, null);
		g.drawImage(tree0, 36, 235, -tree0.getWidth(), tree0.getHeight(), null);

		g.drawImage(tree0, 42, 232, null);
		super.render(g);
	}

	//..............................................
	
	

}
