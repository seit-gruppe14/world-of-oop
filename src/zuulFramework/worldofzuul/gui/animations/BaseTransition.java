package zuulFramework.worldofzuul.gui.animations;

import javafx.animation.Transition;


public abstract class BaseTransition extends Transition {
    protected double lerp(double start, double end, double percent) {
        return start + percent * (end - start);
    }


}
