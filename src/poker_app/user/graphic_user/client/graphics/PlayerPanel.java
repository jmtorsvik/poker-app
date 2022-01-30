package poker_app.user.graphic_user.client.graphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PlayerPanel extends JPanel {
	
	public PlayerPanel(int x, int y) {
		setBounds(x, y, 50, 50);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
	}
	
}
