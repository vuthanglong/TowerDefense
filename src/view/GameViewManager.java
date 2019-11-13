package View;

import Entity.Enemies.Enemy;

import Entity.Enemies.NormalEnemy;
import Entity.Map.Mountain;
import Entity.Map.Road;
import Entity.Towers.Bullets.Bullet;
import Entity.Towers.Bullets.NormalBullet;
import Entity.Towers.NormalTower;

import Entity.Towers.Tower;
import Model.Button.MenuButton;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import sample.Player;

import java.util.ArrayList;
import java.util.List;

public class GameViewManager {
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;
    private Stage menuStage;

    public static final int GAME_WIDTH = 900;
    public static final int GAME_HEIGHT = 600;

    private Image BACKGROUND = new Image("Model/Images/backgroundlv1.png");
    private ImageView background = new ImageView(BACKGROUND);

    private Player player = new Player();

    private List<Enemy> enemies = new ArrayList<>();
    private List<Mountain> mountains = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<Tower> towers = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();

    private AnimationTimer timer;

    public void createNewGame(Stage menuStage) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        gameStage.setTitle("Tower Defense");
        gameStage.show();
    }

    public GameViewManager() {
        initializeStage();
    }

    private void initializeStage() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        gamePane.getChildren().add(background);
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();
        gameStage.setScene(gameScene);
        createMap();
    }

    private void onUpdate() {
        for(Enemy enemy : enemies)
            if(!enemy.isDead()) enemy.move();
        if (Math.random() < 0.0075) {
            enemies.add(spawnEnemy());
        }
        if(player.dead()){
            MenuButton button = new MenuButton("GGWP NOOB!!!");
            button.setTranslateX(350);
            button.setTranslateY(250);
            gamePane.getChildren().add(button);
            timer.stop();
        }
        checkState();
    }

    private void checkState() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if(enemy.isPassed() && !enemy.isDead()) {
                player.getDamage();
                enemy.setDead(true);
                enemy.doDestroy();
                enemies.remove(i);
            }
            if(enemy.getHP() <= 0) {
                enemy.setDead(true);
                enemy.doDestroy();
                player.getReward(enemy.getReward());
                enemies.remove(i);
            }
        }
        for (Tower tower : towers) {
            if(tower.getTarget() != null) {
                if(checkDistance(tower.getTarget(), tower) > tower.getRange() || tower.getTarget().isDead() || tower.getTarget().isPassed()) {
                    tower.setTarget(null);
                }
            }
            if(tower.getTarget() == null) {
                for(Enemy enemy : enemies) {
                    if(checkDistance(enemy, tower) < tower.getRange() && tower.getTarget() == null) {
                        tower.setTarget(enemy);
                    }
                }
            }
            if(tower.getTarget() != null) {
                if(tower.canShot()) {
                    Bullet bullet = new NormalBullet(tower, tower.getTarget());
                    bullets.add(bullet);
                    gamePane.getChildren().add((Node) bullet);
                    tower.setLastAtk(System.currentTimeMillis());
                }
            }
        }
        for (int i = bullets.size() - 1; i >= 0 ; i--) {
            Bullet bullet = bullets.get(i);
            if(bullet.doDestroy()) {
                bullet.getTarget().beGetDamage(bullet.getDamage());
                gamePane.getChildren().remove(bullet);
                bullets.remove(i);
            }
            else bullet.travel();
        }
    }

    private void createMap() {
        roads.add(new Road(0, 60));
        roads.add(new Road(60, 60));
        roads.add(new Road(120, 60));
        for (int i = 0; i < 5; i++) {
            roads.add(new Road(120, 60 + 60 * i));
        }
        for (int i = 0; i < 13; i++) {
            roads.add(new Road(120 + 60 * i,60 + 60 * 5));
        }
        for (int i = 0; i < 20; i++) {
            roads.get(i).setNextRoad(roads.get(i + 1));
        }
        for (Road road : roads) {
            gamePane.getChildren().add(road);
        }
        for(int i =0; i < 4; i++) {
            Mountain mountain = new Mountain();
            mountains.add(mountain);
            mountain.setTranslateX(180 + 180 * i);
            mountain.setTranslateY(420);
            mountain.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    buildTower(mountain);
                }
            });
            gamePane.getChildren().add(mountain);
        }
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                Mountain mountain = new Mountain();
                mountains.add(mountain);
                mountain.setTranslateX(60 + 120 * i);
                mountain.setTranslateY(120 + 180 * j);
                mountain.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        buildTower(mountain);
                    }
                });
                gamePane.getChildren().add(mountain);
            }
        }
    }

    private void buildTower(Mountain mountain) {
        gamePane.getChildren().remove(mountain);
        Tower tower = new NormalTower(mountain.getTranslateX(), mountain.getTranslateY());
        towers.add(tower);
        gamePane.getChildren().add((Node) tower);
    }

    private Enemy spawnEnemy() {
        Enemy enemy = new NormalEnemy();
        enemy.setCurrentRoad(roads.get(0));
        gamePane.getChildren().add((Node) enemy);
        gamePane.getChildren().addAll(enemy.getOuterHealthRect(), enemy.getInnerHealthRect());
        return enemy;
    }

    private double checkDistance(Enemy enemy, Tower tower) {
        return Math.sqrt( (enemy.getTranslateX() - tower.getTranslateX()) * (enemy.getTranslateX() - tower.getTranslateX())
                + ( enemy.getTranslateY() - tower.getTranslateY()) * ( enemy.getTranslateY() - tower.getTranslateY())) ;
    }
}
