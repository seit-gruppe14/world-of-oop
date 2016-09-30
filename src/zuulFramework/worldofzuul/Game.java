package zuulFramework.worldofzuul;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Creates instances of the class Room
     */
    private void createRooms()
    {   
        //Initializing the different rooms
        Room outside, theatre, pub, lab, office;
      
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        //Uses the setExit constructor for each room and defining them
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        //Sets the current location to the outside room
        currentRoom = outside;
    }
    
    
    public void play() 
    {            
        printWelcome();

        //Sets the boolean expression to false        
        boolean finished = false;
        //TODO - kan ikke forklar den her
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }
    /**
     * When the method printWelcome is used, it prints out following lines 
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

    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();
        //This loop checks for the players input - if the used word is not in CommandWord it prints "I don't know what you mean..." and returns a false
        if(commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        /**
         * These loops checks for which command the player has used
         */
        //If the command word HELP is used - printHelp is being executed
        if (commandWord == CommandWord.HELP) {
            printHelp();
        }
        //If the command word GO is used - goRoom is executed
        else if (commandWord == CommandWord.GO) {
            goRoom(command);
        }
        //If the command word QUIT is used - wantToQuit is executed
        else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        }
        return wantToQuit;
    }
    /**
     * When the method printHelp is used it will print out all these lines and show the different words, which can be used
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
     * @param command the command to check
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }
        
        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);
        //Checks if there is not a next room and prints out the line
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
        //If theres a word after QUIT it will print the following line and return a false
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
