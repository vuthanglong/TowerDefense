package Entity.Towers;

import Entity.Enemies.Enemy;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MachineGunTower extends ImageView implements Tower, TowersInfo {
    private double range;
    private Enemy target = null;
    private long lastAtk = 0;
    private double reloadTime;
    private int level;

    public MachineGunTower(double x, double y) {
        super(new Image("Model/Images/tower2.png"));
        level = 1;
        this.setTranslateX(x);
        this.setTranslateY(y);
        setRange(TowersInfo.MACHINE_GUN_TOWER_RANGE);
        setReloadTime(TowersInfo.MACHINE_GUN_TOWER_RELOAD_TIME);

        initializeTowerListeners();
    }

    public int getTowerCost() {
        return MACHINE_GUN_TOWER_COST;
    }

    public String getImageUrl() {
        return MACHINE_GUN_TOWER_IMAGE_URL;
    }

    private void initializeTowerListeners() {
        setOnMouseEntered(mouseEvent -> setEffect(new DropShadow()));
        setOnMouseExited(mouseEvent -> setEffect(null));
    }

    @Override
    public double getRange() {
        return range;
    }

    @Override
    public Enemy getTarget() {
        return target;
    }

    @Override
    public void setLastAtk(long lastAtk) {
        this.lastAtk = lastAtk;
    }

    @Override
    public void setTarget(Enemy enemy) {
        this.target = enemy;
    }

    @Override
    public boolean canShot() {
        return ((System.currentTimeMillis() - lastAtk)/1000.0) >= reloadTime;
    }

    @Override
    public String toString() {
        return "MACHINE GUN TOWER\n" +
                "COST: $" + getTowerCost();
    }
    @Override
    public void doUpgrade() {
        level++;
    }

    @Override
    public int getLevel() {
        return level;
    }

    private void setRange(double range) {
        this.range = range;
    }

    private void setReloadTime(double reloadTime) {
        this.reloadTime = reloadTime;
    }
}

