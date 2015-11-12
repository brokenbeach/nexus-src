package StateManager;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Main.GamePanel;
import ResourceStores.SpriteStore;

public class InstructionState extends State {
	
	BufferedImage nexus;
	BufferedImage player;
	BufferedImage move;
	BufferedImage beamup;
	BufferedImage shoot;
	BufferedImage uncover;
	BufferedImage planet;
	BufferedImage clouds;

	public InstructionState(StateManager sm) {
		super(sm);
		nexus = SpriteStore.get().getImage("nexus");
		player = SpriteStore.get().getImage("playerstill");
		move= SpriteStore.get().getImage("move");
		beamup = SpriteStore.get().getImage("beamup");
		shoot = SpriteStore.get().getImage("shoot");
		uncover = SpriteStore.get().getImage("uncover");
		planet = SpriteStore.get().getImage("finalPlanet");
		clouds = SpriteStore.get().getImage("clouds");

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tick() {
		return false;
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(nexus, (GamePanel.WIDTH - nexus.getWidth()) / 2, 60, null);

		g.drawImage(player, (GamePanel.WIDTH - player.getWidth()) / 2 + 4, 120, null);
		g.drawImage(move, (GamePanel.WIDTH - move.getWidth()) / 2 - 100, 160, null);

		g.drawImage(beamup, (GamePanel.WIDTH - beamup.getWidth()) / 2, 160, null);

		g.drawImage(shoot, (GamePanel.WIDTH - shoot.getWidth()) / 2 + 100, 160, null);
		g.drawImage(uncover, (GamePanel.WIDTH - uncover.getWidth()) / 2, 260, null);
		g.drawImage(planet, (GamePanel.WIDTH - planet.getWidth()) / 2, 310, null);
		g.drawImage(clouds, (GamePanel.WIDTH - clouds.getWidth()) / 2 + 4, 330, null);

	}

	@Override
	public void keyPressed(int k) {
		sm.setState(StateManager.MENU_STATE);
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
