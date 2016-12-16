package zuulFramework.worldofzuul.gui;

import javafx.collections.ObservableList;
import javafx.scene.Node;


public interface IDrawable {
    /**
     * Should add an obejct to the scene
     * @param drawAt A list containing everything that should be drawn
     * @param offset Offset for the object that should be drawn
     */
    void addToScene(ObservableList<Node> drawAt, Offset offset);
    
    /**
     * Should update drawn objects if they should be drawn somewhere else
     */
    void updateDraw();
}
