package StateManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import Fundamentals.Background;
import Main.GamePanel;

public class StateManager  {
	
	private State[] states;
	private int currentState;
	
	public static final int NUMstates = 8;
	public static final int MENU_STATE = 0;
	public static final int LEVEL_0_STATE = 1;
	public static final int LEVEL_1_STATE = 2;
	public static final int LEVEL_2_STATE = 3;
	public static final int LEVEL_3_STATE = 4;
	public static final int LEVEL_4_STATE = 5;
	public static final int LEVEL_5_STATE = 6;
	public static final int INSTRUCTION_STATE = 7;
	
	public boolean level0cleared = false;
	public boolean level1cleared = false;
	public boolean level2cleared = false;
	public boolean level3cleared = false;
	public boolean level4cleared = false;
	public boolean alllevelscleared = false;
	public boolean finallevelcleared = false;
	
	Background bg;
	
	public StateManager() {
		states = new State[NUMstates];
		currentState = INSTRUCTION_STATE;
		loadState(currentState);
		
		try	{
			if( bg == null ){
				bg = new Background("bg0", 2, -0.2);
			}
		}catch(Exception e)	{
			e.printStackTrace();
		}
	}
	
	private void loadState(int state) {
		if( state == MENU_STATE ) {
			bg = new Background("bg0", 2, -0.2);
			states[state] = new MenuState(this);	
		}
		if( state == LEVEL_0_STATE ) {
			states[state] = new Level0State(this);	
			bg = new Background("bg1", 2, -0.2);
		}
		if( state == LEVEL_1_STATE ) {
			states[state] = new Level1State(this);	
			bg = new Background("bg2", 2, -0.2);
		}
		if( state == LEVEL_2_STATE ) {
			states[state] = new Level2State(this);	
			bg = new Background("bg3", 2, -0.2);
		}
		if( state == LEVEL_3_STATE ) {
			states[state] = new Level3State(this);	
			bg = new Background("bg4", 2, -0.2);
		}
		if( state == LEVEL_4_STATE ) {
			states[state] = new Level4State(this);	
			bg = new Background("bg5", 2, -0.2);
		}
		if( state == LEVEL_5_STATE ) {
			states[state] = new Level5State(this);	
			bg = new Background("bg6", 2, -0.2);
		}
		if( state == INSTRUCTION_STATE ) {
			states[state] = new InstructionState(this);	
			bg = new Background("bg0", 2, -0.2);
		}
	}
	
	private void unloadState( int state ) {
		states[state] = null;
	}
	
	public void setState(int state){
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		
		states[currentState].init();
	}
	
	// Update the background, as well as the current gamestate.
	public void tick() {
		if( (level0cleared && level1cleared && level2cleared && level3cleared && level4cleared )) {
			alllevelscleared = true;
		}
		bg.tick();
		try {
			states[currentState].tick();
		}catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	// render the background images, and the current gamestate
	public void render(java.awt.Graphics2D g) {
		try {
			bg.render(g);
			states[currentState].render(g);
		}catch( Exception e ) {
			// Draw black while loading
			g.setColor(new Color( 40, 79, 50));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			e.printStackTrace();
		}
	}
	
	public void win( int i ) {
		switch( i ) {
		case 0:
			level0cleared = true;
			break;
		case 1:
			level1cleared = true;
			break;
		case 2:
			level2cleared = true;
			break;
		case 3:
			level3cleared = true;
			break;
		case 4:
			level4cleared = true;
			break;
		case 5:
			finallevelcleared = true;
			break;
		}
	}
	public boolean checkWin( int i ) {
		switch( i ) {
		case 0:
			return level0cleared;
		case 1:
			return level1cleared;
		case 2:
			return level2cleared;
		case 3:
			return level3cleared;
		case 4:
			return level4cleared;
		case 5:
			return finallevelcleared;
		}
		return false;
	}
	
	public void keyPressed(int k) {
		states[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		states[currentState].keyReleased(k);
	}

	public void mousePressed(MouseEvent m) {
		states[currentState].mousePressed(m);
	}

	public void mouseReleased(MouseEvent m) {
		states[currentState].mouseReleased(m);
	}

	public void mouseDragged(MouseEvent m) {
		states[currentState].mouseDragged(m);
	}

	public void mouseMoved(MouseEvent m) {
		states[currentState].mouseMoved(m);
	}
}