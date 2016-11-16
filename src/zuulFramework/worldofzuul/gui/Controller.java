package zuulFramework.worldofzuul.gui;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;



public class Controller {

    @FXML
    private Button North;
    @FXML
    private Button West;
    @FXML
    private Button South;
    @FXML
    private Button East;
    @FXML
    private TextArea textArea;
    
    @FXML
    private void handleButtonMoveEvent(ActionEvent event){
        if (event.getSource()==North) {
            System.out.println("You went north");
            textArea.appendText("You went North \n");
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
}
