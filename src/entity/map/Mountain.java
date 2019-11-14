package Entity.Map;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Mountain extends ImageView {

    public Mountain() {
        super(new Image("Model/Images/mountain.png"));
        initializeTowerListeners();
    }

    private void initializeTowerListeners() {
        setOnMousePressed(mouseEvent -> {

        });

        setOnMouseEntered(mouseEvent -> setEffect(new DropShadow()));

        setOnMouseExited(mouseEvent -> setEffect(null));

        setOnMouseReleased(mouseEvent -> {

        });
    }


}
