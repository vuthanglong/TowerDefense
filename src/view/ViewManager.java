package View;

import Model.Button.MenuButton;
import Model.Sound.GameMediaPlayer;
import Model.TowerDefenseSubScene.LevelPicker;
import Model.TowerDefenseSubScene.TowerDefenseSubScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ViewManager {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 900;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    private TowerDefenseSubScene helpSubScene;
    private TowerDefenseSubScene creditSubScene;
    private TowerDefenseSubScene sceneToHide;

    List<LevelPicker> levelList;
    private LEVEL chosenLevel;
    private int lv;

    private TowerDefenseSubScene levelChooserScene;
    private GameMediaPlayer gameMediaPlayer = new GameMediaPlayer();
    private Image BACKGROUND = new Image("Model/Images/back.png");

    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane,WIDTH,HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        ImageView back = new ImageView(BACKGROUND);
        mainPane.getChildren().add(back);
        createStartButtons();
        createCreditButtons();
        createHelpButtons();
        createExitButtons();
        gameMediaPlayer.getMediaPlayer(0).play();
        setSubScene();
    }

    private void setSubScene() {

        helpSubScene = new TowerDefenseSubScene();
        mainPane.getChildren().add(helpSubScene);

        creditSubScene = new TowerDefenseSubScene();
        mainPane.getChildren().add(creditSubScene);

        createLevelChooserSubScene();
    }

    private void createLevelChooserSubScene() {
        levelChooserScene = new TowerDefenseSubScene();
        mainPane.getChildren().add(levelChooserScene);
        MenuButton label = new MenuButton("SELECT LEVEL");
        label.setLayoutX(150);
        label.setLayoutY(15);
        levelChooserScene.getPane().getChildren().add(label);
        levelChooserScene.getPane().getChildren().add(createLevelToChoose());
        levelChooserScene.getPane().getChildren().add(createButtonToStart());
    }

    private HBox createLevelToChoose() {
        HBox box = new HBox();
        box.setSpacing(70);
        levelList = new ArrayList<>();
        for (LEVEL level : LEVEL.values()) {
            LevelPicker levelToPick = new LevelPicker(level);
            levelList.add(levelToPick);
            box.getChildren().add(levelToPick);
            levelToPick.setOnMouseClicked(mouseEvent -> {
                for(LevelPicker level1 : levelList) {
                    level1.setIsCircleChosen(false);
                }
                levelToPick.setIsCircleChosen(true);
                chosenLevel = levelToPick.getLevel();
            });
        }
        box.setLayoutX(70);
        box.setLayoutY(150);
        return box;
    }

    private MenuButton createButtonToStart() {
        MenuButton startButton = new MenuButton("START");
        startButton.setLayoutX(150);
        startButton.setLayoutY(400-15-65);
        startButton.setOnAction(actionEvent -> {
            if(chosenLevel == LEVEL.LEVEL1) lv = 1;
            if(chosenLevel == LEVEL.LEVEL2) lv = 2;
            GameViewManager gameManager = new GameViewManager(lv);
            gameManager.createNewGame(mainStage);
            gameMediaPlayer.getMediaPlayer(0).stop();
        });
        return startButton;
    }

    private void showSubScene(TowerDefenseSubScene subScene) {
        if(sceneToHide != null) {
            sceneToHide.moveSubScene();
        }
            subScene.moveSubScene();
            sceneToHide = subScene;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void createStartButtons() {
        MenuButton button = new MenuButton("START");
        button.setLayoutY(150);
        button.setLayoutX(80);
        button.setOnAction(actionEvent -> showSubScene(levelChooserScene));
        mainPane.getChildren().add(button);
    }
    public void createHelpButtons() {
        MenuButton button = new MenuButton("HELP");
        button.setLayoutY(250);
        button.setLayoutX(80);
        button.setOnAction(actionEvent -> showSubScene(helpSubScene));
        mainPane.getChildren().add(button);
    }
    public void createCreditButtons() {
        MenuButton button = new MenuButton("CREDIT");
        button.setLayoutY(350);
        button.setLayoutX(80);
        button.setOnAction(actionEvent -> showSubScene(creditSubScene));
        mainPane.getChildren().add(button);
    }
    public void createExitButtons() {
        MenuButton button = new MenuButton("EXIT");
        button.setLayoutY(450);
        button.setLayoutX(80);
        button.setOnAction(actionEvent -> mainStage.close());
        mainPane.getChildren().add(button);
    }
}
