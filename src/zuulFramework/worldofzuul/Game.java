package zuulFramework.worldofzuul;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zuulFramework.worldofzuul.commands.CommandWord;
import zuulFramework.worldofzuul.commands.Parser;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.entities.Player;
import zuulFramework.worldofzuul.rooms.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        ItemType.COMPUTER,
        ItemType.SOFA
    );
    private IEventMessages eventMessagesCallback;

    /**
     * Creates a new game, with default values
     *
     * @param mapLocation
     */
    public Game(String mapLocation) {
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
        createRooms(mapLocation);

        // Initialize the parser for reading in commands
        parser = new Parser();

        // Add own time callback
        // witch takes an instance of the Game object as a parameter
        time.addTimeEvent(this);

        this.startRoom = getPlayer().getCurrentRoom();

    }

    /**
     * Create the rooms in the game and any exits between them
     *
     * @param mapLocation
     */
    private void createRooms(String mapLocation) {
        //Initializing the different rooms
        List<Room> rooms = null;
        try {
            rooms = WorldLoader.LoadWorld(mapLocation, time);
        } catch (IllegalArgumentException | IOException e) {
            System.out.println("Unable to load world file.");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(404);
        }
        System.out.println(rooms);
        // The first room in the list, will always be the room the player starts in
        player.setCurrentRoom(rooms.get(0));
    }

    /**
     * Prints the welcome message and a description of the current room
     */
    public void getWelcomeMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Welcöme möney spender.").append("\n");
        stringBuilder.append("Tensiön is high at IKEA Ödense as yöu are waiting tö shöp-amök.").append("\n");
        stringBuilder.append("It's BLACK FRIDAY and yöu're ön the löököut för the best öffers pössible tö furnish yöur new appartment.").append("\n");
        stringBuilder.append("But be careful as the öther shöppers might beat yöu tö it or tramble yöu tö death!").append("\n");
        stringBuilder.append("Are yöu ready?").append("\n");
        stringBuilder.append("\n");
        stringBuilder.append(String.format("If you need assistance type '%s' tö ask öne öf the blönde IKEA emplöyees.%n", CommandWord.HELP)).append("\n");
        stringBuilder.append(player.getCurrentRoom().getLongDescription()).append("\n");
        addEventMessages(stringBuilder.toString());
    }

    /**
     * Returns the item help discription, which tells the player what items is
     * needed to fully complete the game, the string is formatted to be handled
     * be JavaFX.
     *
     * @return String
     */
    public void printHelp() {
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
        addEventMessages(stringBuilder.toString());
    }

    /**
     * Input String itemType, and return an string that describes the direction
     * towards the itemTypes that JavaFX can handle.
     *
     * @param itemType
     * @return String direction
     */
    public void askForHelp(String itemType) {
        if (this.player.getCurrentRoom().hasEmployee()) {
            this.time.updateTime(5);
            String helpAnswer = this.player.getCurrentRoom().askForHelp(ItemType.get(itemType));
            addEventMessages(helpAnswer);
        } else {
            addEventMessages("There is no employees in this room you can ask.\n");
        }
    }

    /**
     * Input String direction, moves the player in the direction,
     * and returns a printable signal string that JavaFX can handle.
     *
     * @param direction
     * @return String
     */
    public void handleRoomMovement(String direction) {
        Room nextRoom = this.player.goRoom(direction);
        StringBuilder stringBuilder = new StringBuilder();
        if (nextRoom != null) {

            if (nextRoom.isLocked()) {
                addEventMessages("The room is locked!\n");
            }
            stringBuilder.append("You went " + direction + ".").append("\n");
            stringBuilder.append(nextRoom.getLongDescription()).append("\n");
            time.updateTime(15);
            if (nextRoom instanceof IHaveSpecialEvent) {
                stringBuilder.append(((IHaveSpecialEvent) nextRoom).doSpecialEvent(this));
            } else
                addEventMessages(stringBuilder.toString());
        } else
            addEventMessages("There is no room in that direction!\n");
    }

    //TODO integrate this into JavaFX

    /**
     * The player is able to pick up items in the room
     */
    public void pickUp(String selectedItem) throws Exception {
        time.updateTime(5);
        if (player.getCurrentRoom() instanceof SalesRoom) {
            Item currentItem = this.player.pickUp(selectedItem, this);
            if (currentItem == null) {
                addEventMessages("Oh no something went horribly wrong\n");
            } else {
                if (currentItem.getWeight() + player.getCarryWeight() > player.getMaxCarryWeight()) {
                    addEventMessages("Max carry weight exceeded\n");
                } else {
                    addEventMessages("Item was added to your inventory\n");
                }
            }
        } else {
            throw new Exception("Error on item pickup\n");
        }
    }

    /**
     * A player can drop their items from their inventory
     */
    public void drop(Item selectedItem) {
        // Check if the player can drop an item off in this room.
        if (player.getCurrentRoom() instanceof SalesRoom) {
            time.updateTime(5);
            this.player.drop(selectedItem.getName());
            addEventMessages("You dropped an item: " + selectedItem.getType() + "\n");
        } else {
            addEventMessages("You can't drop items in this room.\n");
        }
    }

    /**
     * Handles the player payment, and score setting.
     */
    public void pay() {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom instanceof ICanPay) {
            StringBuilder stringBuilder = new StringBuilder();
            ICanPay payRoom = (ICanPay) currentRoom;
            String paymentMessage = payRoom.buy(player, this);
            stringBuilder.append(paymentMessage).append("\n");
//            stringBuilder.append("Your score is saved and you can quit safely.").append("\n");
            addEventMessages(stringBuilder.toString());
        } else {
            addEventMessages("There is nowhere you can pay in this room.\n");
        }
    }

    /**
     * Sets the time between each and all callbacks
     *
     * @return
     */
    @Override
    public int getTimeBetweenEvents() {
        // We want 60 minutes between each and all callbacks
        return 60;
    }

    /**
     * Handles the callback that checks if the game has ended.
     *
     * @param timeAt
     */
    @Override
    public void timeCallback(int timeAt, Game game) {
        // If the current time is more than 22 o'clock
        if (timeAt >= gameEndTime) {
            // If the time is up, and the player is in an exit room, then they should end the game
            if (this.player.getCurrentRoom() instanceof Exit) {
                // TODO Exit the game once done
                HighScore.showScore();
            } else {
                this.player.clearBoughtItems();
                // The time is up, but the player cannot yet leave.
                // sooo.. Game over!!
                gameOver("You did not manage to get to the exit before IKEA closed. \n"
                    + "The security guards threw you out, and destroyed all the things you bought.\n");
            }
        }
    }

    /**
     * Marks the game for gameover
     *
     * @param description String
     */
    public String gameOver(String description) {
        return this.gameOverMessage = description;
    }

    /**
     * Get the Player instance of the game
     *
     * @return the player instance of the game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Update the time with a given amount
     *
     * @param timeDif integer
     */
    public void updateTime(int timeDif) {
        time.updateTime(timeDif);
    }

    /**
     * Extend the time with a given amount
     *
     * @param time integer
     */
    public void extendGameTime(int time) {
        gameEndTime += time;
    }

    /**
     * Get the list of game item types
     *
     * @return
     */
    public ObservableList<ItemType> getItemsTypeList() {
        return this.itemList;
    }

    /**
     * Get the current game time
     *
     * @return Time current game time
     */
    public Time getTime() {
        return this.time;
    }

    public HighScore getHighScore() {
        return this.highScore;
    }

    /**
     * Get the player start room
     *
     * @return Room the player start room
     */
    public Room getStartRoom() {
        return startRoom;
    }

    public void addEventMessages(String eventMessage) {
        this.eventMessagesCallback.handle(eventMessage);
    }

    public void addMessageListener(IEventMessages i) {
        this.eventMessagesCallback = i;
    }

    public int getGameEndTime() {
        return gameEndTime;
    }

    public ObservableList<ItemType> getItemList() {
        return itemList;
    }
}
