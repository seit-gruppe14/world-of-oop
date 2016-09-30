package zuulFramework.worldofzuul;

import java.util.Set;
import java.util.HashMap;
//Seems to not be in use.
import java.util.Iterator;


public class Room 
{ //Creates a variable with the type string. 
    private String description;
  //Creates a map of rooms that can be constructed later.
    private HashMap<String, Room> exits;
  
    public Room(String description) 
    {   //The variable 'description' is being named description.  
        this.description = description;
        //HashMap constructor is being called to create the exits.  
        exits = new HashMap<String, Room>();
    }

    public void setExit(String direction, Room neighbor) 
    {   //Gives the direction of the exit and with room the player exits towards.
        exits.put(direction, neighbor);
    }

    public String getShortDescription()
    {   //Gives the player a short description.
        return description;
    }

    public String getLongDescription()
    {   //Gives the player the description in a sentence + direction and neighbor.
        return "You are " + description + ".\n" + getExitString();
    }

    private String getExitString()
    {   
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
}

