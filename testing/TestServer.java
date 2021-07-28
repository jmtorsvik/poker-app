package testing;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import observing.*;
import poker.*;
import users.*;

public class TestServer {

	public static void main(String[] args) throws InterruptedException {
		int n = 6;
		Collection<Player> players = new LinkedList<>();
		for (int i = 0; i < n-1; i++) {
			Player p = new Player();
			p.setUser(new Bot("B-" + i));
			players.add(p);
		}
		Player p1 = new Player();
		p1.setUser(new PrinterTextUser("Console-user"));
		players.add(p1);
		
//		Player p2 = new Player();
//		ServerTextUser stu = new ServerTextUser("Server-user");
//		p2.setUser(stu);
//		players.add(p2);
		
		PrinterWriter pw = new PrinterWriter();
		LocalFileWriter lfw = new LocalFileWriter(
				"C:\\Users\\Bruker\\eclipse-workspace\\poker-application\\test-hands\\Test_1.txt");
		ServerWriter sw = new ServerWriter();
		sw.startAccepting();
		
		System.out.println("Waiting for client...");
//		stu.accept();
		
		Table table = new Table("TEST", n);
		table.addPlayers(players);
		TextObserver txt = new TextObserver(table, Arrays.asList(pw));

		lfw.open();
		txt.startObserving();
		
		table.start();
		
//		sw.stopAccepting();
		txt.stopObserving();
		lfw.close();
		
	}

}
