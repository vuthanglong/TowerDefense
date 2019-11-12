package view;

import entity.enemy.NormalEnemy;
import entity.map.Road;
import entity.map.Mountain;

import entity.tower.NormalTower;
import entity.tower.bullet.NormalBullet;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.MenuButton;
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

    public void createNewGame(Stage menuStage) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        gameStage.setTitle("Tower Defense");
        gameStage.show();
    }
    public GameViewManager() {
        initializeStage();
    }
    private Image BACKGROUND = new Image("model/resources/background.png");
    private ImageView background = new ImageView(BACKGROUND);

    private Player player = new Player();

    private List<NormalEnemy> enemies = new ArrayList<>();
    private List<Mountain> mountains = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<NormalTower> towers = new ArrayList<>();
    private List<NormalBullet> bullets = new ArrayList<>();

    private AnimationTimer timer;

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
        for(NormalEnemy enemy : enemies)
            if(!enemy.dead) enemy.move();
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
            NormalEnemy enemy = enemies.get(i);
            if(enemy.passed() && !enemy.dead) {
                player.getDamage();
                enemy.dead = true;
                enemy.doDestroy();
                enemies.remove(i);
            }
            if(enemy.HP <= 0) {
                enemy.dead = true;
                enemy.doDestroy();
                player.getReward(enemy.reward);
                enemies.remove(i);
            }
        }
        for (NormalTower tower : towers) {
            if(tower.target != null) {
                if(checkDistance(tower.target, tower) > tower.range || tower.target.dead || tower.target.passed()) {
                    tower.setTarget(null);
                }
            }
            if(tower.target == null) {
                for(NormalEnemy enemy : enemies) {
                    if(checkDistance(enemy, tower) < tower.range && tower.target == null) {
                        tower.setTarget(enemy);
                    }
                }
            }
            if(tower.target != null) {
                if(tower.canShot()) {
                    NormalBullet bullet = new NormalBullet(tower, tower.target);
                    bullets.add(bullet);
                    gamePane.getChildren().add(bullet);
                    tower.lastAtk = System.currentTimeMillis();
                }
            }
        }
        for (int i = bullets.size() - 1; i >= 0 ; i--) {
            NormalBullet bullet = bullets.get(i);
            if(bullet.doDestroy()) {
                bullet.target.getDamage(10);
                //System.out.println("dau buoi");
                bullet.target.getDamage(bullet.damage);
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
            roads.get(i).nextRoad = roads.get(i + 1);
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
        NormalTower tower = new NormalTower(mountain.getTranslateX(), mountain.getTranslateY());
        towers.add(tower);
        gamePane.getChildren().add(tower);
    }
    private NormalEnemy spawnEnemy() {
        NormalEnemy enemy = new NormalEnemy();
        enemy.setCurrentRoad(roads.get(0));
        gamePane.getChildren().add(enemy);
        gamePane.getChildren().addAll(enemy.getOuterHealthRect(), enemy.getInnerHealthRect());
        return enemy;
    }

    private double checkDistance(NormalEnemy enemy, NormalTower tower) {
        return Math.sqrt( (enemy.getTranslateX() - tower.getTranslateX()) * (enemy.getTranslateX() - tower.getTranslateX())
                + ( enemy.getTranslateY() - tower.getTranslateY()) * ( enemy.getTranslateY() - tower.getTranslateY())) ;
    }
}