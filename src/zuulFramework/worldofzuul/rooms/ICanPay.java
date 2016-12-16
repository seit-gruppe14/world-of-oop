package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.entities.Player;

/**
 * Implementing this interface means that it is possible to somehow pay in the implementer.
 * This is mainly thought to be used in Room subclasses.
 */
public interface ICanPay {
    /**
     * Pays something in the room
     *
     * @param player The player who buys something
     * @param game   The game instance running
     * @return A string with a message about what exactly happened
     */
    String buy(Player player, Game game);
}
