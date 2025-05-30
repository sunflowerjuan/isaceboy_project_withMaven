package co.edu.uptc.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WindowControlsPane extends HBox {

    private final Stage stage;

    public WindowControlsPane(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        setSpacing(10);
        setAlignment(Pos.CENTER_RIGHT);
        setStyle("-fx-background-color:" + ViewStyles.SECONDARY_COLOR + "; -fx-padding: 5;");

        Button btnMinimize = createButton("\u2013", "Minimizar");
        Button btnMaximize = createButton("\u25A1", "Maximizar / Restaurar");
        Button btnClose = ViewStyles.windowsButton("\u2715", true);
        Tooltip.install(btnClose, new Tooltip("Cerrar"));

        btnMinimize.setOnAction(e -> stage.setIconified(true));
        btnMaximize.setOnAction(e -> stage.setMaximized(!stage.isMaximized()));
        btnClose.setOnAction(e -> stage.close());

        getChildren().addAll(btnMinimize, btnMaximize, btnClose);
    }

    private Button createButton(String text, String tooltipText) {
        Button button = ViewStyles.windowsButton(text, false);
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setShowDelay(Duration.millis(200)); // aparece rápido
        Tooltip.install(button, tooltip);
        return button;
    }

}
