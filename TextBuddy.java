import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class provides basic functions of text document including add, delete,
 * display and clear. File is saved after every operation since total number of
 * entries is expected to be small
 * 
 * Assumptions: 1. Input from user is always valid. 2. No output is expected if
 * input is invalid. (e.g. delete 0) 3. No output is expected if an error
 * occurs. 4. There will always be a filename specified when running TextBuddy
 * 
 * @author Neo Eng Tai
 */

public class TextBuddy {

	// These are the possible command types
	enum COMMAND_TYPE {
		ADD, DELETE, DISPLAY, CLEAR, EXIT, INVALID
	};

	// This is the format for welcome message
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use.";

	// These are the messages displayed after the execution of a command
	private static final String FORMAT_COMMAND = "command: ";
	private static final String FORMAT_ADD = "added to %1$s: \"%2$s\"";
	private static final String FORMAT_DELETE = "deleted from %1$s: \"%2$s\"";
	private static final String FORMAT_EMPTY = "%1$s is empty";
	private static final String FORMAT_CLEAR = "all content deleted from %1$s";

	// These are the error messages
	private static final String ERROR_ACCESS_FILE = "Error accessing file. Please try again.";
	private static final String ERROR_INVALID_COMMAND = "Invalid command.";

	// This is the variable used to hold the text file to be used for processing
	private File textFile;

	/**
	 * Constructs a new TextBuddy object.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public TextBuddy(String fileName) throws IOException {
		textFile = new File(fileName);
		if (!textFile.exists()) {
			textFile.createNewFile();
		}
	}

	public static void main(String[] args) {
		try {
			TextBuddy tb = new TextBuddy(args[0]);
			System.out.println(String.format(MESSAGE_WELCOME,
					tb.getTextFileName()));
			while (true) {
				tb.processCommands();
			}
		} catch (IOException e) {
			System.out.println(ERROR_ACCESS_FILE);
		}
	}

	private void processCommands() {
		String command, commandParameter;
		Scanner scanner = new Scanner(System.in);

		System.out.print(FORMAT_COMMAND);
		command = scanner.next().trim();
		COMMAND_TYPE commandType = determineCommandType(command);

		switch (commandType) {
		case ADD:
			commandParameter = scanner.nextLine().trim();
			if (add(commandParameter)) {
				System.out.println(String.format(FORMAT_ADD, getTextFileName(),
						commandParameter));
			}
			break;

		case DELETE:
			commandParameter = scanner.nextLine().trim();
			String deletedText = delete(commandParameter);
			if (deletedText != null) {
				System.out.println(String.format(FORMAT_DELETE,
						getTextFileName(), deletedText));
			}
			break;

		case DISPLAY:
			display();
			break;

		case CLEAR:
			if (clear()) {
				System.out.println(String.format(FORMAT_CLEAR,
						getTextFileName()));
			}
			break;

		case EXIT:
			System.exit(0);

		default:
			System.out.println(ERROR_INVALID_COMMAND);
			break;
		}
	}

	private COMMAND_TYPE determineCommandType(String command) {
		COMMAND_TYPE commandType;

		switch (command.toLowerCase()) {
		case "add":
			commandType = COMMAND_TYPE.ADD;
			break;

		case "delete":
			commandType = COMMAND_TYPE.DELETE;
			break;

		case "display":
			commandType = COMMAND_TYPE.DISPLAY;
			break;

		case "clear":
			commandType = COMMAND_TYPE.CLEAR;
			break;

		case "exit":
			commandType = COMMAND_TYPE.EXIT;
			break;

		default:
			commandType = COMMAND_TYPE.INVALID;
			break;
		}
		return commandType;
	}

	/**
	 * Adds the input text by appending it to the end of the text file.
	 * 
	 * @param text
	 * @return true if text has been successfully added.
	 */
	public boolean add(String text) {
		try {
			if (text.equals("")) {
				System.out.println("Please input something.");
				return false;
			}

			FileWriter fw = new FileWriter(textFile, true);
			fw.write(text + "\r\n");
			fw.close();

		} catch (IOException e) {
			System.out.println(ERROR_ACCESS_FILE);
			return false;
		}
		return true;
	}

	/**
	 * Deletes an entry from the text file by specifying its corresponding
	 * number.
	 * 
	 * @param number of the item to be deleted
	 * @return the string deleted.
	 */
	public String delete(String number) {
		try {
			int num = Integer.valueOf(number);
			ArrayList<String> contents = retrieveFromFile();

			String deletedText = contents.remove(num - 1);

			clear();
			for (String line : contents) {
				add(line);
			}

			return deletedText;

		} catch (FileNotFoundException e) {
			System.out.println(ERROR_ACCESS_FILE);
			return null;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Please enter a valid number.");
			return null;
		} catch (NumberFormatException e) {
			System.out.println("Please enter a valid number.");
			return null;
		}
	}

	/**
	 * Output all entries stored in the text file with numbers.
	 */
	public void display() {
		try {
			ArrayList<String> contents = retrieveFromFile();

			if (contents.isEmpty()) {
				System.out.println(String.format(FORMAT_EMPTY,
						textFile.getName()));
			} else {
				int entryNumber = 0;
				for (String line : contents) {
					entryNumber++;
					System.out.println(entryNumber + ". " + line);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(ERROR_ACCESS_FILE);
		}
	}

	/**
	 * Clear all contents in the text file.
	 * 
	 * @return true if all contents have been successfully removed.
	 */
	public boolean clear() {
		try {
			FileWriter fw = new FileWriter(textFile);
			fw.write("");
			fw.close();
		} catch (IOException e) {
			System.out.println(ERROR_ACCESS_FILE);
			return false;
		}
		return true;
	}

	/**
	 * @return Name of the text file being used by TextBuddy.
	 */
	public String getTextFileName() {
		return textFile.getName();
	}

	/**
	 * @return ArrayList of strings in the file
	 * @throws FileNotFoundException
	 */
	private ArrayList<String> retrieveFromFile() throws FileNotFoundException {
		Scanner sc = new Scanner(textFile);
		ArrayList<String> contents = new ArrayList<String>();

		while (sc.hasNextLine()) {
			contents.add(sc.nextLine());
		}
		sc.close();

		return contents;
	}

	public void sort() {
		//dummy
	}
}
