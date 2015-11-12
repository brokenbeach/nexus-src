package Entities;

import java.awt.Color;
import java.awt.Graphics2D;

import Fundamentals.Particle;
import Fundamentals.Vector2D;
import ResourceStores.SpriteStore;
import StateManager.LevelState;
import TileMap.TileMap;

public class FlyingEnemyMissiles extends FlyingEnemy {
	Vector2D health;
	double wave = 0.0;
	boolean up = true;
	
	public FlyingEnemyMissiles( LevelState ls, double x, double y, double dx, double dy, double ddx, double ddy, TileMap tileMap) {
		super(ls, x, y, dx, dy, ddx, ddy, tileMap);
		image = SpriteStore.get().getImage("flyingenemy2");
		hitImage = SpriteStore.get().getImage("flyingenemy2hit");
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
		double distance = Math.sqrt( (xDiff * xDiff) + (yDiff * yDiff));
		rotation = Math.atan2(yDiff, xDiff) - Math.PI / 2;
		
		if( distance > 80 ) {
			vel.x = xDiff / 80;
			vel.y = yDiff / 80;
		}else {
			vel.x = 0;
			vel.y = 0;
		}
		
		if( hitDelay == -1 ) return false;
		long elapsed0 = (System.nanoTime() - hitStartTime) / 1000000;
		if(elapsed0 > hitDelay) {
			hit = false;
			hitStartTime = System.nanoTime();
		}
		
		if( ls.player.dead == false ) {
			fire();
		}
		
		pos.x += vel.x;
		pos.y += vel.y;
		
		return false;
	}
	
	private void fire() {
		int rand = 100;
		if( (int) (Math.random() * rand) == 0 ) {
			shoot.play();
			double xDist = ls.player.pos.x - pos.x;
			double yDist = ls.player.pos.y - pos.y;
			rotation = Math.atan2(yDist, xDist);
			
			double dx = Math.cos(rotation) * 3;
			double dy = Math.sin(rotation) * 3;
			ls.damageableBullets.add(new Missile(ls, pos.x, pos.y, dx, dy, 0, 0, rotation, tileMap));
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotation, pos.x, pos.y);
		if( hit ) {
			g.drawImage(hitImage, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		}else {
			g.drawImage(image, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		}
		g.rotate(-rotation, pos.x, pos.y);
	}
}
