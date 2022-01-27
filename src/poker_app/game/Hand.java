package poker_app.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a poker hand constructed as a combination of playing cards of
 * class {@link poker_app.game.Card}.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Hand implements Comparable<Hand> {
	// CONSTANTS
	// STATIC
	/** Names of all the hand rankings in poker. */
	public final static List<String> RANKING_NAMES = Arrays.asList("High Card", "Pair", "Two pair", "Three of a kind",
			"Straight", "Flush", "Full house", "Four of a kind", "Straight flush", "Royal flush");
	// NON-STATIC
	/** The cards by which the hand will be deduced from. */
	private final List<Card> cards;

	// NON-CONSTANTS
	/** The cards which makes up the best hand possible given {@link #cards}. */
	private List<Card> hand;
	/**
	 * A representation of the card's ranking by a list of values where higher value
	 * means better ranking where:
	 * <p>
	 * * The first integer represents the value of the hand ranking (one of
	 * {@link #rankingNames}).
	 * <p>
	 * * Each next value is based on the value of the cards in the hand.
	 */
	private List<Integer> ranking;
	/** The best hands that can be made for each possible hand ranking. */
	private List<List<Card>> hands = new ArrayList<>();
	/**
	 * The best rankings that can be made for each possible hand ranking.
	 */
	private List<List<Integer>> rankings = new ArrayList<>();

	/**
	 * Inits: {@link #cards} as param cards sorted decreasingly by
	 * {@link Card#getValue()}
	 * 
	 * @param cards Cards in hand
	 */
	public Hand(List<Card> cards) {
		// Sort cards decreasingly by value
		cards.sort(new Comparator<Card>() {
			@Override
			public int compare(Card c1, Card c2) {
				return c2.getValue() - c1.getValue();
			}
		});

		// Inits
		this.cards = cards;

		// Find ranking of hand
		for (int i = 0; i < 10; i++) {
			hands.add(null);
			rankings.add(null);
		}
		findRanking();
	}

	// --------------
	// Find rankings:
	// --------------

	/**
	 * Finds the best possible hand for {@link #cards} and its ranking and sets
	 * {@link #hand} and {@link #cards} to them.
	 */
	private void findRanking() {
		// Create a list of the cards of same suit to check for flushes later
		List<Card> ssc = sameSuitCards();

		// If there is a flush or not
		boolean fiveOfASuit = ssc != null;

		// Find the best High Card hand
		highCard();

		// Find the best Pair hand
		pair();

		// Find best Two Pair and Three Of A Kind hand if there is a Pair hand
		if (rankings.get(8) != null) {
			twoPair();
			threeOfAKind();
		}

		// Find the best Straight hand
		straight();

		// Find the best FLush hand if there are at least five of one suit
		if (fiveOfASuit) {
			flush(ssc);
		}

		// Find the best Four Of A Kind hand if there is a Three Of A Kind hand, and
		// the best Full House hand if there also is a Two Pair hand
		if (rankings.get(6) != null) {
			if (rankings.get(7) != null) {
				fullHouse();
			}
			fourOfAKind();
		}

		// Find the best Royal/Straight Flush hand if there are at least five of one
		// suit and if there is Straight hand
		if (fiveOfASuit && rankings.get(5) != null) {
			royalOrStraightFlush(ssc);
		}

		// Find and set the best hand and ranking of the ones found above
		for (int i = 0; i < 10; i++) {
			List<Integer> r = rankings.get(i);
			if (r != null) {
				hand = hands.get(i);
				ranking = r;
				break;
			}
		}
	}

	/**
	 * Finds the best possible High Card hand and ranking for {@link #cards} and
	 * sets {@link #hand} and {@link #ranking} to them.
	 */
	private void highCard() {
		// Find the five cards with highest value in cards
		List<Card> hand = cards.subList(0, 5);

		// Set hand at index 9 in hands as hand
		hands.set(9, hand);

		// Add hand to rankings with ranking value 0 and inner ranking value based on
		// all the cards in hand
		addToRankings(0, hand);
	}

	/**
	 * Finds the best possible Pair hand and ranking for {@link #cards} and sets
	 * {@link #hand} and {@link #ranking} to them.
	 * 
	 * @see #xOfAKind(int, int)
	 */
	private void pair() {
		xOfAKind(2, 1);
	}

	/**
	 * Finds the best possible Two Pair hand and ranking for {@link #cards} and sets
	 * {@link #hand} and {@link #ranking} to them.
	 */
	private void twoPair() {
		// Finds the highest value pair in cards
		List<Card> pair1 = equalRank(2, cards);

		// Finds the cards that are left
		List<Card> restCards = new LinkedList<>(cards);
		restCards.removeAll(pair1);

		// Finds the second highest pair in cards
		List<Card> pair2 = equalRank(2, restCards);

		// Only continue if there are two pair
		if (pair2 != null) {
			// Find the cards that are left
			restCards.removeAll(pair2);

			// Add the pairs and the highest card left to hand
			List<Card> hand = new ArrayList<>(5);
			hand.addAll(pair1);
			hand.addAll(pair2);
			hand.add(restCards.get(0));

			// Set hand at index 7 in hands to hand
			hands.set(7, hand);

			// Add hand to rankings with ranking value 2 and inner ranking value based on
			// indexes 0, 2 and 4 in hand
			addToRankings(2, Arrays.asList(hand.get(0), hand.get(2), hand.get(4)));
		}
	}

	/**
	 * Finds the best possible Three Of A Kind hand and ranking for {@link #cards}
	 * and sets {@link #hand} and {@link #ranking} to them.
	 * 
	 * @see #xOfAKind(int, int)
	 */
	private void threeOfAKind() {
		xOfAKind(3, 3);
	}

	/**
	 * Finds the best possible Straight hand and ranking for {@link #cards} and sets
	 * {@link #hand} and {@link #ranking} to them.
	 */
	private void straight() {
		// Find 5 consecutive cards in cards
		List<Card> consCards = consecutiveCards(cards);

		// Only continue if there are 5 consecutive cards
		if (consCards != null) {
			// Set hand at index 5 in hands as consCards
			hands.set(5, consCards);

			// Add hand to rankings with ranking value 4 and inner ranking value based on
			// the first card in consCards
			addToRankings(4, Arrays.asList(consCards.get(0)));
		}
	}

	/**
	 * Finds the best possible Flush hand and ranking for {@link #cards} and sets
	 * {@link #hand} and {@link #ranking} to them.
	 * 
	 * @param cards Cards of the same suit
	 */
	private void flush(List<Card> cards) {
		// Find the five highest cards in cards
		List<Card> hand = cards.subList(0, 5);

		// Set hand at index 4 in hands as hand
		hands.set(4, hand);

		// Add hand to rankings with ranking value 5 and inner ranking based on all the
		// cards in hand
		addToRankings(5, hand);
	}

	/**
	 * Finds the best possible Full House hand and ranking for {@link #cards} and
	 * sets {@link #hand} and {@link #ranking} to them.
	 */
	private void fullHouse() {
		// Find the three cards with highest and equal rank
		List<Card> toak = equalRank(3, cards);

		// Find the cards that are left
		List<Card> restCards = new LinkedList<>(cards);
		restCards.removeAll(toak);

		// Find the pair of highest value in restCards
		List<Card> pair = equalRank(2, restCards);

		// Only continue if there is a pair
		if (pair != null) {
			// Add the Three Of A Kind hand and the Pair to hand
			List<Card> hand = new ArrayList<>(5);
			hand.addAll(toak);
			hand.addAll(pair);

			// Set hand at index 3 in hands to hand
			hands.set(3, hand);

			// Add hand to rankings with ranking value 6 and inner ranking value based on
			// indexes 0 and 3 in hand
			addToRankings(6, Arrays.asList(hand.get(0), hand.get(3)));
		}
	}

	/**
	 * Finds the best possible Four Of A Kind hand and ranking for {@link #cards}
	 * and sets {@link #hand} and {@link #ranking} to them.
	 * 
	 * @see #xOfAKind(int, int)
	 */
	private void fourOfAKind() {
		xOfAKind(4, 7);
	}

	/**
	 * Finds the best possible Royal/Straight flush hand and ranking for
	 * {@link #cards} and sets {@link #hand} and {@link #ranking} to them.
	 * 
	 * @param cards Cards of the same suit
	 */
	private void royalOrStraightFlush(List<Card> cards) {
		// Find 5 consecutive cards in cards
		cards = consecutiveCards(cards);

		// Only continue if there are 5 consecutive cards
		if (cards != null) {
			// If the rank of card at index 0 is 'A' it is a royal flush
			boolean royal = cards.get(0).getRank() == 'A';

			// Set hand at index 0 if royal, and 1 if not in hands as cards
			int r = royal ? 0 : 1;
			hands.set(r, cards);

			// Add hand to rankings with ranking value 0 if royal, and 1 if not, and inner
			// ranking value based on no card if royal, and the first card in cards if not
			List<Card> rankingCards = royal ? Arrays.asList() : Arrays.asList(/*hands.get(r)*/cards.get(0));
			addToRankings(9 - r, rankingCards);
		}
	}

	// ----------------
	// Helping methods:
	// ----------------

	/**
	 * /** Finds the best possible param x Of A Kinds hand and ranking for
	 * {@link #cards} and sets {@link #hand} and {@link #ranking} to them.
	 * 
	 * @param x Number of cards of a kind
	 * @param r Ranking index
	 */
	private void xOfAKind(int x, int r) {
		// Find the x cards with highest and equal rank
		List<Card> equalRank = equalRank(x, cards);

		// Only continue if there are x cards of equal rank
		if (equalRank != null) {
			// Find the cards that are left
			List<Card> restCards = new LinkedList<>(cards);
			restCards.removeAll(equalRank);

			// Add the (5 - x) highest ranked cards to equalRank and set index (9 - r) in
			// hands as it
			equalRank.addAll(restCards.subList(0, 5 - x));
			hands.set(9 - r, equalRank);

			// Add hand to rankings with ranking value r and inner ranking based on indexes
			// (x - 1) to 4 in equalRank
			addToRankings(r, equalRank.subList(x - 1, 5));
		}
	}

	/**
	 * Finds, if there is, param n cards of equal and highest possible rank.
	 * 
	 * @param n     Number of cards to find
	 * @param cards Cards to search through
	 * @return A list of param n cards of equal rank, if there are n such cards.
	 *         null otherwise
	 */
	private List<Card> equalRank(int n, List<Card> cards) {
		// List for cards of equal rank
		List<Card> sameRank = new ArrayList<>(n);

		for (Card card : cards) {
			// Clear sameRank if card is not of same rank as last card in sameRank
			if (!sameRank.isEmpty() && card.getRank() != sameRank.get(sameRank.size() - 1).getRank()) {
				sameRank.clear();
			}

			// Add card to sameRank
			sameRank.add(card);

			// Break if desired number of cards reached
			if (sameRank.size() == n) {
				break;
			}
		}

		// Return sameRank if there are n cards of equal rank, return null otherwise
		if (sameRank.size() == n) {
			return sameRank;
		}
		return null;
	}

	/**
	 * Finds 5 cards of the same suit in {@link #cards}.
	 * 
	 * @return A list of 5 cards of the same suit, if there are 5 such cards in
	 *         {@link #cards}. null otherwise
	 */
	private List<Card> sameSuitCards() {
		// List for storing result
		List<Card> l1 = null;

		// Try to find five cards of each suit in suits
		for (char suit : Card.SUITS) {
			// Add cards of suit to the same list
			List<Card> l2 = new ArrayList<>(cards.size());
			for (Card card : cards) {
				if (card.getSuit() == suit) {
					l2.add(card);
				}
			}

			// Set l1 to l2 and break if there are 5 or more cards of the same suit
			if (l2.size() >= 5) {
				l1 = l2;
				break;
			}
		}

		return l1;
	}

	/**
	 * Finds 5 cards of consecutive ranks.
	 * 
	 * @param cards The cards to search through
	 * @return A list of 5 cards of consecutive value, if there are 5 such cards in
	 *         param cards. null otherwise
	 */
	private List<Card> consecutiveCards(List<Card> cards) {
		// Lists for storing result and consecutive cards
		List<Card> l1 = null;
		List<Card> l2 = new ArrayList<>(5);

		l2.add(cards.get(0));
		for (Card card : cards.subList(1, cards.size())) {
			// Find the value difference between card and the last card in l2
			int dif = l2.get(l2.size() - 1).getValue() - card.getValue();

			// Add card if difference is greater than or equal to 1
			if (dif >= 1) {
				// Clear l2 if difference is greater than 1
				if (dif > 1) {
					l2.clear();
				}
				l2.add(card);
			}

			// Set l1 to l2 and break if 5 consecutive cards are found
			if (l2.size() == 5) {
				l1 = l2;
				break;
			}
		}

		return l1;
	}

	/**
	 * Adds a new ranking to {@link #rankings}
	 * 
	 * @param r     Ranking value
	 * @param cards The cards the ranking is based on
	 */
	private void addToRankings(int r, List<Card> cards) {
		// A list to represent the ranking
		List<Integer> ranking = new ArrayList<>(cards.size() + 1);

		// Add r as first value in ranking and add each card value in cards to ranking
		ranking.add(r);
		for (Card card : cards) {
			ranking.add(card.getValue());
		}

		// Set ranking at index (9 - r) to ranking
		rankings.set(9 - r, ranking);

	}

	// ----------------
	// Setters/Getters:
	// ----------------

	protected List<Card> getHand() {
		return hand;
	}

	protected List<Integer> getRanking() {
		return ranking;
	}

	public String getRankingName() {
		return RANKING_NAMES.get(ranking.get(0));
	}

	protected List<Card> getCards() {
		return cards;
	}

	// ------
	// Other:
	// ------

	/**
	 * {@link Hand}: Compares the rankings of two hands.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Hand other) {
		// Find ranking of this hand and other hand
		List<Integer> rThis = this.ranking;
		List<Integer> rOther = other.ranking;

		// Find first difference in the rankings that is non-zero and return it
		for (int i = 0; i < rThis.size(); i++) {
			int dif = rThis.get(i) - rOther.get(i);
			if (dif != 0) {
				return dif;
			}
		}

		// If no non-zero difference in the rankings could be found return 0
		return 0;
	}

	/**
	 * @return All the cards in {@link hand}
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < 5; i++) {
			result += hand.get(i) + " ";
		}
		return result;
	}
}
