package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Game;

/**
 * Created by Rasmus Hansen .
 */
public class Ballroom extends Room implements IHaveSpecialEvent {

    /**
     * Creates a new ballroom instanse
     *
     * @param description The description of the room
     * @param id The id of the room
     */
    public Ballroom(String description, int id) {
        super(description, id);
    }

    /**
     * Update the time by 60 minutes, when the player enter the ballRoom
     *
     * @param game The game that the special event should be applied to
     * @return A message about what happened
     */
    @Override
    public String doSpecialEvent(Game game) {

        // Update the time by 60 minutes
        game.updateTime(60);

        return "You have been detracted so much, that you have lost time!\n";
    }
}
