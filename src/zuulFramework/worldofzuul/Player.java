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
     * Constructs the player with 100 HP.
     */
    private int life = 100;
    /**
     * Constructs the attribute money.
     */
    private int money;
    /**
     * Constructs the player item list.
     */
    private List<Item> items = new ArrayList<Item>();
    /**
     * Constructs the current room of the player.
     */
    private Room currentRoom;
    /**
     * Constructs the player's maximum carry weight.
     */
    //TODO ADD A REAL CARRY WEIGHT LIMIT
    private static final double CARRY_WEIGHT = 100.0;

    /**
     *
     * @return CARRY_WEIGHT which is a double representation of the player max
     * carry weight.
     */
    public double getCarryWeight() {
        return CARRY_WEIGHT;
    }

    /**
     * lets the player pick up items from the room they are in currently. If the
     * item doesn't exist or isn't in the room, the method prints a message
     * telling the player that the item isn't avaiable.
     *
     * @param itemName which is a item name String
     */
    public boolean pickUp(String itemName) {
        Item item = ((SalesRoom) currentRoom).removeItem(itemName);
        if (item == null) {
            //System.out.print("The " + itemName + " you were looking for doesn't exist");
            return false;
        }
        items.add(item);
        return true;
    }

    /**
     * The method removes an item from
     *
     * @param itemName
     */
    public boolean drop(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(itemName)) {
                Item item = items.remove(i);
                ((SalesRoom) currentRoom).addItem(item);
                return true;
            }
        }
        return false;
    }

    public Room goRoom(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
        }
        return nextRoom;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void addLife(int life) {
        this.life += life;
    }

    public void removeLife(int life) {
        this.life -= life;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public void removeMoney(int money) {
        this.money -= money;
    }

}
