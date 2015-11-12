package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import Fundamentals.Vector2D;
import Main.GamePanel;
import ResourceStores.AudioPlayer;
import ResourceStores.SoundStore;
import ResourceStores.SpriteStore;

public class TileMap {
	
	Vector2D pos;
	
	// map
	public int[][] map;
	public Tile[][] world;
	private int tileSize = Tile.TILESIZE;
	public int noRows;
	public int noCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int noTilesAcross;
	public BufferedImage[][] tiles;
	public int types[][];
	
	AudioPlayer damage = SoundStore.get().getSound("tileDamaged");
	
	public TileMap() {
		
	}
	
	public void tick() {
		for( int x = 0; x < noCols; x++ ) {
			for( int y = 0; y < noRows; y++ ) {
//				if( x == noCols) break;
				world[y][x].tick();
			}
		}
	}
	
	public void render( Graphics2D g ) {
		for( int x = 0; x < noCols; x++ ) {
			for( int y = 0; y < noRows; y++ ) {
				if( x == noCols) break;
				if( map[y][x] == 0 ) continue;
//				int rc = map[y][x];
//				int r = rc / noTilesAcross;
//				int c = rc % noTilesAcross;
//				g.drawImage(tiles[r][c].getImage(), x * tileSize, y * tileSize, null);
//				g.drawImage(world[y][x].getImage(), x * tileSize, y * tileSize, null);
				world[y][x].render(g);
			}
		}
	}
	
	public void loadTiles( String ref ) {
		try {
			tileset = SpriteStore.get().getImage(ref);
			noTilesAcross = tileset.getWidth() / tileSize;
			tiles = new BufferedImage[4][noTilesAcross];
			types = new int[4][noTilesAcross];
		}catch( Exception e ) {
			e.printStackTrace();
		}
		BufferedImage subimage;
		for( int col = 0; col < noTilesAcross; col++ ) {
			subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
			tiles[0][col] = subimage;
			types[0][col] = Tile.NORMAL;
			subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
			tiles[1][col] = subimage;
			types[1][col] = Tile.BLOCKED;
			subimage = tileset.getSubimage(col * tileSize, tileSize* 2, tileSize, tileSize);
			tiles[2][col] = subimage;
			types[2][col] = Tile.HARMFUL;
			subimage = tileset.getSubimage(col * tileSize, tileSize*3, tileSize , tileSize);
			tiles[3][col] = subimage;
			types[3][col] = Tile.INDESTRUCTABLE;
		}
	}
	
	public void loadMap( String ref ) {
		try {
			InputStream in = getClass().getResourceAsStream( ref );
			BufferedReader br = new BufferedReader( new InputStreamReader(in));
			
			noCols = Integer.parseInt(br.readLine());
			noRows = Integer.parseInt(br.readLine());
			map = new int[noRows][noCols];
			world = new Tile[noRows][noCols];
			width = noCols * tileSize;
			height = noRows * tileSize;
			String delim = ",";
			for(int row = 0; row < noRows; row++ ) {
				String line = br.readLine();
				String[] tokens = line.split(delim);
				for( int col = 0; col < noCols; col++ ) {
					map[row][col] = Integer.parseInt(tokens[col]);
					int rc = map[row][col];
					int r = rc / noTilesAcross;
					int c = rc % noTilesAcross;
					BufferedImage temp = tiles[r][c];
					int type = getType(row, col);
					world[row][col] = new Tile(this, tiles, temp, type, col, row);
				}
			}
		}catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public int getType( int row, int col ) {
		int rc = map[row][col];
		int r = rc / noTilesAcross;
		int c = rc % noTilesAcross;
		return types[r][c];
	}
	
}
