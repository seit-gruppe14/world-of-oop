package zuulFramework.worldofzuul.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.commands.CommandWord;
import zuulFramework.worldofzuul.entities.ItemType;
import zuulFramework.worldofzuul.rooms.Exit;
import zuulFramework.worldofzuul.rooms.Room;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import zuulFramework.worldofzuul.rooms.IHaveSpecialEvent;

public class Controller implements Initializable {
    
    private Game game;
    private ObservableList<ItemType> ItemTypeList;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Game game = new Game();
        this.game = game;
        showHealthBar();
        showWeightBar();
        ItemTypeList = FXCollections.observableArrayList();
        for (ItemType itemType : game.getItemsTypeList()) {
            this.ItemTypeList.add(itemType);
        }
        comboBoxAsk.setItems(ItemTypeList);
        printWelcome();

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
            textArea.appendText(game.pay() + "\n");
            if (game.getPlayer().getCurrentRoom() instanceof Exit) {
                textArea.appendText("Your score is saved and you can quit safely." + "\n");
            }
	    showWeightBar();
	    clock.setText(game.getTime().getNiceFormattedTime());
	    money.setText(game.getPlayer().getMoney());
        }
        if(event.getSource() == comboBoxAsk) {
            this.game.getTime().updateTime(5);
            if (game.getPlayer().getCurrentRoom().hasEmployee()) {
                String itemType = comboBoxAsk.getSelectionModel().getSelectedItem().toString();
                String helpAnswer = this.game.getPlayer().getCurrentRoom().askForHelp(ItemType.get(itemType));
                textArea.appendText(helpAnswer);
            } else {
                textArea.appendText("There is no employees in this room you can ask." + "\n");
            }
        }
        if(event.getSource() == actionButtonHelp) {
            textArea.appendText(game.printHelp());
        }
    }
    
    @FXML
    private void handleButtonMoveEvent(ActionEvent event){
        if (event.getSource()==North) {
            textArea.appendText(game.goRoom("north"));
            //System.out.println(game.getPlayer().getCurrentRoom().getShortDescription());
        } else if(event.getSource()==West) {
            textArea.appendText(game.goRoom("west"));
            //System.out.println(game.getPlayer().getCurrentRoom().getShortDescription());
        } else if(event.getSource()==South) {
            textArea.appendText(game.goRoom("south"));
            //System.out.println(game.getPlayer().getCurrentRoom().getShortDescription());
        }else if(event.getSource()==East){
            textArea.appendText(game.goRoom("east"));
            //System.out.println(game.getPlayer().getCurrentRoom().getShortDescription());
	}
	showHealthBar();
	clock.setText(game.getTime().getNiceFormattedTime());
    }
    
    /**
     * Prints the welcome message and a description of the current room
     */
    private void printWelcome() {
        textArea.appendText("Welcöme möney spender.\n");
        textArea.appendText("Tensiön is high at IKEA Ödense as yöu are waiting tö shöp-amök.\n");
        textArea.appendText("It's BLACK FRIDAY and yöu're ön the löököut för the best öffers pössible tö furnish yöur new appartment.\n");
        textArea.appendText("But be careful as the öther shöppers might beat yöu tö it or tramble yöu tö death!\n");
        textArea.appendText("Are yöu ready?\n");
        textArea.appendText("\n");
        textArea.appendText(String.format("If you need assistance type '%s' tö ask öne öf the blönde IKEA emplöyees.%n\n", CommandWord.HELP));
    }

    public void showHealthBar() {
        this.healthBar.setProgress(((double)(this.game.getPlayer().getLife()))/100);
    }

    public void showWeightBar() {
        this.weightBar.setProgress(this.game.getPlayer().getCarryWeight()/100);
    }

}
