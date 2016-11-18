package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.commands.Command;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.Player;

import java.util.List;

/**
 * Created by Rasmus Hansen .
 */
public class Exit extends Room implements ICanPay {

    public Exit(String description, int id) {
        super(description, id);
    }

    /**
     * Get all the items from the player and calculate the price. The time updates by 45 minutes.
     *
     * @param player  the player that wants to buy the items
     * @param game    the current game
     * @return true (can always pay)
     */

    @Override
    public String buy(Player player, Game game) {

        // Get all the item the player has picked up
        List<Item> itemsToBuy = player.getItems();
        int sum = 0;
        // Use a for loop to go through all of the items and get total items price
        for (Item item : itemsToBuy) {
            sum += item.getPrice();
        }

        // Get the money from the player for the items
        player.removeMoney(sum);
        // Update the time by 45 minutes
        game.updateTime(45);

        // Move the items to a separate referable list of items
        player.moveItemsToBoughtItems();

        return "You spend $" + sum + " and 45 minutes";
    }


}
