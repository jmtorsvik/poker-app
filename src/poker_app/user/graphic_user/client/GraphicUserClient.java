package poker_app.user.graphic_user.client;

import javax.swing.SwingUtilities;

public class GraphicUserClient {
	private static MainFrame mainFrame;
	
	private static void launch() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame = new MainFrame();
			}
		});
	}

	public static void main(String[] args) {
		launch();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.setTitle("TEST");
			}
		});
	}

}
