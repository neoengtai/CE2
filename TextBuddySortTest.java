import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextBuddySortTest {
	// duplicated
	// lexicographically
	@Test
	public void tests() {
		testSort("Test #1", stringToArray(""), "mytextfile.txt is empty\r\n");
		testSort("Test #2", stringToArray("bar,foo"), "1. bar\r\n2. foo\r\n");
		
	}

	public void testSort(String message, String[] data, String expected) {
		try {
			TextBuddy tb = new TextBuddy("mytextfile.txt");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			tb.clear();
			for (String line : data) {
				tb.add(line);
			}

			tb.sort();
			System.setOut(new PrintStream(out));
			tb.display();

			assertEquals(message, expected, out.toString());
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}

	public static String[] stringToArray(String input) {
		return input.split(",");
	}
}
