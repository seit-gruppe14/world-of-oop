/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Game;

/**
 * Implement by classes that needs to do special things
 */
public interface IHaveSpecialEvent {

    /**
     * Called when the class should do it's special event
     *
     * @param game The game instance the event was triggered on
     * @return A String descripting what happened
     */
    String doSpecialEvent(Game game);

}
