package poker_app.user.graphic_user.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
	private BufferedImage plrImg;
	private BufferedImage bgImg;

	public TableFrameV2(int numSeats) {
		// Set content pane to a panel with background image
		setContentPane(new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(bgImg, 0, 0, cp.getWidth(), cp.getHeight(), null);
			}
		});
		cp = getContentPane();
		cp.setBounds(0, 0, width, height);
		
		
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

		// Set images
		try {
			bgImg = ImageIO.read(new File("assets\\images\\poker_table.jpg"));
			plrImg = ImageIO.read(new File("assets\\images\\player_placeholder.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		// Resize components on rescale
		cp.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				positionPlayerPanels();
				for (int i = 0; i < numSeats; i++) {
					positionPlayerPanels();
				}
			}
		});

		// Set content pane layout to null
		cp.setLayout(null);
			
		// Initializing player panels
		initPlayerPanels();

		// Show the frame
		setVisible(true);

	}
	
	private void initPlayerPanels() {
		for (int i = 0; i < numSeats; i++) {
			// Create and init player panel
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setOpaque(false);
			cp.add(p);
			playerPanels.add(p);
			
			// Add labels
			JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
			imgLabel.setOpaque(false);
			imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			p.add(imgLabel);
			
			for (int j = 0; j < 2; j++) {
				JLabel l = new JLabel("XXX", SwingConstants.CENTER);
				l.setBackground(Color.DARK_GRAY);
				l.setForeground(Color.WHITE);
				l.setOpaque(true);
				l.setAlignmentX(Component.CENTER_ALIGNMENT);
				p.add(l);
			}
			
		}
		
		positionPlayerPanels();
		
	}
	
	private void positionPlayerPanels() {
		int cpWidth = cp.getWidth();
		int cpHeight = cp.getHeight();
		
		int panelWidth = cpWidth/playerPanelRatioWidth;
		int panelHeight = cpHeight/playerPanelRatioHeight;
		
		List<Integer> panelX = new ArrayList<>(numSeats);
		List<Integer> panelY = new ArrayList<>(numSeats);
		
		if (numSeats == 6) {
			int x0 = cpWidth/2 - panelWidth/2;
			int x1 = 0;
			int x2 = cpWidth - panelWidth;
			panelX.addAll(Arrays.asList(x0, x1, x1, x0, x2, x2));
			
			int y0 = cpHeight - panelHeight;
			int y1 = cpHeight - panelHeight*3/2;
			int y2 = panelHeight/2;
			int y3 = 0;
			panelY.addAll(Arrays.asList(y0, y1, y2, y3, y2, y1));
		} else if (numSeats == 9) {
			// TO-DO
		}
		
		int iconRatio = plrImg.getWidth()/plrImg.getHeight();
		int iconHeight = panelHeight*2/3; 
		ImageIcon playerIcon = new ImageIcon(plrImg.getScaledInstance(iconHeight*iconRatio, iconHeight, Image.SCALE_DEFAULT));
		
		for (int i = 0; i < numSeats; i++) {
			JPanel p = playerPanels.get(i);
			p.setBounds(panelX.get(i), panelY.get(i), panelWidth, panelHeight);
			JLabel l = (JLabel) p.getComponent(0);
			l.setIcon(playerIcon);
			
			for (int j = 1; j < 3; j++) {
				l = (JLabel) p.getComponent(j);
				l.setMaximumSize(new Dimension(panelWidth, panelHeight));
			}
			
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
