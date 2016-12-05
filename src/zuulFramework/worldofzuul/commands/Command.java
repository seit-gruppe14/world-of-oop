package zuulFramework.worldofzuul.commands;

/**
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 * <p>
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two parts: a CommandWord and a string
 * (for example, if the command was "take map", then the two parts
 * are TAKE and "map").
 * <p>
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the CommandWord is UNKNOWN.
 * <p>
 * If the command had only one word, then the second word is <null>.
 *
 * @author Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */
public class Command {
    /**
     * The command word that belongs to the command
     */
    private CommandWord commandWord;

    /**
     * The command word can consist on two words, the following is the second command word
     */
    private String secondWord;

    /**
     * A command constructor that takes a CommandWord and a String to create a Command.
     *
     * @param commandWord is a CommandWord type object
     * @param secondWord  is a String type object
     */
    public Command(CommandWord commandWord, String secondWord) {
        this.commandWord = commandWord;
        this.secondWord = secondWord;
    }

    /**
     * An accessor method that returns the commandWord of the Command
     *
     * @return a CommandWord type object
     */
    public CommandWord getCommandWord() {
        return commandWord;
    }

    /**
     * An accessor method that returns the secondWord of the Command
     *
     * @return a String type Object
     */
    public String getSecondWord() {
        return secondWord;
    }

    /**
     * Check if the commandWord is unknown
     *
     * @return a boolean, true if the commandword is unknown
     */
    public boolean isUnknown() {
        return (commandWord == CommandWord.UNKNOWN);
    }

    /**
     * Check if the secondWord is set
     *
     * @return a boolean, true if the secondWord is set
     */
    public boolean hasSecondWord() {
        return (secondWord != null);
    }
}

