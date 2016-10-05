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
    private int life = 100;
    private int money;
    private List<Item> items = new ArrayList<Item>();
    private Room currentRoom;

    public double getCarryWeight() {
        return 0;
    }

    public void pickUp(String itemName) {
        Item item = currentRoom.removeItem(itemName);
        if (item == null) {
            System.out.print("The " + itemName + " you were looking for doesn't exist");
        } else {
            items.add(item);
        }
    }

    public void drop(String itemName) {
        for (int i = 0; i < items.size(); i++){
            if (items.get(i).getName().equals(itemName)){
                Item item = items.remove(i);
                currentRoom.addItem(item);
                return;
            }
        }
        // TODO throw message on item not found
    }

    public Room goRoom(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null){
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
    
    public Room getCurrentRoom () {
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

