package poker_app.user.graphic_user.client;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.SwingUtilities;

public class GraphicUserClient {
	private static TableFrame tableFrame;
	private static int gamePort = 7000;
	private static Socket gameSocket;
	private static DataInputStream dataIn;
	private static Thread gameThread;
	
	private static void init() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableFrame = new TableFrame(6);
			}
		});
		
		try {
			gameSocket = new Socket("localhost", gamePort);
			dataIn = new DataInputStream(gameSocket.getInputStream());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void startListening(){
		gameThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						byte msgType = dataIn.readByte();
						
						switch (msgType) {
						case 0:
							break;
						case 1:
							break;
						case 2:
							break;
						default:
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		gameThread.start();
	}
	
	private static void launch() {
		init();
	}

	public static void main(String[] args) {
		launch();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableFrame.drawPlayers(Arrays.asList("Player0", null, "Player1"), Arrays.asList(100, 0, 100));
			}
		});
	}
}
