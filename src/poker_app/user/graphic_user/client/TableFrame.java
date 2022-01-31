package poker_app.user.graphic_user.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TableFrame extends JFrame {
	private final int width = 900;
	private final int height = 600;
	private final int columns = 5;
	private final int rows = 4;
	private final int panelOffset = 10;
	private final int numSeats;
	private final Container cp;
	private final List<JPanel> playerPanels;
	private Image img;
	
	public TableFrame(int numSeats) {
		// Set up frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Poker Table");
		setSize(width, height);
				
		// Set number of seats
		if (numSeats == 6 || numSeats == 9) {
			this.numSeats = numSeats;
			playerPanels = new ArrayList<>(numSeats);
		} else {
			throw new IllegalArgumentException("numSeats must be 6 or 9");
		}
		
		// Set background image
		try {
			img = ImageIO.read(new File("assets\\images\\poker_table.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Set content pane to a panel with background image
		setContentPane(new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
			}
		});
		cp = getContentPane();
		
		// Set content pane layout to a 5x4 grid
		cp.setLayout(new GridLayout(4, 5, panelOffset, panelOffset));
		
		// Add panels to grid cells
		addPanels();

		// Show the frame
		setVisible(true);
	}

	private void addPanels() {
		Collection<Integer> cells = null;
		if (numSeats == 6) {
			cells = Arrays.asList(17, 10, 5, 2, 9, 14);
		} else if (numSeats == 9) {
			cells = Arrays.asList(17, 16, 10, 5, 1, 3, 9, 14);
		}
		
		for (int i = 0; i < columns*rows; i++) {
			JPanel p = new JPanel();
			
			if (cells.contains(i)) {
				p.setBackground(Color.BLACK);
			} else {
				p.setOpaque(false);
			}
			
			cp.add(p);
		}
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
