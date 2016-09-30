package zuulFramework.worldofzuul;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 * Creating a class Parser to get the indput from the user.
 */
public class Parser 
{
    private CommandWords commands; 
    private Scanner reader;

    // A java parser class is made for creating constructors.
    // CommandWords which is refered to by the variable commands and Scanner by reader.
    public Parser() 
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    // Creating a method called a getter method to get the value from the user
    // by defining two variables of the type string. 
    public Command getCommand() 
    {
        String inputLine;
        String word1 = null;
        String word2 = null;

        System.out.print("> "); 

        inputLine = reader.nextLine();
    
    // Use scanner to find tokens by analyzing the words and tokenize it if there are tokens.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next(); 
            }
        }
    // returns ...
        return new Command(commands.getCommandWord(word1), word2);
    }
    
    //returns every commands.
    public void showCommands()
    {
        commands.showAll();
    }
}
