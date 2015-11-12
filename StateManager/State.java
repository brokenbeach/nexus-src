package StateManager;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import ResourceStores.AudioPlayer;


public abstract class State {
	
	protected StateManager sm;
	AudioPlayer bgMusic;
	
	public State( StateManager sm ) {
		this.sm = sm;
	}
	
	public abstract void init();
	public abstract boolean tick();
	public abstract void render(Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void mousePressed(MouseEvent m);
	public abstract void mouseReleased(MouseEvent m);
	public abstract void mouseDragged(MouseEvent m);
	public abstract void mouseMoved(MouseEvent m);
	
}