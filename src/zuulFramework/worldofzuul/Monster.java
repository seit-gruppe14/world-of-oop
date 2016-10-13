package zuulFramework.worldofzuul;

import java.util.Map;
import java.util.Set;

/**
 * Created by Rasmus Hansen .
 */
public class Monster extends Player implements ITimeEventAble {
    
    /**
     * The move method moves the monsters randomly around.
     */
    public void move() {
        Set<Map.Entry<String, Room>> rooms;
        rooms = currentRoom.getExits().entrySet();
        // Beacuse Math.random() returnere lig eller større end 0 men mindre
        // end 1 så kan vi bruge dette til at vælge et tilfældigt rum.
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

    
    
    /**
     * A pickUp method for picking up an item in a room and add the item if the
     * item exist
     *
     * @param itemName items that exist in the room
     * @return true if the item exist otherwise false
     */
    @Override
    public boolean pickUp(String itemName) {
        // Make current room to salesroom to be able to pick up an item in the 
        // room by using removeItem method.
        Item item = ((SalesRoom) currentRoom).removeItem(itemName);
        // if the item doesn't exist return false.
        if (item == null) {
            return false;
        }
        // if the item exist add the item and return true.
        items.add(item);
        return true;
    }
    
        /**
     *
     * @return Returns a damage value between 4 and 10. 
     */
    public int inflictDamage() {
        return (int) (Math.random() * 6 + 4);
    }

    /**
     *
     * @return 
     */

    @Override
    public int getTimeBetweenEvents() {
        //We want an amount of time between monster actions (Subject to change).
        return 60;
    }

    @Override
    public void timeCallback(int timeAt, Player player) {
        if (this.currentRoom.equals(player.getCurrentRoom())){
            if ( Math.random() * 100 < 33.33) {
                player.removeLife(inflictDamage());
                return;
            }  
        } else if ( Math.random() * 100 < 50.00) {
            Item monsterItem = ((SalesRoom) currentRoom).removeRandomItem();
            this.items.add(monsterItem);
            return;
        }
        move();
    }
}
