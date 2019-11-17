package Entity.Enemies;

import Entity.Enemies.Health.HealthBar;
import Entity.Map.Road;


public interface Enemy {
    void move();
    void setCurrentRoad(Road currentRoad);
    boolean isDead();
    void setDead(boolean dead);
    void doDestroy();
    double getHP();
    boolean isPassed();
    double getTranslateX();
    double getTranslateY();
    HealthBar getOuterHealthRect();
    HealthBar getInnerHealthRect();
    void beGetDamage(int damage);
    int getReward();
    int getArmor();
}
