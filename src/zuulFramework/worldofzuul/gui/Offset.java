package zuulFramework.worldofzuul.gui;

import zuulFramework.worldofzuul.Direction;

/**
 * Created by Rasmus Hansen .
 */
public class Offset {
    //X offset
    public final double X;
    //Y offset
    public final double Y;

    public Offset(double x, double y) {
        X = x;
        Y = y;
    }

    public Offset() {
        this(0, 0);
    }
    /**
     * creates a new Offset that should represent one of the four cardinal directions
     * @param direction representing one of the four cardinal directions
     * @return a new offset repsenting a cardinal direction given as koordinates
     */
    public static Offset getOffset(String direction) {
        switch (direction) {
            case Direction.NORTH:
                return new Offset(0, -100);
            case Direction.EAST:
                return new Offset(100, 0);
            case Direction.SOUTH:
                return new Offset(0, 100);
            case Direction.WEST:
                return new Offset(-100, 0);
            default:
                return null;
        }
    }

    /**
     * generates and offset with some random values between 0 and 95
     * @return a new offset with random values
     */
    public static Offset getRandomOffsetForRoom() {
        return new Offset(5 + Math.random() * 90, 5 + Math.random() * 90);
    }
    /**
     * adds two offsets together
     * @param other Offset to b added
     * @return a new offset based on two previous one added together
     */
    public Offset add(Offset other) {
        return new Offset(other.X + X, other.Y + Y);
    }

}
