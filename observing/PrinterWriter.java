package observing;

/**
 * A {@link Writer} that writes to console.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 *
 */
public class PrinterWriter implements Writer {

	@Override
	public void write(String line) {
		System.out.println(line);
	}
}
