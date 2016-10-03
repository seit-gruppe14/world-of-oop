/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

/**
 *
 * @author frede
 */
public class Item {
    ItemType type;
    double weight;
    
    Item(ItemType type, double weight)
    {
        this.type=type;
        this.weight=weight;
    }
}
