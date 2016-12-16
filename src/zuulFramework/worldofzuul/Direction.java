package zuulFramework.worldofzuul;

/**
 * Defines some constants for different directions it's possible to go between rooms
 * Directions are cardinal.
 */
public final class Direction {
    /**
     * Defines a direction to the east
     */
    public final static String EAST = "east";

    /**
     * Defines a direction to the west
     */
    public final static String WEST = "west";

    /**
     * Defines a direction to the north
     */
    public final static String NORTH = "north";

    /**
     * Defines a direction to the south
     */
    public final static String SOUTH = "south";

    /**
     * Checks if the direction is a kardinal direction
     *
     * @param d The direction to check against
     * @return true if d is a cardinal direction, otherwise false
     */
    public static boolean isDirection(String d) {
        return d.equalsIgnoreCase(EAST) ||
            d.equalsIgnoreCase(WEST) ||
            d.equalsIgnoreCase(NORTH) ||
            d.equalsIgnoreCase(SOUTH);
    }
}
