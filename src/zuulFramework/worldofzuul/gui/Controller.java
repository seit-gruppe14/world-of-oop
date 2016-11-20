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

    private void drawInitialRoom() {
        Room startRoom = game.getPlayer().getCurrentRoom();
        startRoom.addToScene(mapPane.getChildren(), new Offset());

        List<Room> drawnRooms = new ArrayList<>();
        drawnRooms.add(startRoom);

        drawRooms(drawnRooms, startRoom.getExits().values(), startRoom);
    }

    private void drawRooms(List<Room> drawnRooms, Collection<Room> entries, Room startRoom) {
        for (Room entry : entries) {
            if (drawnRooms.contains(entry)) continue;
            drawnRooms.add(entry);
            entry.addToScene(mapPane.getChildren(), startRoom.calculateOffsetToRoom(entry));
            drawRooms(drawnRooms, entry.getExits().values(), startRoom);
        }
        clock.setText(game.getTime().getNiceFormattedTime());
        money.setText(game.getPlayer().getMoney());
    }

    @FXML
    private void handleActionButtons(ActionEvent event) {
        if(event.getSource() == actionButtonPay) {
            textArea.appendText(game.pay());
	    updateWeightBar();
	    clock.setText(game.getTime().getNiceFormattedTime());
	    money.setText(game.getPlayer().getMoney());
        }
        if(event.getSource() == comboBoxAsk) {
            String itemType = comboBoxAsk.getSelectionModel().getSelectedItem().toString();
            String helpAnswer = this.game.askForHelp(itemType);
            textArea.appendText(helpAnswer);
        }
        if(event.getSource() == actionButtonHelp) {
            textArea.appendText(game.printHelp());
        }
    }
    
    @FXML
    private void handleButtonMoveEvent(ActionEvent event){
        if (event.getSource()==North) {
            textArea.appendText(game.handleRoomMovement("north"));
        } else if(event.getSource()==West) {
            textArea.appendText(game.handleRoomMovement("west"));
        } else if(event.getSource()==South) {
            textArea.appendText(game.handleRoomMovement("south"));
        }else if(event.getSource()==East){
            textArea.appendText(game.handleRoomMovement("east"));
	}
        updateRoomInventoryTabel();
	updateHealthBar();
	clock.setText(game.getTime().getNiceFormattedTime());
    }

    private void updateHealthBar() {
        this.healthBar.setProgress(((double)(this.game.getPlayer().getLife()))/100);
    }

    private void updateWeightBar() {
        this.weightBar.setProgress(this.game.getPlayer().getCarryWeight()/100);
    }
    
    private void setAskCombBox() {
        ItemTypeList = FXCollections.observableArrayList();
        for (ItemType itemType : game.getItemsTypeList()) {
            this.ItemTypeList.add(itemType);
        }
        comboBoxAsk.setItems(ItemTypeList);
    }

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
    
    private void updateRoomInventoryTabel() {
        this.roomInventory = FXCollections.observableArrayList();
        if (this.game.getPlayer().getCurrentRoom().hasItems()) {
            SalesRoom currentRoom = (SalesRoom) this.game.getPlayer().getCurrentRoom();
            for(Item item : currentRoom.getItems()) {
                roomInventory.add(item);
            }
            this.tableColumnRoomInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
            this.tableColumnRoomInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
            this.tableColumnRoomInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
        }
        this.tableViewRoomInventory.setItems(roomInventory);
    }
    
    Date roomItemLastClick;
    Item roomItemLastSelect;
    
    @FXML
    private void handleRowSelect(MouseEvent event) {
        Item selectedItem = this.tableViewRoomInventory.getSelectionModel().getSelectedItem();
        if (this.roomItemLastSelect == null) {
            this.roomItemLastClick = new Date();
            this.roomItemLastSelect = selectedItem;
        } else if (selectedItem.getName().equals(this.roomItemLastSelect.getName())) {
            Date roomItemSecondClick = new Date();
            long roomItemClickDifference = this.roomItemLastClick.getTime() - roomItemSecondClick.getTime();
            if (roomItemClickDifference < 300) {
                this.game.pickUp(selectedItem.getName());
                updatePlayerInventoryTabel();
                updateRoomInventoryTabel();
                updateWeightBar();
            } else {
                this.roomItemLastClick = roomItemSecondClick;
            }
        } else {
            this.roomItemLastClick = new Date();
            this.roomItemLastSelect = selectedItem;
        }
    }
}
