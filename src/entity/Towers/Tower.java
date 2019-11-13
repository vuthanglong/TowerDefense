package Entity.Towers;

import Entity.Enemies.Enemy;

public interface Tower {
    double getRange();
    Enemy getTarget();
    void setLastAtk(long lastAtk);
    void setTarget(Enemy enemy);
    boolean canShot();
    double getTranslateX();
    double getTranslateY();
    String getImageUrl();
}
