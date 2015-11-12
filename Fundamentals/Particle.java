package Fundamentals;

import java.awt.*;
import java.awt.geom.Rectangle2D.Double;

import Main.GamePanel;

public class Particle {
	
	Vector2D pos;
	Vector2D vel;
	Vector2D acc;
	Vector2D center;
	double rotation;
	double theta;
	Double hitbox;
	Vector2D size;
	Vector2D life;
	boolean alive = true;
	Color colour;
	float alpha = 1.0f;
	
	double sector;
	
	public Particle(double x, double y, double dx, double dy, double ddx, double ddy, double life, double maxLife, Color colour, double size) {
		this.pos = new Vector2D(x, y);
		this.vel = new Vector2D(dx, dy);
		this.acc = new Vector2D(ddx, ddy);
		this.size = new Vector2D(size, size);
		this.colour = colour;
		theta = -0.5 + Math.random();
		if( life > maxLife) {
			life = maxLife;
		}
		this.life = new Vector2D(life, maxLife);
		sector = 1.0 / this.life.y;
	}
	
	public boolean tick() {
		life.x--;
		if(life.x <= 0) {
			alive = false;
			return true;
		}

		bounceOffScreen();
		
		if(size.x > 1.0) {
			size.x -= 0.2;
			size.y -= 0.2;
		}
		
		double diff = (life.y - life.x ) * sector;
		alpha = (float) diff;
		
		rotation += theta;
		vel.x += (0 - vel.x) / 20;
		vel.y += (0 - vel.y) / 20;
		pos.x += vel.x;
		pos.y += vel.y;
		return false;
	}
	
	public void render(Graphics2D g) {
		g.setColor(this.colour);
		g.rotate(theta, pos.x + size.x, pos.y + size.y);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - alpha));
		g.fillOval((int) (pos.x-size.x/2), (int) (pos.y-size.x/2), (int)(size.x), (int)(size.x));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.rotate(-theta, pos.x + size.x, pos.y + size.y);
	}
	
	// If you hit the edges of the screen, reverse veposity (i.e. bounce)
	public void bounceOffScreen() {
		if(pos.x >= GamePanel.WIDTH || pos.x <= 0) {
			vel.x = -vel.x;
		}
		if(pos.y >= GamePanel.HEIGHT || pos.y <= 0) {
			vel.y = -vel.y;
		}
	}
}