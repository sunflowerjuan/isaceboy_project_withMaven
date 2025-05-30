package co.edu.uptc;

import co.edu.uptc.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getRoot(), 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED); // Sin bordes
        primaryStage.setMaximized(true); // Pantalla completa
        primaryStage.setTitle("IASCEBOY - Sistema Gestor de Reservas");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}