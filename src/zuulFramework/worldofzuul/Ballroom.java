package zuulFramework.worldofzuul;

/**
 * Created by Rasmus Hansen .
 */
public class Ballroom extends Room implements IHaveSpecialEvent{

    /**
     * @param description
     */
    public Ballroom(String description) {
        super(description);
    }

    /**
     * Update the time by 60 minutes, when the player enter the ballRoom
     * @param game The game that the special event should be applied to
     */
    
    @Override
    public void doSpecialEvent(Game game) {
        
        // Tell the player that he has lost time when he enter the room
        System.out.println("You have been destracted so much, that you have lost time!");
        // Update the time by 60 minutes
        game.updateTime(60);
    }
}
