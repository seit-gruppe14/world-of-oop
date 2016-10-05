/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

/**
 * Opret en enum klasse der indeholder de forskellige typer af items.
 */
public enum ItemType {
    BED(100), LAMP(10), DESK(50), DINNERTABLE(70), DINNERCHAIR(15), SHELVES(60), CUTLERY(0.5), NONE(0);

    private double weight;
    
    /**
     * Construct hvert item med en vægt, vægten er en parameter som kommer med når man ønsker at constructer et item.
     * @param weight Vægten af et item som er af typen double.
     */
    ItemType(double weight) {
        this.weight = weight;
    }
    
    /**
     * Retunere vægten af et item, denne retur værdi kan svinge med +- 5 i værdi.
     * @return vægten af et item af typen double.
     */
    public double getWeight() {
        return this.weight;
    }
}
