package poker_app.user.graphic_user.client;

import javax.swing.SwingUtilities;

public class GraphicUserClient {
	private static TableFrame tableFrame;
	
	private static void launch() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableFrame = new TableFrame(6);
			}
		});
	}

	public static void main(String[] args) {
		launch();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableFrame.setTitle("TEST");
			}
		});
	}

}
