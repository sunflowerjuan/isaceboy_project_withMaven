package co.edu.uptc.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WelcomePane extends VBox {

    public WelcomePane() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(50));

        ImageView logo = new ImageView(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        logo.setFitHeight(400);
        logo.setPreserveRatio(true);

        Label title = new Label("BIENVENIDO");
        title.setStyle("-fx-font-size: 50px; -fx-text-fill: #333; -fx-font-weight: bold;");

        Label subtitle = new Label("Para navegar, use las opciones del menú lateral");
        subtitle.setStyle("-fx-font-size: 20px; -fx-text-fill: #666;");

        this.getChildren().addAll(logo, title, subtitle);
    }
}
