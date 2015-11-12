package TileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Fundamentals.Vector2D;

public class Tile {
	
	int x;
	int y;
	Vector2D pos;
	Vector2D health;
	public static final int TILESIZE = 16;
	private BufferedImage image;
	private int type;
	boolean hit;
	BufferedImage[][] tileset;
	TileMap tileMap;
	
	protected long startTime;
	protected long delay = 75;
	
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	public static final int HARMFUL = 2;
	public static final int INDESTRUCTABLE = 3;
	
	public Tile( TileMap tileMap, BufferedImage[][] tiles, BufferedImage image, int type, int x, int y) {
		this.image = image;
		this.type = type;
		health = new Vector2D(2, 2);
		if( type == INDESTRUCTABLE ) {
			health = new Vector2D( 10000, 10000 );
		}
		if( type == HARMFUL ) {
			health = new Vector2D( 10000, 10000 );
		}
		this.x = x;
		this.y = y;
		pos = new Vector2D( x * TILESIZE, y * TILESIZE );
		this.tileset = tiles;
		this.tileMap = tileMap;
	}
	
	public boolean tick() {
		if( delay == -1 ) return false;
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			hit = false;
			startTime = System.nanoTime();
		}
		return hit;
		
	}
	
	public void hit() {
		if( type == 0 ) return;
		if( health.x > 0 ) {
			tileMap.damage.play();
		}
		hit = true;
		health.x--;
		if( health.x <= 0 ) {
			this.type = NORMAL;
			this.image = tileset[0][1];
			tileMap.map[y][x] = 1;
		}
	}
	
	public void render( Graphics2D g ) {
		g.setColor(new Color(215, 232, 148));
		if( hit ) {
			g.fillRect((int) pos.x, (int) pos.y, TILESIZE, TILESIZE);
		} else {
			g.drawImage(image, (int) pos.x, (int) pos.y, null );
		}
	}
	
	public boolean isBlocked() { return type == BLOCKED; }
	
	public BufferedImage getImage() { return this.image; }
	public int getType() { return this.type; }
}
