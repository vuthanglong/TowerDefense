package entity.tower;

import entity.enemy.NormalEnemy;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class NormalTower extends ImageView {
    public double range = 120;
    public NormalEnemy target = null;
    public long lastAtk = 0;
    public double reloadTime = 0.5;

    public NormalTower(double x, double y) {
        super(new Image("model/resources/tower.png"));
        setTranslateX(x);
        setTranslateY(y);
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

    public void setTarget(NormalEnemy enemy) {
        this.target = enemy;
    }

    public boolean canShot() {
        return ((System.currentTimeMillis() - lastAtk)/1000.0) >= reloadTime;
    }
}
