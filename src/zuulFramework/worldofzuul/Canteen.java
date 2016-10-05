package zuulFramework.worldofzuul;

/**
 * Created by Rasmus Hansen .
 */
public class Canteen extends Room implements ICanPay {


    /**
     * @param description
     */
    public Canteen(String description) {
        super(description);
    }

    /**
     * Buys food, depends on how much life the player wants to heal himself with
     * @param player the player that wants to buy food
     * @param command the command that the player has inserted
     * @return always returns true when a player buys food
     */
    @Override
    public boolean buy(Player player, Command command) {
        
        // Use command to define how much life a player wishes for, depends on 
        // how much food they buy
        String secondWord = command.getSecondWord();
        // Convert secondWord from string to int
        int toBuyFor = Integer.parseInt(secondWord);
        // The player gets life depends on how much food he bought 
        player.addLife(toBuyFor);
        // Use setter and getter method to calculate how much money a player has 
        // used
        player.setMoney(player.getMoney()-toBuyFor);
        // return true because a player has bought food
        return true;
    }
}
