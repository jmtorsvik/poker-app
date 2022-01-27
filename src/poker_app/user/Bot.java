package poker_app.user;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import poker_app.game.Player;

/**
 * Represents a non-human user as a subclass of {@link poker_app.user.User} that acts
 * at random when on action.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Bot extends User {
	/** How long bot should stall. */
	private int stallTime = 1;

	/**
	 * Inits: {@link User#name} as param name
	 * 
	 * @param name Name of bot
	 */
	public Bot(String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * {@link Bot}: Makes a random action using {@link Random}.
	 */
	@Override
	public void onAction(Player player) {
		// Checks if player is valid
		validPlayer(player);

		// Creates a random integer k in the range [0, 2]
		Random rand = new Random();
		int k = rand.nextInt(3);
		
		// Sleep for stallTime seconds
		try {
			TimeUnit.SECONDS.sleep(stallTime);
		} catch (Exception e) {
			System.out.println("Error while sleeping.");
		}
		
		// Player check/calls, bets or folds based on if k equals 0, 1 or 2 respectively
		switch (k) {
		case 0:
			player.checkOrCall();
			break;
		case 1:
			player.bet(player.getGame().getLastBet() + player.getBetSize());
			break;
		case 2:
			player.fold();
			break;
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * {@link Bot}: Always rebuys.
	 */
	@Override
	public boolean rebuy(Player player) {
		return true;
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

}
