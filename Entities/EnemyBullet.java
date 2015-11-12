package Entities;
import java.awt.Graphics2D;

import Fundamentals.Entity;
import ResourceStores.SpriteStore;
import StateManager.LevelState;
import TileMap.TileMap;

public class EnemyBullet extends Entity {

	public EnemyBullet(LevelState ls, double x, double y, double dx, double dy, double ddx, double ddy, double rotation, TileMap tileMap) {
		super(ls, x, y, dx, dy, ddx, ddy, tileMap);
		image = SpriteStore.get().getImage("enemybullet");
		super.rotation = rotation;
	}
	
	@Override
	public boolean tick() {
		pos.x += vel.x;
		pos.y += vel.y;
		updateHitbox();
		return false;
	}
	
	public boolean hit() {
		return false;
		
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotation, pos.x, pos.y);
		g.drawImage(image, (int) pos.x - image.getWidth() / 2, (int) pos.y - image.getHeight() / 2, null);
		g.rotate(-rotation, pos.x, pos.y);
	}

}
