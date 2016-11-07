package zuulFramework.worldofzuul.entities;

import zuulFramework.worldofzuul.ITimeEventAble;
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
}
