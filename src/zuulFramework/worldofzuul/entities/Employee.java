package zuulFramework.worldofzuul.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.ITimeEventAble;
import zuulFramework.worldofzuul.gui.Offset;
import zuulFramework.worldofzuul.rooms.Room;


public class Employee extends MovingEntity implements ITimeEventAble {
    /**
     * Constructs an Employee with a currentRoom
     * 
     * @param currentRoom is the current room of the employee
     */
    public Employee(Room currentRoom) {
        this.setCurrentRoom(currentRoom);
    }

    /**
     * Should return the wanted in game time between callbacks
     * 
     * @return The wanted in game time between Employee actions
     */
    @Override
    public int getTimeBetweenEvents() {
        //We want an amount of time between Employee actions (Subject to change).
        return 60;
    }

    /**
     * This method will be called whenever the time has been changed since last event happened.
     * 
     * @param timeAt The time of the callback
     * @param game An instance of game
     */
    @Override
    public void timeCallback(int timeAt, Game game) {
        move();
    }
    
    /**
     * Adds an Employee to the scene
     * 
     * @param drawAt An ObserableList
     * @param offset An instance of Offset
     */
    @Override
    public void addToScene(ObservableList<Node> drawAt, Offset offset) {
        super.addToScene(drawAt, offset, Paint.valueOf("#0000FF"));
    }
}
