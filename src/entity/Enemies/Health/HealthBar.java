package Entity.Enemies.Health;

import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class HealthBar extends Rectangle {
    public HealthBar(double width, double height, Color color) {
        super(width, height, color);
    }

    public void setLocation(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
    }

    public void loadHealth(HealthBar other, double percent) {
        this.setWidth(other.getWidth() * percent);
    }

    public void doDestroy() {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.1), this);
        ft.setToValue(0);
        ft.play();
    }
}
