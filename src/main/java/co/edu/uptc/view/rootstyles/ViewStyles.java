package co.edu.uptc.view.rootstyles;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        public static final String BLACK_COLOR = "rgb(0, 0, 0)";
        public static final String ALERT_COLOR = "rgb(139, 70, 62)";
        public static final String ALERT_COLOR_HOVER = "rgb(61, 27, 23)";
        public static final String ERROR_COLOR = "#d9534f";
        public static final String ENABLE_COLOR = "#4a90e2";

        // Títulos
        public static void titleStyle(Label label) {
                label.setStyle("-fx-text-fill: " + LIGTH_TEXT + ";"
                                + " -fx-font-size: 38px;"
                                + " -fx-font-weight: bold;"
                                + " -fx-font-family: 'Segoe UI';");
        }

        public static void titleStyle(Label label, int mode) {
                if (mode == 0) {
                        label.setStyle("-fx-text-fill: " + LIGTH_TEXT + ";"
                                        + " -fx-font-size: 38px;"
                                        + " -fx-font-weight: bold;"
                                        + " -fx-font-family: 'Segoe UI';");
                } else {
                        label.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";"
                                        + " -fx-font-size: 38px;"
                                        + " -fx-font-weight: bold;"
                                        + " -fx-font-family: 'Segoe UI';");
                }
        }

        // Subtítulos
        public static void subtitleStyle(Label label) {
                label.setStyle("-fx-text-fill: " + LIGTH_TEXT + ";"
                                + " -fx-font-size: 14px;"
                                + " -fx-font-weight: bold;");
        }

        public static void labelTextStyle(Label label) {
                label.setStyle("-fx-text-fill: " + ViewStyles.BLACK_COLOR + ";" + " -fx-font-size: 18px;");
        }

        // Labels generales (formularios)
        public static void formLabelStyle(Label label) {
                label.setStyle("-fx-text-fill: " + PRIMARY_COLOR + ";"
                                + " -fx-font-size: 20px;"
                                + " -fx-font-weight: bold;");
        }

        public static void formSubLabelStyle(Label label) {
                label.setStyle("-fx-text-fill: " + BLACK_COLOR + ";"
                                + " -fx-font-size: 14px;");
        }

        // Estilo para campos de texto
        public static void textFieldStyle(TextField field) {
                field.setStyle("-fx-background-color: fafafa;"
                                + " -fx-border-color: " + ENABLE_COLOR + ";"
                                + " -fx-border-radius: 5;"
                                + " -fx-background-radius: 5;"
                                + " -fx-padding: 5;"
                                + " -fx-font-size: 18px;");
        }

        // Estilo para campos de texto
        public static void textFieldNoEdStyle(TextField field) {
                field.setStyle("-fx-background-color: fcfcfc;"
                                + " -fx-border-color: " + PRIMARY_COLOR + ";"
                                + " -fx-border-radius: 5;"
                                + " -fx-background-radius: 5;"
                                + " -fx-padding: 5;"
                                + " -fx-font-size: 18px;");
        }

        // Estilo para mensajes de error debajo del campo
        public static void errorLabelStyle(Label label) {
                label.setStyle("-fx-text-fill: " + ERROR_COLOR + ";"
                                + " -fx-font-size: 12px;"
                                + " -fx-font-style: italic;");
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

                button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + THIRD_COLOR + ";"
                                + " -fx-text-fill: " + PRIMARY_COLOR + ";"
                                + " -fx-font-size: 15px;"
                                + " -fx-font-weight: bold;"
                                + " -fx-background-radius: 10;"));
                button.setOnMouseExited(e -> buttonStyle(button));
                button.setCursor(javafx.scene.Cursor.HAND);
        }

        public static void buttonStyle(Button button, int widthValue, int heightValue) {
                button.setStyle("-fx-background-color: " + SECONDARY_COLOR + ";"
                                + " -fx-text-fill: " + LIGTH_TEXT + ";"
                                + " -fx-font-size: 15px;"
                                + " -fx-font-weight: bold;"
                                + " -fx-background-radius: 10;");
                button.setPrefHeight(heightValue);
                button.setPrefWidth(widthValue);

                button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + THIRD_COLOR + ";"
                                + " -fx-text-fill: " + PRIMARY_COLOR + ";"
                                + " -fx-font-size: 15px;"
                                + " -fx-font-weight: bold;"
                                + " -fx-background-radius: 10;"));
                button.setOnMouseExited(e -> buttonStyle(button, widthValue, heightValue));
                button.setCursor(javafx.scene.Cursor.HAND);
        }

        public static void buttonStyle(Button button, int height) {
                button.setStyle("-fx-background-color: " + SECONDARY_COLOR + ";"
                                + " -fx-text-fill: " + LIGTH_TEXT + ";"
                                + " -fx-font-size: 15px;"
                                + " -fx-font-weight: bold;"
                                + " -fx-background-radius: 10;");
                button.setPrefHeight(height);
                button.setMaxWidth(Double.MAX_VALUE);

                button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + THIRD_COLOR + ";"
                                + " -fx-text-fill: " + PRIMARY_COLOR + ";"
                                + " -fx-font-size: 15px;"
                                + " -fx-font-weight: bold;"
                                + " -fx-background-radius: 10;"));
                button.setOnMouseExited(e -> buttonStyle(button, height));
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
                                        "-fx-background-color: " + WITHE_COLOR + ";"
                                                        + "-fx-padding: 10 20 10 30;"
                                                        + "-fx-spacing: 10;"
                                                        + "-fx-alignment: CENTER_LEFT;");
                }
                pane.setExpanded(false);
        }

        public static Button windowsButton(String text, boolean isCloseButton) {
                Button button = new Button(text);
                String baseStyle = "-fx-background-color: transparent; -fx-text-fill: "
                                + (isCloseButton ? "red" : "white")
                                + "; -fx-font-size: 14px; -fx-cursor: hand;";
                button.setStyle(baseStyle);

                button.setOnMouseEntered(e -> button.setStyle(
                                "-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: "
                                                + (isCloseButton ? "white" : "white")
                                                + "; -fx-font-size: 14px; -fx-cursor: hand;"));

                button.setOnMouseExited(e -> button.setStyle(baseStyle));

                return button;
        }

        public static void comboStyle(ComboBox<?> comboBox) {
                comboBox.setStyle("-fx-background-color: " + WITHE_COLOR + ";"
                                + "-fx-border-color: " + ENABLE_COLOR + ";"
                                + "-fx-border-radius: 5;"
                                + "-fx-background-radius: 5;"
                                + "-fx-padding: 5;"
                                + "-fx-font-size: 16px;"
                                + "-fx-text-fill: " + BLACK_COLOR + ";");

                comboBox.setOnMouseEntered(e -> comboBox.setStyle("-fx-background-color: " + HOVER_COLOR + ";"
                                + "-fx-border-color: " + ENABLE_COLOR + ";"
                                + "-fx-border-radius: 5;"
                                + "-fx-background-radius: 5;"
                                + "-fx-padding: 5;"
                                + "-fx-font-size: 16px;"
                                + "-fx-text-fill: " + BLACK_COLOR + ";"));

                comboBox.setOnMouseExited(e -> comboStyle(comboBox));
        }
}
