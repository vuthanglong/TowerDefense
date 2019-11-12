package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.MenuButton;

public class ViewManager {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 900;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane,WIDTH,HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createButtons("START",350,350);
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void createButtons(String text, int x, int y) {
        MenuButton button = new MenuButton(text);
        button.setLayoutY(y);
        button.setLayoutX(x);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameViewManager gameManager = new GameViewManager();
                gameManager.createNewGame(mainStage);
            }
        });
        mainPane.getChildren().add(button);
    }
}