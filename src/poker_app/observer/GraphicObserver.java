package poker_app.observer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import poker_app.Table;
import poker_app.game.Player;

public class GraphicObserver extends TableObserver {
	private ServerSocket serverSocket;
	
	public GraphicObserver(Table table, int port) {
		super(table);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ServerWriter: Error while initializing server socket on port " + port + ".");
		}
	}

	@Override
	public void startObserving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopObserving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startHand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endHand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newStreet(char street) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fold(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void check(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void call(Player player, int callTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bet(Player player, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void raise(Player player, int raiseTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void win(Player player, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revealHands(List<Player> players) {
		// TODO Auto-generated method stub
		
	}

}
