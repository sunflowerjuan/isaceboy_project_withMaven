package co.edu.uptc;

import co.edu.uptc.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image("file:src/main/resources/images/IASCEBOY.jpg"));
        MainView mainView = new MainView(primaryStage);
        Scene scene = new Scene(mainView.getRoot(), 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("IASCEBOY - Sistema Gestor de Reservas");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}