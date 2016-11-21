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
import java.util.stream.Collectors;

/**
 * Describes an entity that can move around between rooms
 */
public abstract class MovingEntity extends Entity implements IDrawable {
    private Circle drawed;
    private List<Transition> transitions = new ArrayList<>();

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

    void addToScene(ObservableList<Node> drawAt, Offset offset, Paint color) {
        offset = offset.add(Offset.getRandomOffsetForRoom());
        Circle circle = new Circle(offset.X, offset.Y, 5, color);
        drawAt.add(circle);

        this.drawed = circle;
        addWaitingAnimation();
    }

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

    protected double moveStartDelay() {
        return Math.random() * 3000;
    }

    @Override
    public void updateDraw() {
        if (drawed != null) {
            List<Transition> transitionsBefore = transitions.stream().collect(Collectors.toList());
            transitions.clear();

            Offset o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
            MoveTransition mt = new MoveTransition(drawed, o.X, o.Y);
            mt.setOnFinished(event -> {
                addWaitingAnimation();
            });

            PauseTransition pt = new PauseTransition(Duration.millis(moveStartDelay()));
            pt.setOnFinished(event -> {
                transitionsBefore.forEach(Animation::pause);
                mt.beforeStart();
            });

            Transition fullTransition = new SequentialTransition(pt, mt);
            fullTransition.play();

            transitions.add(fullTransition);
        }
    }
}
