package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A {@link TextUser} that takes input using sockets.
 * <p>
 * Last modified: 5 April 2021
 * 
 * @author Jakob Martin Torsvik
 *
 */
public class ServerTextUser extends TextUser {
	/** Port to create server socket on. */
	private final int port = 4998;
	/** Socket where client connects to. */
	private ServerSocket serverSocket;
	/** For writing on {@link #serverSocket}. */
	private PrintWriter printWriter;
	/** For reading on {@link #serverSocket}. */
	private BufferedReader reader;
	
	/**
	 * Calls {@link TextUser#TextUser(String name)}.
	 * <p>
	 * Inits: {@link #serverSocket} as a {@link ServerSocket} on {@link #port}.
	 */
	public ServerTextUser(String name) {
		super(name);
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error while initializing server socket on port " + port + ".");
		}
	}
	
	/**
	 * Look for and accept a connection on {@link #serverSocket}.
	 */
	public void accept() {
		try {
			// Accept connection and initialize printWriter
			Socket s = serverSocket.accept();
			printWriter = new PrintWriter(s.getOutputStream());
			
			// Get inputStream and initialize reader
			InputStreamReader in = new InputStreamReader(s.getInputStream());
			reader = new BufferedReader(in);
			
		} catch (IOException e) {
			System.out.println("Error while accepting on server socket.");
		}
	}

	@Override
	protected String userInput(String line) {
		// Print line to socket
		printWriter.println(line.toString());
		printWriter.flush();
		
		// Read from socket
		String result = null;
		try {
			result = reader.readLine();
		} catch (IOException e) {
			System.out.println("Error while reading from server socket.");
		}
		return result;
	}

}
