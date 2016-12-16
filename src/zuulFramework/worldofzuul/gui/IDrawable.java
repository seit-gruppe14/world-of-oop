package zuulFramework.worldofzuul.gui;

import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * Created by Rasmus Hansen .
 */
public interface IDrawable {
    /**
     * Should add an obejct to the scene
     * @param drawAt
     * @param offset 
     */
    void addToScene(ObservableList<Node> drawAt, Offset offset);
    
    /**
     * Should update drawn objects if they should be drawn somewhere else
     */
    void updateDraw();
}
