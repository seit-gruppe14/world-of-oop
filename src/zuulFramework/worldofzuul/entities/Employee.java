package zuulFramework.worldofzuul.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import zuulFramework.worldofzuul.ITimeEventAble;
import zuulFramework.worldofzuul.gui.Offset;
import zuulFramework.worldofzuul.gui.animations.MoveTransition;
import zuulFramework.worldofzuul.rooms.Room;

/**
 *
 */
public class Employee extends MovingEntity implements ITimeEventAble{

    private Circle drawed = null;

    public Employee(Room currentRoom){
        this.setCurrentRoom(currentRoom);
    }

    @Override
    public int getTimeBetweenEvents() {
        //We want an amount of time between Employee actions (Subject to change).
        return 60;
    }

    @Override
    public void timeCallback(int timeAt, Player player) {
        move();
    }

    @Override
    public void addToScene(ObservableList<Node> drawAt, Offset offset) {
        offset = offset.add(Offset.getRandomOffsetForRoom());
        Circle circle = new Circle(offset.X, offset.Y, 5, Paint.valueOf("#0000FF"));
        drawAt.add(circle);

        this.drawed = circle;
    }

    @Override
    public void updateDraw() {
        if (drawed != null) {
            Offset o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
//            drawed.setCenterX(o.X);
//            drawed.setCenterY(o.Y);
            MoveTransition mt = new MoveTransition(drawed, o.X, o.Y);
            mt.play();

        }
    }
}
