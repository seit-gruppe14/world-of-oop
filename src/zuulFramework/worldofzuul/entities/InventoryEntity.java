package zuulFramework.worldofzuul.entities;

import zuulFramework.worldofzuul.rooms.SalesRoom;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.rooms.Room;

/**
 * Describes an entity that has inventory
 */
public abstract class InventoryEntity extends MovingEntity {
    /**
     * The array list items contains the player's items
     */
    protected ObservableList<Item> items = FXCollections.observableArrayList();;

    /**
     * The MAX_CARRY_WEIGHT indicates the player's maximum carry weight
     */
    private static final double MAX_CARRY_WEIGHT = 100.0;
    
    /**
     * Returns the player's carry weight.
     *
     * @return The current total weight for all the items in the players
     * inventory
     */
    public double getCarryWeight() {
        double sum = 0;
        for (Item item : items) {
            sum += item.getWeight();
        }
        return sum;
    }

    /**
     * The method removes an item from the players item list and adds the item
     * to the player's current room.
     *
     * @param itemName which is a String itemName.
     * @return true if itemName is in player's item list, false otherwise.
     */
    public boolean drop(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(itemName)) {
                Item item = items.remove(i);
                ((SalesRoom) currentRoom).addItem(item);
                return true;
            }
        }
        return false;
    }

    /**
     * lets the player pick up items from the room they are in currently. If the
     * item doesn't exist or isn't in the room, the method prints a message
     * telling the player that the item isn't avaiable.
     *
     * @param itemName which is a item name String
     * @param game
     * @return null if the item was picked up without issues otherwise a
     * string with an error message
     */
    public Item pickUp(String itemName, Game game) {
        Item item = ((SalesRoom) this.currentRoom).removeItem(itemName);
        if (item == null) {
            return null;
        }
        if(item.getWeight() + this.getCarryWeight() > MAX_CARRY_WEIGHT) {
            ((SalesRoom) this.currentRoom).addItem(item);
            return item;
        }
        items.add(item);
        return item;
    }


    /**
     * Returns the player's list of items.
     *
     * @return List of Items which is an ArrayList of the Items type
     */
    public ObservableList<Item> getItems() {
        return items;
    }
    
    public double getMaxCarryWeight(){
        return MAX_CARRY_WEIGHT;
    }

    /**
     * Sets the player's items list. The mutator is used for resetting the
     * player's item list.
     *
     * @param items which is an ArrayList of items
     */
    public void setItems(ObservableList<Item> items) {
        this.items = items;
    }
}
