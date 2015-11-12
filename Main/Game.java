package Main;
import javax.swing.JFrame;

public class Game  {
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Game game = new Game();
	}
	
	private static final String TITLE = "NEXUS";
	
	JFrame window = new JFrame(TITLE);
	GamePanel panel = new GamePanel();
	
	public Game() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setContentPane(panel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}