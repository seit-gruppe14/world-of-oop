package zuulFramework.worldofzuul;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The "main" in the game
 */
public class Game implements ITimeEventAble {

    private static final int GAME_END_TIME = 22 * 60;
    /**
     * Handles reading commands from the user
     */
    private Parser parser;

    /**
     * The player instance
     */
    private Player player;

    private String gameOverMessage = null;
    private Time time;

    /**
     * Creates a new game, with default values
     */
    public Game() {
        // Initialize a new time
        time = new Time(this);
        
        // Create a list to store all the time based callbacks
        time.getList();

        // Initialize a new player
        player = new Player();

        // Create all the rooms in the game
        createRooms();

        // Initialize the parser for reading in commands
        parser = new Parser();

        // Add own time callback
        time.addTimeEvent(this);

    }

    /**
     * Create the rooms in the game and any exits between them
     */
    private void createRooms() {
        //Initializing the different rooms
        Room entrance, exit, ballroom, kitchen, dinningRoom, livingRoom, canteen,
                bedroom, childrensRoom, electronics, toilet, office;

        //Create all the room in the game (as an object)
        entrance = new Room("at the entrance to Ikea");
        exit = new Exit("at the exit of Ikea, you can pay for your stuff");
        canteen = new Canteen("hungry? Well, then buy some food!");
        ballroom = new Ballroom("at the ballroom, here you can play with your kids or yourself");
        kitchen = new SalesRoom("at the room for adults who loves to cook", ItemType.CUTLERY);
        dinningRoom = new SalesRoom("at a place where you probably will eat", ItemType.DINNERCHAIR, ItemType.DINNERTABLE);
        livingRoom = new SalesRoom("at a place where you can relax", ItemType.SHELVES, ItemType.SOFA);
        bedroom = new SalesRoom("sleepy?", ItemType.BED);
        childrensRoom = new SalesRoom("at a place that children love", ItemType.TEDDY_BEAR, ItemType.SOFA);
        electronics = new SalesRoom("at a place where nerds spend their time", ItemType.COMPUTER);
        toilet = new SalesRoom("going to pee? take a bath?", ItemType.TOILET);
        office = new SalesRoom("loving the song by rihanna - Work", ItemType.DESK);

        //Create exits for each room with directions.
        entrance.setExit(Direction.SOUTH, kitchen);
        exit.setExit(Direction.SOUTH, dinningRoom);
        ballroom.setExit(Direction.EAST, kitchen);
        kitchen.setExit(Direction.NORTH, entrance);
        kitchen.setExit(Direction.WEST, ballroom);
        kitchen.setExit(Direction.SOUTH, canteen);
        kitchen.setExit(Direction.EAST, dinningRoom);
        canteen.setExit(Direction.NORTH, kitchen);
        toilet.setExit(Direction.WEST, electronics);
        toilet.setExit(Direction.EAST, office);
        office.setExit(Direction.WEST, toilet);
        office.setExit(Direction.NORTH, bedroom);
        electronics.setExit(Direction.EAST, toilet);
        bedroom.setExit(Direction.SOUTH, office);
        bedroom.setExit(Direction.NORTH, dinningRoom);
        bedroom.setExit(Direction.EAST, childrensRoom);
        dinningRoom.setExit(Direction.SOUTH, bedroom);
        dinningRoom.setExit(Direction.WEST, kitchen);
        dinningRoom.setExit(Direction.EAST, livingRoom);
        dinningRoom.setExit(Direction.NORTH, exit);
        livingRoom.setExit(Direction.WEST, dinningRoom);
        livingRoom.setExit(Direction.SOUTH, childrensRoom);
        childrensRoom.setExit(Direction.NORTH, livingRoom);
        childrensRoom.setExit(Direction.WEST, bedroom);

        //Sets the current location to the outside room
        player.setCurrentRoom(entrance);

        addMonsterToRoom(entrance, 10);
        addMonsterToRoom(canteen, 2);
        addMonsterToRoom(ballroom, 4);
        addMonsterToRoom(kitchen, 5);
        addMonsterToRoom(dinningRoom, 3);
        addMonsterToRoom(livingRoom, 4);
        addMonsterToRoom(bedroom, 2);
        addMonsterToRoom(childrensRoom, 8);
        addMonsterToRoom(electronics, 6);
        addMonsterToRoom(toilet, 4);
        addMonsterToRoom(office, 5);
    }

    /**
     * addMonsterToRoom is used to add a given number of monters to a given
     * room.
     *
     * @param room of the type Room
     * @param numberOfMonsters of the type int
     */
    private void addMonsterToRoom(Room room, int numberOfMonsters) {
        for (int i = 0; i < numberOfMonsters; i++) {
            Monster monster = new Monster(room);
            time.addTimeEvent(monster);
        }
    }

    /**
     * Starts the actual game
     */
    public void play() {
        // Tell the user about the game
        printWelcome();

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
            Command command = parser.getCommand();
            finished = processCommand(command);

            if (player.isPlayerDead()) {
                //TODO add more death messages
                gameOver(SillyMessages.getDeathMessage());
            }
        } while (!finished);
        try {
            int score = calcScore(itemList());
            System.out.println("Your score was " + score);
            printScoreToFile(score);
            System.out.println("Top 5 scores were");
            showScore();
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
                askForHelp(command);
                break;
        }
        return wantToQuit;
    }

    /**
     * Prints a welcome to the user And a list of the commands that can be used
     */
    private void printHelp() {
        // TODO Write proper help text
        System.out.println("");
        System.out.println("");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
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

    /**
     * Calculates the score and prints it into a .txt file
     */
    public int calcScore(ItemType[] listOfItems) {

        int score = 0;
        Set<ItemType> s = new HashSet<ItemType>();
        List<Item> items = player.getBoughtItems();
        for (int i = 0; i < player.getBoughtItems().size(); i++) {
            s.add(items.get(i).getType());
        }
        for (int j = 0; j < s.size(); j++) {
            if (s.contains(listOfItems[j])) {
                score += 10;
            }
        }
        //Creating a multiplier that rewards the player for completing the game faster.
        for (int i = 12; i > 0; i--) {
            score = (int) (score * ((0.083 * i) + 1));
        }
        return score;
    }

    public void printScoreToFile(int score) throws IOException {
        FileWriter fileWriter = null;
        fileWriter = new FileWriter("score.txt", Boolean.TRUE);
        String stringToWrite = score + "";
        fileWriter.write(stringToWrite + System.lineSeparator());
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Reads from a .txt file and prints the first five lines
     */
    public void showScore() {
        ArrayList<Integer> score = new ArrayList<>();
        try {
            File file = new File("score.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                score.add(Integer.parseInt(scanner.nextLine()));
            }
            for (int iteration = 0; iteration < score.size(); iteration++) {
                int endOfArray = score.size() - iteration;
                boolean swapped = false;
                for (int index = 0; index < endOfArray - 1; index++) {
                    if (score.get(index) < score.get(index + 1)) {
                        Integer temp = score.get(index);
                        score.set(index, score.get(index + 1));
                        score.set(index + 1, temp);
                        swapped = true;
                    }
                }
                if (!swapped) {
                    break;
                }
            }
            for (int count = 0; count < 5 && count < score.size(); count++) {
                System.out.println(score.get(count));
            }
        } catch (FileNotFoundException ex) {

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
                showScore();
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

    public ItemType[] itemList() {
        ItemType[] listOfItems = new ItemType[10];
        listOfItems[0] = ItemType.BED;
        listOfItems[1] = ItemType.DINNERTABLE;
        listOfItems[2] = ItemType.DINNERCHAIR;
        listOfItems[3] = ItemType.SHELVES;
        listOfItems[4] = ItemType.DESK;
        listOfItems[5] = ItemType.CUTLERY;
        listOfItems[6] = ItemType.LAMP;
        listOfItems[7] = ItemType.COMPUTER;
        listOfItems[8] = ItemType.LAMP;
        listOfItems[9] = ItemType.SOFA;
        return listOfItems;
    }

    public Player getPlayer() {
        return player;
    }
    
    public void updateTime(int timeDif){
        time.updateTime(timeDif);
    }
    
}
