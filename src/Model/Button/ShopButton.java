package Model.Button;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ShopButton extends Button {
    private final String SHOP_BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('Model/Images/shop_button_pressed.png')";
    private final String SHOP_BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('Model/Images/shop_button.png')";

    public ShopButton() {
        setPrefWidth(60);
        setPrefHeight(64);
        setStyle(SHOP_BUTTON_FREE_STYLE);
    }

    private void setButtonPressedStyle() {
        setStyle(SHOP_BUTTON_PRESSED_STYLE);
        setPrefHeight(60);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonFreeStyle() {
        setStyle(SHOP_BUTTON_FREE_STYLE);
        setPrefHeight(64);
        setLayoutY(getLayoutY() - 4);
    }

    private void initializeButtonListeners() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonFreeStyle();
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
    }

}
