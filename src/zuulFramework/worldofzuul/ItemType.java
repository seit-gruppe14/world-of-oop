/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

/**
 * @author frede
 */
public enum ItemType {
    BED(100), LAMP(10), DESK(50), DINNERTABLE(70), DINNERCHAIR(15), SHELVES(60), CUTLERY(0.5);

    private double weight;
    
    private ItemType(double weight) {
        this.weight = weight;
    }
    
    public double getWeight() {
        return (this.weight+(Math.random()*6));
    }
}
