package co.edu.uptc.view.rootstyles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        contentBox.setStyle("-fx-background-color: " + ViewStyles.SECONDARY_COLOR + ";" +
                "-fx-border-color:rgb(0, 0, 0);");

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

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(360); // Limita el ancho
        ViewStyles.subtitleStyle(messageLabel);
        messageLabel.setStyle(messageLabel.getStyle() + "-fx-text-fill: white; -fx-font-size: 18px;");

        HBox iconBox = new HBox();
        if (icon != null) {
            icon.setFitHeight(100);
            icon.setPreserveRatio(true);
            iconBox.getChildren().add(icon);
            iconBox.setAlignment(Pos.CENTER);
            contentBox.getChildren().add(iconBox);
        }

        Button acceptBtn = new Button("Aceptar");
        acceptBtn.setMinHeight(40); // Más alto
        acceptBtn.setPadding(new Insets(10, 20, 10, 20));
        ViewStyles.buttonStyleModal(acceptBtn);
        acceptBtn.setOnAction(e -> {
            if (onAccept != null)
                onAccept.run();
            dialogStage.close();
        });

        Button cancelBtn = new Button("Cancelar");
        cancelBtn.setMinHeight(40);
        cancelBtn.setPadding(new Insets(10, 20, 10, 20));
        ViewStyles.buttonStyleModal(cancelBtn);
        cancelBtn.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(15, acceptBtn);
        if (showCancel)
            buttonBox.getChildren().add(cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().addAll(messageLabel, buttonBox);

        Scene scene = new Scene(contentBox);
        scene.setFill(Color.TRANSPARENT);

        // Permitir cerrar con ESC
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                dialogStage.close();
            }
        });

        dialogStage.setScene(scene);
        dialogStage.sizeToScene(); // Ajustar automáticamente el tamaño al contenido
    }

    public void show() {
        dialogStage.showAndWait();
    }
}
