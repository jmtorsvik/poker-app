package poker;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a table for running games of poker of class {@link poker.Game}.
 * <p>
 * Last modified: 6 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Table implements Runnable {
	// CONSTANTS
	/** Number of seats at table. */
	private final int numSeats;
	/** List of players waited to be seated. */
	private final List<Player> playerQueue = new LinkedList<>();
	/** List of seated players. */
	private final List<Player> seats;
	/** List of active positions. */
	private final List<Position> positions;
	/** Name of table. */
	private final String name;
	/** Observers of the table. */
	private final Collection<TableObserver> observers = new ArrayList<>();

	// NON-CONSTANTS
	/** Is table running. */
	private boolean running = false;
	/** Running thread. */
	private Thread tableThread;
	/** Chips in small blind. */
	private int smallBlind = 5;
	/** Chips in big blind */
	private int bigBlind = 10;
	/** Minimum chips in starting stack. */
	private int minStartingStack = 50 * bigBlind;
	/** Maximum chips in starting stack. */
	private int maxStartingStack = 100 * bigBlind;
	/** Number of hands played. */
	private int handsPlayed = 0;

	public Table(String name, int numSeats) {
		// Preprocessing
		if (numSeats < 2) {
			numSeats = 2;
		} else if (numSeats > 23) {
			numSeats = 23;
		}

		// Inits
		this.name = name;
		this.numSeats = numSeats;

		// Init seats as a list of null values of length numSeats
		seats = new ArrayList<>(numSeats);
		seats.addAll(Arrays.asList(new Player[numSeats]));

		// Init positions as a list of length numSeats
		positions = new ArrayList<>(numSeats);
	}

	/**
	 * Starts running of the table using {@link #tableThread}.
	 */
	public synchronized void start() {
		// Table is now running
		running = true;

		// Start thread
		tableThread = new Thread(this, name);
		tableThread.start();
	}

	/**
	 * Stops running of the table.
	 */
	public synchronized void stop() {
		// Table is no longer running
		running = false;

		// Stop thread
		try {
			tableThread.join();
		} catch (InterruptedException e) {
			System.out.println("Error while joining thread " + tableThread + ".");
		}
	}

	@Override
	public void run() {
		while (running) {
			// Seat waiting players
			seatWaitingPlayers();

			// If any players will sit in or out
			sittingInOrOut();

			// Play one hand
			play();

			// Rotate
			rotate();
		}
	}

	/**
	 * Seats waiting players while there are waiting players and table is not full.
	 * Finds new positions if any players gets seated.
	 */
	private void seatWaitingPlayers() {
		boolean findPos = false;

		// Loop while playerQueue is not empty and table is not full
		while (!(playerQueue.isEmpty() || full())) {
			findPos = true;

			// Get first player in queue and its user
			Player p = playerQueue.get(0);
			User u = p.getUser();

			// Seat player at picked seat
			int seat = u.pickSeat(p);
			seats.set(seat, p);
			p.setSeat(seat);

			// Set players stack to chosen starting stack
			p.setStack(u.chooseStartingStack(p));

			// Remove player from queue
			playerQueue.remove(0);
		}

		if (findPos) {
			findPositions();
		}
	}

	/**
	 * Checks for all seated players if they will sit in or out. Finds new positions
	 * if any does so.
	 */
	private void sittingInOrOut() {
		boolean findPos = false;

		for (Player p : seats) {
			if (p != null) {
				User u = p.getUser();
				boolean sitOut;
				if (p.getStack() == 0) {
					// Check if player will rebuy
					if (u.rebuy(p)) {
						// Choose starting stack and ask if player will sit out
						p.setStack(u.chooseStartingStack(p));
						sitOut = u.sitOutNextHand(p);
					} else {
						// If player will not rebuy they will sit out
						sitOut = true;
					}
				} else {
					// Ask if player will sit out
					sitOut = u.sitOutNextHand(p);
				}

				// Will find new positions if player changes their sit-out status
				if (p.getSittingOut() != sitOut) {
					p.setSittingOut(sitOut);
					findPos = true;
				}
			}
		}

		if (findPos) {
			findPositions();
		}
	}

	/**
	 * Goes through one {@link #Game}.
	 */
	public void play() {
		List<Player> pInGame = playersInGame();

		// Only play the game if there are more than one player
		if (pInGame.size() > 1) {
			// Create game
			Game game = new Game(pInGame, smallBlind, bigBlind);

			// Add and start hand for observers
			for (TableObserver obs : observers) {
				game.addObserver(obs);
				obs.startHand();
			}

			// Play the game and end hand for observers
			game.play();
			for (TableObserver obs : observers) {
				obs.endHand();
			}

			// Increment handsPlayed
			handsPlayed++;
		}
	}

	/**
	 * Rotates the {@link Positions}s of the players clockwise.
	 */
	private void rotate() {
		for (Player p : seats) {
			if (p != null) {
				// Find current position value
				int pos = p.getPosition().getPos();
				if (pos == 0) {
					// Set position to BTN if current is SB
					p.setPosition(positions.get(countSeatedPlayers(true) - 1));
				} else {
					// Otherwise set position to one less than current
					p.setPosition(positions.get(pos - 1));
				}
			}
		}
	}
	
	/**
	 * Finds all seated players that are not sitting out to include in game.
	 * 
	 * @return A List of the players to be included, ordered by their
	 *         {@link Position}
	 */
	private List<Player> playersInGame() {
		// Creates a list of (number of seated players that are not sitting out) null objects
		List<Player> pInGame = Arrays.asList(new Player[countSeatedPlayers(true)]);

		// Adds players in seats that are not null and not sitting out to playersInGame
		for (Player p : seats) {
			if (p != null && !p.getSittingOut()) {
				pInGame.set(p.getPosition().getPos(), p);
			}
		}

		return pInGame;
	}

	/**
	 * Finds and sets new {@link Position}s in {@link #positions} and for each
	 * seated {@link Player} that is not sitting out.
	 */
	private void findPositions() {
		// Clear positions and add a new position for each player in seats
		positions.clear();
		for (Player p : seats) {
			if (p != null && !p.getSittingOut()) {
				Position pos = new Position(positions.size(), countSeatedPlayers(true));
				positions.add(pos);
				p.setPosition(pos);
			}
		}
	}

	/**
	 * Checks if table is full.
	 * 
	 * @return True if table is full. False if there are one or more available
	 *         seats.
	 */
	private boolean full() {
		return !seats.stream().anyMatch((player) -> player == null);
	}

	/**
	 * Adds a {@link Collection} of players to {@link #playerQueue}.
	 * 
	 * @param players Players to be added
	 */
	public void queuePlayers(Collection<Player> players) {
		for (Player p : players) {
			queuePlayer(p);
		}
	}

	/**
	 * Adds a player to {@link #playerQueue}.
	 * 
	 * @param player Player to be added
	 */
	public void queuePlayer(Player player) {
		if (player != null) {
			playerQueue.add(player);
		}
	}

	/**
	 * Removes a specific player from {@link #playerQueue}.
	 * 
	 * @param player Player to be removed
	 */
	public void deQueuePlayer(Player player) {
		if (player != null) {
			playerQueue.remove(player);
		}
	}

	/**
	 * Counts seated players.
	 * 
	 * @param onlySittingIn If only players that are sitting in should be counted
	 * @return Number of seated players
	 */
	public int countSeatedPlayers(boolean onlySittingIn) {
		int result = 0;
		for (Player p : seats) {
			if (p != null && !(onlySittingIn && p.getSittingOut())) {
				result++;
			}
		}
		return result;
	}
	
	/**
	 * Adds a {@link Collection} of players to {@link #seats}.
	 * @param players Players to add
	 */
	public void addPlayers(Collection<Player> players) {
		for (Player p : players) {
			addPlayer(p);
		}
	}
	
	/**
	 * Adds player to {@link #playerQueue}.
	 * @param player Player to add
	 */
	public void addPlayer(Player player) {
		player.setTable(this);
		playerQueue.add(player);
	}
	
	/**
	 * Removes a specific player from {@link #seats} and finds new positions.
	 * @param player Player to remove
	 */
	public void removePlayer(Player player) {
		seats.remove(player);
		findPositions();
	}

	/**
	 * Adds an observer of the table to {@link #observers}.
	 * @param observer Observer to add
	 */
	public void addObserver(TableObserver observer) {
		observers.add(observer);
	}
	
	public List<Player> getSeats() {
		return seats;
	}

	public String getName() {
		return name;
	}
	
	public int getSmallBlind() {
		return smallBlind;
	}
	
	public int getBigBlind() {
		return bigBlind;
	}

	public int getMinStartingStack() {
		return minStartingStack;
	}

	public int getMaxStartingStack() {
		return maxStartingStack;
	}
	
	public int getHandsPlayed() {
		return handsPlayed;
	}

	public static void main(String[] args) {
		Table table = new Table("Test", 2);
		System.out.println(table.full());
		Player p1 = new Player();

	}
}
