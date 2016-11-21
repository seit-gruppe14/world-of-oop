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
    
    // This date is needed to handle a double click event on the room items table
    Date itemLastClick;
    // This item is needded to handle a dobule click event on the room items table
    Item itemLastSelect;

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
        setPlayerInventoryTabel();
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
        this.comboBoxAsk.setItems(this.game.getItemsTypeList());
    }

    //TODO Observable list into player and room
    /**
     * Call this method when updating the player items observable list.
     */
    private void setPlayerInventoryTabel() {
        this.tableColumnPlayerInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        this.tableColumnPlayerInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
        this.tableColumnPlayerInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
        this.tableViewPlayerInventory.setItems(this.game.getPlayer().getItems());
    }
    
    /**
     * Call this method when updating the room items observable list.
     */
    private void updateRoomInventoryTabel() {
        if (this.game.getPlayer().getCurrentRoom().hasItems()) {
            SalesRoom currentRoom = (SalesRoom) this.game.getPlayer().getCurrentRoom();
            this.tableColumnRoomInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
            this.tableColumnRoomInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
            this.tableColumnRoomInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
            this.tableViewRoomInventory.setItems(currentRoom.getItems());
        } else {
            //Sets a empty observable array list to handle an none-salesroom
            this.tableViewRoomInventory.setItems(FXCollections.observableArrayList());
        }
    }
    
    /**
     * Handles the item selection on room inventory, and adds the selected item
     * to the player when double clicking.
     * @param event MouseClickEvent transformed to a double click event.
     */
    @FXML
    private void onRoomItemPickup(MouseEvent event) throws Exception {
         // Sets a current selected Item which is used for checking with last selected item.
        Item selectedItem = this.tableViewRoomInventory.getSelectionModel().getSelectedItem();
        
        if(isDoubleClick(selectedItem)) {
            this.game.pickUp(selectedItem.getName());
            updateWeightBar();
        }
    }

    @FXML
    private void onPlayerItemDrop(MouseEvent event) {
         // Sets a current selected Item which is used for checking with last selected item.
         if (!this.tableViewPlayerInventory.getSelectionModel().isEmpty()) {
        Item selectedItem = this.tableViewPlayerInventory.getSelectionModel().getSelectedItem();
        if(isDoubleClick(selectedItem)) {
            //this.game.pickUp(selectedItem.getName());
            this.game.drop(selectedItem);
            updateWeightBar();
        }
      }
    }
    
    private boolean isDoubleClick(Item selectedItem) {
        // First if statement handles an error event if no item has been selected before.
        if (this.itemLastSelect == null) {
            System.out.println("test1");
            this.itemLastClick = new Date();
            this.itemLastSelect = selectedItem;
        // Second if statement handles the event of a "double click"
        } else if (selectedItem
                .getName()
                .equalsIgnoreCase
        (this
                .itemLastSelect
                .getName())) {
            System.out.println("test2");
            // Sets a temporary click date to check wether the click was rapid.
            Date roomItemSecondClick = new Date();
            // Calculates the difference on click date to create a difference which is checkable for later use
            long roomItemClickDifference = this.itemLastClick.getTime() - roomItemSecondClick.getTime();
            // If the click difference is smaller than 300 millisecounds then
            // the item has to be added to player inventory and the tables has
            // to be updated accordingly.
            if (roomItemClickDifference < 300) {
                return true;
            } else {
                this.itemLastClick = roomItemSecondClick;
            }
        // If the click is not a double click then store the clicked item and
        // click date.
        } else {
            System.out.println("test3");
            this.itemLastClick = new Date();
            this.itemLastSelect = selectedItem;
        }
        return false;
    }

    @FXML
    private void onPayButtonClick(ActionEvent event) {
        this.textArea.appendText(this.game.pay());
        updateWeightBar();
        this.clock.setText(this.game.getTime().getNiceFormattedTime());
        this.money.setText(this.game.getPlayer().getMoney());
    }

    @FXML
    private void onAskComboBoxSelect(ActionEvent event) {
        String itemType = this.comboBoxAsk.getSelectionModel().getSelectedItem().toString();
        String helpAnswer = this.game.askForHelp(itemType);
        this.textArea.appendText(helpAnswer);
    }

    @FXML
    private void onHelpButtonClick(ActionEvent event) {
        this.textArea.appendText(this.game.printHelp());
    }
    
    
}
