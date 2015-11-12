package StateManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import Entities.Artifact;
import Entities.Boss;
import Entities.Bullet;
import Entities.FlyingEnemy;
import Entities.FlyingEnemyMissiles;
import Entities.Player;
import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Level5State extends LevelState {
	
	public boolean bossSpawned = false;
	public Boss boss;
	
	public Level5State(StateManager sm) {
		super(sm);
		tilemap = new TileMap();
		tilemap.loadTiles("tileset4");
		tilemap.loadMap("/res/maps/map5.csv");
		player = new Player(this, 80, 8, 0, 0, 0.5, 0.2, tilemap);
		player.setFacingRight(true);
		artifact = new Artifact(this, 25 * Tile.TILESIZE, 13.5 * Tile.TILESIZE, 0, 0, 0, 0, tilemap);
		darkFlash(1.0f);
		movementDelay = 400;
		counter = 20;
		currentCount = 0;
		flyingEnemyDelay = 2000;
		boss = new Boss( this, (GamePanel.WIDTH - 100) / 2, -100, 0, 0, 0, 0, tilemap);
	}
	

	@Override
	public boolean tick() {
		if( won ) {
			sm.win(5);
		}
		if( !lost && !won ) {
			if( player.artifactCollected && boss.health.x <= 0) {
				won = true;
				player.beamUp();
			}
		}
		
		if( !canMove ) {
			if( movementDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - movementStartTime) / 1000000;
			if(elapsed > movementDelay) {
				player.enableMovement();
				movementStartTime = System.nanoTime();
			}
		}
		
		if( player.dead || lost || won) {
			darkAlpha += (1 - darkAlpha) * 0.04;
			if( !deadChecked ) {
				deadChecked = true;
				returnStartTime = System.nanoTime();
			}
			if( returnDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - returnStartTime) / 1000000;
			if(elapsed > returnDelay) {
				sm.setState(StateManager.MENU_STATE);
				returnStartTime = System.nanoTime();
			}
		} else {
			darkAlpha += ( 0 - darkAlpha ) * 0.02;
		}
		
		if( bossSpawned && !won ) {
			boss.tick();
			checkBossCollisions();
		}
		damageAlpha += ( 0 - damageAlpha ) * 0.05;
		checkPlayerBulletCollisions();
		tickList( bullets );
		tickList( enemyBullets );
		tickList( damageableBullets );
		tickList( flyingEnemies );
		tilemap.tick();
		artifact.tick();
		player.tick();
		removeBullets();
		removeMissiles();
		tickParticles();
		checkEnemyBulletCollisions();
		checkEnemyCollisions();
		if( !player.artifactCollected ) {
			checkArtifactCollection();
		}
		
		if( player.artifactCollected && !bossSpawned ) {
			bossSpawned = true;
		}
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
					int leftright = (int) (Math.random() * 2);
					flyingEnemies.add( new FlyingEnemy(this, leftright * GamePanel.WIDTH, 200, 0, 0, 0, 0, tilemap) );
					break;
				}
				flyingEnemyDelay = 1000;
				flyingEnemyDelay += (int) ( 3000 * (Math.random()));
				flyingEnemyStartTime = System.nanoTime();
			}
		}
		return false;
	}
	
	public void checkBossCollisions() {
		for( int j = 0; j < bullets.size(); j++ ) {
			Bullet bullet = bullets.get(j);
			
			if( boss.hitbox.intersects(bullet.hitbox)) {
				if(boss.hit()) {
					if( j > 0 ) j--;
				}
				bullet.hit();
				bullets.remove(j);
				if( j > 0 ) {
					j++;
				}
			}
		}
	}
	
	public void render(Graphics2D g) {
		tilemap.render(g);
		renderParticles(g);
		
		renderList( bullets, g );
		renderList( flyingEnemies, g);
		renderList( enemyBullets, g );
		renderList( damageableBullets, g );
		if( bossSpawned && !won ) {
			boss.render(g);
		}
		if(!won) {
			artifact.render(g);
			player.render(g);
		}
		
		if( player.artifactCollected && !won ) {
			g.drawImage(timebar, (GamePanel.WIDTH - timebar.getWidth()) / 2, 34 , null);
			g.setColor(new Color(215, 232, 148));
			int width = (int) (60 - (boss.health.y - boss.health.x) * ( 60 / boss.health.y));
			g.fillRect(210, 40, width, 4);
		}
		
		g.setColor(new Color(215, 232, 148));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, damageAlpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		g.setColor(new Color(40, 79, 50));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, darkAlpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
}