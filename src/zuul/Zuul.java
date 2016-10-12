package zuul;

import zuulFramework.worldofzuul.Game;

/**
 *
 *
 */
public class Zuul {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game game = Game.getInstance();
        game.play();
    }
    
}
