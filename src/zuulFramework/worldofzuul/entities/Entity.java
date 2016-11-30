package zuulFramework.worldofzuul.entities;

import zuulFramework.worldofzuul.rooms.Room;

public abstract class Entity {
	protected Room currentRoom;

	protected void changeRoom(Room targetRoom) {
		// Remove the entity from one room, and add it to the other room
		if (currentRoom != null) currentRoom.removeEntity(this);
		targetRoom.addEntity(this);

		// Have the entity itself remember where it is.
		this.currentRoom = targetRoom;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room targetRoom) {
		changeRoom(targetRoom);
	}
}
