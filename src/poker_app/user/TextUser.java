package poker_app.user;

import java.util.Arrays;
import java.util.Collection;

import poker_app.game.Card;
import poker_app.game.Player;

/**
 * Represents a human user that gives input using text. Subclass of {@link User}.
 * <p>
 * Last modified: 5 April 2021
 * 
 * @author Jakob Martin Torsvik
 *
 */
public abstract class TextUser extends User {

	/**
	 * Calls {@link User#Master(String name)}
	 */
	public TextUser(String name) {
		super(name);
	}

	@Override
	public void onAction(Player player) {
		validPlayer(player);

		// Take a user input which should start with either 'c', 'b', 'f' which
		// represents the actions check/call, bet and fold respectively from the player
		Card[] cards = player.getCards();
		String line = "\n(INPUT) " + this + " [" + cards[0] + " " + cards[1] + "]" + ", it's your action: ";
		String action = userInput(line);

		// While input does not start with 'c', 'b' or 'f' ask for new input
		Collection<Character> validInputs = Arrays.asList('c', 'b', 'f');
		while (!validInputs.contains(action.charAt(0))) {
			action = userInput("Not valid action!\n" + line);			
		}

		// Perform the corresponding action of the user input
		switch (action.charAt(0)) {
		case 'c':
			player.checkOrCall();
			break;
		case 'b':
			int betAmount = Integer.parseInt(userInput("Bet amount: "));
			player.bet(betAmount);
			break;
		case 'f':
			player.fold();
			break;
		}
	}

	@Override
	public boolean rebuy(Player player) {
		// Take an user input which should be either 'y' or 'yes', or 'n' or 'no', which
		// represents if the user would like to rebuy
		String rebuy = userInput("\n(INPUT) " + this + ", your stack is empty. Do you want to rebuy? ");
		
		// Returns true if user input equals 'y' or 'yes', and false if 'n' or 'no'
		if (rebuy.equalsIgnoreCase("y") || rebuy.equalsIgnoreCase("yes")) {
			return true;
		} else if (rebuy.equalsIgnoreCase("n") || rebuy.equalsIgnoreCase("no")) {
			return false;
		}

		return false;
	}
	
	@Override
	public boolean sitOutNextHand(Player player) {
		return false;
	}

	@Override
	public int pickSeat(Player player) {
		return player.getTable().getSeats().indexOf(null);
	}

	@Override
	public int chooseStartingStack(Player player) {
		return player.getTable().getMaxStartingStack();
	}

	/**
	 * Takes input from user.
	 * 
	 * @param line Output for user
	 * @return Input from user as a String
	 */
	protected abstract String userInput(String line);
}
