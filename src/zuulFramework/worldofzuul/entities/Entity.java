package zuulFramework.worldofzuul.entities;

import zuulFramework.worldofzuul.rooms.Room;

public abstract class Entity {
    protected Room currentRoom;
    
    /**
     * Makes the entity able to change room
     * 
     * @param targetRoom Is the targeted room the entity wish to move to
     */
    protected void changeRoom(Room targetRoom) {
        // Remove the entity from one room, and add it to the other room
        if (currentRoom != null) currentRoom.removeEntity(this);
        targetRoom.addEntity(this);

        // Have the entity itself remember where it is.
        this.currentRoom = targetRoom;
    }

    /**
     * Gets the current room of the entity
     * 
     * @return the currentRoom
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Changes the current room of the entity
     * 
     * @param targetRoom Is the targeted room the entity wish to move to
     */
    public void setCurrentRoom(Room targetRoom) {
        changeRoom(targetRoom);
    }
}
