package entity.map;

import javafx.scene.image.ImageView;

public class Road extends ImageView {
    public Road nextRoad;

    public Road(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }
}