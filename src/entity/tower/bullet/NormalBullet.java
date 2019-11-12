package entity.tower.bullet;

import entity.enemy.NormalEnemy;
import entity.tower.NormalTower;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NormalBullet extends Rectangle {
    double dx, dy;
    public int damage = 10;
    int speed = 10;
    int timeToTravel;
    NormalTower tower;
    public NormalEnemy target;

    public NormalBullet(NormalTower tower, NormalEnemy enemy) {
        super(4,4, Color.WHITE);
        this.tower = tower;
        this.target = enemy;
        dx = enemy.getTranslateX() - tower.getTranslateX();
        dy = enemy.getTranslateY() - tower.getTranslateY();
        double normalize = speed / Math.sqrt(dx * dx + dy * dy);
        dx = dx * normalize;
        dy = dy * normalize;
        setTranslateX(tower.getTranslateX() + 30);
        setTranslateY(tower.getTranslateY() + 30);
        timeToTravel = (int)(tower.range / speed) - 5;
    }

    public void travel() {
        timeToTravel--;
        setTranslateX(getTranslateX() + dx);
        setTranslateY(getTranslateY() + dy);
    }
    public boolean doDestroy() {

        return timeToTravel <= 0;
    }
}