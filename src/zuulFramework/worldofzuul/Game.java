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
        // Initialize a new player
        player = new Player();

        // Create all the rooms in the game
        createRooms();


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
        kitchen = new SalesRoom("at the room for adults who loves to cook", ItemType.CUTLERY);
        dinningRoom = new SalesRoom("at a place where you probably will eat", ItemType.DINNERCHAIR, ItemType.DINNERTABLE);
        livingRoom = new SalesRoom("at a place where you can relax", ItemType.SHELVES);
        canteen = new Canteen("hungry? Well, then buy some food!");
        bedroom = new SalesRoom("sleepy?", ItemType.BED);
        childrensRoom = new SalesRoom("at a place that children love");
        electronics = new SalesRoom("at a place where nerds spend their time");
        toilet = new SalesRoom("going to pee? take a bath?");
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
        livingRoom.setExit(Direction.WEST, dinningRoom);
        livingRoom.setExit(Direction.SOUTH, childrensRoom);
        childrensRoom.setExit(Direction.NORTH, livingRoom);
        childrensRoom.setExit(Direction.WEST, bedroom);


        //Sets the current location to the outside room
        player.setCurrentRoom(entrance);
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
        System.out.println("Welcöme möney spender.");
        System.out.println("Tensiön is high at IKEA Ödense as yöu are waiting tö shöp-amök.");
        System.out.println("It's BLACK FRIDAY and yöu're ön the löököut för the best öffers pössible to furnish your new appartment.");
        System.out.println("But be careful as the öther shöppers might beat yöu tö it or tramble yöu tö death!");
        System.out.println("Are yöu ready?");
        System.out.println("");
        System.out.println("If you need assistance type '" + CommandWord.HELP + "' tö ask öne öf the blönde IKEA emplöyees.");
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
                pay(command);
                break;
            case ASK:
                askForHelp(command);
                break;
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

    /**
     * This method updates time for each time a player spends time in a room
     * @param timeDif The amound of time that has changed
     */
    public void updateTime(int timeDif) {
        
        this.time += timeDif;
    }

    /**
     * The player is able to pick up items in the room
     * @param command the command 
     */
    public void pickUp(Command command) {
        
        // Check if the player has choosen an item to pick up
        if(command.hasSecondWord()) {
            
            // Ask the player objekt to pick up an item in the room
            boolean succes = player.pickUp(command.getSecondWord());
            
            // If the player can pick up the item, then print the item the
            // player has picked up
            if(succes) {
                System.out.println("you picked up " + command.getSecondWord() + ".");
            }
            // Else print an error
            else {
                System.out.println("There is not item named " + command.getSecondWord() + ".");
            }
        }
        // else print a list of the items in the room out to the player. 
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
            // else print error
            } else {
                System.out.println("There is nothing in this room that can be picked up.");
            }
        }
        
    }

    /**
     * A player can drop their items from their inventory
     * @param command the command
     */
    
    public void drop(Command command) {
        
        // Check if the player can drop an item off in this room.
        if(player.getCurrentRoom() instanceof SalesRoom) {
            
            // If the player has typed an item that is avaiable then he can drop 
            // the item.
            if(command.hasSecondWord()) {
                boolean succes = player.drop(command.getSecondWord());
                if(succes) {
                    System.out.println("You dropped the item " + command.getSecondWord() + " in the room");
                }
                // else print an error
                else {
                    System.out.println("You have no such item dropped");
                }
            }
            // Else print a list of the items that the player can drop.
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

    /**
     * A player can pay in a room.
     * @param command the command
     */
    public void pay(Command command) {
        
        // Check if the currentroom is a room where you can pay
        Room currentRoom = player.getCurrentRoom();
        // If you can pay, then you pay
        if(currentRoom instanceof ICanPay) {
            ICanPay payRoom = (ICanPay)currentRoom;
            payRoom.buy(player, command, this);
        }
        // else print an error
        else {
            System.out.println("There is nowhere you can pay in this room");
        }
    }

    /**
     * A player can ask for help when he enters a room if he wants to
     * @param command the command
     */
    public void askForHelp(Command command) {
        
        // Check if the player ask for help with a specific item
        if(command.hasSecondWord()) {
            String secondWord = command.getSecondWord();
            ItemType itemType = ItemType.get(secondWord);
            if (itemType == ItemType.NONE) {
                System.out.println("Item type not recognized");
            } else {
                player.getCurrentRoom().askForHelp(itemType);
            }
        }
        // Else print a list with the items that you can get help with out
        else {
            System.out.println("You can ask for help for finding these items:");
            for (ItemType itemType : ItemType.values()) {
                System.out.println(itemType.toString());
            }
        }
        
        
    }
}
