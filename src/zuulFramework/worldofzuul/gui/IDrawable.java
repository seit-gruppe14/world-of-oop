package zuulFramework.worldofzuul.gui;

import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * Created by Rasmus Hansen .
 */
public interface IDrawable {
	void addToScene(ObservableList<Node> drawAt, Offset offset);

	void updateDraw();
}
