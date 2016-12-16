package zuulFramework.worldofzuul.entities;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import zuulFramework.worldofzuul.gui.IDrawable;
import zuulFramework.worldofzuul.gui.Offset;
import zuulFramework.worldofzuul.gui.animations.MoveTransition;
import zuulFramework.worldofzuul.rooms.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Describes an entity that can move around between rooms
 */
public abstract class MovingEntity extends Entity implements IDrawable {
    private Circle drawed;
    private List<Transition> transitions = new ArrayList<>();
    private MoveTransition moveTransition;

    /**
     * Moves the entity
     */
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

    /**
     * Sets the current room to be the targeted room
     * 
     * @param targetRoom is the targeted room the entity wishes to move to
     */
    @Override
    public void setCurrentRoom(Room targetRoom) {
        super.setCurrentRoom(targetRoom);
        updateDraw();
    }

    /**
     * Adds an entity to the scene
     * 
     * @param drawAt An ObservableList
     * @param offset An instance of Offset
     * @param color An instance of Paint
     */
    void addToScene(ObservableList<Node> drawAt, Offset offset, Paint color) {
        offset = offset.add(Offset.getRandomOffsetForRoom());
        Circle circle = new Circle(offset.X, offset.Y, 5, color);
        drawAt.add(circle);

        this.drawed = circle;
        addWaitingAnimation();
    }

    /**
     * Makes the entity move around in the room while waiting for a new user interaction
     */
    private void addWaitingAnimation() {
        Path path = new Path();
        path.getElements().add(new MoveTo(drawed.getCenterX(), drawed.getCenterY()));

        Offset o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
        path.getElements().add(new LineTo(o.X, o.Y));

        o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
        path.getElements().add(new LineTo(o.X, o.Y));

        o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
        path.getElements().add(new LineTo(o.X, o.Y));

        path.getElements().add(new LineTo(drawed.getCenterX(), drawed.getCenterY()));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(10000 + ((int) (Math.random() * 3000))));
        pathTransition.setPath(path);
        pathTransition.setNode(this.drawed);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.play();

        transitions.add(pathTransition);
    }

    /**
     * Delays the entities randomly
     * 
     * @return A random number
     */
    protected double moveStartDelay() {
        return Math.random() * 3000;
    }

    /**
     * Redraws the moved entity everytime a new event occured
     */
    @Override
    public void updateDraw() {
        if (drawed != null) {
            boolean hasMoveTransition = moveTransition != null;
            if (hasMoveTransition) {
                moveTransition.stop();
                moveTransition.setOnFinished(null);
                moveTransition = null;

            }
            List<Transition> transitionsBefore = transitions;
            transitions = new ArrayList<>();

            Offset o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
            moveTransition = new MoveTransition(drawed, o.X, o.Y);
            moveTransition.setOnFinished(event -> {
                addWaitingAnimation();
                moveTransition = null;
            });
            moveTransition.setBeforeStartCallback(() -> transitionsBefore.forEach(Animation::stop));

            double startDelay = moveTransition == null ? moveStartDelay() : 0;

            moveTransition.setDelay(Duration.millis(startDelay));
            moveTransition.play();

            transitions.add(moveTransition);

        }
    }
}
