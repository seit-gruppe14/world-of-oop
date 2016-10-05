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
public class SalesRoom {
    
    private java.util.List<Item> items = new ArrayList<Item>();
    
    private String description;
    private ItemType itemType;
    
    SalesRoom(String description, ItemType itemType)
    {
        this.description = description;
        this.itemType = itemType;
        //construct a set number of items every time a new room is created, the items vary from room to room.
        for(int i=0; i<7; i++)
        {
            items.add(new Item(itemType));
        }
    }
    
}
