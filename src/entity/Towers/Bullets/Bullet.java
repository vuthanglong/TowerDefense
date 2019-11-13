package Entity.Towers.Bullets;

import Entity.Enemies.Enemy;

public interface Bullet {
    void travel();
    boolean doDestroy();
    int getDamage();
    Enemy getTarget();
}
