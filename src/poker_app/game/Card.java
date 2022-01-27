package poker_app.game;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents a playing card.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Card {
	// STATIC
	/*
	 * public final static Collection<String> cards = Arrays.asList("2s", "2d",
	 * "2c", "2h", "3s", "3d", "3c", "3h", "4s", "4d", "4c", "4h", "5s", "5d", "5c",
	 * "5h", "6s", "6d", "6c", "6h", "7s", "7d", "7c", "7h", "8s", "8d", "8c", "8h",
	 * "9s", "9d", "9c", "9h", "Ts", "Td", "Tc", "Th", "Js", "Jd", "Jc", "Jh", "Qs",
	 * "Qd", "Qc", "Qh", "Ks", "Kd", "Kc", "Kh", "As", "Ad", "Ac", "Ah");
	 */
	/** All card ranks as characters. */
	public final static List<Character> RANKS = Arrays.asList('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q',
			'K', 'A');
	/** All card suits as characters. */
	public final static Collection<Character> SUITS = Arrays.asList('s', 'd', 'c', 'h');
	
	// NON-STATIC
	private final char rank, suit;
	private final int value;

	/**
	 * Inits: {@link #rank} as {@link #valueToRank(param value)}, {@link #value} as
	 * param value, {@link #suit} as param suit
	 * 
	 * @param value Value of the card, set to 0 if less than 0, set to 12 if greater
	 *              than 12
	 * @param suit  Suit of the card, either of spades 's', diamonds 'd', clubs 'c'
	 *              or hearts 'h'
	 * @throws IllegalArgumentException if suit is not in {@link #suits}
	 */
	public Card(int value, char suit) {
		// Preprocessing
		if (value < 0) {
			value = 0;
		} else if (value > 12) {
			value = 12;
		}

		// Exceptions
		if (!SUITS.contains(suit)) {
			throw new IllegalArgumentException("Character suit must be either 's', 'd', 'c' or 'h'.");
		}

		// Inits
		rank = valueToRank(value);
		this.value = value;
		this.suit = suit;
	}

	/**
	 * Gives the corresponding value of a rank.
	 * @param rank A rank in {@link #ranks}
	 * @return The corresponding value
	 * @throws IllegalArgumentException if param rank is not in {@link #ranks}
	 */
	public static int rankToValue(char rank) {
		if (!RANKS.contains(rank)) {
			throw new IllegalArgumentException("r should be a character in the List Card.ranks");
		}
		return RANKS.indexOf(rank);
	}
	
	/**
	 * Gives the corresponding rank of a value.
	 * @param value A value in range [0, 12], set to 0 if less than 0, set to 12 if larger than 12
	 * @return The corresponding rank
	 */
	public static char valueToRank(int value) {
		// Preprocessing
		if (value < 0) {
			value = 0;
		} else if (value > 12) {
			value = 12;
		}
		
		return RANKS.get(value);
	}

	// ----------------
	// Setters/Getters:
	// ----------------
	
	protected char getRank() {
		return rank;
	}

	protected int getValue() {
		return value;
	}

	protected char getSuit() {
		return suit;
	}

	@Override
	public String toString() {
		return "" + rank + suit;
	}
}
