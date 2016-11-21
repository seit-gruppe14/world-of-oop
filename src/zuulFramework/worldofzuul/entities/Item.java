/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.entities;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import zuulFramework.worldofzuul.helpers.NameGenerator;

/**
 * @author frede
 */
public class Item {
    private ItemType type;
    private SimpleDoubleProperty weight;
    private SimpleStringProperty name;
    private SimpleIntegerProperty price;

    /**
     * Constructs an item with the type ItemType.
     *
     * @param type is a specified ItemType.
     */
    public Item(ItemType type) {
        this.type = type;
        this.weight = new SimpleDoubleProperty(type.getWeight());
        this.name = new SimpleStringProperty(NameGenerator.pickRandomName());
        this.price = new SimpleIntegerProperty(type.getPrice());
    }

    public Item() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public double getWeight() {
        return this.weight.getValue();
    }

    public void setWeight(double weight) {
        this.weight = new SimpleDoubleProperty(weight);
    }

    public String getName() {
        return this.name.getValue();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public int getPrice() {
        return this.price.getValue();
    }

    public void setPrice(int price) {
        this.price = new SimpleIntegerProperty(price);
    }
    
    public String toString() {
        return this.name.getValue();
    }
}
