package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.MenuButton;

public class ViewManager {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 900;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane,WIDTH,HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        createButtons("Start");
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void createButtons(String text) {
        MenuButton button = new MenuButton(text);
        button.setLayoutY(350);
        button.setLayoutX(350);
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