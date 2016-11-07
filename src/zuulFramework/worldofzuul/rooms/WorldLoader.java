package zuulFramework.worldofzuul.rooms;

import zuulFramework.worldofzuul.Ballroom;
import zuulFramework.worldofzuul.Time;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.entities.Monster;
import zuulFramework.worldofzuul.entities.Employee;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rasmus Hansen .
 */
public class WorldLoader {
    private static ArrayList<Employee> employeeList= new ArrayList<Employee>();

    /**
     *  readWorld reads a file and creates a roomContainer for each room object in the file and returns the list of rooms.
     * @param path which itemStrings a file path.
     * @return a list of RoomContainers 
     * @throws Exception on illegal file content.  
     */
    private static List<RoomContainer> readWorld(String path) throws Exception {
        List<RoomContainer> rooms = new ArrayList<>();
        //Uses try to make sure it reads the file. 
        try (FileReader reader = new FileReader(path)) {
            ParserState ps = ParserState.AWAITING_OBJECT_TYPE;
            RoomContainer rc = new RoomContainer();

            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Avoid empty lines and additional whitespace
                line = line.trim();
                if (line.equals("")) continue;
                System.out.println(line);
                //Checks the parser state to enable state changes.
                switch (ps) {
                    case AWAITING_OBJECT_TYPE:
                        //if the read line itemStrings start of a new room object then 
                        //then set the parser state, else throws error. 
                        if (line.equals("[room]")) {
                            ps = ParserState.PARSING_ROOM;
                        } else {
                            System.out.printf("Unexpected token, %s", line);
                        }

                        break;
                    //if parser state itemStrings passing room then split the read line 
                    //and extract the attribute and value for use in the switch statement
                    //where the attributes are used to set a value for a roomContainer object. 
                    //When the object itemStrings fully defined then it's added to the list of roomContainer. 
                    //Then the parser state goes back to AWAITING_OBJECT_TYPE and a new roomContainer object itemStrings created.
                    case PARSING_ROOM:

                        String[] parts = line.split("=");
                        String attribute, value;
                        if (parts.length == 2) {
                            attribute = parts[0];
                            value = parts[1];
                        } else if (parts.length == 1) {
                            attribute = parts[0];
                            value = "";
                        } else {
                            throw new Exception("Unexpected result of split " + line);
                        }
                        //System.out.println(attribute);
                        //System.out.println(value);
                        switch (attribute) {
                            case "id":
                                rc.setId(value);
                                break;
                            case "name":
                                rc.setName(value);
                                break;
                            case "type":
                                rc.setType(value);
                                break;
                            case "description":
                                rc.setDescription(value);
                                break;
                            case "numberOfMonsters":
                                rc.setNumberOfMonsters(value);
                                break;
                            case "numberOfEmployees":
                                rc.setNumberOfEmployees(value);
                                break;
                            case "links":
                                rc.setLinks(value);
                                break;
                            case "itemTypes":
                                rc.setItemTypes(value);
                                break;
                            case "isLocked":
                                rc.setLock(value);
                            default:
                                throw new Exception("Unknown attribute " + attribute);
                        }

                        if (rc.hasAllData()) {
                            ps = ParserState.AWAITING_OBJECT_TYPE;
                            rooms.add(rc);
                            rc = new RoomContainer();
                        }

                        break;
                }
            }


        } catch (IOException e) {
            System.out.println(e);
        }

        return rooms;
    }
    /**
     * 
     * @param roomContainers which are the roomContainers created in the readWorld method.
     * @param time which itemStrings used for the time handlers when monsters are added to the rooms.
     * @return a list of rooms in the world. 
     * @throws Exception if the RoomContainer doesn't have an id attached. 
     */
    private static List<Room> rebuildWorld(List<RoomContainer> roomContainers, Time time) throws Exception {
        List<Room> rooms = new ArrayList<>();
        //Adds a room to the rooms List for each roomContainer in the roomContainers list.
        for (RoomContainer roomContainer : roomContainers) {
            Room room = roomContainer.getRoom();
            room.setLock(roomContainer.isLocked);
            rooms.add(room);
        }
        //Ensure more descriptive error instead of null pointer. 
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            RoomContainer rc = null;
            for (int j = 0; j < roomContainers.size(); j++) {
                rc = roomContainers.get(j);
                if (rc.id == room.getId()) {
                    break;
                }
            }
            if (rc == null) {
                throw new Exception("Error when searching for room id");
            }
            
            //Sets an exit for the room based on the roomContainer linkStrings. 
            for (Link link : rc.links) {
                Room otherRoom = null;

                for (Room room1 : rooms) {
                    if (link.getTargetId() == room1.getId()) {
                        otherRoom = room1;
                        break;
                    }
                }

                room.setExit(link.direction, otherRoom);
            }
            //Assigns a room to the monsters. 
            for (int j = 0; j < rc.numberOfMonsters; j++) {
                Monster m = new Monster(room);
                time.addTimeEvent(m);
            }
            for (int l = 0; l < rc.numberOfEmployees; l++) {
                Employee e = new Employee(room);
                employeeList.add(e);
                time.addTimeEvent(e);
            }
            
        }
        


        return rooms;
    }
    /**
     * 
     * @param path which itemStrings a file path. 
     * @param time game time.
     * @return
     * @throws Exception throws exception because of rebuildWorld().
     */
    public static List<Room> LoadWorld(String path, Time time) throws Exception {
        List<RoomContainer> roomContainers = readWorld(path);
        return rebuildWorld(roomContainers, time);
    }
    /**
     * The parser states used in rebuildWorld().
     */
    private enum ParserState {
        AWAITING_OBJECT_TYPE,
        PARSING_ROOM
    }
    /**
     * Temporary storage for the rooms used in the game. 
     */
    private static class RoomContainer {
        int id = -1;
        String name = null;
        String type = null;
        String description = null;
        int numberOfMonsters = -1;
        int numberOfEmployees = -1;
        Link[] links = null;
        ItemType[] itemTypes = null;
        boolean isLocked;

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = Integer.parseInt(id);
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setNumberOfMonsters(String numberOfMonsters) {
            this.numberOfMonsters = Integer.parseInt(numberOfMonsters);
        }
        public void setNumberOfEmployees(String numberOfEmployees){
            this.numberOfEmployees = Integer.parseInt(numberOfEmployees);
        }

        /**
         * Sets the links for the room.
         * @param value a string with all the links.
         */
        public void setLinks(String value) {
            //Since the value itemStrings a single string then we need to split it. 
            String[] linkStrings = value.split(",");
            //Since we know the amount of links then we can define the size of the links[].  
            this.links = new Link[linkStrings.length];
            //Since linkStrings contains an id and direction, then we need to 
            //seperate them and create a link-object with the seperated values
            //and adds the object to the links[].
            for (int i = 0; i < linkStrings.length; i++) {
                String[] parts = linkStrings[i].split(":");
                String id = parts[0];
                String direction = parts[1];
                Link l = new Link(id, direction);
                this.links[i] = l;
            }
        }
        /**
         * Sets the itemTypes for the room.
         * @param itemTypes a string of all itemTypes. 
         */
        public void setItemTypes(String itemTypes) {
            //Since itemTypes itemStrings a single String then we need to split it.  
            String[] itemStrings = itemTypes.split(",");
            //Since we know the amount of itemTypes then we can define the size of the itemTypes[].  
            this.itemTypes = new ItemType[itemStrings.length];
            //Since itemStrings contains our itemTypes then we need to add each itemString to ItemTypes[].
            for (int i = 0; i < itemStrings.length; i++) {
                String type = itemStrings[i];
                this.itemTypes[i] = ItemType.get(type);
            }
        }
        
        public void setLock(String bool) {
            this.isLocked = Boolean.getBoolean(bool);
        }
        
        /**
         * Checks if any attribute hasn't been modified.
         * @return true if any attribute still prestine, otherwise false.
         */
        public boolean hasAllData() {
            return
                    id != -1 &&
                            name != null &&
                            type != null &&
                            description != null &&
                            numberOfMonsters != -1 &&
                            numberOfEmployees != -1 &&
                            links != null &&
                            (isLocked == false || isLocked == true) &&
                            itemTypes != null;
        }

        /**
         * Get a room-object from the RoomContainer.
         * @return room-object based on RoomContainer type.
         * @throws Exception throws exception if room isn't found.
         */
        public Room getRoom() throws Exception {
            Room r;
            switch (this.type) {
                case "base":
                    r = new Room(this.description, this.id);
                    break;
                case "salesroom":
                    r = new SalesRoom(this.description, this.id, this.itemTypes);
                    break;
                case "canteen": 
                    r = new Canteen(this.description, this.id);
                    break;
                case "ballroom": 
                    r = new Ballroom(this.description, this.id);
                    break;
                case "exit":
                    r = new Exit(this.description, this.id);
                    break;
                default:
                    throw new Exception("Unknown room type " + this.type);
            }
            return r;
        } 
    }
    /**
     * The link-object stores our link values.
     */
    private static class Link {
        int targetId;
        String direction;
        /**
         * Contructs a link
         * @param targetId the link target id which is the id of the exit room
         * @param direction the link direction which is the direction of the nearby room
         */
        public Link(String targetId, String direction) {
            setTargetId(targetId);
            setDirection(direction);
        }
        /**
         * Sets the targetID
         * @param targetId the link target id which is the id of the exit room
         */
        public void setTargetId(String targetId) {
            this.targetId = Integer.parseInt(targetId);
        }
        /**
         * Sets the direction
         * @param direction the link direction which is the direction of the nearby room
         */
        public void setDirection(String direction) {
            this.direction = direction;
        }
        /**
         * Get the targetId
         * @return the link target id which is the id of the exit room
         */
        public int getTargetId() {
            return this.targetId;
        }
    }
    
    public static ArrayList<Employee> getEmployeeList(){
        return employeeList;
    }
}
