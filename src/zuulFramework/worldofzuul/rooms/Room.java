package zuulFramework.worldofzuul.rooms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import zuulFramework.worldofzuul.Direction;
import zuulFramework.worldofzuul.entities.Employee;
import zuulFramework.worldofzuul.entities.Entity;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.gui.IDrawable;
import zuulFramework.worldofzuul.gui.Offset;

import java.util.*;

/**
 * A basic room
 */
public class Room implements IDrawable {
    /**
     * Describes the current room.
     */
    protected String description;
    /**
     * Describes the items types the room should contain
     */
    protected ObservableList<ItemType> itemTypes = FXCollections.observableArrayList();
    /**
     * The entities currently in the room
     */
    // Use a set, as it can't contain duplicates, which we don't want
    protected Set<Entity> entities = new HashSet<>();
    /**
     * Contains the lines that was drawn for the room
     */
    private ArrayList<Line> lines = new ArrayList<>();
    /**
     * The id of the room
     */
    private int id;
    /**
     * Indicates if the room is locked, and the player shouldn't have access to the room
     */
    private boolean isLocked;
    /**
     * The key that will unlock the room
     */
    private String key;
    /**
     * A map of rooms used save exits.
     */
    private HashMap<String, Room> exits;
    /**
     * Indicates if the room has been drawn
     */
    private boolean hasDrawn = false;
    /**
     * The offset with which the room should be drawn
     */
    private Offset location = null;

    /**
     * Construct at room with a description and a set number of items in it, each room has a number of exits.
     * whom are stored in a hashmap.
     *
     * @param description the description of the room
     * @param id The id of the room
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
     * @return The id of the room
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
     * @return A longer description as a String
     */
    public String getLongDescription() {
        //Gives the player the description in a sentence + direction and neighbor.
        return "You are " + description + ".\n";
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
     * @return A string containing a description of where to find the item
     */
    public String askForHelp(ItemType itemType) {

        // Check if the item user is looking for
        // is in the room they are at
        if (this.itemTypes.contains(itemType)) {
            // Tell the user they are already in the room
            return "The items you are looking for are in this room.\n";
        }

        // Loop over all the nearby room
        List<Room> checkedRooms = new ArrayList<Room>();
        checkedRooms.add(this);
        String direction = findRoomWithItems(itemType, this.getExits().entrySet(), "", checkedRooms);
        // We couldn't find the itemtype anywhere
        if (direction == null) {
            return "Couldn't find any room that contains this item.\n";
        } else {
            direction = shortenDirection(direction);
            return String.format("The item you are looking for is %s of this room.\n", direction);
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
            if (!Direction.isDirection(subRoomEntry.getKey())) continue;
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
     * @return A map of all the exists
     */
    public Map<String, Room> getExits() {
        return this.exits;
    }

    /**
     * Adds an entity to the rooms list of entities
     *
     * @param e The entity to add to the room
     */
    public void addEntity(Entity e) {
        this.entities.add(e);
    }

    /**
     * Removes an entity from the rooms entity list
     *
     * @param e The entity to remove
     */
    public void removeEntity(Entity e) {
        this.entities.remove(e);
    }

    /**
     * Check if there is an employee in the room
     *
     * @return true if there is more than zero employees in the room
     */
    public boolean hasEmployee() {
        for (Entity entity : this.entities) {
            if (entity instanceof Employee) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the room has items types
     *
     * @return true if there is more than zero items in the room otherwise false
     */
    public boolean hasItems() {
        if (this instanceof SalesRoom) {
            return this.itemTypes.size() > 0;
        }
        return false;
    }

    /**
     * Set the locked status
     *
     * @param isLocked boolean
     */
    public void setLock(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * Sets the room key, which is an item type in string format
     *
     * @param key String
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Check if the room is locked
     *
     * @return true if room is locked otherwise false
     */
    public boolean isLocked() {
        return this.isLocked;
    }

    /**
     * Unlock the room using a item
     *
     * @param item The item that should be used for attempting to unlock the room
     * @return true if room is successfully unlocked, otherwise false
     */
    public boolean unlockRoom(Item item) {
        if (item.getType().toString().equalsIgnoreCase(key)) {
            System.out.println("You unlocked the room");
            isLocked = false;
            for (Line line : lines) {
                line.setOpacity(0);
            }
            return true;
        } else {
            System.out.println("You didnt unlock the room");
            return false;
        }
    }

    /**
     * Draws room and adds it to the current JavaFX scene
     *
     * @param drawAt A list where the element should be put into and get drawn
     * @param offset The absolute offset at which the room should be drawn
     */
    @Override
    public void addToScene(ObservableList<Node> drawAt, Offset offset) {
        if (hasDrawn) return;
        hasDrawn = true;
        this.location = offset;
        final double doorSpace = 60;
        final double wallLength = 100;
        double topX = offset.X;
        double topY = offset.Y;

        // Draw north side
        Line l = new Line(topX, topY, topX + wallLength, topY);
        drawAt.add(l);

        // Check if we have a door to the north
        if (exits.containsKey(Direction.NORTH)) {
            // If we do, then make space for a door
            double endX = topX + (wallLength - doorSpace) / 2;
            l.setEndX(endX);
            Line l2 = new Line(topX + (wallLength - doorSpace) / 2 + doorSpace, topY, topX + wallLength, topY);
            if (isLocked) {
                Line l3 = new Line(endX, topY, endX + 60, topY);
                l3.setStroke(Paint.valueOf("FF0000"));
                drawAt.add(l3);
                lines.add(l3);
            }
            drawAt.add(l2);
        }

        // Draw east side
        l = new Line(topX + wallLength, topY, topX + wallLength, topY + wallLength);
        drawAt.add(l);

        if (exits.containsKey(Direction.EAST)) {
            double endY = topY + (wallLength - doorSpace) / 2;
            l.setEndY(endY);
            Line l2 = new Line(topX + wallLength, topY + (wallLength - doorSpace) / 2 + doorSpace, topX + wallLength, topY + wallLength);
            drawAt.add(l2);
            if (isLocked) {
                Line l3 = new Line(topX + wallLength, endY, topX + wallLength, endY + 60);
                l3.setStroke(Paint.valueOf("FF0000"));
                drawAt.add(l3);
                lines.add(l3);
            }
        }

        // Draw west side
        l = new Line(topX, topY, topX, topY + wallLength);
        drawAt.add(l);

        if (exits.containsKey(Direction.WEST)) {
            double endY = topY + (wallLength - doorSpace) / 2;
            l.setEndY(endY);
            Line l2 = new Line(topX, topY + (wallLength - doorSpace) / 2 + doorSpace, topX, topY + wallLength);
            drawAt.add(l2);
            if (isLocked) {
                Line l3 = new Line(topX, endY, topX, endY + 60);
                l3.setStroke(Paint.valueOf("FF0000"));
                drawAt.add(l3);
                lines.add(l3);
            }
        }

        // Draw south side
        l = new Line(topX, topY + wallLength, topX + wallLength, topY + wallLength);
        drawAt.add(l);

        if (exits.containsKey(Direction.SOUTH)) {
            double endX = topX + (wallLength - doorSpace) / 2;
            l.setEndX(endX);
            Line l2 = new Line(topX + (wallLength - doorSpace) / 2 + doorSpace, topY + wallLength, topX + wallLength, topY + wallLength);
            drawAt.add(l2);
            if (isLocked) {
                Line l3 = new Line(endX, topY + wallLength, endX + 60, topY + wallLength);
                l3.setStroke(Paint.valueOf("FF0000"));
                drawAt.add(l3);
                lines.add(l3);
            }
        }

        for (Entity entity : this.entities) {
            if (entity instanceof IDrawable) {
                ((IDrawable) entity).addToScene(drawAt, offset);
            }
        }
    }

    /**
     * Updates the drawning on the room. Is a noop in this class
     */
    @Override
    public void updateDraw() {

    }

    /**
     * Calculates an offset for this room, from the provided room
     *
     * @param r The room to which the offset should be calculated
     * @return An offset to the other room
     */
    public Offset calculateOffsetToRoom(Room r) {
        if (r == this) {
            return new Offset();
        }
        List<Room> searchedRooms = new ArrayList<Room>();
        searchedRooms.add(this);
        for (Map.Entry<String, Room> entry : this.getExits().entrySet()) {
            Offset o = Offset.getOffset(entry.getKey());
            if (o == null) continue;
            if (entry.getValue() == r) return o;

            o = calculateOffset(r, entry.getValue().getExits().entrySet(), o, searchedRooms);
            if (o != null) return o;
        }
        return null;
    }


    /**
     * Recursively calculates offset to a goal
     *
     * @param r             The room to calculate to
     * @param rooms         The rooms to go through in this iteration
     * @param currentOffset The current offset calculated
     * @param searchedRooms The rooms that has already be searched
     * @return The offset to the room if found, otherwise null.
     */
    private Offset calculateOffset(Room r, Set<Map.Entry<String, Room>> rooms, Offset currentOffset, List<Room> searchedRooms) {

        for (Map.Entry<String, Room> subEntry : rooms) {
            // If we checked this room, then don't check it again
            if (searchedRooms.contains(subEntry.getValue())) continue;

            Offset o = Offset.getOffset(subEntry.getKey());
            if (o == null) {
                continue;
            }
            searchedRooms.add(subEntry.getValue());

            Offset totalOffset = currentOffset.add(o);
            if (subEntry.getValue() == r) return totalOffset;
            o = calculateOffset(r, subEntry.getValue().getExits().entrySet(), totalOffset, searchedRooms);
            if (o != null) return o;
        }
        // Nothing was found
        return null;
    }

    /**
     * Get the room offset to be used in a GUI
     *
     * @return Offset
     */
    public Offset getLocation() {
        return this.location;
    }

    /**
     * Get a set of non-kardinal directions, such as for the electronics rooms.
     * @return A set of directions
     */
    public Set<String> getOtherDirections() {
        Set<String> directions = new HashSet<>();
        getExits().entrySet().forEach(stringRoomEntry -> {
            if (!Direction.isDirection(stringRoomEntry.getKey())) {
                directions.add(stringRoomEntry.getKey());
            }
        });

        return directions;
    }

}
