/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

/**
 * @author frede
 */
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
    Item(ItemType type) {
        this.type = type;
        this.weight = type.getWeight();
        this.name = NameGenerator.pickRandomName();
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
