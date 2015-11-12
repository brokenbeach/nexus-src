package StateManager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entities.Artifact;
import Entities.FlyingEnemy;
import Entities.FlyingEnemyMissiles;
import Entities.FlyingEnemyShoots;
import Entities.Player;
import Main.GamePanel;
import ResourceStores.SpriteStore;
import TileMap.Tile;
import TileMap.TileMap;

public class Level3State extends LevelState {
	
	BufferedImage tree0;
	
	public Level3State(StateManager sm) {
		super(sm);
		tilemap = new TileMap();
		tilemap.loadTiles("tileset2");
		tilemap.loadMap("/res/maps/map3.csv");
		player = new Player(this, 50, 8, 0, 0, 0.5, 0.2, tilemap);
		player.setFacingRight(true);
		artifact = new Artifact(this, 14.5 * Tile.TILESIZE, 21 * Tile.TILESIZE, 0, 0, 0, 0, tilemap);
		darkFlash(1.0f);
		movementDelay = 400;
		counter = 20;
		currentCount = 0;
		flyingEnemyDelay = 800;
		tree0 = SpriteStore.get().getImage("tree0");
	}
	
	public boolean tick() {
		if( won ) {
			sm.win(3);
		}
		super.tick();
		if( player.artifactCollected ) {
			if( flyingEnemyDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - flyingEnemyStartTime) / 1000000;
			if(elapsed > flyingEnemyDelay) {
				int rand = (int) (Math.random() * 5);
				switch(rand) {
				case 0:
				case 1:
				case 2:
					flyingEnemies.add( new FlyingEnemyMissiles(this, Math.random() * GamePanel.WIDTH, -16, 0, 0, 0, 0, tilemap) );
					break;
				case 3:
					flyingEnemies.add( new FlyingEnemy(this, Math.random() * GamePanel.WIDTH, -16, 0, 0, 0, 0, tilemap) );
					break;
				case 4:
					int leftright = (int) (Math.random() * 2);
					flyingEnemies.add( new FlyingEnemyShoots(this, leftright * GamePanel.WIDTH, 340, 0, 0, 0, 0, tilemap) );
					break;
				}
				flyingEnemyDelay = 800;
				flyingEnemyDelay += (int) ( 1200 * (Math.random()));
				flyingEnemyStartTime = System.nanoTime();
			}
		}
		return false;
	}
	
	public void render( Graphics2D g) {
		g.drawImage(tree0, 36, 89, -tree0.getWidth(), tree0.getHeight(), null);
		g.drawImage(tree0, 56, 93, -tree0.getWidth(), tree0.getHeight(), null);
		g.drawImage(tree0, 45, 87, null);
		g.drawImage(tree0, 130, 86, -tree0.getWidth(), tree0.getHeight(), null);
		g.drawImage(tree0, 414, 88, null);
		g.drawImage(tree0, 414, 96, -tree0.getWidth(), tree0.getHeight(), null);


		super.render(g);
	}
}
