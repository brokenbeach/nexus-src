package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Fundamentals.Entity;
import Fundamentals.Particle;
import Fundamentals.Vector2D;
import ResourceStores.AudioPlayer;
import ResourceStores.SoundStore;
import ResourceStores.SpriteStore;
import StateManager.LevelState;
import TileMap.TileMap;

public class FlyingEnemy extends Entity {

	public Vector2D health;
	protected long startTime;
	protected long delay = 40;
	boolean hit = false;
	
	BufferedImage hitImage;
	
	protected long hitStartTime;
	protected long hitDelay = 75;
	
	AudioPlayer hitSound  = SoundStore.get().getSound("enemyHit");
	AudioPlayer explode = SoundStore.get().getSound("explode");
	AudioPlayer shoot = SoundStore.get().getSound("enemyShoot");

	
	public FlyingEnemy( LevelState ls, double x, double y, double dx, double dy, double ddx, double ddy, TileMap tileMap) {
		super(ls, x, y, dx, dy, ddx, ddy, tileMap);
		image = SpriteStore.get().getImage("flyingenemy0");
		hitImage = SpriteStore.get().getImage("flyingenemy0hit");
		health = new Vector2D( 2, 2);
	}

	@Override
	public boolean tick() {
		updateHitbox();
		if( delay == -1 ) return false;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			double velx = 2 * (-0.5 + Math.random() );
			double vely = 2 * (-0.5 + Math.random() ); 
			ls.particles.add( new Particle(pos.x, pos.y, velx, vely, 0, 0, 80, 80, new Color(215, 232, 148), 6));
			startTime = System.nanoTime();
		}
		Vector2D playerPos = ls.player.pos;
		double xDiff = (playerPos.x - pos.x);
		double yDiff = (playerPos.y - pos.y);
		rotation = Math.atan2(yDiff, xDiff) - Math.PI / 2;
		
		if( !ls.player.dead ) {
			vel.x = xDiff / 50;
			vel.y = yDiff / 50;
		}
		
		if( hitDelay == -1 ) return false;
		long elapsed0 = (System.nanoTime() - hitStartTime) / 1000000;
		if(elapsed0 > hitDelay) {
			hit = false;
			hitStartTime = System.nanoTime();
		}
		pos.x += vel.x;
		pos.y += vel.y;
		
		return false;
	}

	@Override
	public void render(Graphics2D g) {
		if( hit ) {
			g.drawImage(hitImage, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		}else {
			g.drawImage(image, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		}
	}

	@Override
	public boolean hit() {
		hitSound.play();
		health.x--;
		hit = true;
		boolean dead = false;
		if( health.x <= 0 ) {
			dead = true;
			die();
		}
		return dead;
	}
	
	public void die() {
		explode.play();
		for(int i = 0; i < 20; i++ ) {
			double velx = 5 * ( -0.5 + Math.random() );
			double vely = 5 * ( -0.5 + Math.random() ); 
			ls.particles.add( new Particle(pos.x, pos.y, velx, vely, 0, 0, 40, 40, new Color(215, 232, 148), 6));
		}
	}
}
