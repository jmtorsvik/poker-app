package poker_app.game;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a poker position (seat relative to the button).
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 */
public class Position {
	// STATIC
	/** Names of positions. */
	private final static List<String> names = Arrays.asList("SB", "BB", "UTG", "LJ", "HJ", "CO", "BTN");

	// NON-STATIC
	/** Position as an integer. */
	private final int pos;
	/** Position as a string. */
	private final String name;

	/**
	 * Inits: {@link #pos} as param pos, {@link #name} as
	 * {@link #posToName(param pos, param seats)}
	 * 
	 * @param pos   Index of position at table, set to 0 if less than 0, set to
	 *              (seats - 1) if greater than or equal to seats
	 * @param seats Number of seats at table, set to 2 if less than 2, set to 23 if
	 *              greater than 23
	 */
	public Position(int pos, int seats) {
		// Preprocessing
		if (seats < 2) {
			seats = 2;
		} else if (seats > 23) {
			seats = 23;
		}
		if (pos < 0) {
			pos = 0;
		} else if (pos >= seats) {
			pos = seats - 1;
		}

		// Inits
		this.pos = pos;
		this.name = posToName(pos, seats);
	}

	/**
	 * Find the corresponding name to a position value
	 * @param pos A position value
	 * @param seats Number of seats at table
	 * @return The Corresponding name of param pos
	 */
	public static String posToName(int pos, int seats) {
		if (pos == 1) {
			// 1 is always "BB"
			return "BB";
		} else if (pos == 0) {
			// 0 is "BTN" if two-seated and "SB" if not
			if (seats == 2) {
				return "BTN";
			}
			return "SB";
		} else if (seats < 8) {
			// If there are less than 8 seats it's enough to use the name in the index of pos in names
			return names.subList(7 - seats, 7).get(pos);
		} else {
			// Otherwise the inclusion of "UTG+x" has to be made
			if (pos == 2) {
				return "UTG";
			} else if (pos > seats - 5) {
				return names.get(pos - seats + 7);
			} else {
				return "UTG+" + (pos - 2);
			}
		}
	}

	// ----------------
	// Setters/Getters:
	// ----------------

	public int getPos() {
		return pos;
	}

	protected String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
