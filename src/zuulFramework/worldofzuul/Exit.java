package zuulFramework.worldofzuul;

import java.util.List;

/**
 * Created by Rasmus Hansen .
 */
public class Exit extends Room implements ICanPay {

    public Exit(String description) {
        super(description);
    }

    /**
     * Get all the items from the player and calculate the price. The time updates by 45 minutes.
     * @param player the player that wants to buy the items
     * @param command 
     * @param game the current game
     * @return true (can always pay)
     */
    
    @Override
    public boolean buy(Player player, Command command, Game game) {
       
       // Get all the item the player has picked up
       List<Item> itemsToBuy = player.getItems();
       int sum = 0;
       // Use a for loop to go through all of the items and get total items price
       for (int i = 0; i < itemsToBuy.size(); i++) {
            Item item = itemsToBuy.get(i);
            sum += item.getPrice();      
        }
       
       // Get the money from the player for the items
       player.setMoney(player.getMoney()-sum);
       // Update the time by 45 minutes 
       game.updateTime(45);
       // The player has bought all of the items 
        return true;
    }


}