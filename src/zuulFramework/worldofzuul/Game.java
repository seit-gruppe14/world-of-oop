package zuulFramework.worldofzuul;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
     * @param mapLocation The file path to the .wop file.
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

        // Add own time callback
        // witch takes an instance of the Game object as a parameter
        time.addTimeEvent(this);

        this.startRoom = getPlayer().getCurrentRoom();

    }

    /**
     * Create the rooms in the game and any exits between them
     *
     * @param mapLocation The file path to the .wop file.
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
     * Adds a welcome message to the AddEventMessage system. 
     */
    public void getWelcomeMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Welcöme möney spender.").append("\n");
        stringBuilder.append("Tensiön is high at IKEA Ödense as yöu are waiting tö shöp-amök.").append("\n");
        stringBuilder.append("It's BLACK FRIDAY and yöu're ön the löököut för the best öffers pössible tö furnish yöur new appartment.").append("\n");
        stringBuilder.append("But be careful as the öther shöppers might beat yöu tö it or tramble yöu tö death!").append("\n");
        stringBuilder.append("Are yöu ready?").append("\n");
        stringBuilder.append("\n");
        stringBuilder.append(("If you need assistance type '%s' tö ask öne öf the blönde IKEA emplöyees.")).append("\n");
        stringBuilder.append(player.getCurrentRoom().getLongDescription()).append("\n");
        addEventMessages(stringBuilder.toString());
    }

    /**
     * Adds a message that shows the items that needs to bought to the addEventMessage system. 
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
     * Adds a direction message to the addEventMessage system if there is an employee present.  
     * @param itemType The types of items in the game. 
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
     * Moves the player in the direction,
     * and uses addEventMessages a printable signal string that JavaFX can handle.
     * 
     * @param direction a string of the direction the entity moves. 
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

    /**
     * Makes the player able to pick up items in the room and adds messages to
     * the addEventMessages system based on the succes of the action. 
     * @param selectedItem a string of the name of the item the entity picks up. 
     * @throws IllegalArgumentException. 
     */
    public void pickUp(String selectedItem) throws IllegalArgumentException {
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
            throw new IllegalArgumentException("Error on item pickup\n");
        }
    }

    /**
     * Makes the player able to drop their items from their inventory and adds a
     * message to the addEventMessages system. 
     * @param selectedItem a string of the name of the item the entity drops.
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
     * @return the time between the callbacks. 
     */
    @Override
    public int getTimeBetweenEvents() {
        // We want 60 minutes between each and all callbacks
        return 60;
    }

	/**
	 * Handles the callback that checks if the game has ended.
	 *
	 * @param timeAt an int of the current time in the game. 
         * @param game the instance of game. 
	 */
	@Override
	public void timeCallback(int timeAt, Game game) {
		// If the current time is more than 22 o'clock
		if (timeAt >= gameEndTime) {
			// If the time is up, and the player is in an exit room, then they should end the game
			if (this.player.getCurrentRoom() instanceof Exit) {
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
     * @return a game over message. 
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
     * @return a list of item types. 
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
    
    /**
     * Get's the highScore instance of the game. 
     * @return the instance of highScore. 
     */
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
    
    /**
     * adds an eventMessage to the eventMessage system. 
     * @param eventMessage a message. 
     */
    public void addEventMessages(String eventMessage) {
        this.eventMessagesCallback.handle(eventMessage);
    }
    
    /**
     * sets an instance of the eventMessage interface to this object. 
     * @param i IEventMessages an object that is called when there are new messages. 
     */

    public void addMessageListener(IEventMessages i) {
        this.eventMessagesCallback = i;
    }
    
    /**
     * Get's the time when IKEA closes.
     * @return the time when the game ends. 
     */

    public int getGameEndTime() {
        return gameEndTime;
    }
    
    /**
     * Get's the list of the item types in the game. 
     * @return a list of itemTypes.  
     */

    public ObservableList<ItemType> getItemList() {
        return itemList;
    }
}
