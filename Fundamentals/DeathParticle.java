package Fundamentals;

import java.awt.Color;

public class DeathParticle extends Particle{

	public DeathParticle(double x, double y, double dx, double dy, double ddx, double ddy, double life, double maxLife, Color colour, double size) {
		super(x, y, dx, dy, ddx, ddy, life, maxLife, colour, size);
		// TODO Auto-generated constructor stub
	}
	
	public boolean tick() {
		life.x--;
		if(life.x <= 0) {
			alive = false;
			return true;
		}
		
		if(size.x > 1.2 ) {
			size.x -= 0.2;
			size.y -= 0.2;
		}
		
		double diff = (life.y - life.x ) * sector;
		alpha = (float) diff;
		
		rotation += theta;
		vel.x += (0 - vel.x) / 30;
		vel.y += (-5 - vel.y) / 50;
		pos.x += vel.x;
		pos.y += vel.y;
		return false;
	}

}
