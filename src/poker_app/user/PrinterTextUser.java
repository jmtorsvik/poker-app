package poker_app.user;

import java.util.Scanner;

/**
 * A {@link TextUser} that takes input using console.
 * <p>
 * Last modified: 5 April 2021
 * 
 * @author Jakob Martin Torsvik
 *
 */
public class PrinterTextUser extends TextUser {
	/** For taking input from console. */
	private final Scanner scanner = new Scanner(System.in);
	
	/**
	 * Calls {@link TextUser#TextUser(String name)}
	 */
	public PrinterTextUser(String name) {
		super(name);
	}

	@Override
	protected String userInput(String line) {
		System.out.print(line.toString());
		return scanner.nextLine();
	}

}
