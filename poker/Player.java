package poker;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a poker player.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Player {
	/** The entity who is in control of player's actions. */
	private User user;
	/** The table where this player is seated. */
	private Table table;
	/** Specific seat at table this player is seated at. */
	private int seat;
	/** In-game position. */
	private Position position;
	/** Current game this player is participating in. */
	private Game game;
	/** Complete hand player is holding. */
	private Hand hand;
	/** Cards held by player. */
	private Card[] cards = { null, null };
	/** If player is big blind, can only be true pre-flop. */
	private boolean isBigBlind = false;
	/** If player is sitting out of the next game at {@link #table}. */
	private boolean sittingOut = false;
	/** Amount of chips won or lost by player. */
	private int profit = 0;
	/** Last amount of chips put into pot by player. */
	private int lastBet = 0;
	/** Default bet size. */
	private int betSize = 10;
	/** Amount of chips possessed by player. */
	private int stack;

	public Player() {

	}

	/**
	 * Inits: {@link #stack} as param stack
	 * 
	 * @param stack Amount of chips instance possesses, set to 0 if less than 0
	 */
	public Player(int stack) {
		// Preprocessing
		if (stack < 0) {
			stack = 0;
		}

		// Inits
		this.stack = stack;
	}

	// -----------------
	// Handling actions:
	// -----------------
	
	/**
	 * {@link #user} decides what action player should take.
	 * 
	 * @see User#onAction(Player)
	 */
	public void onAction() {
		if (stack > 0) {
			user.onAction(this);
		}
	}

	/**
	 * Tells {@link #game} that player either checks or calls depending if there is
	 * a bet to call or not.
	 * 
	 * @see Game#checkOrCall(Player)
	 */
	public void checkOrCall() {
		game.checkOrCall(this);
	}

	/**
	 * Tells {@link #game} that player bets.
	 * 
	 * @param betTo Amount of chips to bet
	 * @see {@link Game#bet(Player, int)}
	 */
	public void bet(int betTo) {
		game.bet(this, betTo);
	}

	/**
	 * Tells {@link #game} that player folds.
	 * 
	 * @see Game#fold(Player)
	 */
	public void fold() {
		game.fold(this);
	}

	// -------------------
	// Player-adjustments:
	// -------------------

	/**
	 * Finds and {@link #hand} based on the cards on the board and the
	 * {@link #cards} of the player.
	 */
	protected void findHand() {
		// Cards to find hand of
		List<Card> cardsInHand = new ArrayList<>(7);

		// Add all cards from board to cardsInHand
		Card[] board = game.getBoard();
		for (int i = 0; i < 5; i++) {
			if (board[i] == null) {
				break;
			}
			cardsInHand.add(board[i]);
		}

		// Add the cards that player is holding to cardsInHand
		cardsInHand.add(cards[0]);
		cardsInHand.add(cards[1]);

		// Find and set the hand
		hand = new Hand(cardsInHand);
	}

	/**
	 * Adjusts the {@link #profit} of both player and {@link #master}.
	 * 
	 * @param amount Amount to adjust by
	 * @see User#adjustNetProfit(int)
	 */
	protected void adjustProfit(int amount) {
		profit += amount;
		user.adjustNetProfit(amount);
	}

	/**
	 * Adjusts the {@link #stack} as well as the {@link #profit} of the player
	 * 
	 * @param amount Amount to adjust by
	 * @see #adjustProfit(int)
	 */
	protected void adjustStack(int amount) {
		stack += amount;
		adjustProfit(amount);
	}

	// ----------------
	// Setters/Getters:
	// ----------------

	public void setUser(User user) {
		this.user = user;
		user.addPlayer(this);
	}

	public User getUser() {
		return user;
	}

	protected void setSeat(int seat) {
		this.seat = seat;
	}

	public int getSeat() {
		return seat;
	}

	protected void setTable(Table table) {
		this.table = table;
	}

	public Table getTable() {
		return table;
	}

	protected void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	protected void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public Hand getHand() {
		return hand;
	}

	protected void setCards(Card[] cards) {
		this.cards = cards;
	}

	public Card[] getCards() {
		return cards;
	}

	protected void setIsBigBlind(boolean isBigBlind) {
		this.isBigBlind = isBigBlind;
	}

	protected void setStack(int stack) {
		this.stack = stack;
	}

	public int getStack() {
		return stack;
	}

	public int getProfit() {
		return profit;
	}

	protected void setLastBet(int lastBet) {
		this.lastBet = lastBet;
	}

	public int getLastBet() {
		return lastBet;
	}

	public boolean getIsBigBlind() {
		return isBigBlind;
	}

	protected void setSittingOut(boolean sittingOut) {
		this.sittingOut = sittingOut;
	}

	public boolean getSittingOut() {
		return sittingOut;
	}

	public int getBetSize() {
		return betSize;
	}
}
