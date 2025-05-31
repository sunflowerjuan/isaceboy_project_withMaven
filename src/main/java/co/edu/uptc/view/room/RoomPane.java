package co.edu.uptc.view.room;

import co.edu.uptc.model.RoomType;
import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RoomPane extends VBox {
    private ComboBox<RoomType> roomTypeCombo;
    private TextField roomCountField;
    private TextField priceField;
    private Button updateButton;
    private Label priceError;

    private Presenter presenter;
    private Stage stage;

    private String previousPrice = "";

    public RoomPane(Presenter presenter, Stage stage) {
        this.presenter = presenter;
        this.stage = stage;
        initComponents();
        setupPane();
        createLayout();
    }

    private void setupPane() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color:" + ViewStyles.WITHE_COLOR + ";");
        setAlignment(Pos.TOP_CENTER);
    }

    private void createLayout() {
        Label title = new Label("HABITACIONES");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        title.setPadding(new Insets(30, 10, 100, 20));
        ViewStyles.titleStyle(title, 1);

        GridPane grid = new GridPane();
        grid.setHgap(200);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        int row = 0;
        grid.add(createComboWithLabel("Tipo de habitación:", roomTypeCombo), 0, row++);
        grid.add(createFieldWithLabel("Cantidad:", roomCountField), 0, row++);
        grid.add(createFieldWithError("Precio por Día:", priceField, priceError = new Label()), 0, row++);

        VBox container = new VBox(20);
        container.setAlignment(Pos.TOP_CENTER);
        container.getChildren().addAll(title, grid, updateButton);
        getChildren().add(container);
    }

    private void initComponents() {
        createRoomTypeCombo();
        createRoomCountField();
        createPriceField();
        createUpdateButton();
    }

    private void createRoomTypeCombo() {
        roomTypeCombo = new ComboBox<>();
        roomTypeCombo.setPrefWidth(350);
        roomTypeCombo.setPrefHeight(40);
        roomTypeCombo.setPromptText("Seleccione tipo...");
        ViewStyles.comboStyle(roomTypeCombo);

        // Cargar los valores del enum desde el presentador
        roomTypeCombo.getItems().addAll(presenter.getRoomTypes());

        roomTypeCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadRoomInfo(newVal);
            }
        });
    }

    private void createRoomCountField() {
        roomCountField = new TextField();
        roomCountField.setEditable(false);
        roomCountField.setPrefWidth(350);
        roomCountField.setPrefHeight(40);
        ViewStyles.textFieldStyle(roomCountField);
    }

    private void createPriceField() {
        priceField = new TextField();
        ViewStyles.textFieldStyle(priceField);
        priceField.setPrefWidth(350);
        priceField.setPrefHeight(40);

        priceField.textProperty().addListener((obs, oldVal, newVal) -> {
            String digits = newVal.replaceAll("[^\\d]", "");
            if (digits.isEmpty()) {
                priceField.setText("");
                return;
            }
            priceField.setText(formatCurrency(digits));
            priceError.setVisible(false);
            updateButton.setDisable(priceField.getText().equals(previousPrice));
        });

        priceField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal)
                validatePrice();
        });
    }

    private void createUpdateButton() {
        updateButton = new Button("Actualizar Precio");
        updateButton.setDisable(true);
        ViewStyles.buttonStyle(updateButton, 650, 60);
        updateButton.setOnAction(e -> {
            if (roomCountField.getText().isEmpty()) {
                DialogMessage.showWarningDialog(stage,
                        "Debe seleccionar un tipo de habitación con información válida.");
                clean();
                return;
            }

            if (validatePrice()) {
                String strPrice = priceField.getText()
                        .replaceAll("\\$", "")
                        .replaceAll("\\.", "")
                        .trim();
                presenter.updateRoomPrice(strPrice, roomTypeCombo.getValue());
                DialogMessage.showSuccessDialog(stage, "Precio actualizado correctamente");
                previousPrice = priceField.getText();
                updateButton.setDisable(true);
            } else {
                DialogMessage.showWarningDialog(stage, "Ingrese un precio válido");
                clean();
            }
        });

    }

    private boolean validatePrice() {
        boolean valid = priceField.getText().matches("^\\$\\d{1,3}(\\.\\d{3})*(\\,\\d{2})?$|^\\$\\d+$");
        priceError.setVisible(!valid);
        priceError.setText(valid ? "" : "Precio inválido");
        return valid;
    }

    private VBox createComboWithLabel(String labelText, ComboBox<?> combo) {
        Label label = new Label(labelText);
        ViewStyles.formLabelStyle(label);
        VBox box = new VBox(5);
        box.getChildren().addAll(label, combo);
        return box;
    }

    private VBox createFieldWithLabel(String labelText, TextField field) {
        Label label = new Label(labelText);
        ViewStyles.formLabelStyle(label);
        VBox box = new VBox(5);
        box.getChildren().addAll(label, field);
        return box;
    }

    private VBox createFieldWithError(String labelText, TextField field, Label errorLabel) {
        Label label = new Label(labelText);
        ViewStyles.formLabelStyle(label);
        VBox box = new VBox(5);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        VBox fieldBox = new VBox(3, field, errorLabel);
        box.getChildren().addAll(label, fieldBox);
        return box;
    }

    private String formatCurrency(String raw) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = raw.length() - 1; i >= 0; i--) {
            sb.insert(0, raw.charAt(i));
            count++;
            if (count % 3 == 0 && i > 0)
                sb.insert(0, ".");
        }
        return "$" + sb;
    }

    private void loadRoomInfo(RoomType roomType) {
        String[] info = presenter.getRoomInfo(roomType);
        if (info != null && info.length == 2) {
            String count = info[0];
            String rawPrice = info[1];

            roomCountField.setText(count);
            String formattedPrice = formatCurrency(rawPrice);
            priceField.setText(formattedPrice);
            previousPrice = formattedPrice;
            updateButton.setDisable(true);
            priceError.setVisible(false);
        } else {
            DialogMessage.showWarningDialog(stage, "No se encontró información para la habitación seleccionada.");
            clean();
        }
    }

    public void clean() {
        priceField.setText("");
    }

}
