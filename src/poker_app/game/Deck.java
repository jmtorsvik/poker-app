package poker_app.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

/**
 * Represents a deck of playing cards of class {@link poker_app.game.Card}.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Deck implements Iterable<Card> {
	/** Cards in deck. */
	private final List<Card> cards = new ArrayList<>(52);

	/**
	 * Creates and shuffles deck.
	 * 
	 * @see #create()
	 * @see #shuffle()
	 */
	public Deck() {
		create();
		shuffle();
	}

	/**
	 * Adds a {@link Card} of each rank and suit to {@link cards}.
	 */
	private void create() {
		for (char rank : Card.RANKS) {
			for (char suit : Card.SUITS) {
				cards.add(new Card(Card.rankToValue(rank), suit));
			}
		}
	}

	/**
	 * Shuffles {@link cards}.
	 * 
	 * @see Collections#shuffle(List)
	 */
	protected void shuffle() {
		Collections.shuffle(cards);
	}

	/**
	 * Returns number of cards in deck.
	 * 
	 * @return Size of {@link cards}
	 */
	protected int size() {
		return cards.size();
	}

	/**
	 * @return An iterator over {@link #cards}
	 */
	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}

	/**
	 * @return All the cards in {@link #cards} and the number of cards in the deck
	 */
	@Override
	public String toString() {
		String result = "";
		for (Card card : this) {
			result += "\n" + card.getRank() + card.getSuit();
		}
		result += "\n\n" + size();

		return result;
	}
}
