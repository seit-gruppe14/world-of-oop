package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.commands.Command;
import zuulFramework.worldofzuul.entities.Player;

/**
 * Created by Rasmus Hansen .
 */
public class Canteen extends Room implements ICanPay {

    /**
     * @param description
     */
    public Canteen(String description, int id) {
        super(description, id);
    }

    /**
     * Buys food, depends on how much life the player wants to heal himself with
     *
     * @param player the player that wants to buy food
     * @param command the command that the player has inserted
     * @return always returns true when a player buys food
     */
    @Override
    public String buy(Player player, Game game) {
        return "/n";
        /*
        if (command.hasSecondWord()) {
            // Use command to define how much life a player wishes for, depends on 
            // how much food they buy

            String secondWord = command.getSecondWord();
            try {
                // Convert secondWord from string to int
                int toBuyFor = Integer.parseInt(secondWord);
                // The player gets life depends on how much food he bought 
                player.addLife(toBuyFor);
                // Use setter and getter method to calculate how much money a player has 
                // used
                player.removeMoney(toBuyFor);

                System.out.println("You bought food for " + toBuyFor + " $ ");

                // return true because a player has bought food
                return true;
            } catch(NumberFormatException e) {
                System.out.println("You need to enter a number");
            }

        } else {
            System.out.println("You must specify how much you would by for");
        }
        return false;
        */
    }

}
