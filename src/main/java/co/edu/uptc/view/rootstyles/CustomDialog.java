package co.edu.uptc.view.rootstyles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomDialog {

    private final Stage dialogStage;
    private double xOffset = 0;
    private double yOffset = 0;

    public CustomDialog(Stage owner, String title, String message, ImageView icon, boolean showCancel,
            Runnable onAccept) {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: " + ViewStyles.PRIMARY_COLOR + ";" +
                "-fx-border-color:rgb(0, 0, 0);");

        // Aplicar sombra
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(Color.color(0, 0, 0, 0.4));
        contentBox.setEffect(shadow);

        contentBox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        contentBox.setOnMouseDragged(event -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double newX = event.getScreenX() - xOffset;
            double newY = event.getScreenY() - yOffset;

            if (newX < screenBounds.getMinX())
                newX = screenBounds.getMinX();
            else if (newX + dialogStage.getWidth() > screenBounds.getMaxX())
                newX = screenBounds.getMaxX() - dialogStage.getWidth();

            if (newY < screenBounds.getMinY())
                newY = screenBounds.getMinY();
            else if (newY + dialogStage.getHeight() > screenBounds.getMaxY())
                newY = screenBounds.getMaxY() - dialogStage.getHeight();

            dialogStage.setX(newX);
            dialogStage.setY(newY);
        });

        // Etiqueta
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(360); // <- evita desbordes horizontales
        messageLabel.setMinHeight(100);
        ViewStyles.subtitleStyle(messageLabel);
        messageLabel.setStyle(messageLabel.getStyle() + "-fx-text-fill: white; -fx-font-size: 18px;");

        // Ícono
        if (icon != null) {
            icon.setFitHeight(100);
            icon.setPreserveRatio(true);
        }
        HBox iconBox = new HBox();
        if (icon != null) {
            iconBox.getChildren().add(icon);
            iconBox.setAlignment(Pos.CENTER);
        }

        // Botones
        Button acceptBtn = new Button("Aceptar");
        ViewStyles.buttonStyle(acceptBtn);
        acceptBtn.setOnAction(e -> {
            if (onAccept != null)
                onAccept.run();
            dialogStage.close();
        });

        Button cancelBtn = new Button("Cancelar");
        ViewStyles.buttonStyle(cancelBtn);
        cancelBtn.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(15, acceptBtn);
        if (showCancel)
            buttonBox.getChildren().add(cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Estructura
        if (icon != null)
            contentBox.getChildren().add(iconBox);
        contentBox.getChildren().addAll(messageLabel, buttonBox);

        Scene scene = new Scene(contentBox, 500, 300);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);
    }

    public void show() {
        dialogStage.showAndWait();
    }
}
