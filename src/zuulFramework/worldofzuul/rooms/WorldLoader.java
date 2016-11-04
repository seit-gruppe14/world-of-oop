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
     * @param path which is a file path.
     * @return a list of RoomContainers 
     * @throws Exception on illegal file content.  
     */
    private static List<RoomContainer> readWorld(String path) throws Exception {
        List<RoomContainer> rooms = new ArrayList<>();
        //Uses try to make sure it reads the file. 
        try (FileReader reader = new FileReader(path)) {
            ParserState ps = ParserState.AwaitingObjectType;
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
                    case AwaitingObjectType:
                        //if the read line is start of a new room object then 
                        //then set the parser state, else throws error. 
                        if (line.equals("[room]")) {
                            ps = ParserState.ParsingRoom;
                        } else {
                            System.out.printf("Unexpected token, %s", line);
                        }

                        break;
                    //if parser state is passing room then split the read line 
                    //and extract the attribute and value for use in the switch statement
                    //where the attributes are used to set a value for a roomContainer object. 
                    //When the object is fully defined then it's added to the list of roomContainer. 
                    //Then the parser state goes back to AwaitingObjectType and a new roomContainer object is created.
                    case ParsingRoom:

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
                            default:
                                throw new Exception("Unknown attribute " + attribute);
                        }

                        if (rc.hasAllData()) {
                            ps = ParserState.AwaitingObjectType;
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
     * @param time which is used for the time handlers when monsters are added to the rooms.
     * @return a list of rooms in the world. 
     * @throws Exception if the RoomContainer doesn't have an id attached. 
     */
    private static List<Room> rebuildWorld(List<RoomContainer> roomContainers, Time time) throws Exception {
        List<Room> rooms = new ArrayList<>();
        for (RoomContainer roomContainer : roomContainers) {
            Room room = roomContainer.getRoom();
            rooms.add(room);
        }

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

            for (Link link : rc.links) {
                Room otherRoom = null;

                for (Room room1 : rooms) {
                    if (link.targetId == room1.getId()) {
                        otherRoom = room1;
                        break;
                    }
                }

                room.setExit(link.direction, otherRoom);
            }

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

    public static List<Room> LoadWorld(String path, Time time) throws Exception {
        List<RoomContainer> roomContainers = readWorld(path);
        return rebuildWorld(roomContainers, time);
    }

    private enum ParserState {
        AwaitingObjectType,
        ParsingRoom
    }

    private static class RoomContainer {
        int id = -1;
        String name = null;
        String type = null;
        String description = null;
        int numberOfMonsters = -1;
        int numberOfEmployees = -1;
        Link[] links = null;
        ItemType[] itemTypes = null;

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

        public void setLinks(String value) {
            String[] links = value.split(",");
            this.links = new Link[links.length];
            for (int i = 0; i < links.length; i++) {
                String[] parts = links[i].split(":");
                String id = parts[0];
                String direction = parts[1];
                Link l = new Link(id, direction);
                this.links[i] = l;
            }

        }

        public void setItemTypes(String itemTypes) {
            String[] is = itemTypes.split(",");
            this.itemTypes = new ItemType[is.length];
            for (int i = 0; i < is.length; i++) {
                String type = is[i];
                this.itemTypes[i] = ItemType.get(type);
            }
        }

        public boolean hasAllData() {
            return
                    id != -1 &&
                            name != null &&
                            type != null &&
                            description != null &&
                            numberOfMonsters != -1 &&
                            links != null &&
                            itemTypes != null;

        }

        public Room getBase() {
            return new Room(description, id);
        }

        public SalesRoom getSalesRoom() {
            return new SalesRoom(description, id, itemTypes);
        }

        public Canteen getCanteen() {
            return new Canteen(description, id);
        }

        public Ballroom getBallroom() {
            return new Ballroom(description, id);
        }

        public Room getRoom() throws Exception {
            Room r;
            switch (type) {
                case "base":
                    r = getBase();
                    break;
                case "salesroom":
                    r = getSalesRoom();
                    break;
                case "canteen":
                    r = getCanteen();
                    break;
                case "ballroom":
                    r = getBallroom();
                    break;
                case "exit":
                    r = getExit();
                    break;
                default:
                    throw new Exception("Unknown room type " + type);
            }
            return r;
        }

        public Exit getExit() {
            return new Exit(description, id);
        }
    }

    private static class Link {
        int targetId;
        String direction;

        public Link(String targetId, String direction) {
            setTargetId(targetId);
            setDirection(direction);
        }

        public void setTargetId(String targetId) {
            this.targetId = Integer.parseInt(targetId);
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
    
    public static ArrayList<Employee> getEmployeeList(){
        return employeeList;
    }
}
