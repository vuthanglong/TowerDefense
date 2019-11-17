package Entity.Enemies;

import Entity.Enemies.Health.HealthBar;
import Entity.Map.Road;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TankerEnemy extends ImageView implements Enemy, EnemiesInfo {
    private HealthBar outerHealthRect;
    private HealthBar innerHealthRect;

    private Road currentRoad;
    private boolean passed = false;
    private int velocity;
    private double HP;
    private double maxHP;
    private int armor;

    private boolean dead = false;
    private int reward;

    public TankerEnemy() {
        super(new Image(""));

        setMaxHP(EnemiesInfo.TANKER_ENEMY_MAX_HP);
        setVelocity(EnemiesInfo.TANKER_ENEMY_VELOCITY);
        setReward(EnemiesInfo.TANKER_ENEMY_REWARD);

        setHP(maxHP);

        outerHealthRect = new HealthBar(60,5, Color.RED);
        innerHealthRect = new HealthBar(60,5,Color.LIMEGREEN);
        outerHealthRect.setLocation(this.getTranslateX(), this.getTranslateY());
        innerHealthRect.setLocation(this.getTranslateX(), this.getTranslateY());
    }

    @Override
    public void move() {
        if(currentRoad.getNextRoad() == null) {
            passed = true;
            return;
        }
        if (this.getTranslateX() < currentRoad.getNextRoad().getTranslateX()) {
            this.setTranslateX(this.getTranslateX() + velocity);
        }
        if (this.getTranslateY() < currentRoad.getNextRoad().getTranslateY()) {
            this.setTranslateY(this.getTranslateY() + velocity);
        }
        if (this.getTranslateX() == currentRoad.getNextRoad().getTranslateX() && this.getTranslateY() == currentRoad.getNextRoad().getTranslateY()) {
            currentRoad = currentRoad.getNextRoad();
        }

        outerHealthRect.setLocation(this.getTranslateX(), this.getTranslateY());
        innerHealthRect.setLocation(this.getTranslateX(), this.getTranslateY());
        innerHealthRect.loadHealth(outerHealthRect, HP / maxHP);
    }

    @Override
    public void setCurrentRoad(Road currentRoad) {
        this.currentRoad = currentRoad;
        this.setTranslateX(currentRoad.getTranslateX());
        this.setTranslateY(currentRoad.getTranslateY());
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public void doDestroy() {
        FadeTransition ft = new FadeTransition(Duration.seconds(0.1), this);
        ft.setToValue(0);
        ft.play();

        innerHealthRect.doDestroy();
        outerHealthRect.doDestroy();
    }

    @Override
    public double getHP() {
        return HP;
    }

    @Override
    public boolean isPassed() {
        return passed;
    }

    @Override
    public HealthBar getOuterHealthRect() {
        return outerHealthRect;
    }

    @Override
    public HealthBar getInnerHealthRect() {
        return innerHealthRect;
    }

    @Override
    public void beGetDamage(int damage) {
        HP -= damage - getArmor();
    }

    @Override
    public int getReward() {
        return reward;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    private void setHP(double HP) {
        this.HP = HP;
    }

    private void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }

    private void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    private void setReward(int reward) {
        this.reward = reward;
    }
}
