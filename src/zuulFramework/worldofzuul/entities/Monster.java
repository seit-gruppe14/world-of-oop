package zuulFramework.worldofzuul.entities;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.ITimeEventAble;
import zuulFramework.worldofzuul.gui.Offset;
import zuulFramework.worldofzuul.helpers.SillyMessages;
import zuulFramework.worldofzuul.rooms.Room;
import zuulFramework.worldofzuul.rooms.SalesRoom;

import java.util.List;


public class Monster extends InventoryEntity implements ITimeEventAble {
    private static final double PICK_UP_CHANCE = 0.02;
    private static final double INFLICT_DAMAGE_CHANCE = 0.01;

    public Monster(Room currentRoom) {
        this.setCurrentRoom(currentRoom);

    }

    /**
     * Randomizes an amount of damage the "monsters" inflicts on the player.
     *
     * @return Returns a damage value between 4 and 10.
     */
    public int inflictDamage() {
        return (int) (Math.random() * 6 + 4);
    }

    /**
     * Gets a message when damage is taken
     * 
     * @return a fine text when damage is inflicted to the player
     */
    public String damageMessages() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SillyMessages.getDamageMessage()).append(" You took ").append(inflictDamage()).append(" damage.").append("\n");
        return stringBuilder.toString();
    }

    /**
     * Limits the amount of moves the "monsters" can make
     * in the span of 12 hours in the game.
     *
     * @return Returns the amount of in-game time between events.
     */

    @Override
    public int getTimeBetweenEvents() {
        //We want an amount of time between monster actions (Subject to change).
        return 60;
    }

    /**
     * The method defines the "monsters" actions throughout the game.
     *
     * @param timeAt It's the given time when the callback happens.
     *               For example after 22*60 minutes the game ends.
     */
    @Override
    public void timeCallback(int timeAt, Game game) {

        //Check if the player is in the same room as a monster.
        if (getCurrentRoom().equals(game.getPlayer().getCurrentRoom())) {
            //The monster has 10% chance of doing damage to the player.
            if (Math.random() < INFLICT_DAMAGE_CHANCE) {
                game.getPlayer().removeLife(inflictDamage());
                game.addEventMessages(damageMessages());
                return;
            }
            //The monster has 20% chance of picking up an item.
        } else if ((Math.random()) < PICK_UP_CHANCE) {
            // Checks if currentRoom has the type SalesRoom, because its not
            // possible to pickup items from the entrance and salesroom, and
            // the game would throw an error if it tried.
            if (currentRoom instanceof SalesRoom) {
                //Defines currentRoom as SalesRoom in order to remove an item
                //from the room and add it to the monster's itemList.
                List<Item> itemsFromRoom = ((SalesRoom) currentRoom).getItems();
                if (itemsFromRoom.isEmpty()) {
                    // Return if we can't remove any items from the room
                    return;
                }
                int randomIndex = (int) (Math.random() * itemsFromRoom.size());
                Item monsterItem = itemsFromRoom.remove(randomIndex);
                this.items.add(monsterItem);
                return;
            }
        }
        //If none of the above happens the monster moves to a random location
        //linked to the former currentRoom.
        move();
    }

    /**
     * Adds a monster to the scene
     * 
     * @param drawAt An ObservableList
     * @param offset An instance of Offset
     */
    @Override
    public void addToScene(ObservableList<Node> drawAt, Offset offset) {
        super.addToScene(drawAt, offset, Paint.valueOf("#FF0000"));
    }
}
