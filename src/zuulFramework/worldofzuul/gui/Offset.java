package zuulFramework.worldofzuul.gui;

import zuulFramework.worldofzuul.Direction;

/**
 * Created by Rasmus Hansen .
 */
public class Offset {
	public final double X;
	public final double Y;

	public Offset(double x, double y) {
		X = x;
		Y = y;
	}

	public Offset() {
		this(0, 0);
	}

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

	public static Offset getRandomOffsetForRoom() {
		return new Offset(5 + Math.random() * 90, 5 + Math.random() * 90);
	}

	public Offset add(Offset other) {
		return new Offset(other.X + X, other.Y + Y);
	}

}
