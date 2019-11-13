package Entity.Towers;

import Entity.Enemies.Enemy;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MachineGunTower extends ImageView implements Tower, TowersInfo {
    private double range;
    private Enemy target = null;
    private long lastAtk = 0;
    private double reloadTime;

    public MachineGunTower(double x, double y) {
        super(new Image(""));

        this.setTranslateX(x);
        this.setTranslateY(y);
        setRange(TowersInfo.MACHINE_GUN_TOWER_RANGE);
        setReloadTime(TowersInfo.MACHINE_GUN_TOWER_RELOAD_TIME);

        initializeTowerListeners();
    }

    private void initializeTowerListeners() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                }
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(new DropShadow());
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(null);
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });
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

    private void setRange(double range) {
        this.range = range;
    }

    private void setReloadTime(double reloadTime) {
        this.reloadTime = reloadTime;
    }
}

