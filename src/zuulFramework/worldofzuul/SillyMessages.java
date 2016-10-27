package zuulFramework.worldofzuul;

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

    /**
     * Gets a silly death message
     *
     * @return A silly death message
     */
    public static String getDeathMessage() {
        int at = (int) (Math.random() * deathMessages.length);
        return deathMessages[at];
    }
    
    private static String[] damageMessages = {
            "Another shopper pushed you out of the way! \n ",
            "Two shoppers fought over a lamp and you were hit as one of them lost their grip and fell in to you. \n",
    };
    
    public static String getDamageMessage() {
        int at = (int) (Math.random() * damageMessages.length);
        return damageMessages[at];
    }
            
}
