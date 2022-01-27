package poker_app.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Writer} that writes to all connections on a {@link ServerSocket}.
 * <p>
 * Last modified: 4 April 2021
 *
 * @author Jakob Martin Torsvik
 *
 */
public class ServerWriter implements Writer, Runnable {
	// CONSTANTS
	/**
	 * List of {@link PrintWriter}s for writing to connections on
	 * {@link #serverSocket}.
	 */
	private final List<PrintWriter> printWriters = new ArrayList<>();
	/** Port to create server socket on. */
	private final int port;
	/** Maximum number of connections on {@link #serverSocket}. */
	private final int maxConns;

	// NON-CONSTANTS
	/** Socket where clients connects to. */
	private ServerSocket serverSocket;
	/**
	 * True if currently accepting new connections on {@link #serverSocket}. False
	 * otherwise.
	 */
	private boolean accepting = false;
	/** Thread for looking for and accepting new connections. */
	private Thread acceptThread;

	/**
	 * Inits: {@link #serverSocket} as a {@link ServerSocket} on {@link #port}.
	 */
	public ServerWriter(int port, int maxConns) {
		this.port = port;
		this.maxConns = maxConns;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ServerWriter: Error while initializing server socket on port " + port + ".");
		}
	}

	/**
	 * Initializes {@link acceptThread} to start accepting connections.
	 */
	public synchronized void startAccepting() {
		// Now accepting connections
		accepting = true;
		
		// Initialize and start acceptThread
		acceptThread = new Thread(this);
		acceptThread.start();
	}
	
	/**
	 * Makes {@link #serverSocket} stop accepting connections.
	 */
	public synchronized void stopAccepting() {
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
	
	/**
	 * Runs {@link ServerSocket#accept()} to look for and accept connections.
	 */
	@Override
	public void run() {
		while (accepting && printWriters.size() < maxConns) {
			try {
				Socket s = serverSocket.accept();
				printWriters.add(new PrintWriter(s.getOutputStream()));
			} catch (IOException e) {
				System.out.println("Error while accepting on server socket.");
			}
		}
	}

	/**
	 * Writes one line on all connected sockets using {@link #printWriters}.
	 */
	@Override
	public void write(String line) {
		for (PrintWriter pw : printWriters) {
			pw.println(line);
			pw.flush();
		}
	}

}
