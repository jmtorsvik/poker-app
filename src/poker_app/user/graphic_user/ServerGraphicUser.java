package poker_app.user.graphic_user;

import java.io.IOException;
import java.net.ServerSocket;

import poker_app.game.Player;
import poker_app.user.User;

public class ServerGraphicUser extends User {
	private ServerSocket serverSocket;
	
	public ServerGraphicUser(String name, int port) {
		super(name);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ServerTextUser: Error while initializing server socket on port " + port + ".");
		}
	}

	@Override
	public void onAction(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean rebuy(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sitOutNextHand(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int pickSeat(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int chooseStartingStack(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

}
