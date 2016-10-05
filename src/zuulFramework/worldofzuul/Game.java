package zuulFramework.worldofzuul;

import java.util.List;

/**
 * The "main" in the game
 */
public class Game {
    /**
     * Handles reading commands from the user
     */
    private Parser parser;

    private int time = 0;
    
    /**
     * The player instance
     */
    private Player player;

    /**
     * Creates a new game, with default values
     */
    public Game() {
        // Create all the rooms in the game
        createRooms();
        
        // Initialize a new player
        player = new Player();

        // Initialize the parser for reading in commands
        parser = new Parser();

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
        ballroom = new Ballroom("at the ballroom, here you can play with your kids or yourself");
        kitchen = new Room("at the room for adults who loves to cook");
        dinningRoom = new Room("at a place where you probably will eat");
        livingRoom = new Room("at a place where you can relax");
        canteen = new Canteen("hungry? Well, then buy some food!");
        bedroom = new Room("sleepy?");
        childrensRoom = new Room("at a place that children love");
        electronics = new Room("at a place where nerds spend their time");
        toilet = new Room("going to pee? take a bath?");
        office = new Room("loving the song by rihanna - Work");

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
        dinningRoom.setExit(Direction.NORTH, exit);
        dinningRoom.setExit(Direction.EAST, livingRoom);
        livingRoom.setExit(Direction.WEST, dinningRoom);
        livingRoom.setExit(Direction.SOUTH, childrensRoom);
        childrensRoom.setExit(Direction.NORTH, livingRoom);
        childrensRoom.setExit(Direction.WEST, bedroom);


        //Sets the current location to the outside room
    }

    /**
     * Starts the actual game
     */
    public void play() {
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
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
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
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Checks if there is a second word after the CommandWord "GO"
     * And change to that room of the player if it exists
     *
     * @param command the command to check
     */
    private void goRoom(Command command) {
        // Check if the command has a room to go to
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }
        // Gets the direction of the room
        String direction = command.getSecondWord();
        // Get to the room we want to go to
        Room nextRoom = player.goRoom(command.getSecondWord());
        //Checks if there is not a next room and print and error to user
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        //If there is a next room the current room will be the next room and prints out the method
        else {
            System.out.println(nextRoom.getLongDescription());
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
        }
        //If there is only the word QUIT it will return a true
        else {
            return true;
        }
    }

    public void updateTime(int timeDif) {
        
        this.time += timeDif;
    }

    public void pickUp(Command command) {
        
        if(command.hasSecondWord()) {
            boolean succes = player.pickUp(command.getSecondWord());
            if(succes) {
                System.out.println("you picked up " + command.getSecondWord() + ".");
            }
            else {
                System.out.println("There is not item named " + command.getSecondWord() + ".");
            }
        }
        else {
            Room currentRoom = player.getCurrentRoom();
            if(currentRoom instanceof SalesRoom) {
                System.out.println("The following items can be picked up in this room: ");
                SalesRoom sr = (SalesRoom)currentRoom;
                List<Item> items = sr.getItems();
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    System.out.println(item.getName());
                }
            } else {
                System.out.println("There is nothing in this room that can be picked up.");
            }
        }
        
    }

    public void drop(Command command) {
        
        if(player.getCurrentRoom() instanceof SalesRoom) {
            
            if(command.hasSecondWord()) {
                boolean succes = player.drop(command.getSecondWord());
                if(succes) {
                    System.out.println("You dropped the item " + command.getSecondWord() + " in the room");
                }
                else {
                    System.out.println("You have no such item dropped");
                }
            }
            
            else {
                System.out.println("Here is a list of items that you can drop");
                
                List<Item> items = player.getItems();
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    System.out.println(item.getName());
                }
            }
        }
       
    }

    public void pay(Command command) {
        
    }

    public void askForHelp(Command command) {
    }
}
