package zuulFramework.worldofzuul.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import zuulFramework.worldofzuul.ITimeEventAble;
import zuulFramework.worldofzuul.gui.Offset;
import zuulFramework.worldofzuul.rooms.Room;

/**
 *
 */
public class Employee extends MovingEntity implements ITimeEventAble{

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
        super.addToScene(drawAt, offset, Paint.valueOf("#0000FF"));
    }
}
