package Model.TowerDefenseSubScene;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class TowerDefenseSubScene extends SubScene {

    private final static String FONT_PATH = "src/Model/Button/Font/SF Atarian System Bold.ttf";
    private final static String BACKGROUND_IMAGE = "Model/Images/subscene.png";

    private boolean isHidden;

    public TowerDefenseSubScene() {
        super(new AnchorPane(), 500, 400);
        prefWidth(500);
        prefHeight(400);
        AnchorPane root = (AnchorPane) this.getRoot();

        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, 500, 400, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        root.setBackground(new Background(image));

        isHidden = true;
        setLayoutX(900);
        setLayoutY(600-80-400);
    }
    public void moveSubScene() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);
        if(isHidden) {
            transition.setToX(-500-80);
            isHidden = false;
        } else {
            transition.setToX(0);
            isHidden = true;
        }
        transition.play();
    }

    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }
}
