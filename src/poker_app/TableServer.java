package poker_app;

import java.util.Scanner;

import poker_app.game.Player;
import poker_app.observer.TextObserver;
import poker_app.user.ServerTextUser;
import poker_app.writer.ServerWriter;

public class TableServer {
	private static Table table;
	private static final int SERVER_PORT = 4999;
	private static final Scanner SCANNER = new Scanner(System.in);
	
	private static void launch() {
		System.out.println("How many players?");
		int numSeats = Integer.parseInt(SCANNER.nextLine());
		
		while (numSeats < 2 || numSeats > 3) {
			System.out.println("How many players?");
			numSeats = Integer.parseInt(SCANNER.nextLine());
		}
		
		table = new Table("Table 1", numSeats);
		ServerWriter serverWriter = new ServerWriter(SERVER_PORT, numSeats);
		new TextObserver(table, serverWriter);
		System.out.println("Writing on port " + SERVER_PORT);
		serverWriter.startAccepting();
		
		for (int i = 0; i < numSeats; i++) {
			Player p = new Player();
			int port = SERVER_PORT - i - 1;
			ServerTextUser user = new ServerTextUser("User " + (i + 1), port);
			p.setUser(user);
			System.out.println(p.getUser() + " takes input on port " + port);
			table.addPlayer(p);
			user.accept();
		}
		
		table.start();
	}
	
	public static void main(String[] args) {
		launch();
	}
	
}
