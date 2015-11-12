package StateManager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entities.Artifact;
import Entities.FlyingEnemy;
import Entities.FlyingEnemyShoots;
import Entities.Player;
import Main.GamePanel;
import ResourceStores.SpriteStore;
import TileMap.Tile;
import TileMap.TileMap;

public class Level1State extends LevelState{

	BufferedImage tree0;
	
	public Level1State(StateManager sm) {
		super(sm);
		tilemap = new TileMap();
		tilemap.loadTiles("tileset0");
		tilemap.loadMap("/res/maps/map1.csv");
		player = new Player(this, 220, 8, 0, 0, 0.5, 0.2, tilemap);
		artifact = new Artifact(this, 15 * Tile.TILESIZE, 23.5 * Tile.TILESIZE, 0, 0, 0, 0, tilemap);
		darkFlash(1.0f);
		
		counter = 16;
		currentCount = 0;
		
		tree0 = SpriteStore.get().getImage("tree0");
	}
	
	public boolean tick() {
		if( won ) {
			sm.win(1);
		}
		super.tick();
		if( player.artifactCollected ) {
			if( flyingEnemyDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - flyingEnemyStartTime) / 1000000;
			if(elapsed > flyingEnemyDelay) {
				int rand = (int) (Math.random() * 3);
				switch(rand) {
				case 0:
				case 1:
					flyingEnemies.add( new FlyingEnemy(this, Math.random() * GamePanel.WIDTH, -16, 0, 0, 0, 0, tilemap) );
					break;
				case 2:
					int leftright = (int) (Math.random() * 2);
					flyingEnemies.add( new FlyingEnemyShoots(this, leftright * GamePanel.WIDTH, 340, 0, 0, 0, 0, tilemap) );
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
		g.drawImage(tree0, 36, 137, -tree0.getWidth(), tree0.getHeight(), null);
		g.drawImage(tree0, 42, 118, null);
		g.drawImage(tree0, 112, 122, -tree0.getWidth(), tree0.getHeight(), null);
		
		g.drawImage(tree0, 208, 102, null);
		g.drawImage(tree0, 182, 108, null);
		g.drawImage(tree0, 272, 110, -tree0.getWidth(), tree0.getHeight(), null);

		g.drawImage(tree0, 380, 124, -tree0.getWidth(), tree0.getHeight(), null);		
		g.drawImage(tree0, 424, 134, null);
		super.render(g);
	}

}
