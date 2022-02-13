package poker_app.user.graphic_user.client;

import javax.swing.SwingUtilities;

public class GraphicUserClient {
	private static TableFrameV2 tableFrame;
	
	private static void launch() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableFrame = new TableFrameV2(6);
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
