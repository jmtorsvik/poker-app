package poker_app.user.graphic_user.client.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	private Image img = Toolkit.getDefaultToolkit().getImage("assets\\images\\poker_table.jpg");
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}
}
