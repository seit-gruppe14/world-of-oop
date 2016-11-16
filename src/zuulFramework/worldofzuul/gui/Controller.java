package zuulFramework.worldofzuul.gui;

import java.awt.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Controller {

    @FXML
    private Button actionButtonPay;
    @FXML
    private Button actionButtonAsk;
    @FXML
    private Button actionButtonHelp;
    @FXML
    private TextArea textArea;
    
    @FXML
    private void handleActionButtons(javafx.event.ActionEvent event) {
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
}
