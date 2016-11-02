package zuulFramework.worldofzuul.commands;

/**
* Create an enum that contains possible keywords.
*/
public enum CommandWord
{
    //Initialize values(constants)
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), PICKUP("pickup"), DROP("drop"), PAY("pay"), ASK("ask");

    /**
     * A string that can contain the string value of CommandWord
     */
    private String commandString;

    /**
     * A CommandWord constructor that takes a parameter of type string from enum CommandWord
     */
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }

    /**
     * display the contents of commandString
     */
    public String toString()
    {
        return commandString;
    }
}
