package Fundamentals;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

import Entities.Player;
import ResourceStores.AudioPlayer;
import ResourceStores.SoundStore;
import StateManager.LevelState;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class Entity  {
	
	protected TileMap tileMap;
	public LevelState ls;
	public BufferedImage image;
	 
	public Vector2D pos;
	public Vector2D dest; // next pos
	public Vector2D temp; // temporary holder of value for collision
	
	public Vector2D vel;
	public Vector2D acc;
	public Vector2D dec;
	public Vector2D max; // max speed for moving & falling
	public Vector2D center;
	protected double rotation; // angle
	protected double theta;	 // change in angle per tick
	public Double hitbox;
	
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	protected Animation animation;
	protected int currentAction;
	protected boolean facingRight;
	
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	

	AudioPlayer land = SoundStore.get().getSound("land");
	
	public Entity(LevelState ls, double x, double y, double dx, double dy, double ddx, double ddy, TileMap tileMap) {
		this.pos = new Vector2D(x, y);
		this.vel = new Vector2D(dx, dy);
		this.acc = new Vector2D(ddx, ddy);
		this.dest = new Vector2D(0, 0);
		this.temp = new Vector2D(0, 0);
		this.dec = new Vector2D(0, 0);
		hitbox = new Rectangle2D.Double( pos.x, pos.y, 16, 16);
		center = new Vector2D( pos.x + 16 / 2, pos.y + 16 / 2);
		this.tileMap = tileMap;
		this.ls = ls;
	}
	
	 protected void updateHitbox() {
		hitbox.setFrame( pos.x - 8, pos.y - 8, 16, 16);
		center.x = pos.x - 8;
		center.y = pos.y - 8;
	}

	public abstract boolean tick();
	public abstract void render( Graphics2D g );
	public abstract boolean hit();
	
	public boolean checkMapCollision() {
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
			}else {
				temp.y += vel.y;
			}
		}
		if( vel.y > 0 ) {
			if( bottomLeft || bottomRight ) {
				vel.y = 0;
				land.play();
				falling = false;
				temp.y = (currentRow + 1) * Tile.TILESIZE - hitbox.height / 2;
			}else {
				temp.y += vel.y;
			}
		}
		
		checkCorners( dest.x, pos.y );
		if( vel.x < 0 ) {
			if( topLeft || bottomLeft ) {
				vel.x = 0;
				temp.x = currentColumn * Tile.TILESIZE + hitbox.width / 2;
			}else {
				temp.x += vel.x;
			}
		}
		if( vel.x > 0 ) {
			if( topRight || bottomRight ) {
				vel.x = 0;
				temp.x = (currentColumn + 1) * Tile.TILESIZE - hitbox.width / 2;
			}else {
				temp.x += vel.x;
			}
		}
		
		if( !falling ) {
			checkCorners(pos.x, dest.y + 1);
				if( !bottomLeft && !bottomRight ) {
					falling = true;
			}
		}
		
		pos.x = temp.x;
		pos.y = temp.y;
		return false;
	}
	
	public void checkCorners( double x, double y ) {
		// gets the position of the adjecant tile within the map array
		int leftTile = (int) (x - hitbox.width / 2) / Tile.TILESIZE;
		int rightTile = (int) (x + hitbox.width / 2 - 1) / Tile.TILESIZE;
		int topTile = (int) (y - hitbox.height / 2) / Tile.TILESIZE;
		int bottomTile = (int) (y + hitbox.height / 2 - 1) / Tile.TILESIZE;
		
		//	checks the type of each block
		int topLeft = tileMap.getType( topTile, leftTile);
		int topRight = tileMap.getType( topTile, rightTile);
		int bottomLeft = tileMap.getType( bottomTile, leftTile);
		int bottomRight = tileMap.getType( bottomTile, rightTile);
		
		if( this instanceof Player ) {
			if( bottomRight == Tile.HARMFUL || bottomLeft == Tile.HARMFUL ) {
				ls.player.hit();
			}
		}
		
		this.topLeft     = (topLeft == Tile.BLOCKED) || (topLeft == Tile.INDESTRUCTABLE) || (topLeft == Tile.HARMFUL);
		this.topRight    = (topRight== Tile.BLOCKED) || (topRight == Tile.INDESTRUCTABLE)  || (topRight == Tile.HARMFUL);;
		this.bottomLeft  = (bottomLeft == Tile.BLOCKED) || (bottomLeft == Tile.INDESTRUCTABLE)  || (bottomLeft == Tile.HARMFUL);
		this.bottomRight = (bottomRight == Tile.BLOCKED) || (bottomRight == Tile.INDESTRUCTABLE)  || (bottomLeft == Tile.HARMFUL);
	}
	
	public BufferedImage getImage() { return this.image; }
	
	public void setLeft( boolean bool ) { this.left = bool; }
	public void setRight( boolean bool ) { this.right = bool; }
	public void setUp( boolean bool ) { this.up = bool; }
	public void setDown( boolean bool ) { this.down = bool; }
	public void setJumping( boolean bool ) { this.jumping = bool; }
	public void setFalling( boolean bool ) { this.falling = bool; }
}