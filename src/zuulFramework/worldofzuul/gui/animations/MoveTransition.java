package zuulFramework.worldofzuul.gui.animations;

import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Created by Rasmus Hansen .
 */
public class MoveTransition extends BaseTransition {

    private Circle toAnimate;
    private double startX;
    private double startY;
    private double endX;
    private double endY;


    public MoveTransition(Circle circle, double endX, double endY) {
        toAnimate = circle;
        this.endX = endX;
        this.endY = endY;
        startX = toAnimate.getCenterX();
        startY = toAnimate.getCenterY();

        this.setCycleDuration(Duration.seconds(2));
    }

    @Override
    protected void interpolate(double frac) {
        toAnimate.setCenterX(lerp(startX, endX, frac));
        toAnimate.setCenterY(lerp(startY, endY, frac));
    }
}
