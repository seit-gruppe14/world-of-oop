package zuulFramework.worldofzuul.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.rooms.Room;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.rooms.SalesRoom;

public class Controller implements Initializable {
    
    private Game game;
    private ObservableList<ItemType> ItemTypeList;
    private ObservableList<Item> playerInventory;
    private ObservableList<Item> roomInventory;
    
    // This date is needed to handle a double click event on the room items table
    Date roomItemLastClick;
    // This item is needded to handle a dobule click event on the room items table
    Item roomItemLastSelect;

    @FXML
    private ProgressBar healthBar;
    @FXML
    private ProgressBar weightBar;
    @FXML
    private TextArea textArea;
    @FXML
    private Button actionButtonPay;
    @FXML
    private Button actionButtonHelp;
    @FXML
    private Button North;
    @FXML
    private Button West;
    @FXML
    private Button South;
    @FXML
    private Button East;
    @FXML
    private ComboBox<ItemType> comboBoxAsk;
    @FXML
    private Label clock;
    @FXML
    private Label money;
    @FXML
    private Pane mapPane;
    @FXML
    private TableView<Item> tableViewPlayerInventory;
    @FXML
    private TableView<Item> tableViewRoomInventory;
    @FXML
    private TableColumn<Item, String> tableColumnPlayerInventoryName;
    @FXML
    private TableColumn<Item, Double> tableColumnPlayerInventoryWeight;
    @FXML
    private TableColumn<Item, Integer> tableColumnPlayerInventoryPrice;
    @FXML
    private TableColumn<Item, String> tableColumnRoomInventoryName;
    @FXML
    private TableColumn<Item, Double> tableColumnRoomInventoryWeight;
    @FXML
    private TableColumn<Item, Integer> tableColumnRoomInventoryPrice;

    /**
     * The controller initialization, sets the new game.
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Game game = new Game();
        this.game = game;
        
        updateHealthBar();
        updateWeightBar();
        updatePlayerInventoryTabel();
        updateRoomInventoryTabel();
        setAskCombBox();
        textArea.setText(this.game.getWelcomeMessage());
        drawInitialRoom();
    }

    // TODO finish comment
    /**
     * 
     */
    private void drawInitialRoom() {
        Room startRoom = game.getPlayer().getCurrentRoom();
        startRoom.addToScene(mapPane.getChildren(), new Offset());

        List<Room> drawnRooms = new ArrayList<>();
        drawnRooms.add(startRoom);

        drawRooms(drawnRooms, startRoom.getExits().values(), startRoom);
    }

    // TODO finish comment
    /**
     * 
     * @param drawnRooms
     * @param entries
     * @param startRoom 
     */
    private void drawRooms(List<Room> drawnRooms, Collection<Room> entries, Room startRoom) {
        for (Room entry : entries) {
            if (drawnRooms.contains(entry)) continue;
            drawnRooms.add(entry);
            entry.addToScene(this.mapPane.getChildren(), startRoom.calculateOffsetToRoom(entry));
            drawRooms(drawnRooms, entry.getExits().values(), startRoom);
        }
        this.clock.setText(this.game.getTime().getNiceFormattedTime());
        this.money.setText(this.game.getPlayer().getMoney());
    }
    
    /**
     * This method handles the action button events, the event types are ASK, PAY, HELP
     * @param event 
     */
    @FXML
    private void handleActionButtons(ActionEvent event) {
        if(event.getSource() == this.actionButtonPay) {
            this.textArea.appendText(this.game.pay());
	    updateWeightBar();
	    this.clock.setText(this.game.getTime().getNiceFormattedTime());
	    this.money.setText(this.game.getPlayer().getMoney());
        }
        if(event.getSource() == this.comboBoxAsk) {
            String itemType = this.comboBoxAsk.getSelectionModel().getSelectedItem().toString();
            String helpAnswer = this.game.askForHelp(itemType);
            this.textArea.appendText(helpAnswer);
        }
        if(event.getSource() == this.actionButtonHelp) {
            this.textArea.appendText(this.game.printHelp());
        }
    }
    
    /**
     * This method handles the move buttons, the event types are north, west, south, east.
     * @param event 
     */
    @FXML
    private void handleButtonMoveEvent(ActionEvent event){
        if (event.getSource() == North) {
            this.textArea.appendText(this.game.handleRoomMovement("north"));
        } else if(event.getSource() == West) {
            this.textArea.appendText(this.game.handleRoomMovement("west"));
        } else if(event.getSource() == South) {
            this.textArea.appendText(this.game.handleRoomMovement("south"));
        }else if(event.getSource() == East){
            this.textArea.appendText(this.game.handleRoomMovement("east"));
	}
        updateRoomInventoryTabel();
	updateHealthBar();
	this.clock.setText(this.game.getTime().getNiceFormattedTime());
    }

    /**
     * Call this method to update the health bar
     */
    private void updateHealthBar() {
        this.healthBar.setProgress(((double)(this.game.getPlayer().getLife()))/100);
    }

    /**
     * Call this method to update the weight bar
     */
    private void updateWeightBar() {
        this.weightBar.setProgress(this.game.getPlayer().getCarryWeight()/100);
    }
    
    /**
     * Sets the items of the combo box used for the ask method.
     */
    private void setAskCombBox() {
        this.ItemTypeList = FXCollections.observableArrayList();
        for (ItemType itemType : this.game.getItemsTypeList()) {
            this.ItemTypeList.add(itemType);
        }
        this.comboBoxAsk.setItems(this.ItemTypeList);
    }

    /**
     * Call this method when updating the player items observable list.
     */
    private void updatePlayerInventoryTabel() {
        this.playerInventory = FXCollections.observableArrayList();
        for(Item item : this.game.getPlayer().getItems()) {
            this.playerInventory.add(item);
        }
        this.tableColumnPlayerInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        this.tableColumnPlayerInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
        this.tableColumnPlayerInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
        
        this.tableViewPlayerInventory.setItems(this.playerInventory);
    }
    
    /**
     * Call this method when updating the room items observable list.
     */
    private void updateRoomInventoryTabel() {
        this.roomInventory = FXCollections.observableArrayList();
        if (this.game.getPlayer().getCurrentRoom().hasItems()) {
            SalesRoom currentRoom = (SalesRoom) this.game.getPlayer().getCurrentRoom();
            for(Item item : currentRoom.getItems()) {
                this.roomInventory.add(item);
            }
            this.tableColumnRoomInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
            this.tableColumnRoomInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
            this.tableColumnRoomInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
        }
        this.tableViewRoomInventory.setItems(this.roomInventory);
    }
    /**
     * Handles the item selection on room inventory, and adds the selected item
     * to the player when double clicking.
     * @param event MouseClickEvent transformed to a double click event.
     */
    @FXML
    private void handleRowSelect(MouseEvent event) {
        // Sets a current selected Item which is used for checking with last selected item.
        Item selectedItem = this.tableViewRoomInventory.getSelectionModel().getSelectedItem();
        // First if statement handles an error event if no item has been selected before.
        if (this.roomItemLastSelect == null) {
            this.roomItemLastClick = new Date();
            this.roomItemLastSelect = selectedItem;
        // Second if statement handles the event of a "double click"
        } else if (selectedItem.getName().equals(this.roomItemLastSelect.getName())) {
            // Sets a temporary click date to check wether the click was rapid.
            Date roomItemSecondClick = new Date();
            // Calculates the difference on click date to create a difference which is checkable for later use
            long roomItemClickDifference = this.roomItemLastClick.getTime() - roomItemSecondClick.getTime();
            // If the click difference is smaller than 300 millisecounds then
            // the item has to be added to player inventory and the tables has
            // to be updated accordingly.
            if (roomItemClickDifference < 300) {
                this.game.pickUp(selectedItem.getName());
                updatePlayerInventoryTabel();
                updateRoomInventoryTabel();
                updateWeightBar();
            } else {
                this.roomItemLastClick = roomItemSecondClick;
            }
        // If the click is not a double click then store the clicked item and
        // click date.
        } else {
            this.roomItemLastClick = new Date();
            this.roomItemLastSelect = selectedItem;
        }
    }
}
