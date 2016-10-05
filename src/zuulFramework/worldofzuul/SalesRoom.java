/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

import java.util.ArrayList;

/**
 *
 * @author frede
 */
public class SalesRoom extends Room {

    /**
     * A list containing items for every room
     */
    private java.util.List<Item> items = new ArrayList<Item>();

    private ItemType itemType;

    public SalesRoom(String description,ItemType itemType) {
        this.description = description;
        this.itemType = itemType;
        //construct a set number of items every time a new room is created, the items vary from room to room.
        for (int i = 0; i < 7; i++) {
            items.add(new Item(itemType));
        }
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item removeItem(String itemName) {
        for (int itemNumber = 0; itemNumber < items.size(); itemNumber++) {
            if (items.get(itemNumber).getName().equals(itemName)) {
                return items.remove(itemNumber);
            }
        }
        return null;
    }

}
