package zuulFramework.worldofzuul.commands;

import java.util.HashMap;

/**
 * Handles creating and fetching command words
 * for use in commands.
 * Allows you to get commands, and validate that commands are valid.
 */
public class CommandWords {
	/**
	 * A map of valid commands.
	 */
	private HashMap<String, CommandWord> validCommands;

	/**
	 * Creates a new instance of the commands words checker
	 */
	public CommandWords() {
		// Initialize the HashMap
		validCommands = new HashMap<String, CommandWord>();

		// Iterate all the known possible commands words
		// and save them into the hashmap
		for (CommandWord command : CommandWord.values()) {

			// If the command word is not unknown, then
			// we'll save it for later, so we can quickly validate
			// if a word is valid.
			if (command != CommandWord.UNKNOWN) {
				// Save the word into the hashmap
				validCommands.put(command.toString(), command);
			}
		}
	}

	/**
	 * Gets the command word enum corresponding to the provided string.
	 *
	 * @param commandWord The string that corresponds to a certain word
	 * @return The enum for the given command word. Or unknown, if the given command
	 * does not correspond to anything valid.
	 */
	public CommandWord getCommandWord(String commandWord) {
		// Get the command from the hashmap.
		// If the hashmap doesn't contain the command
		// it will return null
		CommandWord command = validCommands.get(commandWord);
		// If a command was found, then return it
		if (command != null) {
			return command;
		}
		// Otherwise return unknown command.
		else {
			return CommandWord.UNKNOWN;
		}
	}

	/**
	 * Checks if the given string actually corresponds to a command
	 *
	 * @param aString The string to check
	 * @return true if the string is a valid command
	 */
	public boolean isCommand(String aString) {
		return validCommands.containsKey(aString);
	}

	/**
	 * Prints a list of all the commands to the console that can be used in the game.
	 */
	public void showAll() {
		// Iterate all the commands
		for (String command : validCommands.keySet()) {
			// Print out the command and 2 whitespaces
			// All these will be printed on the same line
			System.out.print(command + "  ");
		}
		// Change to a new line
		System.out.println();
	}
}
