package zuulFramework.worldofzuul.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameGUI extends Application {

	public static void startGUI(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
		primaryStage.setTitle("Shop-a-Holic in IKEA - Black Friday Edition!");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
}
