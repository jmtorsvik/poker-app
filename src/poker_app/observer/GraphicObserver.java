package poker_app.observer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import poker_app.Table;
import poker_app.game.Player;

public class GraphicObserver extends TableObserver {
	private final int maxConns = 1;
	private final List<Socket> connections = new ArrayList<>(maxConns);
	private ServerSocket serverSocket;
	private boolean accepting = false;
	private Thread acceptThread;
	
	public GraphicObserver(Table table, int port) {
		super(table);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("GraphicObserver: Error while initializing server socket on port " + port + ".");
		}
	}
	
	public void startAccepting() {
		// Now accepting connections
		accepting = true;
		
		// Initialize and start acceptThread
		acceptThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (accepting && connections.size() < maxConns) {
					try {
						Socket s = serverSocket.accept();
						connections.add(s);
					} catch (IOException e) {
						System.out.println("Error while accepting on server socket.");
					}
				}
			}
		});
		acceptThread.start();
	}
	
	public void stopAccepting() {
		// No longer accepting new connections
		accepting = false;
		
		// Close serverSocket
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error while closing server socket.");
		}
		
		// Join acceptThread
		try {
			acceptThread.join();
		} catch (InterruptedException e) {
			System.out.println("Error while joining thread " + acceptThread + ".");
		}
	}

	@Override
	public void startObserving() {
		
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
