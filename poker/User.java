package poker;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a user that can control multiple players.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public abstract class User {
	/** Name of user. */
	private final String name;
	/** Controlled players. */
	private final Collection<Player> players = new LinkedList<>();
	/** Amount of chips won or lost. */
	private int netProfit = 0;

	/**
	 * Inits: {@link #name} as param name
	 * 
	 * @param name Name of user
	 */
	public User(String name) {
		this.name = name;
	}

	/**
	 * Decides if player should fold ({@link Player#fold()}), check/call
	 * ({@link Player#checkOrCall()}) or bet/raise some amount
	 * ({@link Player#bet(int)}).
	 * 
	 * @param player Player to make decision, should be in {@link #players}
	 */
	public abstract void onAction(Player player);

	/**
	 * Decides if player should rebuy.
	 * 
	 * @param player Player to make decision, should be in {@link #players}
	 * @return True if player is to rebuy, False otherwise
	 */
	public abstract boolean rebuy(Player player);
	
	/**
	 * Decides if player should sit out next hand.
	 * 
	 * @param player Player to make decision, should be in {@link #players}
	 * @return True if player will sit out next hand, False otherwise
	 */
	public abstract boolean sitOutNextHand(Player player);

	/**
	 * Picks seat for player.
	 * 
	 * @param player Player to pick seat for
	 * @return The number of the picked seat
	 */
	public abstract int pickSeat(Player player);
	
	/**
	 * Chooses a starting stack for player.
	 * 
	 * @param player Player to choose stack for
	 * @return Chosen stack size
	 */
	public abstract int chooseStartingStack(Player player);
	
	/**
	 * Adds a single player to {@link #players}
	 * 
	 * @param player Player to be added
	 */
	public void addPlayer(Player player) {
		players.add(player);
	}

	/**
	 * Removes param player from {@link #players}
	 * 
	 * @param player Player to be removed, should be in {@link #players}
	 */
	public void removePlayer(Player player) {
		validPlayer(player);
		players.remove(player);
	}

	/**
	 * Adjusts {@link #netProfit} by param amount
	 * 
	 * @param amount Amount of chips to adjust by
	 */
	public void adjustNetProfit(int amount) {
		netProfit += amount;
	}

	/**
	 * Checks if param player is in {@link #players}
	 * 
	 * @throws IllegalArgumentException if param player is not in {@link #players}
	 * @param player Player to check for
	 */
	public void validPlayer(Player player) {
		if (!players.contains(player)) {
			throw new IllegalArgumentException("Player must be in field Collection<Player> players.");
		}
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	@Override
	public String toString() {
		return name;
	}

}
