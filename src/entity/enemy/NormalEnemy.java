package entity.enemy;

import entity.map.Road;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NormalEnemy extends ImageView {

    public Road currentRoad;
    private int velocity = 3;
    private boolean passed = false;
    public double HP = 120;
    public boolean dead = false;
    public int reward = 10;
    public double maxHP = 120;

    public HealthBar innerHealthRect;
    public HealthBar outerHealthRect;

    public NormalEnemy() {
        super(new Image("model/resources/enemy.png"));
        outerHealthRect = new HealthBar(60,5, Color.RED);
        innerHealthRect = new HealthBar(60,5, Color.LIMEGREEN);

        outerHealthRect.setLocation(this.getTranslateX(), this.getTranslateY());
        innerHealthRect.setLocation(this.getTranslateX(), this.getTranslateY());
    }

    public void move() {
        if (currentRoad.nextRoad == null) {
            passed = true;
            return;
        }
        if (getTranslateX() < currentRoad.nextRoad.getTranslateX())
            setTranslateX(getTranslateX() + velocity);
        if (getTranslateY() < currentRoad.nextRoad.getTranslateY())
            setTranslateY(getTranslateY() + velocity);
        if (getTranslateX() == currentRoad.nextRoad.getTranslateX() && getTranslateY() == currentRoad.nextRoad.getTranslateY())
            currentRoad = currentRoad.nextRoad;

        outerHealthRect.setTranslateX(this.getTranslateX());
        outerHealthRect.setTranslateY(this.getTranslateY());

        innerHealthRect.setTranslateX(this.getTranslateX());
        innerHealthRect.setTranslateY(this.getTranslateY());

        innerHealthRect.loadHealth(this.getHP() / maxHP, outerHealthRect);
    }

    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad;
        setTranslateX(currentRoad.getTranslateX());
        setTranslateY(currentRoad.getTranslateY());
    }

    public boolean passed() {
        return passed;
    }

    public void getDamage(int dame) {
        HP-=dame;
    }

    public void doDestroy() {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.1), this);
        ft.setToValue(0);
        ft.play();

        innerHealthRect.doDestroy();
        outerHealthRect.doDestroy();
    }

    public HealthBar getInnerHealthRect() {
        return innerHealthRect;
    }

    public HealthBar getOuterHealthRect() {
        return outerHealthRect;
    }

    public double getHP() {
        return HP;
    }

    public void setHP(double HP) {
        this.HP = HP;
    }
}