package zuulFramework.worldofzuul;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The "main" in the game
 */
public class Game implements ITimeEventAble {
    private static Game instance;
    /**
     * Handles reading commands from the user
     */
    private Parser parser;
    /**
     * The current ingame time in minutes since the start of the day at 10 o'clock
     * Starts as 10:00
     */
    private int time = 60 * 10; // 60 minutes times 10 hours
    /**
     * All the callbacks that should be done according with different times.
     */
    private List<TimeCallback> callbacks;
    /**
     * The player instance
     */
    private Player player;

    /**
     * Creates a new game, with default values
     */
    private Game() {
        // Create a list to store all the time based callbacks
        this.callbacks = new ArrayList<>();

        // Initialize a new player
        player = new Player();

        // Create all the rooms in the game
        createRooms();


        // Initialize the parser for reading in commands
        parser = new Parser();

        // Add own time callback
        addTimeEvent(this);

    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
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
        childrensRoom = new SalesRoom("at a place that children love", ItemType.BEAR, ItemType.SOFA);
        electronics = new SalesRoom("at a place where nerds spend their time", ItemType.COMPUTER);
        toilet = new SalesRoom("going to pee? take a bath?", ItemType.TOILET_PAPER);
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
     * Gets the current time nicely formatted as a string
     *
     * @return A string like "13:37"
     */
    private String getNiceFormattedTime() {
        // Calculate the hours
        String hours = time / 60 + "";
        String minutes = time % 60 + "";

        if (hours.length() != 2) {
            hours = "0" + hours;
        }
        if (minutes.length() != 2) {
            minutes = "0" + minutes;
        }

        return String.format("%2s:%2s", hours, minutes);
    }

    /**
     * Starts the actual game
     */
    public void play() {
        // Tell the user about the game
        printWelcome();

        // The user hasn't finished the game when they start
        boolean finished;

        try {
            // Ask the user for commands, and do whatever the user told us
            do {
                doTimeEvent();
                // Write the current time
                System.out.printf("The time is now %s\n", getNiceFormattedTime());
                Command command = parser.getCommand();
                finished = processCommand(command);
            }
            while (!finished);
        } catch (GameOverException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * This method is called each time the play event happens, and should be used
     * to hook into things that should happen based on time.
     */
    private void doTimeEvent() {
        boolean didCallback;
        do {
            didCallback = false;
            for (TimeCallback callback : this.callbacks) {
                int timeSinceLastCall = callback.getTimeSinceLastCallback();
                ITimeEventAble event = callback.getCallback();
                // Check if it has been at least the requiret amount of time since last callback
                if (timeSinceLastCall >= event.getTimeBetweenEvents()) {
                    // Indicate that we have done at least one callback
                    didCallback = true;

                    // Calculate the new "time since last callback".
                    // This is actually not the complete case, as we allow events to have a very high rate
                    // Say we had 50 minutes between doTimeEvent calls, however an event expects to happen every
                    // 5 minute, then we should make sure the event is emitted 10 times. For this reason we
                    // only increment by the timeBetweenEvents specified.
                    int newTime = timeSinceLastCall - event.getTimeBetweenEvents();
                    callback.setTimeSinceLastCallback(newTime);

                    // Calculate the time at which the event "happened"
                    int timeAt = time - newTime;

                    // Do the actual callback.
                    event.timeCallback(timeAt, this.player);
                }
            }
            // Keep doing this until we didn't do a callback
        } while (didCallback);
    }

    /**
     * Add a callback to time based events.
     *
     * @param callback The callback that should be called when ever a time event has happened.
     */
    public void addTimeEvent(ITimeEventAble callback) {
        this.callbacks.add(new TimeCallback(callback));
    }

    /**
     * Removes a time callback from the list.
     * Call this method before you remove something that is in the callback list, otherwise it
     * cannot be garbage collected.
     *
     * @param callback The callback to remove
     */
    public void removeTimeEvent(ITimeEventAble callback) {
        this.callbacks = this.callbacks.stream().filter(timeCallback -> timeCallback.getCallback() != callback).collect(Collectors.toList());
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

            // Change the game time. It always take 15 minutes to change room.
            updateTime(15);
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
        for (TimeCallback callback : callbacks) {
            callback.setTimeSinceLastCallback(callback.getTimeSinceLastCallback() + timeDif);
        }
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
                System.out.println("The blonde assistant doesn't know about that thing. ");
            } else {
                player.getCurrentRoom().askForHelp(itemType);
            }
            updateTime(5);
        }
        // Else print a list with the items that you can get help with out
        else {
            System.out.println("You can ask for help for finding these items:");
            for (ItemType itemType : ItemType.values()) {
                System.out.println(itemType.toString());
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
        if (timeAt >= (22 * 60)) {
            // If the time is up, and the player is in an exit room, then they should end the game
            if (this.player.getCurrentRoom() instanceof Exit) {
                // TODO Exit the game once done
            } else {
                // The time is up, but the player cannot yet leave.
                // sooo.. Game over!!
                gameOver("You did not manage to get to the exit before IKEA closed. \n" +
                        "The security guards threw you out, and destroyed all the things you bought.");
            }
        }
        if (player.isPlayerDead()){
<<<<<<< Updated upstream
            gameOver("TODO Add description for health death");
=======
<<<<<<< HEAD
            gameOver();   
=======
            gameOver("TODO Add description for health death");
>>>>>>> origin/master
>>>>>>> Stashed changes
        }
    }

    private void gameOver(String description) {
        // Yes, I'm using exception for flow control.
        // Yes, I know it's bad.
        // No, I don't care.
        throw new GameOverException(description);
    }

    /**
     * Stored specific callbacks that needs to happen at specific time.
     */
    private class TimeCallback {
        /**
         * The time since the callback was last called
         */
        private int timeSinceLastCallback = 0;

        /**
         * The callback itself to call
         */
        private ITimeEventAble callback;

        /**
         * Creates a callback store.
         *
         * @param callback
         */
        public TimeCallback(ITimeEventAble callback) {
            this.callback = callback;
        }

        /**
         * Gets the time since the callback was last called
         *
         * @return
         */
        public int getTimeSinceLastCallback() {
            return timeSinceLastCallback;
        }

        /**
         * Sets the time since the callback was last called
         *
         * @param timeSinceLastCallback
         */
        public void setTimeSinceLastCallback(int timeSinceLastCallback) {
            this.timeSinceLastCallback = timeSinceLastCallback;
        }

        /**
         * Gets the callback that should be made
         *
         * @return
         */
        public ITimeEventAble getCallback() {
            return callback;
        }

    }
}
