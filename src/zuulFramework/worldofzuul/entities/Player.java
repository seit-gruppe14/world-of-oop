/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.gui.Offset;
import zuulFramework.worldofzuul.gui.animations.MoveTransition;
import zuulFramework.worldofzuul.helpers.SillyMessages;
import zuulFramework.worldofzuul.rooms.IHaveSpecialEvent;
import zuulFramework.worldofzuul.rooms.Room;
import zuulFramework.worldofzuul.rooms.SalesRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian
 */
public class Player extends InventoryEntity{
    /**
     * The life indicates the remaining life of the Player
     */
    private int life = 100;
    /**
     * The money indicates the remaining money of the Player
     */
    private int money = 10000;
    /**
     * An array list of items the player has bought
     */
    private List<Item> boughtItems = new ArrayList<Item>();
    private Circle drawed = null;

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

            if (nextRoom.isLocked()) {
                for (Item item : items) {
                    if(nextRoom.unlockRoom(item)) {
                        break;
                    }
                }
                if (!nextRoom.isLocked()) {
                    this.setCurrentRoom(nextRoom);
                    return nextRoom;
                } else {
                    System.out.println("The door is locked");
                    return null;
                }
            } else {
                System.out.println("The door is open");
                this.setCurrentRoom(nextRoom);
                updateDraw();
                return nextRoom;
            }

        } else {
            System.out.println("There is no door");
            return null;
        }
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
    public String getMoney() {
        return Integer.toString(money) + " SEK";
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
        System.out.printf("You lost %d life\n" + SillyMessages.getDamageMessage(), life);
    }

    /**
     * Adds a given amount of money to the player's money.
     *
     * @param money which is an int type
     */
    public void addMoney(double money) {
        this.money += money;
    }

    /**
     * Removes a given amount of money from the player's money.
     *
     * @param money which is an int type
     */
    public void removeMoney(double money) {
        this.money -= money;
    }

    /**
     * Moves all the items from the inventory to the list of
     * items that has been bought
     */
    public void moveItemsToBoughtItems() {
        this.boughtItems = this.items;
        this.items = new ArrayList<>();
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

    @Override
    public String pickUp(String itemName, Game game){
        SalesRoom sr = ((SalesRoom) currentRoom);
        Item item = sr.getItem(itemName);
        if(item instanceof IHaveSpecialEvent) {
            ((IHaveSpecialEvent)item).doSpecialEvent(game);
            sr.removeItem(itemName);

        } else {
            super.pickUp(itemName, game);
        }

        return null;
    }

    @Override
    public void addToScene(ObservableList<Node> drawAt, Offset offset) {
        offset = offset.add(Offset.getRandomOffsetForRoom());
        Circle circle = new Circle(offset.X, offset.Y, 5, Paint.valueOf("#000000"));
        drawAt.add(circle);

        this.drawed = circle;
    }

    @Override
    public void updateDraw() {
        if (drawed != null) {
            Offset o = getCurrentRoom().getLocation().add(Offset.getRandomOffsetForRoom());
            MoveTransition mt = new MoveTransition(drawed, o.X, o.Y);
            mt.play();

        }
    }
}
