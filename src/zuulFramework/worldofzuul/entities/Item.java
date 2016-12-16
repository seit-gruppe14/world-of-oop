/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.entities;

import zuulFramework.worldofzuul.helpers.NameGenerator;

public class Item {
    private ItemType type;
    private double weight;
    private String name;
    private int price;

    /**
     * Constructs an item with the type ItemType.
     *
     * @param type is a specified ItemType.
     */
    public Item(ItemType type) {
        this.type = type;
        this.weight = type.getWeight();
        this.name = NameGenerator.pickRandomName();
        this.price = type.getPrice();
    }
    
    /**
     * Returns the type of the item
     * 
     * @return the type
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Sets the type of an item
     * 
     * @param type is the type of the item
     */
    public void setType(ItemType type) {
        this.type = type;
    }

    /**
     * Gets the weight of the item
     * 
     * @return the weight of the item
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Changes the weight of an item
     * 
     * @param weight is new weight of an item
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Gets the name of an item
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Changes the name of an item
     * 
     * @param name is the new name of an item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of an item
     * 
     * @return the item's price
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Sets the price of an item
     * 
     * @param price is the new price of an item
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Gets a String representation of the item's name
     * 
     * @return the name of an item
     */
    public String toString() {
        return this.name;
    }
}
