package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import Fundamentals.Particle;
import Fundamentals.Vector2D;
import ResourceStores.SpriteStore;
import StateManager.LevelState;
import TileMap.TileMap;

public class Boss extends FlyingEnemy {

	public Boss(LevelState ls, double x, double y, double dx, double dy,
			double ddx, double ddy, TileMap tileMap) {
		super(ls, x, y, dx, dy, ddx, ddy, tileMap);
		image = SpriteStore.get().getImage("boss");
		hitImage = SpriteStore.get().getImage("bosshit");
		health = new Vector2D( 100, 100 );
		hitbox = new Rectangle2D.Double( pos.x, pos.y, image.getWidth(), image.getHeight());
	}
	
	protected void updateHitbox() {
		hitbox.setFrame( pos.x - (image.getWidth() / 2), pos.y - (image.getHeight() / 2), image.getWidth(), image.getHeight());
		center.x = pos.x - (image.getWidth() / 2);
		center.y = pos.y - (image.getHeight() / 2);
	}
	
	@Override
	public boolean tick() {
		updateHitbox();
		Vector2D playerPos = ls.player.pos;
		double xDiff = (playerPos.x - pos.x);
		double yDiff = (100 - pos.y);
	
		vel.x = xDiff / 100;
		vel.y = yDiff / 80;
		
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
		int rand = 75;
		if( (int) (Math.random() * rand) == 0 ) {

			double xDist = ls.player.pos.x - pos.x;
			double yDist = ls.player.pos.y - pos.y;
			rotation = Math.atan2(yDist, xDist);
			
			double dx = Math.cos(rotation) * 3;
			double dy = Math.sin(rotation) * 3;
			ls.enemyBullets.add(new EnemyBullet(ls, pos.x, pos.y + 48, dx, dy, 0, 0, rotation, tileMap));
		}
		
		rand = 100;
		if( (int) (Math.random() * rand) == 0 ) {
			ls.damageableBullets.add(new Missile(ls, pos.x - 32, pos.y + 44, 0, 3, 0, 0, Math.PI / 2, tileMap));
			ls.damageableBullets.add(new Missile(ls, pos.x + 32, pos.y + 44, 0, 3, 0, 0, Math.PI / 2, tileMap));
		}
	}
	

	@Override
	public void render(Graphics2D g) {
		if( hit ) {
			g.drawImage(hitImage, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		}else {
			g.drawImage(image, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		}
	}
	
	public void die() {
		explode.play();
		for(int i = 0; i < 500; i++ ) {
			double velx0 = 10 * ( -0.5 + Math.random() );
			double vely0 = 10 * ( -0.5 + Math.random() ); 
			double velx1 = 10 * ( -0.5 + Math.random() );
			double vely1 = 10 * ( -0.1 + Math.random() ); 
			int size = (int) (10 + ( 10 * Math.random() ));
			ls.particles.add( new Particle(pos.x, pos.y, velx0 + velx1, vely0 + vely1, 0, 0, 100, 100, new Color(215, 232, 148), size));
		}
	}

}
