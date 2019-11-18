package View;

import Entity.Enemies.*;

import Entity.Map.Mountain;
import Entity.Map.Road;
import Entity.Towers.Bullets.Bullet;
import Entity.Towers.Bullets.NormalBullet;
import Entity.Towers.MachineGunTower;
import Entity.Towers.NormalTower;

import Entity.Towers.SniperTower;
import Entity.Towers.Tower;
import Model.Button.MenuButton;

import Model.Button.ShopButton;
import Model.Sound.GameMediaPlayer;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

import sample.Player;

import java.util.ArrayList;
import java.util.List;

public class GameViewManager {
    private AnchorPane gamePane;
    private Scene gameScene;
    private Stage gameStage;
    private Stage menuStage;

    private static final int GAME_WIDTH = 900;
    private static final int GAME_HEIGHT = 600;
    private int lv;

    private Player player = new Player();
    private Label label = new Label();
    private Label labelShowing;
    private GameMediaPlayer gameMediaPlayer = new GameMediaPlayer();

    private List<Label> towerLabels = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Mountain> mountains = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<Tower> towers = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<ImageView> shopButtons = new ArrayList<>();
    private List<ShopButton> buttons = new ArrayList<>();
    private List<Node> upgradeButtons = new ArrayList<>();
    private long lastSpawn;
    private int a[] = {1, 1, 1, 1, 2, 2, 2, 3, 3, 2, 1, 1, 1, 4};
    private int count = 0;
    private int killCount = 0;

    private AnimationTimer timer;

    public void createNewGame(Stage menuStage) {
        this.menuStage = menuStage;
        this.menuStage.hide();
        gameStage.setTitle("Tower Defense");
        gameStage.show();
    }

    public GameViewManager(int lv) {
        initializeStage(lv);
        this.lv = lv;
        gameMediaPlayer.getMediaPlayer(1).stop();
        gameMediaPlayer.getMediaPlayer(1).play();
    }

    private void initializeStage(int lv) {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
        gameStage = new Stage();
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();
        gameStage.setScene(gameScene);
        createMap(lv);
        createPauseButton();
    }
    private boolean muted;

    private void createPauseButton() {
        ImageView pause = new ImageView(new Image("Model/Images/pause.png"));
        pause.setTranslateX(0);
        pause.setTranslateY(0);
        pause.setOnMouseClicked(mouseEvent -> {
            showPauseMenu();
            timer.stop();
        });
        gamePane.getChildren().add(pause);
        ImageView mute = new ImageView(new Image("Model/Images/mute.png"));
        mute.setTranslateX(70);
        mute.setTranslateY(0);
        mute.setOnMouseClicked(mouseEvent -> {
            gameMediaPlayer.mute();
        });
        gamePane.getChildren().add(mute);
    }

    private void showPauseMenu() {
        ImageView backScene = new ImageView(new Image("Model/Images/subscene.png"));
        backScene.setTranslateX(200);
        backScene.setTranslateY(85);
        MenuButton button = new MenuButton("RESTART");
        button.setTranslateX(350);
        button.setTranslateY(200);
        button.setOnAction(mouseEvent -> {
            GameViewManager gameManager = new GameViewManager(lv);
            gameManager.createNewGame(gameStage);
            gameMediaPlayer.getMediaPlayer(0).stop();
        });
        MenuButton menuButton = new MenuButton("MAIN MENU");
        menuButton.setTranslateX(350);
        menuButton.setTranslateY(300);
        menuButton.setOnAction(mouseEvent -> {
            gameStage.close();
            Stage primaryStage;
            ViewManager manager = new ViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.setTitle("Demo");
            primaryStage.show();
        });
        MenuButton exitButton = new MenuButton("EXIT");
        exitButton.setTranslateX(350);
        exitButton.setTranslateY(400);
        exitButton.setOnAction(mouseEvent -> {
            gameStage.close();
        });
        MenuButton resumeButton = new MenuButton("RESUME");
        resumeButton.setTranslateX(350);
        resumeButton.setTranslateY(100);
        resumeButton.setOnAction(mouseEvent -> {
            timer.start();
            gamePane.getChildren().removeAll(backScene, button, menuButton, exitButton, resumeButton);
        });
        gamePane.getChildren().addAll(backScene, button, menuButton, exitButton, resumeButton);
    }

    private void onUpdate() {
        if(killCount == a.length) showAfterLevelScene("YOU WON");
        for(Enemy enemy : enemies)
            if(!enemy.isDead()) enemy.move();
        if((System.currentTimeMillis() - lastSpawn)/1000.0 >= 1 && count < a.length) {
            enemies.add(spawnEnemy(a[count++]));
            lastSpawn = System.currentTimeMillis();
        }
        if(player.dead()){
            showAfterLevelScene("YOU LOST");
            timer.stop();
            gameMediaPlayer.getMediaPlayer(1).stop();
            gameMediaPlayer.getMediaPlayer(8).play();
        }
        updatePlayerInfo();
        checkState();
    }
    private void showAfterLevelScene(String txt) {
        Label label = new Label();
        label.setText(txt);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 50));
        label.setTranslateX(320);
        label.setTranslateY(125);
        ImageView backScene = new ImageView(new Image("Model/Images/subscene.png"));
        backScene.setTranslateX(200);
        backScene.setTranslateY(100);
        gamePane.getChildren().addAll(backScene, label);
        MenuButton button = new MenuButton("RESTART");
        button.setTranslateX(350);
        button.setTranslateY(200);
        button.setOnAction(mouseEvent -> {
            timer.stop();
            GameViewManager gameManager = new GameViewManager(lv);
            gameManager.createNewGame(gameStage);
            gameMediaPlayer.getMediaPlayer(0).stop();
        });
        MenuButton menuButton = new MenuButton("MAIN MENU");
        menuButton.setTranslateX(350);
        menuButton.setTranslateY(300);
        menuButton.setOnAction(mouseEvent -> {
            timer.stop();
            gameStage.hide();
            Stage primaryStage;
            ViewManager manager = new ViewManager();
            primaryStage = manager.getMainStage();
            primaryStage.setTitle("Demo");
            primaryStage.show();
        });
        MenuButton exitButton = new MenuButton("EXIT");
        exitButton.setTranslateX(350);
        exitButton.setTranslateY(400);
        exitButton.setOnAction(mouseEvent -> {
            gameStage.close();
        });
        gamePane.getChildren().addAll(button, menuButton, exitButton);
    }

    private void checkState() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if(enemy.isPassed() && !enemy.isDead()) {
                player.getDamage();
                gameMediaPlayer.getMediaPlayer(9).stop();
                gameMediaPlayer.getMediaPlayer(9).play();
                enemy.setDead(true);
                enemy.doDestroy();
                enemies.remove(i);
                killCount++;
            }
            if(enemy.getHP() <= 0) {
                enemy.setDead(true);
                enemy.doDestroy();
                player.getReward(enemy.getReward());
                enemies.remove(i);
                killCount ++;
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
                    Bullet bullet = tower.getBullet();
                    bullets.add(bullet);
                    gamePane.getChildren().add((Node) bullet);
                    tower.setLastAtk(System.currentTimeMillis());
                    gameMediaPlayer.getMediaPlayer(6).stop();
                    gameMediaPlayer.getMediaPlayer(6).play();
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

    private void createMap(int lv) {
        if(lv == 1) {
            Image BACKGROUND = new Image("Model/Images/backgroundlv1.png");
            ImageView background = new ImageView(BACKGROUND);
            gamePane.getChildren().add(background);
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
                mountain.setOnMouseReleased(mouseEvent -> {
                    showShop(mountain);
                    hideUpgrade();
                });
                gamePane.getChildren().add(mountain);
            }
            for(int i = 0; i < 2; i++) {
                for(int j = 0; j < 2; j++) {
                    Mountain mountain = new Mountain();
                    mountains.add(mountain);
                    mountain.setTranslateX(60 + 120 * i);
                    mountain.setTranslateY(120 + 180 * j);
                    mountain.setOnMouseReleased(mouseEvent -> {
                        showShop(mountain);
                        hideUpgrade();
                    });
                    gamePane.getChildren().add(mountain);
                }
            }
        }
        if(lv == 2) {
            Image BACKGROUND = new Image("Model/Images/backgroundlv2.png");
            ImageView background = new ImageView(BACKGROUND);
            gamePane.getChildren().add(background);
            for(int i = 0; i < 3; i++) {
                roads.add(new Road(60 * i, 60));
            }
            for (int i = 0; i < 2; i++) {
                roads.add(new Road(120, 120 + 60 * i));
            }
            for (int i = 0; i < 7 ; i++) {
                roads.add(new Road(180 + 60 * i, 180));
            }
            for (int i = 0; i < 3; i++) {
                roads.add(new Road(540, 240 + 60 * i));
            }
            for (int i = 0; i < 4; i++) {
                roads.add(new Road(600 + 60 * i, 360));
            }
            roads.add(new Road(900 - 120, 420));
            for (int i = 0; i < 19; i++) {
                roads.get(i).setNextRoad(roads.get(i+1));
            }
            for(Road road : roads) {
                gamePane.getChildren().add(road);
            }
            for (int i = 0; i < 4; i++) {
                Mountain mountain = new Mountain();
                mountains.add(mountain);
                mountain.setTranslateX(60 + 180 * i);
                mountain.setTranslateY(120);
                mountain.setOnMouseReleased(mouseEvent -> {
                    showShop(mountain);
                    hideUpgrade();
                });
                gamePane.getChildren().add(mountain);
            }
            for (int i = 0; i < 3; i++) {
                Mountain mountain = new Mountain();
                mountains.add(mountain);
                mountain.setTranslateX(120 + 180 * i);
                mountain.setTranslateY(240);
                mountain.setOnMouseReleased(mouseEvent -> {
                    showShop(mountain);
                    hideUpgrade();
                });
                gamePane.getChildren().add(mountain);
            }
            for (int i = 0; i < 3; i++) {
                Mountain mountain = new Mountain();
                mountains.add(mountain);
                mountain.setTranslateX(60 * 8 + 180 * i);
                mountain.setTranslateY(420);
                mountain.setOnMouseReleased(mouseEvent -> {
                    showShop(mountain);
                    hideUpgrade();
                });
                gamePane.getChildren().add(mountain);
            }
            for (int i = 0; i < 2; i++) {
                Mountain mountain = new Mountain();
                mountains.add(mountain);
                mountain.setTranslateX(60 * 10 + 180 * i);
                mountain.setTranslateY(300);
                mountain.setOnMouseReleased(mouseEvent -> {
                    showShop(mountain);
                    hideUpgrade();
                });
                gamePane.getChildren().add(mountain);
            }

        }
    }

    private void showShop(Mountain mountain) {
        Tower normalTower = new NormalTower(mountain.getTranslateX(), mountain.getTranslateY());
        Label normalLabel = towerInfo(normalTower, 120, 520);
        createShopButton(mountain, normalTower, 50, 520);
        Tower machineGunTower = new MachineGunTower(mountain.getTranslateX(), mountain.getTranslateY());
        Label machineLabel = towerInfo(machineGunTower, 370, 520);
        createShopButton(mountain, machineGunTower,300, 520);
        Tower sniperTower = new SniperTower(mountain.getTranslateX(), mountain.getTranslateY());
        Label sniperLabel = towerInfo(sniperTower, 620, 520);
        createShopButton(mountain, sniperTower,550, 520);
        gamePane.getChildren().addAll(normalLabel, machineLabel, sniperLabel);
        towerLabels.add(normalLabel);
        towerLabels.add(machineLabel);
        towerLabels.add(sniperLabel);
        createHideShopButton();
    }
    private void createHideShopButton() {
        ImageView hideButton = new ImageView(new Image("Model/Images/hide_shop.png"));
        hideButton.setOnMouseReleased(mouseEvent -> hideShop());
        hideButton.setTranslateX(790);
        hideButton.setTranslateY(520);
        gamePane.getChildren().add(hideButton);
        shopButtons.add(hideButton);
    }
    private void createShopButton(Mountain mountain, Tower tower, int x, int y) {
        ImageView towerImage = new ImageView(new Image(tower.getImageUrl()));
        towerImage.setTranslateX(x);
        towerImage.setTranslateY(y);
        ShopButton button = new ShopButton();
        button.setTranslateX(x-4);
        button.setTranslateY(y-4);
        gamePane.getChildren().add(button);
        buttons.add(button);
        button.setOnAction(actionEvent -> {
            if(player.getGold() >= tower.getTowerCost())
            {
                player.subtractGold(tower.getTowerCost());
                buildTower(mountain, tower);
                hideShop();
                showUpgrade(tower);
                gameMediaPlayer.getMediaPlayer(3).stop();
                gameMediaPlayer.getMediaPlayer(3).play();
            }
        });
        towerImage.setOnMouseReleased(mouseEvent -> {
            if(player.getGold() >= tower.getTowerCost())
            {
                player.subtractGold(tower.getTowerCost());
                buildTower(mountain, tower);
                hideShop();
                showUpgrade(tower);
                gameMediaPlayer.getMediaPlayer(3).stop();
                gameMediaPlayer.getMediaPlayer(3).play();
            }
        });
        gamePane.getChildren().add(towerImage);
        shopButtons.add(towerImage);
    }
    private void updatePlayerInfo() {
        gamePane.getChildren().remove(label);
        label.setText(player.toString());
        label.setLayoutX(785);
        label.setLayoutY(0);
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 50));
        gamePane.getChildren().add(label);
    }
    private Label towerInfo(Tower tower, int x, int y) {
        Label towerLabel = new Label();
        towerLabel.setText(tower.toString());
        towerLabel.setTextFill(Color.WHITE);
        towerLabel.setLayoutX(x);
        towerLabel.setLayoutY(y+10);
        towerLabel.setFont(new Font("Arial", 15));
        return towerLabel;
    }
    private void hideShop() {
        for (int i = shopButtons.size() - 1; i >= 0 ; i--) {
            ImageView buttonToRemove = shopButtons.get(i);
            gamePane.getChildren().remove(buttonToRemove);
            shopButtons.remove(buttonToRemove);
        }
        for (int i = buttons.size() - 1; i >= 0 ; i--) {
            ShopButton btToRemove = buttons.get(i);
            gamePane.getChildren().remove(btToRemove);
            buttons.remove(btToRemove);
        }
        for (int i = towerLabels.size() - 1; i >= 0 ; i--) {
            Label twL = towerLabels.get(i);
            gamePane.getChildren().remove(twL);
            towerLabels.remove(twL);
        }
    }
    private void buildTower(Mountain mountain, Tower tower) {
        gamePane.getChildren().remove(mountain);
        towers.add(tower);
        Rectangle clickToUp = new Rectangle(60, 60, Color.TRANSPARENT);
        clickToUp.setTranslateX(tower.getTranslateX());
        clickToUp.setTranslateY(tower.getTranslateY());
        clickToUp.setOnMouseClicked(mouseEvent -> {
            if(player.getGold() >= tower.getTowerCost()){
                showUpgrade(tower);
                hideShop();
            }
        });
        gamePane.getChildren().add((Node) tower);
        gamePane.getChildren().add(clickToUp);
    }

    private void showUpgrade(Tower tower) {
        createUpgradeButton(tower);
        createSellButton(tower);
        createHideButton();
        gamePane.getChildren().add(updateLabel(tower));
    }
    private Label updateLabel(Tower tower) {
        gamePane.getChildren().remove(labelShowing);
        upgradeButtons.remove(labelShowing);
        Label towerLabel = new Label();
        towerLabel.setText("Level: " + tower.getLevel() + "\n"
                + "Upgrade: $" + tower.getTowerCost() + "\n"
                + "Sell: $" + tower.getTowerCost() * tower.getLevel() / 2);
        towerLabel.setTextFill(Color.WHITE);
        towerLabel.setLayoutX(50);
        towerLabel.setLayoutY(520);
        towerLabel.setFont(new Font("Arial", 15));
        upgradeButtons.add(towerLabel);
        labelShowing = towerLabel;
        return labelShowing;
    }
    private void createHideButton() {
        ImageView hideButton = new ImageView(new Image("Model/Images/hide_shop.png"));
        hideButton.setOnMouseReleased(mouseEvent -> hideUpgrade());
        hideButton.setTranslateX(790);
        hideButton.setTranslateY(520);
        gamePane.getChildren().add(hideButton);
        upgradeButtons.add(hideButton);
    }
    private void hideUpgrade() {
        for (int i = upgradeButtons.size() - 1; i >= 0 ; i--) {
            Node toRemove = upgradeButtons.get(i);
            gamePane.getChildren().remove(toRemove);
            upgradeButtons.remove(i);
        }
    }
    private void createUpgradeButton(Tower tower) {
        ImageView upgradeButton = new ImageView(new Image("Model/Images/upgrade_button.png"));
        upgradeButton.setTranslateX(200);
        upgradeButton.setTranslateY(520);
        gamePane.getChildren().add(upgradeButton);
        upgradeButtons.add(upgradeButton);
        upgradeButton.setOnMouseClicked(mouseEvent -> {
            if(player.getGold() >= tower.getTowerCost() && tower.getLevel() < 3) {
                tower.doUpgrade();
                player.subtractGold(tower.getTowerCost());
                gamePane.getChildren().add(updateLabel(tower));
                gameMediaPlayer.getMediaPlayer(4).stop();
                gameMediaPlayer.getMediaPlayer(4).play();
            }
        });
    }
    private void createSellButton(Tower tower) {
        ImageView sellButton = new ImageView(new Image("Model/Images/sell_button.png"));
        sellButton.setTranslateX(300);
        sellButton.setTranslateY(520);
        gamePane.getChildren().add(sellButton);
        upgradeButtons.add(sellButton);
        sellButton.setOnMouseClicked(mouseEvent -> {
                gamePane.getChildren().remove(tower);
                towers.remove(tower);
                player.subtractGold(tower.getTowerCost() * tower.getLevel() / -2);
                Mountain mountain = new Mountain();
                mountains.add(mountain);
                mountain.setTranslateX(tower.getTranslateX());
                mountain.setTranslateY(tower.getTranslateY());
                mountain.setOnMouseReleased(mouseEvent1 -> {
                    showShop(mountain);
                    hideUpgrade();
                });
                gamePane.getChildren().add(mountain);
                hideUpgrade();
        });
    }

    private Enemy spawnEnemy(int i) {
        switch (i) {
            case 1 :
                Enemy enemy = new NormalEnemy();
                enemy.setCurrentRoad(roads.get(0));
                gamePane.getChildren().add((Node) enemy);
                gamePane.getChildren().addAll(enemy.getOuterHealthRect(), enemy.getInnerHealthRect());
                return enemy;
            case 2 :
                Enemy enemy2 = new TankerEnemy();
                enemy2.setCurrentRoad(roads.get(0));
                gamePane.getChildren().add((Node) enemy2);
                gamePane.getChildren().addAll(enemy2.getOuterHealthRect(), enemy2.getInnerHealthRect());
                return enemy2;
            case 3 :
                Enemy enemy3 = new SmallerEnemy();
                enemy3.setCurrentRoad(roads.get(0));
                gamePane.getChildren().add((Node) enemy3);
                gamePane.getChildren().addAll(enemy3.getOuterHealthRect(), enemy3.getInnerHealthRect());
                return enemy3;
            case 4 :
                Enemy enemy4 = new BossEnemy();
                enemy4.setCurrentRoad(roads.get(0));
                gamePane.getChildren().add((Node) enemy4);
                gamePane.getChildren().addAll(enemy4.getOuterHealthRect(), enemy4.getInnerHealthRect());
                return enemy4;
        }
        return new NormalEnemy();
    }

    private double checkDistance(Enemy enemy, Tower tower) {
        return Math.sqrt( (enemy.getTranslateX() - tower.getTranslateX()) * (enemy.getTranslateX() - tower.getTranslateX())
                + ( enemy.getTranslateY() - tower.getTranslateY()) * ( enemy.getTranslateY() - tower.getTranslateY())) ;
    }
}