package zuulFramework.worldofzuul.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import zuulFramework.worldofzuul.entities.Player;
public class Controller implements Initializable  {
    Player player = new Player();

    @FXML
    private ProgressBar healthBar;
    @FXML
    private ProgressBar weightBar;
    
    public void showHealthBar() {
        this.healthBar.setProgress(((double)(player.getLife()))/100);
    }
    
    public void showWeightBar() {
        this.weightBar.setProgress(player.getCarryWeight()/100);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showHealthBar();
        showWeightBar();     
    }
}
