package zuulFramework.worldofzuul;

/**
 * Contains a bunch of silly messages used around the game
 */
public class SillyMessages {
    private static String[] deathMessages = {
            "You have been trampled!",
            "You have been reduced to nothing!"
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
}
