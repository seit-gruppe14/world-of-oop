/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zuulFramework.worldofzuul;

public interface IEventMessages {
    
    /**
     * This method is used to handle message strings which should be passed between
     * the Backend and the GUI.
     * The method should be defined in the GUI as an annonymous method, and linked
     * to the backend.
     * @param s String message which should be handled by the GUI
     */
    void handle(String s);

}


