package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Direction;
import zuulFramework.worldofzuul.entities.Employee;
import zuulFramework.worldofzuul.entities.Entity;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.entities.Item;

import java.util.*;


public class Room {
    /**
     * Describes the current room.
     */   
    protected String description;
    protected ArrayList<ItemType> itemTypes = new ArrayList<ItemType>();

    /**
     * The entities currently in the room
     */
    // Use a set, as it can't contain duplicates, which we don't want
    protected Set<Entity> entities = new HashSet<>();

    private int id;
    private boolean isLocked;
    private String key;
    /**
     * A map of rooms used save exits.
     */
    private HashMap<String, Room> exits;

    /**
     * Construct at room with a description and a set number of items in it, each room has a number of exits.
     * whom are stored in a hashmap.
     *
     * @param description the description of the room
     */
    public Room(String description, int id) {
        // Set the description to be whatever the used said the description was.
        this.description = description;
        // Create the hashmaps to save exists.
        exits = new HashMap<String, Room>();
        
        this.id = id;
        
        
    }

    /**
     * Gets the id of the room
     *
     * @return
     */
    public int getId() {
        return id;
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
     * @return String
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Gets a longer description of the room
     * which is description + a list of exists
     *
     * @return String
     */
    public String getLongDescription() {
        //Gives the player the description in a sentence + direction and neighbor.
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * This is used in each room to print an overview of exits
     * @return String
     */
    private String getExitString() {
        // The base of our string
        StringBuilder sb = new StringBuilder();
        sb.append("Nearby rooms to the:");

        // Get all directions from the room and save to a list
        Set<String> keys = exits.keySet();

        // Iterate all the exits in the exit hashmap
        for (String exit : keys) {
            // Assign the exit into a string
            sb.append(" ").append(exit);
        }
        for (Room room : exits.values()) {
            sb.append(" ").append(room.isLocked);
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

        // Check if the item user is looking for
        // is in the room they are at
        if (this.itemTypes.contains(itemType)) {
            // Tell the user they are already in the room
            System.out.println("The items you are looking for are in this room.");
            return;
        }

        // Loop over all the nearby room
        List<Room> checkedRooms = new ArrayList<Room>();
        checkedRooms.add(this);
        String direction = findRoomWithItems(itemType, this.getExits().entrySet(), "", checkedRooms);
        // We couldn't find the itemtype anywhere
        if (direction == null) {
            System.out.println("Couldn't find any room that contains this item. ");
        } else {
            direction = shortenDirection(direction);
            System.out.printf("The item you are looking for is %s of this room. \n", direction);
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
        if (shortDirection.charAt(0) == '-') {
            shortDirection = shortDirection.substring(1);
        }

        // Finally done!
        return shortDirection;
    }

    /**
     * Recursively looks in rooms for rooms that has a certain itemtype
     *
     * @param itemType     The itemtype to look for
     * @param rooms        The rooms to check. These rooms will have their rooms checked and so on
     * @param direction    The current direction that has to be moved to get here
     * @param checkedRooms All the rooms we have crrently checked
     * @return null if a valid room could not be found. A string of directions if the room was found.
     */
    private String findRoomWithItems(ItemType itemType, Set<Map.Entry<String, Room>> rooms, String direction, List<Room> checkedRooms) {
        for (Map.Entry<String, Room> subRoomEntry : rooms) {
            String subDirection = direction + "-" + subRoomEntry.getKey();
            Room subRoom = subRoomEntry.getValue();

            if (checkedRooms.contains(subRoom)) {
                continue;
            }

            if (subRoom.itemTypes.contains(itemType)) {
                return subDirection;
            }
            checkedRooms.add(subRoom);

            String result = findRoomWithItems(itemType, subRoom.getExits().entrySet(), subDirection, checkedRooms);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Gets all the exists of a given room.
     *
     * @return
     */
    public Map<String, Room> getExits() {
        return this.exits;
    }

    /**
     * Adds an entity to the rooms list of entities
     * @param e The entity to add to the room
     */
    public void addEntity(Entity e) {
        this.entities.add(e);
    }

    /**
     * Removes an entity from the rooms entity list
     * @param e The entity to remove
     */
    public void removeEntity(Entity e) {
        this.entities.remove(e);
    }

    public boolean hasEmployee() {
        for (Entity entity : this.entities) {
            if(entity instanceof Employee) {
                return true;
            }
        }
        return false;
    }
    
    public void setLock(boolean isLocked) {
        this.isLocked = isLocked;
    }
    public void setKey(String key){
        this.key=key;
    }
    
    public boolean isLocked (){
        return this.isLocked;
    }
    
    public void unlockRoom(Item item){
        if (item.getType().toString().equalsIgnoreCase(key)) {
            System.out.println("You unlocked the room");
            isLocked=false;
        } else {
            System.out.println("You didnt unlock the room");
        }
    }
}
