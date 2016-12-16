package zuulFramework.worldofzuul.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameGUI extends Application {
    /**
     * Starts the program
     * @param args 
     */
    public static void startGUI(String[] args) {
        launch(args);
    }
    
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Shop-a-Holic in IKEA - Black Friday Edition!");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
