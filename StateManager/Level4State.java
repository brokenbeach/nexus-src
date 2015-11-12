package StateManager;

import Entities.Artifact;
import Entities.FlyingEnemy;
import Entities.FlyingEnemyShoots;
import Entities.Player;
import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Level4State extends LevelState {
	public Level4State(StateManager sm) {
		super(sm);
		tilemap = new TileMap();
		tilemap.loadTiles("tileset3");
		tilemap.loadMap("/res/maps/map4.csv");
		player = new Player(this, 50, 8, 0, 0, 0.5, 0.2, tilemap);
		player.setFacingRight(true);
		artifact = new Artifact(this, 15 * Tile.TILESIZE, 11 * Tile.TILESIZE, 0, 0, 0, 0, tilemap);
		darkFlash(1.0f);
		movementDelay = 400;
		counter = 20;
		currentCount = 0;
		flyingEnemyDelay = 1100;
	}
	
	public boolean tick() {
		if( won ) {
			sm.win(4);
		}
		super.tick();
		if( player.artifactCollected ) {
			if( flyingEnemyDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - flyingEnemyStartTime) / 1000000;
			if(elapsed > flyingEnemyDelay) {
				int rand = (int) (Math.random() * 3);

				int leftright = (int) (Math.random() * 2);
				switch(rand) {
				case 0:
				case 1:
					flyingEnemies.add( new FlyingEnemy(this, leftright * GamePanel.WIDTH, Math.random() * GamePanel.HEIGHT, 0, 0, 0, 0, tilemap) );
					break;
				case 2:
					flyingEnemies.add( new FlyingEnemyShoots(this, leftright * GamePanel.WIDTH, Math.random() * GamePanel.HEIGHT, 0, 0, 0, 0, tilemap) );
					break;
				}
				flyingEnemyDelay = 1100;
				flyingEnemyDelay += (int) ( 1200 * (Math.random()));
				flyingEnemyStartTime = System.nanoTime();
			}
		}
		return false;
	}
}
