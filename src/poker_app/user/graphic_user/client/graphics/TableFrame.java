package poker_app.user.graphic_user.client.graphics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TableFrame extends JFrame {
	private List<JPanel> playerPanels = new ArrayList<>();
	
	public TableFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Poker Table");
		setSize(640, 480);

		add(new BackgroundPanel());
		setVisible(true);
		
		//add(new PlayerPanel(0, 0));
		//setVisible(true);
	}
	
	public void drawPlayers(List<String> names, List<String> stackSizes) {
		if (names.size() != stackSizes.size()) {
			throw new IllegalArgumentException("The arguments names and stackSizes must be of equal size!");
		}
		for (int i = 0; i < names.size(); i++) {
		
		}
	}
	
	public void startHand(List<Integer> activeSeats, List<Integer> stackSizes, int btn, int smallBlind, int bigBlind) {
		// Draw cards and labels of stack sizes for active seats
	}
	
	public void fold(int seat) {
		// Draw animation of player folding
	}
	
	public void chipsIntoPot(int seat, int amount, int stackSize) {
		// Draw animation of player putting chips into the pot
	}
}
