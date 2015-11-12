package Entities;

import java.awt.Color;
import java.awt.Graphics2D;

import Fundamentals.Entity;
import Fundamentals.Particle;
import Fundamentals.Vector2D;
import ResourceStores.AudioPlayer;
import ResourceStores.SoundStore;
import ResourceStores.SpriteStore;
import StateManager.LevelState;
import TileMap.TileMap;

public class Artifact extends Entity {

	boolean collected;
	double wave = 0.0;
	boolean up = true;
	
	AudioPlayer collect = SoundStore.get().getSound("collection");
	
	public Artifact(LevelState ls, double x, double y, double dx, double dy, double ddx, double ddy, TileMap tileMap) {
		super(ls, x, y, dx, dy, ddx, ddy, tileMap);
		image = SpriteStore.get().getImage("artifact");
	}

	@Override
	public boolean tick() {
		updateHitbox();
		if( collected && !ls.won ) {
			double velx = 2 * (-0.5 + Math.random() );
			double vely = 2 * (-0.5 + Math.random() ); 
			ls.particles.add( new Particle(pos.x, pos.y, velx, vely, 0, 0, 80, 80, new Color(215, 232, 148), 6));
			Vector2D playerPos = ls.player.pos;
			double xDiff = (playerPos.x - pos.x);
			double yDiff = (playerPos.y - pos.y);
			vel.x = xDiff / 8;
			vel.y = yDiff / 8;
		}else {
			if(up) {
				if( wave < 0.3 ) {
					wave += 0.01;
				}else {
					up = false;
				}
			}else {
				if( wave > -0.3 ) {
					wave -= 0.01;
				}else {
					up = true;
				}
			}
			
			vel.y = Math.sin(wave);
		}
		
		
		pos.x += vel.x;
		pos.y += vel.y;
		return false;
	}
	
	public void collect() {
		collect.play();
		this.collected = true;
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
	}

	@Override
	public boolean hit() {
		return false;
		// TODO Auto-generated method stub
		
	}

}
