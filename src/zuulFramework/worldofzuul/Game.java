package zuulFramework.worldofzuul;

import zuulFramework.worldofzuul.commands.Command;
import zuulFramework.worldofzuul.commands.CommandWord;
import zuulFramework.worldofzuul.commands.Parser;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.entities.Player;
import zuulFramework.worldofzuul.helpers.SillyMessages;
import zuulFramework.worldofzuul.rooms.*;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The "main" in the game
 */
public class Game implements ITimeEventAble {

    private final Room startRoom;
    private int gameEndTime = 22 * 60;
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

    private ObservableList<ItemType> itemList = FXCollections.observableArrayList(
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
    );

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

        this.startRoom = getPlayer().getCurrentRoom();

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
            Command command = parser.getCommand();
            finished = processCommand(command);
            if (player.isPlayerDead()) {
                gameOver(SillyMessages.getDeathMessage());
            }
        } while (!finished);
        highScore.printScore(itemList);
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Prints the welcome message and a description of the current room
     */
    public String getWelcomeMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Welcöme möney spender.").append("\n");
        stringBuilder.append("Tensiön is high at IKEA Ödense as yöu are waiting tö shöp-amök.").append("\n");
        stringBuilder.append("It's BLACK FRIDAY and yöu're ön the löököut för the best öffers pössible tö furnish yöur new appartment.").append("\n");
        stringBuilder.append("But be careful as the öther shöppers might beat yöu tö it or tramble yöu tö death!").append("\n");
        stringBuilder.append("Are yöu ready?").append("\n");
        stringBuilder.append("\n");
        stringBuilder.append(String.format("If you need assistance type '%s' tö ask öne öf the blönde IKEA emplöyees.%n", CommandWord.HELP)).append("\n");
        stringBuilder.append(player.getCurrentRoom().getLongDescription()).append("\n");
        return stringBuilder.toString();
    }
    
    //TODO remove processCommand method when all the following commands are integrated into JavaFX
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
            // If the user asked to quit the game, quit
            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    /**
     * Returns the item help discription, which tells the player what items is
     * needed to fully complete the game, the string is formatted to be handled
     * be JavaFX.
     * @return String
     */
    public String printHelp() {
        List<ItemType> itemsToBuy = new ArrayList<ItemType>();
        for (ItemType itemType : getItemsTypeList()) {
            itemsToBuy.add(itemType);
        }

        List<Item> boughtItems = new ArrayList<>();
        boughtItems.addAll(player.getBoughtItems());
        boughtItems.addAll(player.getItems());
        for (Item boughtItem : boughtItems) {
            itemsToBuy.remove(boughtItem.getType());
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (itemsToBuy.size() > 0) {
            stringBuilder.append("You still need to buy the following:").append("\n");
            for (ItemType itemType : itemsToBuy) {
                stringBuilder.append(itemType.toString()).append("\n");
            }
        } else {
            stringBuilder.append("You have bought everything you need.");
        }
        return stringBuilder.toString();
    }

    /**
     * Input String itemType, and return an string that describes the direction
     * towards the itemTypes that JavaFX can handle.
     * @param itemType
     * @return String direction
     */
    public String askForHelp(String itemType) {
        if (this.player.getCurrentRoom().hasEmployee()) {
            this.time.updateTime(5);
            String helpAnswer = this.player.getCurrentRoom().askForHelp(ItemType.get(itemType));
            return helpAnswer;
        }
        return "There is no employees in this room you can ask.\n";
    }
    
    /**
     * Input String direction, moves the player in the direction,
     * and returns a printable signal string that JavaFX can handle.
     * @param direction
     * @return String
     */
    public String handleRoomMovement(String direction) {
        Room nextRoom = this.player.goRoom(direction);
        StringBuilder stringBuilder = new StringBuilder();
        if (nextRoom != null) {
            if(nextRoom.isLocked()) {
                return "The room is locked!\n";
            }
            stringBuilder.append("You went " + direction + ".").append("\n");
            stringBuilder.append(nextRoom.getLongDescription()).append("\n");
            time.updateTime(15);
            if (nextRoom instanceof IHaveSpecialEvent) {
                stringBuilder.append(((IHaveSpecialEvent) nextRoom).doSpecialEvent(this));
            }
            return stringBuilder.toString();
        }
        return "There is no room in that direction!\n";
    }

    //TODO integrate this into JavaFX
    /**
     * Checks if the command QUIT has been used
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
    
    //TODO integrate this into JavaFX
    /**
     * The player is able to pick up items in the room
     * @param command the command
     */
    public void pickUp(String selectedItem) throws Exception {
        time.updateTime(5);
        if (player.getCurrentRoom() instanceof SalesRoom) {
            this.player.pickUp(selectedItem, this);
        } else {
            throw new Exception("Error on item pickup");
        }
    }

    /**
     * A player can drop their items from their inventory
     *
     * @param command the command
     */
    public String drop(Item selectedItem) {
        // Check if the player can drop an item off in this room.
        if (player.getCurrentRoom() instanceof SalesRoom) {
            time.updateTime(5);
            this.player.drop(selectedItem.getName());
            return "You dropped an item: " + selectedItem.getType();
        } else {
            return "You can't drop items in this room.";
        }
    }

    /**
     * Handles the player payment, and score setting.
     * @param command the command
     */
    public String pay() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom instanceof ICanPay) {
            StringBuilder stringBuilder = new StringBuilder();
            ICanPay payRoom = (ICanPay) currentRoom;
            String paymentMessage = payRoom.buy(player, this);
            stringBuilder.append(paymentMessage).append("\n");
            stringBuilder.append("Your score is saved and you can quit safely.").append("\n");
            return stringBuilder.toString();
        } else {
            return "There is nowhere you can pay in this room.\n";
        }
    }

    /**
     * Sets the time between each and all callbacks
     * @return 
     */
    @Override
    public int getTimeBetweenEvents() {
        // We want 60 minutes between each and all callbacks
        return 60;
    }
    
    /**
     * Handles the callback that checks if the game has ended.
     * @param timeAt
     * @param player 
     */
    @Override
    public void timeCallback(int timeAt, Player player) {
        // If the current time is more than 22 o'clock
        if (timeAt >= gameEndTime) {
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
     * @param description String
     */
    private void gameOver(String description) {
        this.gameOverMessage = description;
    }

    /**
     * Get the Player instance of the game
     * @return the player instance of the game
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Update the time with a given amount
     * @param timeDif integer
     */
    public void updateTime(int timeDif){
        time.updateTime(timeDif);
    }
    
    /**
     * Extend the time with a given amount
     * @param time integer
     */
    public void extendGameTime(int time){
        gameEndTime += time;
    }

    /**
     * Get the list of game item types
     * @return 
     */
    public ObservableList<ItemType> getItemsTypeList (){
        return this.itemList;
    }

    /**
     * Get the current game time
     * @return Time current game time
     */
    public Time getTime() {
        return this.time;
    }

    /**
     * Get the player start room
     * @return Room the player start room
     */
    public Room getStartRoom() {
        return startRoom;
    }
}
