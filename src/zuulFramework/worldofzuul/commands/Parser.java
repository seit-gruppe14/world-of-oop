package zuulFramework.worldofzuul.commands;

import java.util.Scanner;

/**
 * Creating a class Parser to get the indput from the user.
 */
public class Parser {
    /**
     *
     */
    private CommandWords commands;

    /**
     *
     */
    private Scanner reader;

    /**
     * A java parser class is made for creating constructors.
     * CommandWords which is refered to by the variable commands and Scanner by reader.
     */
    public Parser() {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * Creating a method called a getter method to get the value from the user
     * by defining two variables of the type string.
     */
    public Command getCommand() {
        String inputLine;
        String word1 = null;
        String word2 = null;

        System.out.print("> ");

        inputLine = reader.nextLine();

        // Use scanner to find tokens by analyzing the words and tokenize it if there are tokens.
        Scanner tokenizer = new Scanner(inputLine);

        // If a new word is in the entered command
        if (tokenizer.hasNext()) {
            // Get the first word the user entered
            word1 = tokenizer.next();

            // If there is another word in the string
            if (tokenizer.hasNext()) {

                // Get the second word the user entered
                word2 = tokenizer.next();
            }
        }

        // returns a new command
        return new Command(commands.getCommandWord(word1), word2);
    }

    /**
     * Print a list of all the commands to the console.
     */
    public void showCommands() {
        commands.showAll();
    }
}
