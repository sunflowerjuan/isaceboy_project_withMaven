package co.edu.uptc.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class ViewStyles {
    // Paleta de colores
    public static final String PRIMARY_COLOR = "#213448";
    public static final String SECONDARY_COLOR = "#547792";
    public static final String THIRD_COLOR = "#94B4C1";
    public static final String HOVER_COLOR = "#ECEFCA";
    public static final String LIGTH_TEXT = "white";
    public static final String WITHE_COLOR = "#ebebeb";
    public static final String ALERT_COLOR = "rgb(139, 70, 62)";
    public static final String ALERT_COLOR_HOVER = "rgb(61, 27, 23)";

    // Títulos
    public static void titleStyle(Label label) {
        label.setStyle("-fx-text-fill: " + LIGTH_TEXT + ";"
                + " -fx-font-size: 38px;"
                + " -fx-font-weight: bold;"
                + " -fx-font-family: 'Segoe UI';");
    }

    // Subtítulos
    public static void subtitleStyle(Label label) {
        label.setStyle("-fx-text-fill: " + LIGTH_TEXT + ";"
                + " -fx-font-size: 14px;"
                + " -fx-font-weight: bold;");
    }

    // Botones generales
    public static void buttonStyle(Button button) {
        button.setStyle("-fx-background-color: " + SECONDARY_COLOR + ";"
                + " -fx-text-fill: " + LIGTH_TEXT + ";"
                + " -fx-font-size: 15px;"
                + " -fx-font-weight: bold;"
                + " -fx-background-radius: 10;");
        button.setPrefHeight(40);
        button.setMaxWidth(Double.MAX_VALUE);

        // Hover
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + THIRD_COLOR + ";"
                + " -fx-text-fill: " + PRIMARY_COLOR + ";"
                + " -fx-font-size: 15px;"
                + " -fx-font-weight: bold;"
                + " -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> buttonStyle(button));
        button.setCursor(javafx.scene.Cursor.HAND);
    }

    // Botón especial para cerrar
    public static void closeBtnStyle(Button button) {
        button.setStyle("-fx-background-color: " + ALERT_COLOR + ";"
                + " -fx-text-fill: " + LIGTH_TEXT + ";"
                + " -fx-font-size: 16px;"
                + " -fx-font-weight: bold;"
                + " -fx-background-radius: 10;");
        button.setPrefHeight(50);
        button.setMaxWidth(Double.MAX_VALUE);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + ALERT_COLOR_HOVER + ";"
                + " -fx-text-fill: " + LIGTH_TEXT + ";"
                + " -fx-font-size: 16px;"
                + " -fx-font-weight: bold;"
                + " -fx-background-radius: 10;"));
        button.setOnMouseExited(e -> closeBtnStyle(button));
        button.setCursor(javafx.scene.Cursor.HAND);
    }

    public static void titledPane(TitledPane pane) {
        pane.getStyleClass().add("titled-pane-custom");

        if (pane.getContent() instanceof VBox vbox) {
            vbox.setStyle(
                    "-fx-background-color: " + WITHE_COLOR + ";" +
                            "-fx-padding: 10 20 10 30;" + // top right bottom left: deja espacio a la izquierda
                            "-fx-spacing: 10;" +
                            "-fx-alignment: CENTER_LEFT;"); // Alinea a la izquierda para que no se amontonen a la
                                                            // derecha
        }
        pane.setExpanded(false);
    }
}
