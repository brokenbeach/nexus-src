package Entities;

import java.awt.Color;
import java.awt.Graphics2D;

import Fundamentals.Particle;
import Main.GamePanel;
import ResourceStores.AudioPlayer;
import ResourceStores.SoundStore;
import ResourceStores.SpriteStore;
import StateManager.LevelState;
import TileMap.Tile;
import TileMap.TileMap;

public class Missile extends Bullet {
	
	protected long startTime;
	protected long delay = 80;
	AudioPlayer explode = SoundStore.get().getSound("explode");
	
	public Missile(LevelState ls, double x, double y, double dx, double dy, double ddx, double ddy, double rotation, TileMap tileMap) {
		super(ls, x, y, dx, dy, ddx, ddy, rotation, tileMap);
		image = SpriteStore.get().getImage("missile");
	}
	
	@Override
	public boolean tick() {
		checkMapCollision();
		updateHitbox();
		return false;
	}
	
	public void hit(int x, int y) {
		for(int i = 0; i < 5; i++ ) {
			double velx = 0.6 * (x + (-0.5 + Math.random() ));
			double vely = 0.6 * (y + ( 0.1 + Math.random() )); 
			ls.particles.add( new Particle(pos.x - 8 * x, pos.y - 8 * y, velx, vely, 0, 0, 20, 20, new Color(215, 232, 148), 6));
		}
	}
	
	@Override
	public boolean checkMapCollision() {
		if( delay == -1 ) return false;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			double velx = 2 * (-0.5 + Math.random() );
			double vely = 2 * (-0.5 + Math.random() ); 
			ls.particles.add( new Particle(pos.x, pos.y, velx, vely, 0, 0, 80, 80, new Color(215, 232, 148), 6));
			startTime = System.nanoTime();
		}
		if( pos.x < 0 ||
				pos.x > GamePanel.WIDTH||
				pos.y < 0 ||
				pos.y > GamePanel.HEIGHT) {
				remove = true;
				return true;
			}
		int currentColumn = (int) (pos.x) / Tile.TILESIZE;
		int currentRow    = (int) (pos.y)  / Tile.TILESIZE;
		
		dest.x = pos.x + vel.x;
		dest.y = pos.y + vel.y;
		temp.x = pos.x;
		temp.y = pos.y;
		
		
		
		checkCorners(pos.x, dest.y);
		if( vel.y < 0 ) {
			if( topLeft || topRight ) {
				vel.y = 0;
				temp.y = currentRow * Tile.TILESIZE + hitbox.height / 2;
				tileHit = tileMap.world[currentRow-1][currentColumn];
				tileHit.hit();
				tileHit.hit();
				remove = true;
				hit(0, 1);
			}else {
				temp.y += vel.y;
			}
		}
		if( vel.y > 0 ) {
			if( bottomLeft || bottomRight ) {
				vel.y = 0;
				falling = false;
				temp.y = (currentRow + 1) * Tile.TILESIZE - hitbox.height / 2;
				tileHit = tileMap.world[currentRow+1][currentColumn];
				tileHit.hit();
				tileHit.hit();
				remove = true;
				hit(0, -1);
			}else {
				temp.y += vel.y;
			}
		}
		
		checkCorners( dest.x, pos.y );
		if( vel.x < 0 ) {
			if( topLeft || bottomLeft ) {
				vel.x = 0;
				temp.x = currentColumn * Tile.TILESIZE + hitbox.width / 2;
				tileHit = tileMap.world[currentRow][currentColumn -1];
				tileHit.hit();
				tileHit.hit();
				remove = true;
				hit(1, 0);
			}else {
				temp.x += vel.x;
			}
		}
		if( vel.x > 0 ) {
			if( topRight || bottomRight ) {
				vel.x = 0;
				temp.x = (currentColumn + 1) * Tile.TILESIZE - hitbox.width / 2;
				tileHit = tileMap.world[currentRow][currentColumn +1];
				tileHit.hit();
				tileHit.hit();
				hit(-1, 0);
				remove = true;
			}else {
				temp.x += vel.x;
			}
		}
		pos.x = temp.x;
		pos.y = temp.y;
		return false;
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotation, pos.x, pos.y);
		g.drawImage(image, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		g.rotate(-rotation, pos.x, pos.y);
	}

	@Override
	public boolean hit() {
		explode.play();
		return false;
		// TODO Auto-generated method stub
		
	}
}
