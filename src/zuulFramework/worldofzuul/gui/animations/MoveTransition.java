package zuulFramework.worldofzuul.gui.animations;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private EventHandler<ActionEvent> outerOnFinished;


    public MoveTransition(Circle circle, double endX, double endY) {
        toAnimate = circle;
        this.endX = endX;
        this.endY = endY;

        this.setCycleDuration(Duration.seconds(2));
    }

    @Override
    protected void interpolate(double frac) {
        toAnimate.setTranslateX(lerp(0, endX, frac));
        toAnimate.setTranslateY(lerp(0, endY, frac));
    }

    public void beforeStart() {
        this.outerOnFinished = getOnFinished();

        resetExistingAnimations();

        startX = toAnimate.getCenterX();
        startY = toAnimate.getCenterY();
        endX -= startX;
        endY -= startY;

        setOnFinished(event -> {

            resetExistingAnimations();

            // Call parent
            this.outerOnFinished.handle(event);
        });
    }


    private void resetExistingAnimations() {
        toAnimate.setCenterX(toAnimate.getCenterX() + toAnimate.getTranslateX());
        toAnimate.setCenterY(toAnimate.getCenterY() + toAnimate.getTranslateY());
        toAnimate.setTranslateX(0);
        toAnimate.setTranslateY(0);
    }
}
