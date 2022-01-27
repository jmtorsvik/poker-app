package poker_app.observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import poker_app.Table;
import poker_app.game.Card;
import poker_app.game.Player;
import poker_app.writer.Writer;

/**
 * A {@link TableObserver} that converts observations from a {@link Table} to text and shares this with multiple {@link Writer}s.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 *
 */
public class TextObserver extends TableObserver {
	/** Writers to write the converted text. */
	private final Collection<Writer> writers = new ArrayList<>();
	
	/**
	 * Inits: {@link #table} as param table, {@link #writers} as a list of param writer
	 * 
	 * @param table    Table to be observed
	 * @param writer   Writer to send observed text to
	 */
	public TextObserver(Table table, Writer writer) {
		this(table, Arrays.asList(writer));
	}
	
	/**
	 * Inits: {@link #table} as param table, {@link #writers} as param writers
	 * 
	 * @param table    Table to be observed
	 * @param writers  A collection of writers to send observed text to
	 */
	public TextObserver(Table table, Collection<Writer> writers) {
		super(table);
		this.writers.addAll(writers);
	}

	@Override
	public void startObserving() {
		tellWriters("<<<Table " + table.getName() + " | Blinds " + table.getSmallBlind() + "/" + table.getBigBlind() + ">>>");
	}

	@Override
	public void stopObserving() {
	}
	
	@Override
	public void startHand() {
		// Indicate start of hand with hand number
		int number = table.getHandsPlayed();
		tellWriters("\n---START OF HAND " + number + "---");
		// Write participating players
		String line = "Players:";
		for (Player player : game.getPlayers()) {
			line += "\n  " + player.getUser() + "(" + player.getPosition() + ")" + " " + player.getStack();
		}
		tellWriters(line);
	}

	@Override
	public void endHand() {
		// Indicate end of hand with hand number
		tellWriters("---END OF HAND " + table.getHandsPlayed() + "---\n");
	}

	@Override
	public void newStreet(char street) {
		String line = "\n";
		
		// Indicate new street
		switch (street) {
		case 'p':
			line += "PREFLOP";
			break;
		case 'f':
			line += "FLOP";
			break;
		case 't':
			line += "TURN";
			break;
		case 'r':
			line += "RIVER";
			break;
		case 's':
			line += "SHOWDOWN";
			break;
		}
		
		// Board as [_ _ _ _ _]
		line += " [";
		Card[] board = game.getBoard();
		for (int i = 0; i < 5; i++) {
			Card card = board[i];
			line += card == null ? "__" : card;
			if (i < 4) {
				line += " ";
			}
		}
		line += "]";
		
		tellWriters(line);
	}

	@Override
	public void fold(Player player) {
		tellWriters("  " + player.getUser() + " folded");
	}

	@Override
	public void check(Player player) {
		tellWriters("  " + player.getUser() + " checked");
	}

	@Override
	public void call(Player player, int callTo) {
		tellWriters("  " + player.getUser() + " called");
	}

	@Override
	public void bet(Player player, int amount) {
		tellWriters("  " + player.getUser() + " bet " + amount);
	}

	@Override
	public void raise(Player player, int raiseTo) {
		tellWriters("  " + player.getUser() + " raised to " + raiseTo);
	}

	@Override
	public void win(Player player, int amount) {
		tellWriters("  " + player.getUser() + " won " + amount);
	}

	@Override
	public void revealHands(List<Player> players) {
		String line = "";
		for (Player player : players) {
			Card[] cards = player.getCards();
			line += "  " + player.getUser() + " had [" + cards[0] + " " + cards[1] + "] (\""
					+ player.getHand().getRankingName() + "\")\n";
		}
		tellWriters(line);
	}

	/**
	 * Tells all writers in {@link #writers} to write param line.
	 * 
	 * @param line Line to write
	 */
	private void tellWriters(String line) {
		for (Writer w : writers) {
			w.write(line);
		}
	}

}
