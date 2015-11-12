package Fundamentals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ResourceStores.SpriteStore;

public class Background {
	
	/*
	 * Background is made up of a background image, it's location, velocity and movescale.
	 * It moves by the velocity each frame.
	 */
	private BufferedImage image;
	Vector2D loc = new Vector2D(0,0);
	Vector2D vel = new Vector2D(0,0);
	private double moveScale;
	
	public Background(String ref, double ms, double dx){
		try{
			image = SpriteStore.get().getImage(ref);
			moveScale = ms;
			this.vel.x = dx;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		loc.x = (x * moveScale) % image.getWidth();
		loc.y = (y * moveScale) % image.getHeight();
	}
	
	public void setVel(double dx, double dy) {
		vel.x = dx;
		vel.y = dy;
	}
	
	public void tick() {
		loc.x += vel.x;
		loc.y += vel.y;
		
		if( loc.x < -image.getWidth() ) {
			loc.x = 0;
		}
	}
	
	public void render(Graphics2D g) {
		g.drawImage(image,  (int) loc.x,  (int) loc.y, null);
		if( loc.x < 0 ) {
			g.drawImage(image, (int) loc.x + image.getWidth(), (int) loc.y, null);
		}else if ( loc.x > 0 ) {
			g.drawImage(image, (int) loc.x - image.getWidth(), (int) loc.y, null);
		}
	}
}