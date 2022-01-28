package poker_app.user.text_user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TextUserClient {

	private static BufferedReader readerOnSocket(Socket s) throws IOException {
			InputStreamReader in = new InputStreamReader(s.getInputStream());
			return new BufferedReader(in);
	}
	
	private static void launch() throws IOException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Game port:");
		int gamePort = Integer.parseInt(scanner.nextLine());
		
		System.out.print("Input port:");
		int inputPort = Integer.parseInt(scanner.nextLine());
		
		
		Socket gameSocket = new Socket("localhost", gamePort);
		Socket inputSocket = new Socket("localhost", inputPort);
		
		BufferedReader gameReader = readerOnSocket(gameSocket);
		BufferedReader inputReader = readerOnSocket(inputSocket);
		
		PrintWriter printWriter = new PrintWriter(inputSocket.getOutputStream());
		
		while (true) {
			if (gameReader.ready()) {
				while (gameReader.ready()) {
					System.out.println(gameReader.readLine());
				}
			}
			if (inputReader.ready()) {
				System.out.print(inputReader.readLine());
				while (inputReader.ready()) {
					System.out.println();
					System.out.print(inputReader.readLine());
				}
				
				printWriter.println(scanner.nextLine());
				printWriter.flush();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			launch();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
}
