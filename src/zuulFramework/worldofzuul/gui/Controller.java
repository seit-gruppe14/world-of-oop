package zuulFramework.worldofzuul.gui;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import zuulFramework.worldofzuul.Game;
import zuulFramework.worldofzuul.commands.CommandWord;
import zuulFramework.worldofzuul.entities.Player;

public class Controller implements Initializable {
    
    private Game game;

    Player player = new Player();

    @FXML
    private ProgressBar healthBar;
    @FXML
    private ProgressBar weightBar;
    @FXML
    private TextArea textArea;
    @FXML
    private Button actionButtonPay;
    @FXML
    private Button actionButtonAsk;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Game game = new Game();
        this.game = game;
        showHealthBar();
        showWeightBar(); 
        printWelcome();
    }
    
    @FXML
    private void handleActionButtons(ActionEvent event) {
        if(event.getSource() == actionButtonPay) {
            textArea.appendText("Pay");
        }
        if(event.getSource() == actionButtonAsk) {
            textArea.appendText("Ask");
        }
        
        if(event.getSource() == actionButtonHelp) {
            textArea.appendText("Help");
        }
    }
    
    @FXML
    private void handleButtonMoveEvent(ActionEvent event){
        if (event.getSource()==North) {
            System.out.println("You went north");
            textArea.appendText("You went North \n");
            //game
        } else if(event.getSource()==West) {
            System.out.println("You went West");
            textArea.appendText("You went West \n");
        } else if(event.getSource()==South) {
            System.out.println("You went south");
            textArea.appendText("You went South \n");
        }else{
            System.out.println("You went east");
            textArea.appendText("You went East \n");
        }
    }
    
    /**
     * Prints the welcome message and a description of the current room
     */
    private void printWelcome() {
        textArea.appendText("\n");
        textArea.appendText("Welcöme möney spender.\n");
        textArea.appendText("Tensiön is high at IKEA Ödense as yöu are waiting tö shöp-amök.\n");
        textArea.appendText("It's BLACK FRIDAY and yöu're ön the löököut för the best öffers pössible tö furnish yöur new appartment.\n");
        textArea.appendText("But be careful as the öther shöppers might beat yöu tö it or tramble yöu tö death!\n");
        textArea.appendText("Are yöu ready?\n");
        textArea.appendText("\n");
        textArea.appendText(String.format("If you need assistance type '%s' tö ask öne öf the blönde IKEA emplöyees.%n\n", CommandWord.HELP));
        textArea.appendText("\n");
        textArea.appendText("\n");
    }
    
    public void showHealthBar() {
        this.healthBar.setProgress(((double)(player.getLife()))/100);
    }
    
    public void showWeightBar() {
        this.weightBar.setProgress(player.getCarryWeight()/100);
    }
}
