package zuulFramework.worldofzuul;

/**
 * The "main" in the game
 */
public class Game 
{
    /**
     * Handles reading commands from the user
     */
    private Parser parser;

    /**
     * Represents the room we are currently in
     */
    private Room currentRoom;

    /**
     * Creates a new game, with default values
     */
    public Game() 
    {
        // Create all the rooms in the game
        createRooms();

        // Initialize the parser for reading in commands
        parser = new Parser();
    }

    /**
     * Create the rooms in the game and any exits between them
     */
    private void createRooms()
    {   
        //Initializing the different rooms
        Room outside, theatre, pub, lab, office;

        // Construct the different rooms we have in the game
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        // Define all the exits for the outside room
        outside.setExit(Direction.EAST, theatre);
        outside.setExit(Direction.SOUTH, lab);
        outside.setExit(Direction.WEST, pub);

        // Define the exits from theater
        theatre.setExit(Direction.NORTH, outside);

        // Define the exits for the pub
        pub.setExit("east", outside);

        // Define the exits for the lab
        lab.setExit("north", outside);
        lab.setExit("east", office);

        // Define the exits for the office
        office.setExit("west", lab);

        //Sets the current location to the outside room
        currentRoom = outside;
    }

    /**
     * Starts the actual game
     */
    public void play() 
    {
        // Tell the user about the game
        printWelcome();

        // The user hasn't finished the game when they start
        boolean finished;

        // Ask the user for commands, and do whatever the user told us
        do {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        while (!finished);

        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Prints the welcome message and a description of the current room
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Process the provided command, and acts upon it
     * @param command The command to process
     * @return true if the game has finished, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        // If we don't know the command, then tell the user
        if(commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        // If the user asked for help, print that
        if (commandWord == CommandWord.HELP) {
            printHelp();
        }
        // If the user ask to go to a certain room, to there
        else if (commandWord == CommandWord.GO) {
            goRoom(command);
        }
        // If the user asked to quit the game, quit
        else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        }
        return wantToQuit;
    }
    /**
     * Prints a welcome to the user
     * And a list of the commands that can be used
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    /**
     * Checks if there is a second word after the CommandWord "GO"
     * And change to that room if it exists
     * @param command the command to check
     */
    private void goRoom(Command command) 
    {
        // Check if the command has a room to go to
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        // Gets the direction of the room
        String direction = command.getSecondWord();
        // Get to the room we want to go to
        Room nextRoom = currentRoom.getExit(direction);
        //Checks if there is not a next room and print and error to user
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        //If there is a next room the current room will be the next room and prints out the method
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    /**
     * Checks if the command QUIT has been used 
     * @param command the command to check
     * @return a boolean expression
     */
    private boolean quit(Command command) 
    {
        // There is only one thing to quit, so the user shouldn't specify anything after
        // the command
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        //If there is only the word QUIT it will return a true
        else {
            return true;
        }
    }
}
