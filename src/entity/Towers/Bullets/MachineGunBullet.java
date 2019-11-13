package Entity.Towers.Bullets;

import Entity.Enemies.Enemy;
import Entity.Towers.Tower;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MachineGunBullet extends Rectangle implements Bullet, BulletsInfo {
    private double dx;
    private double dy;
    private int damage;
    private int speed;
    private int timeToTravel;
    private Tower tower;
    private Enemy target;

    public MachineGunBullet(Tower tower, Enemy enemy) {
        super(4,4, Color.WHITE);
        setDamage(BulletsInfo.MACHINE_GUN_BULLET_DAMAGE);
        setSpeed(BulletsInfo.MACHINE_GUN_BULLET_SPEED);
        this.tower = tower;
        this.target = enemy;
        dx = enemy.getTranslateX() - tower.getTranslateX();
        dy = enemy.getTranslateY() - tower.getTranslateY();
        double normalize = speed / Math.sqrt(dx * dx + dy * dy);
        dx = dx * normalize;
        dy = dy * normalize;
        setTranslateX(tower.getTranslateX() + 30);
        setTranslateY(tower.getTranslateY() + 30);
        timeToTravel = (int)(tower.getRange() / speed) - 5;
    }

    @Override
    public void travel() {
        timeToTravel--;
        setTranslateX(getTranslateX() + dx);
        setTranslateY(getTranslateY() + dy);
    }

    @Override
    public boolean doDestroy() {
        return timeToTravel <= 0;
    }

    @Override
    public Enemy getTarget() {
        return target;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    private void setDamage(int damage) {
        this.damage = damage;
    }

    private void setSpeed(int speed) {
        this.speed = speed;
    }
}
