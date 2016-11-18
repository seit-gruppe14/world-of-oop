package zuulFramework.worldofzuul.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import zuulFramework.worldofzuul.gui.IDrawable;
import zuulFramework.worldofzuul.gui.Offset;
import zuulFramework.worldofzuul.gui.animations.MoveTransition;
import zuulFramework.worldofzuul.rooms.Room;

import java.util.Map;
import java.util.Set;

/**
 * Describes an entity that can move around between rooms
 */
public abstract class MovingEntity extends Entity implements IDrawable {
    private Circle drawed;

    public void move() {
        Set<Map.Entry<String, Room>> rooms;
        rooms = currentRoom.getExits().entrySet();
        // We are using the amount of rooms in the array rooms to random
        // generate a number which we can use to select a random room.
        int randomNumber = (int) (Math.random() * rooms.size());
        int iterator = 0;
        // Get rooms randomly
        for (Map.Entry<String, Room> room : rooms) {
            // if the iterator is a random number, set the currentRoom
            if (iterator == randomNumber) {
                this.setCurrentRoom(room.getValue());
                return;
            }
            iterator++;
        }
    }

    @Override
    public void setCurrentRoom(Room targetRoom) {
        super.setCurrentRoom(targetRoom);
        updateDraw();
    }

    public void addToScene(ObservableList<Node> drawAt, Offset offset, Paint color) {
        offset = offset.add(Offset.getRandomOffsetForRoom());
        Circle circle = new Circle(offset.X, offset.Y, 5, color);
        drawAt.add(circle);

        this.drawed = circle;
    }

    @Override
    public void updateDraw() {
        if (drawed != null) {
            Offset o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
            MoveTransition mt = new MoveTransition(drawed, o.X, o.Y);
            mt.play();
        }
    }
}
