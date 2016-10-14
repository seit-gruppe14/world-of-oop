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
    BED(100, "bed"), LAMP(10, "lamp"), DESK(50, "desk"), DINNERTABLE(70, "dinnertable"), DINNERCHAIR(15, "dinnerchair"),
    SHELVES(60, "shelves"), CUTLERY(0.5, "cutlery"), TOILET_PAPER(1, "toiletpaper"), COMPUTER(3, "computer"),
    BEAR(5, "bear"), SOFA(100, "sofa"), NONE(0, "none");

    private double weight;
    private String name;

    /**
     * Construct hvert item med en vægt, vægten er en parameter som kommer med når man ønsker at constructer et item.
     *
     * @param weight Vægten af et item som er af typen double.
     */
    ItemType(double weight, String name) {
        this.weight = weight;
        this.name = name;
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

    /**
     * Get a weight of a certain item. This value is the weight +- 10% of the weight itself.
     *
     * @return
     */
    public double getWeight() {
        this.weight = this.weight + this.weight * ((Math.random() -0.5) * 0.1);
        return this.weight;
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
