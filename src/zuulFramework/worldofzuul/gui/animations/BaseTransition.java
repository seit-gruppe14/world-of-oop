package zuulFramework.worldofzuul.gui.animations;

import javafx.animation.Transition;

/**
 * Created by Rasmus Hansen .
 */
public abstract class BaseTransition extends Transition {
    protected double lerp(double start, double end, double percent) {
        return start + percent * (end - start);
    }


}
