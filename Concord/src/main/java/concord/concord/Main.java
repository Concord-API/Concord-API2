package concord.concord;

import concord.concord.controllers.ControllerCadAula;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private ControllerCadAula controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/concord/concord/hello-view.fxml"));
            Parent root = loader.load();
            controller = loader.getController();

            primaryStage.setTitle("Gerenciamento de Aulas");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}