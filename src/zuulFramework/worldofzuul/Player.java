/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian
 */
public class Player {

    //TODO DISCUSS COMMENTS
    /**
     * The MAX_CARRY_WEIGHT indicates the player's maximum carry weight
     */
    //TODO ADD A REAL CARRY WEIGHT LIMIT
    private static final double MAX_CARRY_WEIGHT = 100.0;
    /**
     * The items contains the player's items
     */
    protected List<Item> items = new ArrayList<Item>();
    /**
     * The currentRoom indicates the player's current room
     */
    protected Room currentRoom;
    /**
     * The life indicates the remaining life of the Player
     */
    private int life = 100;
    /**
     * The money indicates the remaining money of the Player
     */
    private int money;
    /**
     * A list of items the player has bought
     */
    private List<Item> boughtItems = new ArrayList<Item>();

    /**
     * Returns the player's carry weight.
     *
     * @return The current total weight for all the items in the players
     * inventory
     */
    public double getCarryWeight() {
        double sum = 0;
        for (Item item : items) {
            sum += item.getWeight();
        }
        return sum;
    }

    /**
     * lets the player pick up items from the room they are in currently. If the
     * item doesn't exist or isn't in the room, the method prints a message
     * telling the player that the item isn't avaiable.
     *
     * @param itemName which is a item name String
     * @return true if item exists in player's current room, false otherwise
     */
    public boolean pickUp(String itemName) {
        Item item = ((SalesRoom) currentRoom).removeItem(itemName);
        if (item == null) {
            //System.out.print("The " + itemName + " you were looking for doesn't exist");
            return false;
        }

        if(item.getWeight() + this.getCarryWeight() > MAX_CARRY_WEIGHT) {
            return false;
        }

        items.add(item);
        return true;
    }

    /**
     * The method removes an item from the players item list and adds the item
     * to the player's current room.
     *
     * @param itemName which is a String itemName.
     * @return true if itemName is in player's item list, false otherwise.
     */
    public boolean drop(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(itemName)) {
                Item item = items.remove(i);
                ((SalesRoom) currentRoom).addItem(item);
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the current room of the player based on the direction
     *
     * @param direction which is a direction String
     * @return next room if the direction exist in the current room, otherwise
     * return null
     */
    public Room goRoom(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
        }
        return nextRoom;
    }

    /**
     * Returns the life of the player. The life decides if the player has died.
     *
     * @return The player's life
     */
    public int getLife() {
        return life;
    }

    /**
     * Sets the player's life. The mutator is used for resetting the player's
     * life.
     *
     * @param life which is an int
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * Returns the player's money. The money decides if a player can afford
     * items.
     *
     * @return money which is an int representation of the player's life
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the player's money. The mutator is used for resetting player money.
     *
     * @param money which is an int representation of the player's money
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Returns the player's list of items.
     *
     * @return List of Items which is an ArrayList of the Items type
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Sets the player's items list. The mutator is used for resetting the
     * player's item list.
     *
     * @param items which is an ArrayList of items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Returns the Player's current room.
     *
     * @return currentRoom which is a Room type
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Adds a given amount of life to the player's life.
     *
     * @param life which is an int type
     */
    public void addLife(int life) {
        this.life += life;
    }

    /**
     * Removes a given amount of life from the player's life.
     *
     * @param life which is an int type
     */
    public void removeLife(int life) {
        this.life -= life;
        System.out.printf("You lost %d life\n", life);
    }

    /**
     * Adds a given amount of money to the player's money.
     *
     * @param money which is an int type
     */
    public void addMoney(int money) {
        this.money += money;
    }

    /**
     * Removes a given amount of money from the player's money.
     *
     * @param money which is an int type
     */
    public void removeMoney(int money) {
        this.money -= money;
    }

    /**
     * Moves all the items from the inventory to the list of
     * items that has been bought
     */
    public void moveItemsToBoughtItems() {
        this.boughtItems = this.items;
        this.items = new ArrayList<Item>();
    }

    /**
     * Gets a list of all the items the player has bough
     * @return
     */
    public List<Item> getBoughtItems() {
        return this.boughtItems;
    }

    /**
     * Clears all the bought items, for cases of when the player
     * loses all the items they bought
     */
    public void clearBoughtItems() {
        this.boughtItems.clear();
    }

    public boolean isPlayerDead() {
        return life <= 0;
    }
}
