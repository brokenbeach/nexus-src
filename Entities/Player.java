package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Fundamentals.Animation;
import Fundamentals.DeathParticle;
import Fundamentals.Entity;
import Fundamentals.Particle;
import Fundamentals.SpawnParticle;
import Fundamentals.Vector2D;
import Main.GamePanel;
import ResourceStores.AudioPlayer;
import ResourceStores.SoundStore;
import ResourceStores.SpriteStore;
import StateManager.LevelState;
import TileMap.Tile;
import TileMap.TileMap;

public class Player extends Entity {
	
	Vector2D health;
	boolean canMove;
	
	BufferedImage[] healthSprites;
	BufferedImage healthImage;
	BufferedImage gun;
	Vector2D gunPos;
	double gunRotation;
	
	public boolean fired;
	public boolean artifactCollected;
	public boolean dead;
	public boolean beamedUp;
	
	private Animation animation = new Animation();
	private ArrayList<BufferedImage[]> sprites = new ArrayList<BufferedImage[]>();
	private final int[] noFrames = { 1, 2, 1 };
	
	public static final int NOACTIONS = 3;
	public static final int IDLE = 0;
	public static final int RUNNING = 1;
	public static final int JUMPING = 2;
	
	protected long hitStartTime;
	protected long hitDelay = 1000;
	
	protected long resetStartTime;
	protected long resetDelay = 600;

	public boolean shootingUp = false;
	public boolean shootingDown = false;
	
	public boolean normal = true;
	
	AudioPlayer shoot = SoundStore.get().getSound("shoot0");
	AudioPlayer hit = SoundStore.get().getSound("playerHit");
	AudioPlayer beamUp = SoundStore.get().getSound("beamup");
	
	public Player( LevelState ls, double x, double y, double dx, double dy, double ddx, double ddy, TileMap tileMap) {
		super(ls, x, y, dx, dy, ddx, ddy, tileMap);
		for(int i = 0; i < 100; i++ ) {
			double velx = 3 * ((-0.5 + Math.random() ));
			double vely = 7 * ((-0.1 + Math.random() )); 
			int size = (int) (Math.random() * 10) + 4;
			ls.particles.add( new SpawnParticle(pos.x, -10, velx, vely, 0, 0, 100, 100, new Color(215, 232, 148), size));
		}
		max = new Vector2D( 3, 5 );
		health = new Vector2D(4, 4);
		// for each player animation, read the subimages from the spritesheet, create a buffered image array, and add that to the list of animations.
		BufferedImage spritesheet = SpriteStore.get().getImage("player");
		for( int i = 0; i < NOACTIONS; i++ ) {
			BufferedImage[] animation = new BufferedImage[noFrames[i]];
			for( int j = 0; j < noFrames[i]; j++ ) {
				animation[j] = spritesheet.getSubimage(j * Tile.TILESIZE, i * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE);
			}
			sprites.add(animation);
		}
		this.animation.setFrames(sprites.get(0));
		
		spritesheet = SpriteStore.get().getImage("health");
		healthSprites = new BufferedImage[5];
		for( int i = 0; i < healthSprites.length; i++ ) {
			healthSprites[i] = spritesheet.getSubimage(0, i * 20, spritesheet.getWidth(), 20);
		}
		healthImage = healthSprites[0];
		gun = SpriteStore.get().getImage("gun");
		gunPos = new Vector2D( pos.x, pos.y );
	}

	@Override
	public boolean tick() {
		if( !dead && !beamedUp) {
			updateHitbox();
			if( pos.y > GamePanel.HEIGHT - 16 ) {
				beamUp();
				ls.lost = true;
			}
			nextPosition();
			checkMapCollision();
			
			gunPos.x += ( pos.x - gunPos.x ) / 2;
			gunPos.y += ( pos.y - gunPos.y ) / 3;
		}
		
		if( (left || right)) {
			if(normal) {
				if( left && gunRotation == 0) {
					facingRight = false;
				}else if( right && gunRotation == 0){
					facingRight = true;
				}
			}
			
			if( currentAction != RUNNING ) {
				currentAction = RUNNING;
				animation.setFrames(sprites.get(RUNNING));
				animation.setDelay(100);
			}
		} else if( vel.x == 0){
			if( currentAction != IDLE ) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(-1);
			}
		}

		if( vel.y != 0 ) {
			if( currentAction != JUMPING ) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
			}
		}
		animation.tick();
		
		if( shootingUp ) {
			gunRotation = Math.PI * 1.5;
		}else if( shootingDown ) {
			gunRotation = Math.PI * 0.5;
		}else {
			if( resetDelay == -1 ) return false;
			long elapsed = (System.nanoTime() - resetStartTime) / 1000000;
			if( elapsed > resetDelay ) {
				gunRotation = 0;
				normal = true;
				resetStartTime = System.nanoTime();
			}
		}
		
		return false;
	}
	
	private void nextPosition() {
		if( left  && pos.x > 16 && canMove) {
			vel.x -= acc.x;
			if( vel.x < -max.x ) {
				vel.x = -max.x;
			}
		} else if( right && pos.x < GamePanel.WIDTH - 16 && canMove) {
			vel.x += acc.x;
			if( vel.x > max.x ) {
				vel.x = max.x;
			}
		}
		else {
			vel.x += ( 0 - vel.x ) / 5;
			if( Math.abs(vel.x) < 0.05 ) {
				vel.x = 0;
			}
		}
		
		if( jumping && !falling ) {


//			jump.play();
			vel.y = -3.5;
			falling = true;
		}
		
		if( falling ) {
			vel.y += acc.y;
			if( vel.y > max.y ) {
				vel.y = max.y;
			}
			
			if( vel.y > 0 ) jumping = false;
			if( vel.y < 0 && !jumping ) falling = true;
		}
	}
	
	public boolean hit() {
		if( hitDelay == -1 ) return false;
		long elapsed = (System.nanoTime() - hitStartTime) / 1000000;
		if(elapsed > hitDelay) {
			if( !dead ) {
				hit.play();
				ls.damageFlash(0.65f);
				health.x--;
				if( health.x < 0 ) {
					health.x = 0;
				}
				int i = (int) (health.y - health.x);
				healthImage = healthSprites[i];
				
				if( health.x == 0 ) {
					dead = true;
					gameOver();
				}
			}
			hitStartTime = System.nanoTime();
		}
		return false;
	}


	public void render( Graphics2D g ) {
		
		if( !dead && !beamedUp) {
			if( facingRight ) {
				g.drawImage(animation.getImage(), (int) pos.x - 8, (int) pos.y- 8, null);
				g.rotate(gunRotation, (int) gunPos.x + 4, (int) gunPos.y);
				g.drawImage(gun, (int) gunPos.x - 4, (int) gunPos.y, null);
				g.rotate(-gunRotation, (int) gunPos.x + 4, (int) gunPos.y);
			}else {
	
				g.drawImage(animation.getImage(), (int) (pos.x - 8 + hitbox.width), (int) pos.y - 8, -Tile.TILESIZE, Tile.TILESIZE, null);
				g.rotate(-gunRotation, (int) gunPos.x - 4, (int) gunPos.y);
				g.drawImage(gun, (int) gunPos.x + 4, (int) gunPos.y, - gun.getWidth(), gun.getHeight(), null);
				g.rotate(gunRotation, (int) gunPos.x - 4, (int) gunPos.y);
			}
		}	
		g.drawImage(healthImage, (int) (GamePanel.WIDTH - healthImage.getWidth()) / 2, (int) 10, null);
	}
	
	public void fire( int k ) {
		if( !dead && !beamedUp ) {
			if( !fired ) {
				shoot.play();
				resetStartTime = System.nanoTime();
				normal = false;
				double velx = 0;
				double vely = 0;
				double rotation = 0;
				if( k == KeyEvent.VK_LEFT) {
					velx  = -6;
					vely =  0;
					rotation = Math.PI * 1.5;
					gunRotation = 0;
				}
				if( k == KeyEvent.VK_RIGHT) {
					velx  =  6;
					vely =  0;
					rotation = Math.PI * 0.5;
					gunRotation = 0;
				};
				if( k == KeyEvent.VK_UP) {
					velx  =  0;
					vely = -6;
				};
				if( k == KeyEvent.VK_DOWN) {
					velx  =  0;
					vely =  6;
					rotation = Math.PI;
				};
				
				ls.bullets.add(new Bullet(ls, pos.x, pos.y, velx, vely, 0, 0, rotation, tileMap));
			}
		}
	}
	
	public void enableMovement() {
		canMove = true;
	}
	
	public void setFacingRight( boolean b ) { this.facingRight = b; }
	
	public void beamUp() {
		beamUp.play();
		beamedUp = true;
		gameOver();
	}
	
	private void gameOver() {
		for(int i = 0; i < 100; i++ ) {
			double velx = 3 * ((-0.5 + Math.random() ));
			double vely = 5 * ((-0.5 + Math.random() )); 
			int size = (int) (Math.random() * 10) + 4;
			ls.particles.add( new DeathParticle(pos.x, pos.y, velx, vely, 0, 0, 500, 500, new Color(215, 232, 148), size));
		}
	}

}
