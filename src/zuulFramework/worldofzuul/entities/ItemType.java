/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.entities;

public enum ItemType {
    BED(95, "bed", 3000), DESK(50, "desk", 500), DINNERTABLE(70, "dinnertable",400), DINNERCHAIR(15, "dinnerchair",100),
    SHELVES(60, "shelves",300), CUTLERY(0.5, "cutlery", 10), TOILET(40, "toilet", 1200 ), COMPUTER(3, "computer", 2500),
    TEDDY_BEAR(5, "teddybear", 75), SOFA(95, "sofa", 1500), NONE(0, "none",0), SPECIAL(0, "special", 0);

    private double weight;
    private String name;
    private int price;

    /**
     * Sets the values of the attributes.
     * @param weight The weight of the ittem.
     * @param name The name of the item.
     * @param price The price of the item.
     */
    ItemType(double weight, String name, int price) {
        this.weight = weight;
        this.name = name;
        this.price = price;
        
    }

    /**
     * Gets an enum representation of the String
     *
     * @param s The string to parse
     * @return The enum value or NONE
     */
    public static ItemType get(String s) {
        // Iterate all the values in the itemtype
        for (ItemType itemType : ItemType.values()) {
            if (itemType.name.equalsIgnoreCase(s)) {
                return itemType;
            }
        }
        // No matching enum was found, print none
        return ItemType.NONE;
    }

    /*
     * Get a weight of a certain item. This value is the weight +- 5% of the weight itself.
     *
     * @return
     */
    public double getWeight() {
        return this.weight + this.weight * ((Math.random() - 0.5) * 0.1);
    }
    
    /**
     * Get a price of a certain item. This value is the price +- 5% of the price itself.
     *
     * @return
     */
    public int getPrice() {
        return (int) (this.price + this.price * ((Math.random() -0.5) * 0.1));
    }

    /**
     * Gets a string representation of the enum
     *
     * @return the name of the enum
     */
    @Override
    public String toString() {
        return this.name;
    }

}
