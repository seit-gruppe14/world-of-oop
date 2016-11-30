package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Game;

/**
 * Created by Rasmus Hansen .
 */
public class Ballroom extends Room implements IHaveSpecialEvent {

	/**
	 * @param description
	 */
	public Ballroom(String description, int id) {
		super(description, id);
	}

	/**
	 * Update the time by 60 minutes, when the player enter the ballRoom
	 *
	 * @param game The game that the special event should be applied to
	 */
	@Override
	public String doSpecialEvent(Game game) {

		// Update the time by 60 minutes
		game.updateTime(60);

		return "You have been destracted so much, that you have lost time!\n";
	}
}
