package Entity.Map;

import javafx.scene.image.ImageView;

public class Road extends ImageView {
    private Road nextRoad;

    public Road(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    public Road getNextRoad() {
        return nextRoad;
    }

    public void setNextRoad(Road other) {
        nextRoad = other;
    }
}