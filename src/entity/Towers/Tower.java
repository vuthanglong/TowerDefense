package Entity.Towers;

import Entity.Enemies.Enemy;
import Entity.Towers.Bullets.Bullet;

public interface Tower {
    double getRange();
    Enemy getTarget();
    void setLastAtk(long lastAtk);
    void setTarget(Enemy enemy);
    boolean canShot();
    double getTranslateX();
    double getTranslateY();
    String getImageUrl();
    int getTowerCost();
    String toString();
    void doUpgrade();
    int getLevel();
    Bullet getBullet();
}
