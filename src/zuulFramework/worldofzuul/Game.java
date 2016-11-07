package zuulFramework.worldofzuul;

import zuulFramework.worldofzuul.commands.Command;
import zuulFramework.worldofzuul.commands.CommandWord;
import zuulFramework.worldofzuul.commands.Parser;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.entities.Player;
import zuulFramework.worldofzuul.entities.Employee;
import zuulFramework.worldofzuul.helpers.SillyMessages;
import zuulFramework.worldofzuul.rooms.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The "main" in the game
 */
public class Game implements ITimeEventAble {

    private static final int GAME_END_TIME = 22 * 60;
    /**
     * Handles reading commands from the user
     */
    private Parser parser;

    private Time time;
    /**
     * The player instance
     */
    private Player player;

    private HighScore highScore;


    private String gameOverMessage = null;

    private ItemType[] itemList = {
            ItemType.BED,
            ItemType.DINNERTABLE,
            ItemType.DINNERCHAIR,
            ItemType.SHELVES,
            ItemType.DESK,
            ItemType.CUTLERY,
            ItemType.LAMP,
            ItemType.COMPUTER,
            ItemType.LAMP,
            ItemType.SOFA
    };

    /**
     * Creates a new game, with default values
     */
    public Game() {
        // Initialize a new time, 
        // with an instance of the Game object as a parameter
        time = new Time(this);

        // Initialize a highscore
        // with an instance of the Game object as a parameter
        highScore = new HighScore(this);

        // Create a list to store all the time based callbacks
        time.getList();

        // Initialize a new player
        player = new Player();

        // Create all the rooms in the game
        createRooms();

        // Initialize the parser for reading in commands
        parser = new Parser();

        // Add own time callback
        // witch takes an instance of the Game object as a parameter
        time.addTimeEvent(this);

    }

    /**
     * Create the rooms in the game and any exits between them
     */
    private void createRooms() {
        //Initializing the different rooms
        List<Room> rooms = null;
        try {
            rooms = WorldLoader.LoadWorld("map.wop", time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(rooms);

        // The first room in the list, will always be the room the player starts in
        player.setCurrentRoom(rooms.get(0));
    }

    /**
     * Starts the actual game
     */
    public void play() {
        // Tell the user about the game
        printWelcome();
        
        highScore.showScore();
        // The user hasn't finished the game when they start
        boolean finished;

        System.out.printf("You have %d life.\n", player.getLife());
        
        // Ask the user for commands, and do whatever the user told us
        do {
            if (gameOverMessage != null) {
                System.out.println(gameOverMessage);
                break;
            }
            // Write the current time
            System.out.printf("The time is now %s\n", time.getNiceFormattedTime());
            for (Employee employee : WorldLoader.getEmployeeList()){
                if (employee.getCurrentRoom() == player.getCurrentRoom()){
                    System.out.println("You met an employee and can ask for the location of an item.\n");    
                }
            }
            Command command = parser.getCommand();
            finished = processCommand(command);

            if (player.isPlayerDead()) {
                gameOver(SillyMessages.getDeathMessage());
            }
        } while (!finished);
        try {
            int score = highScore.calcScore(itemList);
            System.out.println("Your score was " + score);
            highScore.printScoreToFile(score);
            System.out.println("Top 5 scores were");
            highScore.showScore();
        } catch (IOException ex) {
            System.out.println("IOException caught");
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Prints the welcome message and a description of the current room
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcöme möney spender.");
        System.out.println("Tensiön is high at IKEA Ödense as yöu are waiting tö shöp-amök.");
        System.out.println("It's BLACK FRIDAY and yöu're ön the löököut för the best öffers pössible tö furnish yöur new appartment.");
        System.out.println("But be careful as the öther shöppers might beat yöu tö it or tramble yöu tö death!");
        System.out.println("Are yöu ready?");
        System.out.println("");
        System.out.printf("If you need assistance type '%s' tö ask öne öf the blönde IKEA emplöyees.%n", CommandWord.HELP);
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Process the provided command, and acts upon it
     *
     * @param command The command to process
     * @return true if the game has finished, false otherwise
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        // If we don't know the command, then tell the user
        if (commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        // If the user asked for help, print that
        switch (commandWord) {
            case HELP:
                printHelp();
                break;
            // If the user ask to go to a certain room, to there
            case GO:
                goRoom(command);
                break;
            // If the user asked to quit the game, quit
            case QUIT:
                wantToQuit = quit(command);
                break;
            case PICKUP:
                pickUp(command);
                break;
            case DROP:
                drop(command);
                break;
            case PAY:
                wantToQuit = pay(command);
                break;
            case ASK:
                for (Employee employee : WorldLoader.getEmployeeList()){
                    if (employee.getCurrentRoom() == player.getCurrentRoom()){
                        askForHelp(command);
                        break;
                    }
                }
                break;
        }
        return wantToQuit;
    }

    /**
     * Prints a welcome to the user And a list of the commands that can be used
     */
    private void printHelp() {


        // Make new list of items to buy
        List<ItemType> itemsToBuy = new ArrayList<ItemType>();
        for (ItemType itemType : itemList) {
            itemsToBuy.add(itemType);
        }

        // Copy both bought items and items in inventory
        // to new list
        List<Item> boughtItems = new ArrayList<>();
        boughtItems.addAll(player.getBoughtItems());
        boughtItems.addAll(player.getItems());
        
        // If the item is bought by the player 
        // remove the item from itemsToBuy
        for (Item boughtItem : boughtItems) {
            itemsToBuy.remove(boughtItem.getType());
        }
        
        // if you haven't bought every item, then print the rest of 
        // itemsToBuy
        if (itemsToBuy.size() > 0) {
            System.out.println("You still need to buy the following:");
            
            // Print the list of items you still need to buy
            for (ItemType itemType : itemsToBuy) {
                System.out.println(itemType.toString());
            }
        
        } else {
            System.out.println("You have bought everything you need. ");
        }
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println("Type a command word to get further information.");
    }

    /**
     * Checks if there is a second word after the CommandWord "GO" And change to
     * that room of the player if it exists
     *
     * @param command the command to check
     */
    private void goRoom(Command command) {
        // Check if the command has a room to go to
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            System.out.println(player.getCurrentRoom().getLongDescription());
            return;
        }
        // Get to the room we want to go to
        Room nextRoom = player.goRoom(command.getSecondWord());
        //Checks if there is not a next room and print and error to user
        if (nextRoom == null) {
            System.out.println("There is no door!");
        } //If there is a next room the current room will be the next room and prints out the method
        else {
            System.out.println(nextRoom.getLongDescription());

            // Change the game time. It always take 15 minutes to change room.
            time.updateTime(15);
            // Handle special event rooms
            if (nextRoom instanceof IHaveSpecialEvent) {
                ((IHaveSpecialEvent) nextRoom).doSpecialEvent(this);
            }
        }
    }

    /**
     * Checks if the command QUIT has been used
     *
     * @param command the command to check
     * @return a boolean expression
     */
    private boolean quit(Command command) {
        // There is only one thing to quit, so the user shouldn't specify anything after
        // the command
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } //If there is only the word QUIT it will return a true
        else {
            return true;
        }
    }

    /**
     * The player is able to pick up items in the room
     *
     * @param command the command
     */
    public void pickUp(Command command) {

        // Check if the player has choosen an item to pick up
        if (command.hasSecondWord()) {

            // Ask the player objekt to pick up an item in the room
            String success = player.pickUp(command.getSecondWord());

            // If the player can pick up the item, then print the item the
            // player has picked up
            if (success == null) {
                System.out.println("you picked up " + command.getSecondWord() + ".");
            } // Else print an error
            else {
                System.out.printf("Could not pick up %s.\n%s\n", command.getSecondWord(), success);
            }
        } // else print a list of the items in the room out to the player.
        else {
            Room currentRoom = player.getCurrentRoom();
            if (currentRoom instanceof SalesRoom) {
                System.out.println("The following items can be picked up in this room: ");
                SalesRoom sr = (SalesRoom) currentRoom;
                List<Item> items = sr.getItems();
                printItemList(items);
                // else print error
            } else {
                System.out.println("There is nothing in this room that can be picked up.");
            }
        }
    }

    private void printItemList(List<Item> items) {
        // Find the length of the longest name of the items
        int longestItemNameStringLength = 0;
        for (Item item : items) {
            int length = item.getName().length();
            if (length > longestItemNameStringLength) {
                longestItemNameStringLength = length;
            }
        }
        String itemNameString = "Item name";
        if (longestItemNameStringLength < itemNameString.length()) {
            longestItemNameStringLength = itemNameString.length();
        }

        // Find the longest price string length
        int longestPriceStringLength = 0;
        for (Item item : items) {
            String priceString = String.format("$%d", item.getPrice());
            int length = priceString.length();
            if(length > longestPriceStringLength){
                longestPriceStringLength = length;
            }
        }
        String priceString = "Price";
        if (priceString.length() > longestPriceStringLength){
            longestPriceStringLength = priceString.length();
        }


        // Find the longest weight string length
        int longestWeightStringLength = 0;
        for (Item item : items) {
            String weightString = String.format("%3.2f kg", item.getWeight());
            int length = weightString.length();
            if (length > longestWeightStringLength) {
                longestWeightStringLength = length;
            }
        }
        String weightString = "Weight";
        if (weightString.length() > longestWeightStringLength) {
            longestWeightStringLength = weightString.length();
        }

        System.out.printf("| %" + longestItemNameStringLength + "s | Price | %" + longestWeightStringLength + "s |\n", itemNameString, weightString);
        for (Item item : items) {
            System.out.printf("| %" + longestItemNameStringLength + "s | ", item.getName());
            System.out.printf("%" + longestPriceStringLength + "s | ", String.format("$%d", item.getPrice()));
            System.out.printf("%" + longestWeightStringLength + "s |\n", String.format("%3.2f kg", item.getWeight()));
        }
    }

    /**
     * A player can drop their items from their inventory
     *
     * @param command the command
     */
    public void drop(Command command) {

        // Check if the player can drop an item off in this room.
        if (player.getCurrentRoom() instanceof SalesRoom) {

            // If the player has typed an item that is avaiable then he can drop
            // the item.
            if (command.hasSecondWord()) {
                boolean succes = player.drop(command.getSecondWord());
                if (succes) {
                    System.out.println("You dropped the item " + command.getSecondWord() + " in the room");
                } // else print an error
                else {
                    System.out.println("You have no such item dropped");
                }
            } // Else print a list of the items that the player can drop.
            else {
                List<Item> items = player.getItems();
                if (items.isEmpty()) {
                    System.out.println("You don't have anything in your inventory you can drop");
                } else {
                    System.out.println("Here is a list of items that you can drop");

                    printItemList(items);
                }
            }
        } else {
            System.out.println("You cannot drop anything in this room.");
        }

    }

    /**
     * A player can pay in a room.
     *
     * @param command the command
     */
    public boolean pay(Command command) {

        // Check if the currentroom is a room where you can pay
        Room currentRoom = player.getCurrentRoom();
        // If you can pay, then you pay
        if (currentRoom instanceof ICanPay) {
            ICanPay payRoom = (ICanPay) currentRoom;
            payRoom.buy(player, command, this);

        } // else print an error
        else {
            System.out.println("There is nowhere you can pay in this room");
        }
        if (currentRoom instanceof Exit) {
            System.out.println("If you wish to quit the game type 'quit'");
            String wishToQuit;
            Scanner quit = new Scanner(System.in);
            wishToQuit = quit.nextLine();
            if (wishToQuit.equalsIgnoreCase("quit")) {
                return true;
            }
        }
        return false;
    }

    /**
     * A player can ask for help when he enters a room if he wants to
     *
     * @param command the command
     */
    public void askForHelp(Command command) {

        // Check if the player ask for help with a specific item
        if (command.hasSecondWord()) {
            String secondWord = command.getSecondWord();
            ItemType itemType = ItemType.get(secondWord);
            if (itemType == ItemType.NONE) {
                System.out.println("The blonde assistant doesn't know about that thing. ");
            } else {
                player.getCurrentRoom().askForHelp(itemType);
            }
            time.updateTime(5);
        } // Else print a list with the items that you can get help with out
        else {
            System.out.println("You can ask for help for finding these items:");
            for (ItemType itemType : ItemType.values()) {
                if (itemType != ItemType.NONE) {
                    System.out.println(itemType.toString());
                }
            }
        }
    }

    @Override
    public int getTimeBetweenEvents() {
        // We want 60 minutes between each and all callbacks
        return 60;
    }

    @Override
    public void timeCallback(int timeAt, Player player) {
        // If the current time is more than 22 o'clock
        if (timeAt >= GAME_END_TIME) {
            // If the time is up, and the player is in an exit room, then they should end the game
            if (this.player.getCurrentRoom() instanceof Exit) {
                // TODO Exit the game once done
                highScore.showScore();
            } else {
                this.player.clearBoughtItems();
                // The time is up, but the player cannot yet leave.
                // sooo.. Game over!!
                gameOver("You did not manage to get to the exit before IKEA closed. \n"
                        + "The security guards threw you out, and destroyed all the things you bought.");
            }
        }

    }

    /**
     * Marks the game for gameover
     *
     * @param description
     */
    private void gameOver(String description) {
        this.gameOverMessage = description;
    }

    public Player getPlayer() {
        return player;
    }
    
    public void updateTime(int timeDif){
        time.updateTime(timeDif);
    }
    
}
