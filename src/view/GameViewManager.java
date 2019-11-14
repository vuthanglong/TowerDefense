package View;

import Entity.Enemies.Enemy;

import Entity.Enemies.NormalEnemy;
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
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    private Image BACKGROUND = new Image("Model/Images/backgroundlv1.png");
    private ImageView background = new ImageView(BACKGROUND);

    private Player player = new Player();
    private Label label = new Label();

    private List<Label> towerLabels = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Mountain> mountains = new ArrayList<>();
    private List<Road> roads = new ArrayList<>();
    private List<Tower> towers = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<ImageView> shopButtons = new ArrayList<>();
    private List<ShopButton> buttons = new ArrayList<>();
    private List<Node> upgradeButtons = new ArrayList<>();

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
        updatePlayerInfo();
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
            }
        });
        towerImage.setOnMouseReleased(mouseEvent -> {
            if(player.getGold() >= tower.getTowerCost())
            {
                player.subtractGold(tower.getTowerCost());
                buildTower(mountain, tower);
                hideShop();
                showUpgrade(tower);
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
    private Label labelShowing;

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
            if(player.getGold() >= tower.getTowerCost()) {
                tower.doUpgrade();
                player.subtractGold(tower.getTowerCost());
                gamePane.getChildren().add(updateLabel(tower));
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