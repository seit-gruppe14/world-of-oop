package zuulFramework.worldofzuul;

import java.util.*;


public class Room {
    protected String description;
    /**
     * Describes the current room.
     */
    private ItemType itemType = ItemType.NONE;
    /**
     * A map of rooms used save exits.
     */
    private HashMap<String, Room> exits;

    /**
     * Construct at room with a description and a set number of items in it, each room has a number of exits.
     * whom are stored in a hashmap.
     * @param description the description of the room
     * @param ItemType which itemtype the specific room contains.
     */
    public Room(String description) {
        // Set the description to be whatever the used said the description was.
        this.description = description;
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

    /**
     * Searches for a room with the items the user is asking for.
     *
     * @param itemType The itemtype to search for
     */
    public void askForHelp(ItemType itemType) {

        // Check if the user is already in the room they are looking at
        if (this.itemType == itemType) {
            // Tell the user they are already in the room
            System.out.println("The items you are looking for are in this room.");
            return;
        }

        // Loop over all the nearby room
        List<Room> checkedRooms = new ArrayList<Room>();
        String direction = findRoomWithItems(itemType, this.getExists().entrySet(), "", checkedRooms);
        // We couldn't find the itemtype anywhere
        if (direction == null) {
            System.out.println("Couldn't find any room that contains this item. ");
        } else {
            direction = shortenDirection(direction);
            System.out.printf("The item you are looking for is %s of this room. ", direction);
        }
    }

    /**
     * Normalizes a direction string to be more useable, and less rediculous.
     *
     * @param longDirection the long direction string
     * @return a nice direction string
     */
    private String shortenDirection(String longDirection) {
        // Count all the directions in the direction string
        int north = 0, east = 0;
        String[] directions = longDirection.split("-");
        for (String direction : directions) {
            switch (direction) {
                case Direction.NORTH: {
                    north++;
                    break;
                }
                case Direction.SOUTH: {
                    north--;
                    break;
                }
                case Direction.EAST: {
                    east++;
                    break;
                }
                case Direction.WEST: {
                    east--;
                    break;
                }
            }
        }
        // Make sure we don't get some completely stupid directions
        if (north > 2) north = 2;
        if (north < -2) north = -2;
        if (east > 2) east = 2;
        if (east < -2) east = -2;

        String shortDirection = "";
        for (int i = 0; i < north; i++) {
            shortDirection = String.join("-", shortDirection, Direction.NORTH);
        }
        for (int i = 0; i > north; i--) {
            shortDirection = String.join("-", shortDirection, Direction.SOUTH);
        }
        for (int i = 0; i < east; i++) {
            shortDirection = String.join("-", shortDirection, Direction.EAST);
        }
        for (int i = 0; i > east; i--) {
            shortDirection = String.join("-", shortDirection, Direction.WEST);
        }
        // Finally done!
        return shortDirection;
    }

    private String findRoomWithItems(ItemType itemType, Set<Map.Entry<String, Room>> rooms, String direction, List<Room> checkedRooms) {
        for (Map.Entry<String, Room> subRoomEntry : rooms) {
            String subDirection = direction + "-" + subRoomEntry.getKey();
            Room subRoom = subRoomEntry.getValue();

            if (checkedRooms.contains(subRoom)) {
                continue;
            }

            if (subRoom.itemType == itemType) {
                return subDirection;
            }
            checkedRooms.add(subRoom);
            findRoomWithItems(itemType, subRoom.getExists().entrySet(), subDirection, checkedRooms);
        }
        return null;
    }


    private Map<String, Room> getExists() {
        return this.exits;
    }


}

