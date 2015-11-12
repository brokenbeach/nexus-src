package StateManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Entities.Artifact;
import Entities.Bullet;
import Entities.EnemyBullet;
import Entities.FlyingEnemy;
import Entities.FlyingEnemyShoots;
import Entities.Player;
import Fundamentals.Entity;
import Fundamentals.Particle;
import Main.GamePanel;
import ResourceStores.SpriteStore;
import TileMap.TileMap;

public class LevelState extends State {

	protected TileMap tilemap;
	
	float damageAlpha = 0;
	float darkAlpha = 1.0f;
	
	protected long movementStartTime;
	protected long movementDelay = 600;
	boolean canMove;

	protected long flyingEnemyStartTime;
	protected long flyingEnemyDelay = 500;
	
	public Player player;
	public Artifact artifact;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();
	public List<Bullet> damageableBullets = new ArrayList<Bullet>();
	public List<EnemyBullet> enemyBullets = new ArrayList<EnemyBullet>();

	public List<FlyingEnemy> flyingEnemies = new ArrayList<FlyingEnemy>();
	public List<Particle> particles = new ArrayList<Particle>();
	
	BufferedImage timebar;
	protected int counter;
	protected int currentCount;
	protected long startTime;
	protected long delay = 1000;
	public boolean lost;
	public boolean won;
	
	boolean deadChecked;
	protected long returnStartTime;
	protected long returnDelay = 2000;
	
	public LevelState(StateManager sm) {
		super(sm);
		timebar = SpriteStore.get().getImage("timebar");
		movementStartTime = System.nanoTime();
	}
	
	@Override
	public boolean tick() {
		if( !lost && !won ) {
			if( currentCount > counter ) {
				won = true;
				player.beamUp();
			}
		}
		
		if( player.artifactCollected && !won && !lost) {
			if( delay == -1 ) return false;
			long elapsed = (System.nanoTime() - startTime) / 1000000;
			if(elapsed > delay) {
				currentCount++;
				startTime = System.nanoTime();
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
			darkAlpha += ( 0 - darkAlpha ) * 0.05;
		}
		
		damageAlpha += ( 0 - damageAlpha ) * 0.05;
		tilemap.tick();
		artifact.tick();
		player.tick();
		tickList( bullets );
		tickList( enemyBullets );
		tickList( damageableBullets );
		tickList( flyingEnemies );
		removeBullets();
		removeMissiles();
		tickParticles();
		checkEnemyBulletCollisions();
		checkPlayerBulletCollisions();
		checkEnemyCollisions();
		if( !player.artifactCollected ) {
			checkArtifactCollection();
		}
		return false;
	}
	
	protected void tickList( List< ? extends Entity > list ) {
		for(int i = 0; i < list.size(); i++ ) {
			if ( list.get(i).tick() ) {
				list.remove(i);
				if( i > 0 ) 
					i--;
			}
		}
	}
	
	public void tickParticles() {
		for(int i = 0; i < particles.size(); i++ ) {
			if ( particles.get(i).tick() ) {
				particles.remove(i);
				if( i > 0 ) 
					i--;
			}
		}
	}
	
	protected void removeBullets() {
		for(int k = 0; k < bullets.size(); k++ ) {
			if( bullets.size() == 0) break;
			Bullet bullet = bullets.get(k);
			if(bullet.remove) {
				bullets.remove(k);
				if( k > 0 ) k--;
			}
		}
	}
	
	protected void removeMissiles() {
		for(int k = 0; k < damageableBullets.size(); k++ ) {
			if( damageableBullets.size() == 0) break;

			Bullet bullet = damageableBullets.get(k);
			if(bullet.remove) {
				damageableBullets.remove(k);
				if( k > 0 ) k--;
			}
		}
	}
	
	public void checkArtifactCollection() {
		if( player.hitbox.intersects(artifact.hitbox)) {
			player.artifactCollected = true;
			artifact.collect();
		}
	}
	
	public void checkEnemyBulletCollisions() {
		for(int i = 0; i < enemyBullets.size(); i++ ) {
			if( enemyBullets.size() == 0) break;

			EnemyBullet bullet = enemyBullets.get(i);
			if( bullet.hitbox.intersects(player.hitbox)) {
				player.hit();
				bullet.hit();
				enemyBullets.remove(i);
				if( i >  0) i--;
			}
		}
	}
	
	public void checkPlayerBulletCollisions() {
		for(int i = 0; i < flyingEnemies.size(); i++ ) {
			for( int j = 0; j < bullets.size(); j++ ) {
				if( flyingEnemies.size() == 0) break;
				if( bullets.size() == 0) break;

				FlyingEnemy enemy = flyingEnemies.get(i);
				Bullet bullet = bullets.get(j);
				
				if( enemy.hitbox.intersects(bullet.hitbox)) {
					if(enemy.hit()) {
						flyingEnemies.remove(i);
						if( i > 0 ) i--;
					}
					bullet.hit();
					bullets.remove(j);
					if( j > 0 ) {
						j++;
					}
				}
			}
		}
	}
	
	public void checkEnemyCollisions() {
		for(int i = 0; i < flyingEnemies.size(); i++ ) {
			if( flyingEnemies.size() == 0) break;

			FlyingEnemy enemy = flyingEnemies.get(i);
			if( enemy instanceof FlyingEnemy && !(enemy instanceof FlyingEnemyShoots ) ){
				if( enemy.hitbox.intersects(player.hitbox)) {
					enemy.die();
					flyingEnemies.remove(i);
					if( i > 0 ) i--;
					if( !won && !lost ) {
						player.hit();
					}
				}
			}
		}
	}
	
	
	//.............................................

	@Override
	public void render(Graphics2D g) {
		tilemap.render(g);
		renderParticles(g);
		
		renderList( bullets, g );
		renderList( flyingEnemies, g);
		renderList( enemyBullets, g );
		renderList( damageableBullets, g );
		
		if(!won) {
			artifact.render(g);
			player.render(g);
		}
		
		if( player.artifactCollected && !won ) {
			g.drawImage(timebar, (GamePanel.WIDTH - timebar.getWidth()) / 2, 34 , null);
			g.setColor(new Color(215, 232, 148));
			int width = currentCount * ( 60 / counter );
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
	
	protected void renderList( List< ? extends Entity > list, Graphics2D g ) {
		for(int i = 0; i < list.size(); i++ ) {
			list.get(i).render(g);
		}
	}
	
	protected void renderParticles( Graphics2D g ) {
		for(int i = 0; i < particles.size(); i++ ) {
			particles.get(i).render(g);
		}
	}
	
	//............................................

	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_Q && !lost) {
			lost = true;
			player.beamUp();
		}
		
		if( k == KeyEvent.VK_A ) player.setLeft( true );
		if( k == KeyEvent.VK_D ) player.setRight( true );
		if( k == KeyEvent.VK_W ) player.setJumping( true );
		if( k == KeyEvent.VK_S ) player.setDown( true );
		if( k == KeyEvent.VK_LEFT) {
			player.fire( k );
			player.fired = true;
			player.setFacingRight(false);
		}
		if( k == KeyEvent.VK_RIGHT) {
			player.fire( k );
			player.fired = true;
			player.setFacingRight(true);
		}
		if( k == KeyEvent.VK_UP)  {
			player.fire( k );
			player.fired = true;
			player.shootingUp = true;
		}
		if( k == KeyEvent.VK_DOWN) {
			player.fire( k );
			player.fired = true;
			player.shootingDown = true;
		}
	}

	@Override
	public void keyReleased(int k) {
		if( k == KeyEvent.VK_A ) player.setLeft( false );
		if( k == KeyEvent.VK_D ) player.setRight( false );
		if( k == KeyEvent.VK_W ) player.setJumping( false );
		if( k == KeyEvent.VK_S ) player.setDown( false );
		
		if( k == KeyEvent.VK_LEFT) {
			player.fired = false;
		}
		if( k == KeyEvent.VK_RIGHT) {
			player.fired = false;
		}
		if( k == KeyEvent.VK_UP)  {
			player.fired = false;
			player.shootingUp = false;
		}
		if( k == KeyEvent.VK_DOWN) {
			player.fired = false;
			player.shootingDown = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		
	}

	public void damageFlash(float f) {
		this.damageAlpha = f;
	}
	
	public void darkFlash(float f) {
		this.darkAlpha = f;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
}
