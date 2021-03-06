package zuulFramework.worldofzuul.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.HighScore;
import zuulFramework.worldofzuul.entities.Item;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.helpers.SillyMessages;
import zuulFramework.worldofzuul.rooms.Exit;
import zuulFramework.worldofzuul.rooms.Room;
import zuulFramework.worldofzuul.rooms.SalesRoom;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Controller implements Initializable {


    public ListView<String> mapsList;
    public BorderPane startPane;
    public SplitPane gamePane;
    public BorderPane quitPane;
    // This date is needed to handle a double click event on the room items table
    Date itemLastClick;
    // This item is needded to handle a dobule click event on the room items table
    Item itemLastSelect;
    private Game game;

    @FXML
    private ListView<String> highScoreList;
    @FXML
    private ListView<String> quitHighScoreList;
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
    @FXML
    private Button actionButtonQuit;
    @FXML
    private Label quitText;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label gameOverMessage;

    @FXML
    private ComboBox<String> otherDirectionsDropdown;

    private ObservableList<String> otherDirections;
    @FXML
    private Label labelInventory;
    @FXML
    private TextField nameField;
    @FXML
    private Button saveButton;
    @FXML
    private Label saveMessageLabel;
    @FXML
    private Button playAgainButton;

    /**
     * The controller initialization, sets the new game.
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		otherDirections = FXCollections.observableArrayList();
		otherDirectionsDropdown.setItems(otherDirections);
		highScoreList.itemsProperty().set(HighScore.showScore());
		
		// Display possible maps that can be played
		try (Stream<Path> paths = Files.walk(Paths.get(""))) {
			ObservableList<String> mapFiles = FXCollections.observableArrayList();


            paths
                // Filter for files only, remove all folders
                .filter(Files::isRegularFile)
                // Make the Path object into a string
                .map(Path::toString)
                // Only take .wop files
                .filter(path -> path.endsWith(".wop"))
                // Only take the nice file name
                .map(path -> path.substring(0, path.lastIndexOf(".")))
                // Add each path to the collection of files
                .forEach(mapFiles::add);

            // Actually display the items
            mapsList.setItems(mapFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Starts a new game with the specified map
     *
     * @param mapLocation The path to the map file for the game
     */
    private void initializeGame(String mapLocation) {
        this.game = new Game(mapLocation);

        clock.setText(game.getTime().getNiceFormattedTime());
        updateHealthBar();
        updateWeightBar();
        setPlayerInventoryTabel();
        updateRoomInventoryTabel();
        setAskCombBox();

        drawInitialRoom();

        this.game.addMessageListener(message -> {
            this.textArea.appendText(message);
        });
        this.game.getTime().addListener((observable, oldValue, newValue) -> {
            this.clock.setText(newValue);
        });
    }


    // TODO finish comment

    /**
     * Draws the initial room, and creates the variables needed for the drawRoom method.
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
     * iterates through all the rooms in a collection and, calls a new method to draw them, before it calls itself again
     * It will add the room currently being drawn to a list of drawnrooms
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
        this.money.setText(this.game.getPlayer().getMoney());
    }

    /**
     * This method handles the move buttons, the event types are north, west, south, east.
     * roomInventory(), healthbar() and playerInventoryLabel() will also be updated
     * Ends the game if time exceeds maxTime
     * 
     * @param event
     */
    @FXML
    private void handleButtonMoveEvent(ActionEvent event) {
        if (event.getSource() == North) {
            changeRoom("north");
        } else if (event.getSource() == West) {
            changeRoom("west");
        } else if (event.getSource() == South) {
            changeRoom("south");
        } else if (event.getSource() == East) {
            changeRoom("east");
        }
        if (this.game.getTime().getCurrentTime() >= this.game.getGameEndTime()) {
            gameOver();
        }
        if (this.game.getPlayer().isPlayerDead()) {
            gameOver();
        }


        updateRoomInventoryTabel();
        updateHealthBar();
        setPlayerInventoryTabel();
    }

    /**
     * 
     */
    private void setOtherDirectionsValues() {
        // Due to this method being called from a selected callback, it needs to defer the actual thing
        // it needs to do, to avoid causing a crash with modifying the observable (otherDirections) at
        // the same time as the selection callback is going on.
        Platform.runLater(() -> {
            Set<String> directions = game.getPlayer().getCurrentRoom().getOtherDirections();

            otherDirections.clear();
            otherDirections.addAll(directions);

            otherDirectionsDropdown.setVisible(otherDirections.size() > 0);
        });
    }

    /**
     * Updates the health bar
     */
    private void updateHealthBar() {
        this.healthBar.setProgress(((double) (this.game.getPlayer().getLife())) / 100);
    }

    /**
     * Updates the weight bar
     */
    private void updateWeightBar() {
        this.weightBar.setProgress(this.game.getPlayer().getCarryWeight() / 100);
    }

    /**
     * Sets the items of the combo box used for the ask method.
     */
    private void setAskCombBox() {
        this.comboBoxAsk.setItems(this.game.getItemsTypeList());
    }

    
    /**
     * Updates the player items observable list.
     */
    private void setPlayerInventoryTabel() {
        this.tableColumnPlayerInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        this.tableColumnPlayerInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
        this.tableColumnPlayerInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
        this.tableViewPlayerInventory.setItems(this.game.getPlayer().getItems());
    }

    /**
     * Updates the room items observable list.
     */
    private void updateRoomInventoryTabel() {
        if (this.game.getPlayer().getCurrentRoom() instanceof Exit) {
            labelInventory.setText("Bought items inventory");
            this.tableColumnRoomInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
            this.tableColumnRoomInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
            this.tableColumnRoomInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
            this.tableViewRoomInventory.setItems(this.game.getPlayer().getBoughtItems());
        } else if (this.game.getPlayer().getCurrentRoom().hasItems()) {
            labelInventory.setText("Room inventory");
            SalesRoom currentRoom = (SalesRoom) this.game.getPlayer().getCurrentRoom();
            this.tableColumnRoomInventoryName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
            this.tableColumnRoomInventoryWeight.setCellValueFactory(new PropertyValueFactory<Item, Double>("weight"));
            this.tableColumnRoomInventoryPrice.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
            this.tableViewRoomInventory.setItems(currentRoom.getItems());
        } else {
            //Sets a empty observable array list to handle a none-salesroom
            this.tableViewRoomInventory.setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * Handles item selection on room inventory, and adds the selected item
     * to the player when double clicking.
     * 
     * @param event MouseClickEvent transformed to a double click event.
     */
    @FXML
    private void onRoomItemPickup(MouseEvent event) throws Exception {
        // Sets a current selected Item which is used for checking with last selected item.
        Item selectedItem = this.tableViewRoomInventory.getSelectionModel().getSelectedItem();

        if (isDoubleClick(selectedItem) && !this.game.getPlayer().getBoughtItems().contains(selectedItem)) {
            this.game.pickUp(selectedItem.getName());
            updateWeightBar();
        }
    }
    /**
     * Handles item selevtion on player inventory, and adds the selected item to the room inventory when double clicked
     * 
     * @param event MouseClickEvent transformed to a double click event
     */
    @FXML
    private void onPlayerItemDrop(MouseEvent event) {
        // Sets a current selected Item which is used for checking with last selected item.
        if (!this.tableViewPlayerInventory.getSelectionModel().isEmpty()) {
            Item selectedItem = this.tableViewPlayerInventory.getSelectionModel().getSelectedItem();
            if (isDoubleClick(selectedItem)) {
                //this.game.pickUp(selectedItem.getName());
                this.game.drop(selectedItem);
                updateWeightBar();
            }
        }
    }
    
    /**
     * Call this method if you want to handle a doubleclick event. 
     * looks if the same 'event' has been clicked twice within a specified amount of time
     * @param selectedItem
     * @return true if the same event was clicked twice within a specified amount of time, false if otherwise. 
     */

    private boolean isDoubleClick(Item selectedItem) {
        // First if statement handles an error event if no item has been selected before.
        if (this.itemLastSelect == null) {
            this.itemLastClick = new Date();
            this.itemLastSelect = selectedItem;
            // Second if statement handles the event of a "double click"
        } else if (selectedItem.getName().equalsIgnoreCase(this.itemLastSelect.getName())) {
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
            this.itemLastClick = new Date();
            this.itemLastSelect = selectedItem;
        }
        return false;
    }
    
    /**
     * Handles the pay event, will update Weight, health and the room inventory table.
     * @param event 
     */

    @FXML
    private void onPayButtonClick(ActionEvent event) {
        this.game.pay();
        if (this.game.getTime().getCurrentTime() >= this.game.getGameEndTime()) {
            gameOver();
        }
        if (this.game.getPlayer().isPlayerDead()) {
            gameOver();
        }
        updateWeightBar();
        updateHealthBar();
        updateRoomInventoryTabel();
    }
    
    @FXML
    private void onAskComboBoxSelect(ActionEvent event) {
        String itemType = this.comboBoxAsk.getSelectionModel().getSelectedItem().toString();
        this.game.askForHelp(itemType);
    }

    @FXML
    private void onHelpButtonClick(ActionEvent event) {
        this.game.printHelp();
    }
    
    /**
     * starts a new game
     * @param event 
     */
    @FXML
    private void onStartNewGameClicked(ActionEvent event) {
        // If nothing is selected, then we can't start the game
        if (mapsList.getSelectionModel().isEmpty()) {
            // Show an error message to the player
            new Alert(Alert.AlertType.ERROR, "Please select a map file to the right before you attempt to start the game").showAndWait();
            return;
        }

        String mapToLoad = mapsList.getSelectionModel().getSelectedItem();
        mapToLoad += ".wop";
        initializeGame(mapToLoad);
        startPane.setVisible(false);
        gamePane.setVisible(true);
        this.game.getWelcomeMessage();
        quitPane.setVisible(false);
        setOtherDirectionsValues();

    }

    @FXML
    private void onExitClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void onQuitButtonClick(ActionEvent event) {
        quitText.setText("You sucessfully quitted!");
        quitGame();

    }

	public void quitGame() {

		scoreLabel.setText(this.game.getHighScore().calcScore(game.getItemList()) + "");
		quitHighScoreList.itemsProperty().set(HighScore.showScore());
		startPane.setVisible(false);
		gamePane.setVisible(false);
		quitPane.setVisible(true);
		saveMessageLabel.setVisible(false);
		saveButton.setDisable(false);
	}

    public void gameOver() {
        quitText.setText("GAME OVER!");
        if (this.game.getPlayer().isPlayerDead()) {
            gameOverMessage.setText(SillyMessages.getDeathMessage());
        } else {
            gameOverMessage.setText(this.game.gameOver("You did not manage to get to the exit before IKEA closed. \n"
                + "The security guards threw you out, and destroyed all the things you bought.\n"));
        }
        quitGame();
    }

    private void changeRoom(String direction) {
        this.game.handleRoomMovement(direction);
        setOtherDirectionsValues();
    }

    @FXML
    public void goToOtherDirectionsSelected(ActionEvent e) {
        String directionToMove = otherDirectionsDropdown.getValue();
        changeRoom(directionToMove);

    }

    @FXML
    private void onSaveButtonClicked(ActionEvent event) {
        try {
            game.getHighScore().printScoreToFile(nameField.getText(), this.game.getHighScore().calcScore(game.getItemList()));
        } catch (IOException ex) {

	}
	saveButton.setDisable(true);
	saveMessageLabel.setVisible(true);
	quitHighScoreList.itemsProperty().set(HighScore.showScore());
    }

    @FXML
    private void onPlayAgainButtonClicked(ActionEvent event) {
	highScoreList.itemsProperty().set(HighScore.showScore());
	textArea.clear();
	mapPane.getChildren().clear();
	clock.setText(game.getTime().getNiceFormattedTime());
	updateHealthBar();
	updateWeightBar();
	setPlayerInventoryTabel();
	updateRoomInventoryTabel();
	setAskCombBox();

        drawInitialRoom();


        this.game.addMessageListener(message -> {
            this.textArea.appendText(message);
        });
        this.game.getTime().addListener((observable, oldValue, newValue) -> {
            this.clock.setText(newValue);
        });

        startPane.setVisible(true);
        gamePane.setVisible(false);
        quitPane.setVisible(false);
    }
}

