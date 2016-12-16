package zuulFramework.worldofzuul.helpers;

/**
 * Contains a bunch of silly messages used around the game
 */
public class SillyMessages {
    
    private static String[] deathMessages = {
        "You have been trampled!",
        "You have been reduced to nothing!",
        "You have been crushed by the shoppers!",
        "You were swarmed by the shoppers and overheated to death!",
    };
    
    private static String[] damageMessages = {
        "Another shopper pushed you out of the way! \n ",
        "Two shoppers fought over a lamp and you were hit as one of them lost their grip and fell in to you! \n",
        "You were hit by a shopping cart! \n",
        "You were caught between two shoppers who were fighting!"
    };

    /**
     * Returns a silly death message
     * @return String silly message
     */
    public static String getDeathMessage() {
        int at = (int) (Math.random() * deathMessages.length);
        return deathMessages[at];
    }

    /**
     * Returns a silly damage message
     * @return String silly message
     */
    public static String getDamageMessage() {
        int at = (int) (Math.random() * damageMessages.length);
        return damageMessages[at];
    }

}
