/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.entities;

import java.util.Map;
import java.util.Set;
import zuulFramework.worldofzuul.ITimeEventAble;
import zuulFramework.worldofzuul.rooms.Room;

/**
 *
 * @author Caoda
 */
public class Employee extends Player implements ITimeEventAble{
    
    public Employee(Room currentRoom){
        this.currentRoom = currentRoom;
    }
    
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
                currentRoom = room.getValue();
                return;
            }
            iterator++;
        }
    }
    
    @Override
    public int getTimeBetweenEvents() {
        //We want an amount of time between monster actions (Subject to change).
        return 60;
    }
    
    @Override
    public void timeCallback(int timeAt, Player player) { 
        move();
    }
}
