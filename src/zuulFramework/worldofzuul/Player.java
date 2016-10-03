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
    private Room currentRom;

    public double getCarryWeight() {
        return 0;
    }

    public void pickUp(String itemName) {
    }

    public void drop(String itemName) {
    }

    public boolean goRoom(String direction) {
        return false;
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

    public Room getCurrentRom() {
        return currentRom;
    }

    public void setCurrentRom(Room currentRom) {
        this.currentRom = currentRom;
    }

    public void addLife(int life) {
    }

    public void removeLife(int life) {
    }
}


