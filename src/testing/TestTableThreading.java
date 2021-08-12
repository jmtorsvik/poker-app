package testing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import observing.PrinterWriter;
import observing.ServerWriter;
import observing.TextObserver;
import poker.Player;
import poker.Table;
import users.Bot;
import users.PrinterTextUser;
import users.ServerTextUser;

public class TestTableThreading {

	public static void main(String[] args) {
		int n = 3;
		
		Table table1 = new Table("TABLE_1", n);
		Table table2 = new Table("TABLE_2", n);
		
		Player p1 = new Player();
		p1.setUser(new PrinterTextUser("CONSOLE-USER"));
		table1.addPlayer(p1);
		Player p2 = new Player();
		ServerTextUser stu = new ServerTextUser("REMOTE-USER");
		p2.setUser(stu);
		table2.addPlayer(p2);
		
		Bot bot1 = new Bot("BOT_1");
		Bot bot2 = new Bot("BOT_2");
		
		Collection<Player> players1 = new ArrayList<>();
		Collection<Player> players2 = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
			Player p3 = new Player();
			p3.setUser(bot1);
			Player p4 = new Player();
			p4.setUser(bot2);
			
			players1.add(p3);
			players2.add(p4);
		}
		
		table1.addPlayers(players1);
		table2.addPlayers(players2);
		
		PrinterWriter pw = new PrinterWriter();
		ServerWriter sw = new ServerWriter();
		
		TextObserver obs1 = new TextObserver(table1, Arrays.asList(pw));
		obs1.startObserving();
		TextObserver obs2 = new TextObserver(table2, Arrays.asList(sw));
		obs2.startObserving();
		
		sw.startAccepting();
		System.out.println("Waiting for connection...");
		stu.accept();
		sw.stopAccepting();
		
		table1.start();
		table2.start();
	}
	
}
