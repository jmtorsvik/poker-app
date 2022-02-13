package poker_app.user.graphic_user.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TableFrameV2 extends JFrame {
	private final int width = 900;
	private final int height = 600;
	private final int playerPanelRatioWidth = 6;
	private final int playerPanelRatioHeight = 4;	
	private final int numSeats;
	private final Container cp;
	private final List<JPanel> playerPanels;
	private Image img;

	public TableFrameV2(int numSeats) {
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
		
		// Resize components on rescale
		cp.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				for (JPanel p : playerPanels) {
					
				}
			}
		});

		// Set content pane layout to null
		cp.setLayout(null);
				
		//TEST
		addPlayerPanels();

		// Show the frame
		setVisible(true);

	}
	
	private void addPlayerPanels() {
		List<Integer> panelX = new ArrayList<>(9);
		List<Integer> panelY = new ArrayList<>(9);
		int panelWidth = getWidth()/playerPanelRatioWidth;
		int panelHeight = getHeight()/playerPanelRatioHeight;
		
		if (numSeats == 6) {
			int x0 = getWidth()/2 - panelWidth/2;
			int x1 = 0;
			int x2 = getWidth() - panelWidth;
			panelX.addAll(Arrays.asList(x0, x1, x1, x0, x2, x2));
			
			int y0 = getHeight() - panelHeight;
			int y1 = getHeight() - panelHeight*3/2;
			int y2 = panelHeight/2;
			int y3 = 0;
			panelY.addAll(Arrays.asList(y0, y1, y2, y3, y2, y1));
		} else if (numSeats == 9) {
			// TO-DO
		}
		
		for (int i = 0; i < numSeats; i++) {
			JPanel p = new JPanel();
			p.setBounds(panelX.get(i), panelY.get(i), panelWidth, panelHeight);
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			
			List<JLabel> playerLabels = Arrays.asList(new JLabel("IMAGE", SwingConstants.CENTER),
					new JLabel("Player", SwingConstants.CENTER), new JLabel("Stack", SwingConstants.CENTER));
			for (JLabel l : playerLabels) {
				l.setBackground(Color.RED);
				l.setForeground(Color.BLUE);
				l.setOpaque(true);
				l.setAlignmentX(Component.CENTER_ALIGNMENT);
				p.add(l);
			}
			
			p.setOpaque(false);
			cp.add(p);
			playerPanels.add(p);
		}
		
	}
	
	
	
	public void drawPlayers(List<String> names, List<String> stackSizes) {
		if (names.size() != stackSizes.size()) {
			throw new IllegalArgumentException("The arguments names and stackSizes must be of equal size!");
		}
		for (int i = 0; i < names.size(); i++) {
			JPanel p = playerPanels.get(i);
			JLabel l1 = (JLabel) p.getComponent(1);
			l1.setText(names.get(i));
			JLabel l2 = (JLabel) p.getComponent(2);
			l2.setText(stackSizes.get(i));
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
