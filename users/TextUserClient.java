package users;

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
	
	public static void main(String[] args) throws IOException {
		Socket gameSocket = new Socket("localhost", 4999);
		Socket inputSocket = new Socket("localhost", 4998);
		
		BufferedReader gameReader = readerOnSocket(gameSocket);
		BufferedReader inputReader = readerOnSocket(inputSocket);
		
		Scanner scanner = new Scanner(System.in);
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
	
}
