package zuulFramework.worldofzuul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class Room extends SalesRoom {
    /**
     * Describes the current room.
     */
    private String description;
    private ItemType itemType;

    /**
     * A map of rooms used save exits.
     */
    private HashMap<String, Room> exits;
    
    /**
     * A list containing items for every room
     */

    /**
     * Construct at room with a description and a set number of items in it, each room has a number of exits.
     * whom are stored in a hashmap.
     * @param description the description of the room
     * @param ItemType which itemtype the specific room contains.
     */
    public Room(String description) {
        // Set the description to be whatever the used said the description was.
        this.description = description;
        this.itemType = itemType;
        
        // Create the hashmaps to save exists.
        exits = new HashMap<String, Room>();
    }

    /**
     * Set an exit to to points to a certain room
     *
     * @param direction The direction of the exit
     * @param neighbor  The room the exit goes in to.
     */
    public void setExit(String direction, Room neighbor) {
        //Gives the direction of the exit and with room the player exits towards.
        exits.put(direction, neighbor);
    }

    /**
     * Gets the description of the room
     *
     * @return
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Gets a longer description of the room
     * which is description + a list of exists
     *
     * @return
     */
    public String getLongDescription() {
        //Gives the player the description in a sentence + direction and neighbor.
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * @return
     */
    private String getExitString() {
        // The base of our string
        StringBuilder sb = new StringBuilder();
        sb.append("Exits:");

        // Get all directions from the room and save to a list
        Set<String> keys = exits.keySet();

        // Iterate all the exits in the exit hashmap
        for (String exit : keys) {
            // Assign the exit into a string
            sb.append(" ").append(exit);
        }

        // Return the final string
        return sb.toString();
    }

    /**
     * Get the room in the direction we requests
     *
     * @param direction The direction of the other room, compared to the this
     * @return The neighbor room
     */
    public Room getExit(String direction) {
        // Get the room from the hashmap
        return exits.get(direction);
    }

    public void askForHelp(ItemType itemType) {
    }

    public Item removeItem(String itemName) {
        return null;
    }

    public void addItem(Item item) {
    }
}

