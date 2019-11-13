package Model.Button;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MenuButton extends Button {
    private final String FONT_PATH = "src/Model/Button/Font/SF Atarian System Bold.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('Model/Images/button_pressed.png')";
    private final String BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('Model/Images/button.png')";

    public MenuButton(String text) {
        setText(text);
        setTextFill(Color.WHITE);
        setButtonFont();
        setPrefHeight(70);
        setPrefWidth(183);
        setStyle(BUTTON_FREE_STYLE);
        initializeButtonListeners();
    }

    private void setButtonFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH),23));
        } catch (FileNotFoundException e){
            setFont(Font.font("Verdana",23));
        }
    }

    private void setButtonPressedStyle() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(63);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonFreeStyle() {
        setStyle(BUTTON_FREE_STYLE);
        setPrefHeight(70);
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
