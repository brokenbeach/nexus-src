package StateManager;

import Entities.Artifact;
import Entities.FlyingEnemy;
import Entities.FlyingEnemyMissiles;
import Entities.FlyingEnemyShoots;
import Entities.Player;
import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Level2State extends LevelState {

	public Level2State(StateManager sm) {
		super(sm);
		tilemap = new TileMap();
		tilemap.loadTiles("tileset1");
		tilemap.loadMap("/res/maps/map2.csv");
		player = new Player(this, 424, 8, 0, 0, 0.3, 0.1, tilemap);
		artifact = new Artifact(this, 5 * Tile.TILESIZE, 3.5 * Tile.TILESIZE, 0, 0, 0, 0, tilemap);
		darkFlash(1.0f);
		movementDelay = 1100;
		counter = 20;
		currentCount = 0;
		flyingEnemyDelay = 700;
	}
	
	public boolean tick() {
		if( won ) {
			sm.win(2);
		}
		super.tick();
		if( player.artifactCollected ) {
			if( flyingEnemyDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - flyingEnemyStartTime) / 1000000;
			if(elapsed > flyingEnemyDelay) {
				int rand = (int) (Math.random() * 4);
				switch(rand) {
				case 0:
				case 1:
					flyingEnemies.add( new FlyingEnemyMissiles(this, Math.random() * GamePanel.WIDTH, -16, 0, 0, 0, 0, tilemap) );
					break;
				case 2:
					flyingEnemies.add( new FlyingEnemy(this, Math.random() * GamePanel.WIDTH, -16, 0, 0, 0, 0, tilemap) );
					break;
				case 3:
					int leftright = (int) (Math.random() * 2);
					flyingEnemies.add( new FlyingEnemyShoots(this, leftright * GamePanel.WIDTH, 340, 0, 0, 0, 0, tilemap) );
					break;
				}
				flyingEnemyDelay = 700;
				flyingEnemyDelay += (int) ( 1000 * (Math.random()));
				flyingEnemyStartTime = System.nanoTime();
			}
		}
		return false;
	}

}
