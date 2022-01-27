package poker_app.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import poker_app.observer.TableObserver;


/**
 * Represents one game of poker.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Game {
	// CONSTANTS
	/** Chips in small blind. */
	private int smallBlind;
	/** Chips in big blind. */
	private final int bigBlind;
	/** Deck where the cards are dealt from. */
	private final Deck deck = new Deck();
	/** For iterating through deck. */
	private final Iterator<Card> cIterator = deck.iterator();
	/** List of initial players. */
	private final List<Player> players;
	/** List of players that has not yet folded. */
	private final List<Player> playersLeft;
	/**
	 * List of amount of chips each player is eligible to win based on
	 * contributions.
	 */
	private final List<Integer> subPots;
	/** For observation of game. */
	private final Collection<TableObserver> observers = new ArrayList<>();
	
	// NON-CONSTANTS
	/** For iterating through players. */
	private Iterator<Player> pIterator;
	/** Last player to bet/raise in a round. */
	private Player lastBettor;
	/** Total amount of chips put into pot by all players. */
	private int pot = 0;
	/** Last bet made on current betting round. */
	private int lastBet = 0;
	/** Last raise made on current betting round. */
	private int lastRaise = 0;
	/** Number of bets/raises in a round. */
	private int numBets = 0;
	/** List of amount of chips each player has put into pot. */
	private List<Integer> contributions;
	/** If game is finished or not. */
	private boolean gameFinished = false;
	/** If current betting round is finished or not. */
	private boolean roundFinished = false;
	/** If all players are all in. */
	private boolean allAllIn = false;
	/**
	 * Current round of betting, 'p' = pre-flop, 'f' = flop, 't' = turn, 'r' =river,
	 * 's' = show-down.
	 */
	private char street = 'p';
	/** The cards that has been dealt face up. */
	private Card[] board = { null, null, null, null, null };

	/**
	 * Inits: {@link #players} as param players, {@link #bigBlind} as param
	 * bigBlind, {@link #smallBlind} as param smallBlind, {@link #playersLeft} as a
	 * {@link java.util.LinkedList} of param players, {@link #pIterator} as a
	 * {@link java.util.Iterator} of playersLeft, {@link #subPots} as a
	 * {@link java.util.ArrayList}, {@link #contributions} as a
	 * {@link java.util.ArrayList}
	 * <p>
	 * 
	 * @param players    Players to participate in game, size in [2, 24)
	 * @param bigBlind   Amount of chips that makes up the big blind, set to 1 if
	 *                   less than 1
	 * @param smallBlind Amount of chips that makes up the small blind, set to
	 *                   bigBlind if greater than bigBlind, set to 1 if less than 1
	 * @throws IllegalArgumentException if size of players is less than 2 or greater
	 *                                  than 23
	 */
	public Game(List<Player> players, int smallBlind, int bigBlind) {
		// Preprocessing
		if (bigBlind < 1) {
			bigBlind = 1;
		}
		if (smallBlind > bigBlind) {
			smallBlind = bigBlind;
		} else if (smallBlind < 1) {
			smallBlind = 1;
		}

		int s = players.size();
		
		// Exceptions
		if (s < 2 || s > 23) {
			throw new IllegalArgumentException("The size of List<Player> players must be in the interval [2, 24).");
		}

		// Inits
		this.players = players;
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		
		playersLeft = new LinkedList<>(players);
		pIterator = playersLeft.iterator();
		subPots = new ArrayList<>(s);
		contributions = new ArrayList<>(s);

		// Handles players and deals cards:
		for (Player player : players) {
			player.setGame(this);

			Card[] cards = { cIterator.next(), cIterator.next() };
			player.setCards(cards);

			subPots.add(null);
			contributions.add(0);
		}
	}

	// ---------------
	// Course of game:
	// ---------------

	/**
	 * The game loop as a switch for {@link #street}. Proceeds with the following
	 * methods based on cases: 'p': {@link #preFlop()}, 'f': {@link #flop()}, 't':
	 * {@link #turn()}, 'r': {@link #river()}, 's': {@link #showDown()}.
	 */
	public void play() {
		// Loop as long as game is not finished
		while (!gameFinished) {
			// Switch for what street the game is on
			switch (street) {
			case 'p':
				// Pre-flop
				preFlop();
				break;
			case 'f':
				// Flop
				flop();
				break;
			case 't':
				// Turn
				turn();
				break;
			case 'r':
				// River
				river();
				break;
			case 's':
				// Show-down
				showDown();
				break;
			}
		}
	}

	// --------------------
	// Per specific street:
	// --------------------

	/**
	 * Handles the course of the pre-flop street.
	 * 
	 * @see #blinds()
	 * @see #round()
	 */
	private void preFlop() {
		// Observers: new street
		for (TableObserver obs : observers) {
			obs.newStreet(street);
		}

		// Posting of the blinds
		blinds();

		// If there are two players, the order of the betting should be switched
		if (players.size() == 2) {
			Player p = playersLeft.get(0);
			playersLeft.set(0, playersLeft.get(1));
			playersLeft.set(1, p);
		}

		// Round of betting
		round();

		// There is no longer a big blind in play
		for (Player player : players) {
			player.setIsBigBlind(false);
		}

		// Next street is the flop
		street = 'f';
	}

	/**
	 * Handles the course of the flop street.
	 * 
	 * @see #round()
	 */
	private void flop() {
		// Put three cards on board
		for (int i = 0; i < 3; i++) {
			board[i] = cIterator.next();
		}

		// Observers: new street
		for (TableObserver obs : observers) {
			obs.newStreet(street);
		}
		
		// Round of betting
		round();

		// Next street is the turn
		street = 't';
	}

	/**
	 * Handles the course of the turn street.
	 * 
	 * @see #round()
	 */
	private void turn() {
		// Put fourth card on board
		board[3] = cIterator.next();

		// Observers: new street
		for (TableObserver obs : observers) {
			obs.newStreet(street);
		}
		
		// Round of betting
		round();

		// Next street is the river
		street = 'r';
	}

	/**
	 * Handles the course of the river street.
	 * 
	 * @see #round()
	 */
	private void river() {
		// Put fifth card on board
		board[4] = cIterator.next();

		// Observers: new street
		for (TableObserver obs : observers) {
			obs.newStreet(street);
		}
		
		// Round of betting
		round();

		// Next street is the show-down
		street = 's';
	}

	/**
	 * Handles the show-down of the remaining players' hands.
	 */
	private void showDown() {
		// Find the hand and sub-pots of all players remaining
		for (Player p1 : playersLeft) {
			p1.findHand();
			
			int i = players.indexOf(p1);
			int c1 = contributions.get(i);
			int subPot = 0;
			
			// Each player may win maximum what they have contributed from each player
			for (Player p2 : players) {
				int j = players.indexOf(p2);
				int c2 = contributions.get(j);
				subPot += Integer.min(c1, c2);
			}
			
			subPots.set(i, subPot);
		}

		// Observers: new street and reveal hands of players left
		for (TableObserver obs : observers) {
			obs.newStreet(street);
			obs.revealHands(playersLeft);
		}
		
		// Sort remaining players by hand values in descending order
		int numPlayers = playersLeft.size();
		List<Player> sortedByHand = new ArrayList<>(numPlayers);
		sortedByHand.addAll(playersLeft);
		sortedByHand.sort(new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {
				return p2.getHand().compareTo(p1.getHand());
			}
		});

		// List of lists of players with equal hand values
		List<List<Player>> winners = new ArrayList<>(numPlayers);

		// Add player with best hand
		List<Player> first = new LinkedList<>();
		first.add(sortedByHand.get(0));
		winners.add(first);

		// Add players with equal hand values to the same list in winners
		for (int i = 1; i < sortedByHand.size(); i++) {
			Player p1 = sortedByHand.get(i);
			Player p2 = sortedByHand.get(i - 1);
			if (p1.getHand().compareTo(p2.getHand()) == 0) {
				int ws = winners.size();
				List<Player> l = winners.get(ws - 1);
				l.add(p1);
				winners.set(ws - 1, l);
			} else {
				List<Player> l = new LinkedList<>();
				l.add(p1);
				winners.add(l);
			}
		}

		// Pay players
		for (List<Player> ties : winners) {
			// When pot is empty stop paying
			if (pot == 0) {
				break;
			}

			// List for storing chip amounts each player in ties should get
			int tiedPlayers = ties.size();
			List<Integer> amounts = new ArrayList<>(tiedPlayers);
			for (int i = 0; i < tiedPlayers; i++) {
				amounts.add(0);
			}

			// Sort tied players by subPot size in ascending order
			ties.sort(new Comparator<Player>() {
				@Override
				public int compare(Player p1, Player p2) {
					int subPot1 = subPots.get(players.indexOf(p1));
					int subPot2 = subPots.get(players.indexOf(p2));
					return subPot2 - subPot1;
				}
			});

			// Find the chip amounts each tied player should get
			int localPot = pot;
			for (int i = 0; i < tiedPlayers; i++) {
				// Stop if no chips left in local pot
				if (localPot == 0) {
					break;
				}

				// Find total amount of chips player should win
				Player player = ties.get(i);
				int subPot = subPots.get(players.indexOf(player));
				int total = Integer.min(subPot, localPot);
				localPot -= total;

				// Increase the amounts to win for each player by the total to be payed out
				// divided by number of tied players
				int split = tiedPlayers - i;
				for (int j = i; j < tiedPlayers; j++) {
					amounts.set(j, amounts.get(j) + total / split);
				}

				// Remaining amount of chips to be split by tied players
				int rest = total % split;
				List<Integer> splitRest = new ArrayList<>(tiedPlayers);
				for (int j = i; j < tiedPlayers; j++) {
					splitRest.add(j);
				}

				// Pay one chip at a time from rest at random
				for (int j = 0; j < rest; j++) {
					int r = new Random().nextInt(splitRest.size());
					int k = splitRest.get(r);
					amounts.set(k, amounts.get(k) + 1);
					splitRest.remove(r);
				}
			}

			// Pay appropriate amount to each tied player
			for (int i = 0; i < tiedPlayers; i++) {
				win(ties.get(i), amounts.get(i));
			}
		}

		// Game is finished
		gameFinished = true;
	}

	// ------------------
	// Per betting-round:
	// ------------------

	/**
	 * Handles one round of betting.
	 */
	private void round() {
		// Make sure that round is not finished
		roundFinished = false;

		// Player on action
		Player currentPlayer;
		
		// Action before first bet is put into pot
		while (lastBet == 0 && !allAllIn && !roundFinished) {
			// Go through all players and finish round if no one bets
			if (pIterator.hasNext()) {
				currentPlayer = pIterator.next();
				currentPlayer.onAction();
			} else {
				finishRound();
			}

			// Check if all players have folded or are all in
			allFold();
			allAllIn();
		}

		// If someone has put in a bet
		while (!allAllIn && !roundFinished) {
			// Restart player iteration if end is reached
			if (!pIterator.hasNext()) {
				pIterator = playersLeft.iterator();
			}

			// Player on action
			currentPlayer = pIterator.next();

			// Handle if player was last to bet, otherwise player is to act
			if (currentPlayer == lastBettor) {
				if (!currentPlayer.getIsBigBlind()) {
					// Finish round if player is not the big blind
					finishRound();
				} else {
					// Otherwise change last bettor to another player
					int i = playersLeft.indexOf(currentPlayer);
					if (playersLeft.size() != 2 || i == 0) {
						lastBettor = playersLeft.get(i + 1);
					} else {
						lastBettor = playersLeft.get(0);
					}

					// Player is to act
					currentPlayer.onAction();
				}
			} else {
				currentPlayer.onAction();
			}

			// Check if all players have folded or are all in
			allFold();
			allAllIn();
		}

	}

	/**
	 * Handles the finishing and resetting after a betting street
	 */
	private void finishRound() {
		lastBet = 0;
		lastRaise = 0;
		numBets = 0;
		lastBettor = null;
		for (Player player : players) {
			player.setLastBet(0);
		}
		pIterator = playersLeft.iterator();
		roundFinished = true;
	}

	// ---------------
	// Player actions:
	// ---------------

	/**
	 * Handles when a player is checking if {@link #lastBet} is 0, or calling
	 * otherwise.
	 * 
	 * @param player Player that is checking or calling
	 */
	public void checkOrCall(Player player) {
		// Amount to call
		int plb = player.getLastBet();
		int amount = lastBet - plb;
		int stack = player.getStack();
		if (stack + plb < lastBet) {
			amount = stack;
		}

		// Player contributes amount to pot
		contribute(player, amount);
		
		// Observers: player checked or called
		boolean didCheck = plb == lastBet;
		for (TableObserver obs : observers) {
			if (didCheck) {
				obs.check(player);
			} else {
				obs.call(player, lastBet);
			}
		}

		// Set last amount put into pot by player
		player.setLastBet(plb + amount);
	}

	/**
	 * Handles when a player is betting.
	 * 
	 * @param player Player that is betting
	 * @param betTo  Amount player is betting to
	 */
	public void bet(Player player, int betTo) {
		// Control the bet
		betTo = controlBet(player, betTo);

		// Bet is only valid if it is larger than the last bet
		if (betTo > lastBet) {
			// Player is no longer Big Blind
			player.setIsBigBlind(false);

			// How much more player needs to contribute
			int bet = betTo - player.getLastBet();
			contribute(player, bet);

			// Bet is a raise if the last bet is not zero
			if (lastBet != 0) {
				lastRaise = betTo - lastBet;
			}

			// Set some variables
			lastBet = betTo;
			player.setLastBet(betTo);
			lastBettor = player;
			numBets++;
			
			// Observers: player bet or raised
			boolean didBet = lastRaise == 0;
			for (TableObserver obs : observers) {
				if (didBet) {
					obs.bet(player, betTo);
				} else {
					obs.raise(player, betTo);
				}
			}
		}
	}

	/**
	 * Handles when a player is folding.
	 * 
	 * @param player Player that is folding
	 */
	public void fold(Player player) {
		// Remove player from the game
		pIterator.remove();
		
		// Observers: player folded
		for (TableObserver obs : observers) {
			obs.fold(player);
		}
	}

	/**
	 * Controls if the bet made by a player is valid, should be an all-in or should
	 * be a check/call instead.
	 * 
	 * @param player Player that is betting
	 * @param betTo  Amount player is betting to
	 * @return
	 */
	private int controlBet(Player player, int betTo) {
		// Size of the bet for player to be all-in
		int allIn = player.getStack() + player.getLastBet();

		// Corrections of the bet size
		if (lastBet > 0) {
			// If the bet size for player to be all in is less than the last bet player may
			// only call
			if (allIn < lastBet) {
				checkOrCall(player);
				betTo = allIn;
			} else {
				// Player needs to bet at least twice the last bet or the sum of the last bet
				// and the last raise
				int minBet = (lastRaise > 0) ? lastBet + lastRaise : 2 * lastBet;
				if (betTo < minBet) {
					betTo = (allIn < minBet) ? allIn : minBet;
				}
			}
		}
		// Minimum size of a bet is one big blind
		else if (allIn >= bigBlind && betTo < bigBlind) {
			betTo = bigBlind;
		}

		// Player can not bet larger than all-in
		if (betTo > allIn) {
			betTo = allIn;
		}

		return betTo;
	}

	/**
	 * Records the contribution of a player to the pot.
	 * 
	 * @param player Player that is putting chips into pot
	 * @param amount Amount to put into pot
	 */
	private void contribute(Player player, int amount) {
		// Remove chips from stack
		player.adjustStack(-amount);

		// Adds the players contribution to the list of player contributions
		int i = players.indexOf(player);
		contributions.set(i, contributions.get(i) + amount);

		// Increase the size of the pot
		increasePot(amount);
	}

	// ------
	// Other:
	// ------

	/**
	 * Handles the posting of the blinds.
	 */
	private void blinds() {
		smallBlind(playersLeft.get(0));
		bigBlind(playersLeft.get(1));
		pIterator.next();
		if (playersLeft.size() > 2) {
			pIterator.next();
		}
	}

	/**
	 * Handles the posting of the small blind
	 * 
	 * @param player Player in the Small Blind
	 */
	private void smallBlind(Player player) {
		int amount = Integer.min(smallBlind, player.getStack());
		contribute(player, amount);
		player.setLastBet(amount);
	}

	/**
	 * Handles the posting of the big blind
	 * 
	 * @param player Player in the Big Blind
	 */
	private void bigBlind(Player player) {
		bet(player, bigBlind);
		player.setIsBigBlind(true);
	}

	/**
	 * Handles when there are only one player left who then will win the pot.
	 */
	private void allFold() {
		if (playersLeft.size() == 1) {
			win(playersLeft.get(0), pot);
		}
	}

	/**
	 * Handles when all players are all-in and there are no more possible bets to be
	 * made.
	 */
	private void allAllIn() {
		// Counter for players that are not all-in
		int notAllIn = playersLeft.size();

		// If there are any player who is yet to act
		boolean yetToAct = false;

		for (Player player : playersLeft) {
			// If player's stack is 0 then they are all-in
			if (player.getStack() == 0) {
				notAllIn--;
			}
			// If player is not all in and their last contribution to the pot is less than
			// the last bet, they are yet to act
			else if (player.getLastBet() < lastBet) {
				yetToAct = true;
				break;
			}
		}

		// Stores the result in the boolean field allAllIn
		allAllIn = (notAllIn > 1 || yetToAct) ? false : true;

	}

	/**
	 * Handles when a player has won the pot or some amount of the pot
	 * 
	 * @param winner Player who has won
	 * @param amount Amount player won
	 */
	private void win(Player winner, int amount) {
		
		// Observers: player won amount
		for (TableObserver obs : observers) {
			obs.win(winner, amount);
		}

		// Remove the amount won from the pot and add it to the winner's stack
		pot -= amount;
		winner.adjustStack(amount);

		// If the pot is empty the game is over
		if (pot == 0) {
			finishRound();
			gameFinished = true;
		}
	}

	/**
	 * Increase the size of the pot by some amount
	 * 
	 * @param amount Amount of chips to increase the pot by
	 */
	protected void increasePot(int amount) {
		pot += amount;
	}
	
	/**
	 * Adds a {@link poker_app.observer.TableObserver} for observing the game.
	 * @param obs TableObserver to add
	 */
	public void addObserver(TableObserver obs) {
		observers.add(obs);
		obs.setGame(this);
	}

	// ----------------
	// Setters/Getters:
	// ----------------

	public int getLastBet() {
		return lastBet;
	}

	public int getLastRaise() {
		return lastRaise;
	}

	public Card[] getBoard() {
		return board;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
}
