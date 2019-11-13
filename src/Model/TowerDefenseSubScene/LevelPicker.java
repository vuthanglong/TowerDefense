package Model.TowerDefenseSubScene;

import View.LEVEL;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class LevelPicker extends VBox {
    private ImageView circleImage;
    private ImageView levelImage;

    private String circleNotChosen = "Model/Images/emptycircle.png";
    private String circleChosen = "Model/Images/filledcircle.png";

    private LEVEL level;

    private boolean isCircleChosen;

    public LevelPicker(LEVEL level) {
        circleImage = new ImageView(circleNotChosen);
        levelImage = new ImageView(level.getUrl());
        this.level = level;
        isCircleChosen = false;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(circleImage);
        this.getChildren().add(levelImage);
    }

    public LEVEL getLevel() {
        return level;
    }

    public boolean getIsCircleChosen() {
            return isCircleChosen;
    }

    public void setIsCircleChosen(boolean isCircleChosen) {
        this.isCircleChosen = isCircleChosen;
        String imageToSet = this.isCircleChosen ? circleChosen : circleNotChosen;
        circleImage.setImage(new Image(imageToSet));
    }
}
