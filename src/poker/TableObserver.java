package poker;

import java.util.List;

/**
 * Represents an observer of a {@link poker.Table} for handling all events at the table.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 *
 */
public abstract class TableObserver {
	/** Table to observe. */
	protected final Table table;
	/** Current game to observe. */
	protected Game game;

	/**
	 * Inits: {@link #table} as param table
	 * 
	 * @param table Table to be observed
	 */
	public TableObserver(Table table) {
		this.table = table;
		table.addObserver(this);
	}

	/**
	 * Starting the observation.
	 */
	public abstract void startObserving();

	/**
	 * Stopping the observation.
	 */
	public abstract void stopObserving();


	/**
	 * At the start of a hand.
	 */
	public abstract void startHand();

	/**
	 * At the end of a hand.
	 */
	public abstract void endHand();

	/**
	 * Indicate start of new street
	 * 
	 * @param round The street the game is on. Pre-flop ('p'), flop ('f'), turn
	 *              ('t'), river ('r'), or showdown ('s')
	 */
	public abstract void newStreet(char street);

	/**
	 * When a player folds.
	 * 
	 * @param player Player that folds
	 */
	public abstract void fold(Player player);

	/**
	 * When a player checks.
	 * 
	 * @param player Player that checks.
	 */
	public abstract void check(Player player);

	/**
	 * When a player calls.
	 * 
	 * @param player Player that calls
	 * @param callTo Amount to call to
	 */
	public abstract void call(Player player, int callTo);

	/**
	 * When a player bets.
	 * 
	 * @param player Player that bets
	 * @param amount Bet amount
	 */
	public abstract void bet(Player player, int amount);

	/**
	 * When a player raises.
	 * 
	 * @param player  Player that raises
	 * @param raiseTo Amount raised to
	 */
	public abstract void raise(Player player, int raiseTo);

	/**
	 * When a player wins the pot / a part of the pot.
	 * 
	 * @param player Player that wins
	 * @param amount Amount of pot player wins
	 */
	public abstract void win(Player player, int amount);
	
	/**
	 * When the players reveal their hands.
	 * @param players List of players to reveal their hands
	 */
	public abstract void revealHands(List<Player> players);

	public void setGame(Game game) {
		this.game = game;
	}
}
