package poker_app.user.graphic_user.client;

import javax.swing.SwingUtilities;

import poker_app.user.graphic_user.client.graphics.TableFrame;

public class GraphicUserClient {
	private static TableFrame tableFrame;
	
	private static void launch() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableFrame = new TableFrame();
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
