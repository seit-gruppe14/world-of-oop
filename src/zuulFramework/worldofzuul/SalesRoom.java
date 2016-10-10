/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

import java.util.ArrayList;
import java.util.List;

/**
 * SalesRoom can create an instance of a room that contains a set of items in a
 * list. It is possible to add and remove items from the room.
 *
 * @author frede
 */
public class SalesRoom extends Room {

    /**
     * A list containing items for every room
     */
    private java.util.List<Item> items = new ArrayList<Item>();

    /**
     * Creates an instance of a SalesRoom with seven items of a specific
     * ItemType, which will be placed in an arraylist.
     *
     * @param description gives the room a description.
     * @param itemType specifies the ItemType in the room.
     */
    public SalesRoom(String description, ItemType... itemType) {
        super(description);
        this.itemType = itemType;
        //construct a set number of items every time a new room is created, the items vary from room to room.
        for (int i = 0; i < itemType.length; i++) {
            for (int j = 0; j < 7; i++) {
                items.add(new Item(itemType[i]));
            }
        }
    }

    /**
     * Moves an item from a player to the room.
     *
     * @param item is of type Item, and has a specific ItemType.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the room and gives it to the player.
     *
     * @param itemName is the name of the item that we wish to check for in the
     * room.
     * @return returns the item if it exists in the room, else return null.
     */
    public Item removeItem(String itemName) {
        //loops the arraylist to find the item the player specified.
        for (int itemNumber = 0; itemNumber < items.size(); itemNumber++) {
            if (items.get(itemNumber).getName().equals(itemName)) {
                return items.remove(itemNumber);
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return items;
    }

}
