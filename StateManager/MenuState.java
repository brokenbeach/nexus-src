package StateManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Main.GamePanel;
import ResourceStores.AudioPlayer;
import ResourceStores.SoundStore;
import ResourceStores.SpriteStore;

public class MenuState extends State{

	float darkAlpha = 1.0f;
	
	BufferedImage title;
	BufferedImage cursor;
	
	BufferedImage planet0;
	BufferedImage planet1;
	BufferedImage planet2;
	BufferedImage planet3;
	BufferedImage planet4;
	BufferedImage finalPlanet;
	BufferedImage clouds;
	
	BufferedImage excavated;
	BufferedImage unable;
	
	BufferedImage thankyou;
	
	static int currentChoice = 0;
	
	private BufferedImage[] planetNames = new BufferedImage[6];
	
	AudioPlayer changeSelection = SoundStore.get().getSound("changeSelection");
	AudioPlayer select = SoundStore.get().getSound("selection");

	
	public MenuState(StateManager sm) {
		super(sm);
		title = SpriteStore.get().getImage("title");
		cursor = SpriteStore.get().getImage("cursor");
		planet0 = SpriteStore.get().getImage("planet0");
		planet1 = SpriteStore.get().getImage("planet1");
		planet2 = SpriteStore.get().getImage("planet2");
		planet3 = SpriteStore.get().getImage("planet3");
		planet4 = SpriteStore.get().getImage("planet4");
		finalPlanet = SpriteStore.get().getImage("finalPlanet");
		clouds = SpriteStore.get().getImage("clouds");
		excavated = SpriteStore.get().getImage("excavated");
		unable = SpriteStore.get().getImage("unable");
		thankyou = SpriteStore.get().getImage("thankyou");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tick() {
		darkAlpha += ( 0 - darkAlpha ) * 0.07;
		return false;
	}
	
	public void select(){
		switch(currentChoice){
		case 0:
			select.play();
			sm.setState(StateManager.LEVEL_0_STATE);
			break;
		case 1:
			select.play();
			sm.setState(StateManager.LEVEL_1_STATE);
			break;
		case 2:
			select.play();
			sm.setState(StateManager.LEVEL_2_STATE);
			break;
		case 3:
			select.play();
			sm.setState(StateManager.LEVEL_3_STATE);
			break;
		case 4:
			select.play();
			sm.setState(StateManager.LEVEL_4_STATE);
			break;
		case 5:
			if( sm.alllevelscleared ) {
				select.play();
				sm.setState(StateManager.LEVEL_5_STATE);	
			}
			break;
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(title, 32, 32, null);
		g.drawImage(planet0, 350, 220, null);
		g.drawImage(planet1, 60, 270, null);
		g.drawImage(planet2, 300, 360, null);
		g.drawImage(planet3, 32, 100, null);
		g.drawImage(planet4, 340, 32, null);
		g.drawImage(finalPlanet, (GamePanel.WIDTH - finalPlanet.getWidth()) / 2, (GamePanel.HEIGHT - finalPlanet.getHeight()) / 2 - 10, null);
		
		if( !sm.alllevelscleared ) {
			g.drawImage(clouds, (GamePanel.WIDTH - clouds.getWidth()) / 2 + 4, (GamePanel.HEIGHT - clouds.getHeight()) / 2 - 4, null);
		}
		
		if( sm.finallevelcleared ) {
			g.drawImage(thankyou, (GamePanel.WIDTH - thankyou.getWidth()) / 2, 150, null);
		}
	
		renderCursor(g);
		
		g.setColor(new Color(40, 79, 50));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, darkAlpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
	private void renderCursor(Graphics2D g) {
		int x = 0;
		int y = 0;
		switch( currentChoice ) {
		case 0:
			x = 372;
			y = 276;
			break;
		case 1:
			x = 81;
			y = 330;
			break;
		case 2:
			x = 324;
			y = 416;
			break;
		case 3:
			x = 356;
			y = 96;
			break;
		case 4:
			x = 56;
			y = 150;
			break;
		case 5:
			x = (GamePanel.WIDTH - cursor.getWidth()) / 2;
			y = (GamePanel.HEIGHT - cursor.getHeight()) / 2 + 40;
			break;
		}

		g.drawImage(cursor, x, y, null);
		if( sm.checkWin(currentChoice ) ) {
			g.drawImage(excavated, x - 44, y+ 20, null);
		}
		
		if( currentChoice == 5 && !sm.alllevelscleared) {
			g.drawImage(unable, x - (unable.getWidth() / 2) + 14, y+ 20, null);
		}
	}
	

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		
		if(k == KeyEvent.VK_LEFT){
			changeSelection.play();
			currentChoice--;
			if(currentChoice < 0){
				currentChoice = planetNames.length - 1;
			}
		}
		
		if(k == KeyEvent.VK_RIGHT){
			changeSelection.play();
			currentChoice++;
			if(currentChoice >= planetNames.length){
				currentChoice = 0;
			}
		}
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

}
