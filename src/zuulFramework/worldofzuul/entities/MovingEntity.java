package zuulFramework.worldofzuul.entities;

import zuulFramework.worldofzuul.rooms.Room;

import java.util.Map;
import java.util.Set;

/**
 * Describes an entity that can move around between rooms
 */
public abstract class MovingEntity extends Entity {
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
}
