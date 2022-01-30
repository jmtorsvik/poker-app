package poker_app.user.graphic_user.client;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	private Image img = Toolkit.getDefaultToolkit().getImage("assets\\images\\poker_table.jpg");

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Poker Table");
		setSize(640, 480);
		
		add(new BackgroundPanel());
		setVisible(true);
	}

}
