package zuulFramework.worldofzuul;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rasmus Hansen .
 */
public class Monster extends Player implements ITimeEventAble {
    private static final double PICK_UP_CHANCE = 0.02;
    private static final double INFLICT_DAMAGE_CHANCE = 0.01;
    
    public Monster(Room currentRoom){
        this.currentRoom = currentRoom; 
    }
    
    /**
     * The move method moves the monsters randomly around.
     */
    public void move() {
        Set<Map.Entry<String, Room>> rooms;
        rooms = currentRoom.getExits().entrySet();
        // TODO Fix danish
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
    public String pickUp(String itemName) {
        // Make current room to salesroom to be able to pick up an item in the 
        // room by using removeItem method.
        Item item = ((SalesRoom) currentRoom).removeItem(itemName);
        // if the item doesn't exist return false.
        if (item == null) {
            return "";
        }
        // if the item exist add the item and return true.
        items.add(item);
        return null;
    }
    
    /**
     * Randomizes an amount of damage the "monsters" inflicts on the player. 
     * @return Returns a damage value between 4 and 10. 
     */
    public int inflictDamage() {
        return (int) (Math.random() * 6 + 4);
    }

    /**
     * Limits the amount of moves the "monsters" can make
     * in the span of 12 hours in the game. 
     * @return Returns the amount of in-game time between events.
     */

    @Override
    public int getTimeBetweenEvents() {
        //We want an amount of time between monster actions (Subject to change).
        return 60;
    }
    /**
     * The method defines the "monsters" actions throughout the game. 
     * @param timeAt It's the given time when the callback happens. 
     * For example after 22*60 minutes the game ends. 
     * @param player The player object, is used to get items. 
     */
    @Override
    public void timeCallback(int timeAt, Player player) { 
         
        //Check if the player is in the same room as a monster. 
        if (this.currentRoom.equals(player.getCurrentRoom())){
            //The monster has 10% chance of doing damage to the player.
            if (Math.random() < INFLICT_DAMAGE_CHANCE) {
                player.removeLife(inflictDamage());
                return;
            }  
            //The monster has 20% chance of picking up an item.
        } else if ((Math.random()) < PICK_UP_CHANCE) {
            // Checks if currentRoom has the type SalesRoom, because its not
            // possible to pickup items from the entrance and salesroom, and
            // the game would throw an error if it tried.
            if (currentRoom instanceof SalesRoom) {
                //Defines currentRoom as SalesRoom in order to remove an item
                //from the room and add it to the monster's itemList.
                List<Item> itemsFromRoom = ((SalesRoom) currentRoom).getItems();
                if (itemsFromRoom.isEmpty()) {
                    // Return if we can't remove any items from the room
                    return;
                }
                int randomIndex = (int) (Math.random() * itemsFromRoom.size());
                Item monsterItem = itemsFromRoom.remove(randomIndex);
                this.items.add(monsterItem);
                return;
            }
        }
        //If none of the above happens the monster moves to a random location
        //linked to the former currentRoom. 
        move();
    }
}