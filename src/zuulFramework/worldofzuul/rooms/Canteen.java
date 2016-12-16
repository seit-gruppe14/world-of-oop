package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.entities.Player;


public class Canteen extends Room implements ICanPay {

    /**
     * Creates a new Canteen instanse
     *
     * @param description The description of the room
     * @param id The id of the room
     */
    public Canteen(String description, int id) {
        super(description, id);
    }

    /**
     * Buys food, depends on how much life the player wants to heal himself with
     *
     * @param player the player that wants to buy food
     * @return A message about what happened
     */
    @Override
    public String buy(Player player, Game game) {

        player.removeMoney(((100 - player.getLife())));
        player.setLife(100);

        return "Your life has been restored";
    }

}
