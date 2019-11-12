package entity.enemy;

import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class HealthBar extends Rectangle {
    public HealthBar(double w, double h, Color color) {
        super(w, h, color);
    }

    public void setLocation(double x1, double y1) {
        setTranslateX(x1);
        setTranslateY(y1);
    }

    public void loadHealth(double percent, HealthBar other)
    {
        this.setWidth(other.getWidth() * percent);
    }

    public void doDestroy() {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.1), this);
        ft.setToValue(0);
        ft.play();
    }
}