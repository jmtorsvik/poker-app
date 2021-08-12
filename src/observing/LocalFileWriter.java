package observing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A {@link Writer} that writes to a file.
 * <p>
 * Last modified: 4 April 2021
 * 
 * @author Jakob Martin Torsvik
 *
 */
public class LocalFileWriter implements Writer {
	/** Path to the file to write to. */
	private final String filePath;
	/** For writing to the file. */
	private BufferedWriter writer;

	/**
	 * Inits: {@link #filePath} as param filePath
	 * 
	 * @param filePath Path to file to be written to
	 */
	public LocalFileWriter(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Opens the writer.
	 */
	public void open() {
		try {
			writer = new BufferedWriter(new FileWriter(filePath, true));
		} catch (IOException e) {
			System.out.println("Exception error!");
		}
	}

	/**
	 * Closes the writer.
	 */
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			System.out.println("Exception error!");
		}
	}

	/**
	 * Writes one line to {@link #printedHandsFile}.
	 * 
	 * @param line Line to write
	 */
	@Override
	public void write(String line) {
		try {
			writer.write(line + "\n");
		} catch (IOException e) {
			System.out.println("Exception error!");
		}
	}

}
