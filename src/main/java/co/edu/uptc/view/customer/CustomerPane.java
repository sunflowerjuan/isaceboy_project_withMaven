package co.edu.uptc.view.customer;

import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CustomerPane extends VBox {
    private TextField idField, nameField, lastNameField, addressField, emailField, phoneField;
    private Label idError, nameError, lastNameError, addressError, emailError, phoneError;
    private Button saveButton;

    private Stage stage;
    private Presenter presenter;

    public CustomerPane(Presenter presenter, Stage stage) {
        this.presenter = presenter;
        this.stage = stage;
        initComponets();
        setupPane();
        createLayout();
    }

    public void setupPane() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color:" + ViewStyles.WITHE_COLOR + ";");

    }

    public void createLayout() {
        Label title = new Label("REGISTRAR HUESPED");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        title.setPadding(new Insets(30, 10, 125, 20));
        ViewStyles.titleStyle(title, 1);
        GridPane formGrid = new GridPane();
        formGrid.setHgap(200);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.BOTTOM_CENTER);

        int row = 0;
        formGrid.add(createFieldWithError("Cédula:", idField, idError = new Label()), 0, row);
        formGrid.add(createFieldWithError("Nombre:", nameField, nameError = new Label()), 1, row++);

        formGrid.add(createFieldWithError("Apellido:", lastNameField, lastNameError = new Label()), 0, row);
        formGrid.add(createFieldWithError("Dirección:", addressField, addressError = new Label()), 1, row++);

        formGrid.add(createFieldWithError("Email:", emailField, emailError = new Label()), 0, row);
        formGrid.add(createFieldWithError("Teléfono:", phoneField, phoneError = new Label()), 1, row++);

        createSaveBtn();

        spacer(formGrid, title);

    }

    public void initComponets() {
        idField();
        nameField();
        lastNameField();
        addressField();
        emailField();
        phoneField();
    }

    public void spacer(GridPane formGrid, Label title) {
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.getChildren().addAll(title, formGrid);
        VBox.setVgrow(formGrid, Priority.ALWAYS);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        getChildren().addAll(contentBox, spacer, saveButton);
        setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(this, Priority.ALWAYS);

    }

    public void createSaveBtn() {
        saveButton = new Button("Guardar");
        ViewStyles.buttonStyle(saveButton, 650, 60);
        saveButton.setOnAction(e -> {
            if (validateFields()) {
                if (presenter.createCustomer(getCustomerFromForm())) {
                    DialogMessage.showSuccessDialog(stage, "Huesped Agregado correctamente");
                } else {
                    DialogMessage.showErrorDialog(stage, "Operación no completada. \nVerifique los datos.");
                }
                clearForm();
            } else {
                DialogMessage.showWarningDialog(stage, "RELLENE TODOS LOS CAMPOS PRIMERO");
            }
        });
    }

    public void idField() {
        idField = createTextField();
        idField.textProperty().addListener((obs, oldVal, newVal) -> {
            newVal = newVal.replaceAll("[^\\d]", "");
            idField.setText(formatId(newVal));
            if (!newVal.isBlank())
                idError.setVisible(false);
        });
    }

    public void nameField() {
        nameField = createTextField();
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            newVal = newVal.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", "");
            nameField.setText(newVal);
            if (!newVal.isBlank())
                nameError.setVisible(false);
        });
    }

    public void lastNameField() {
        lastNameField = createTextField();
        lastNameField.textProperty().addListener((obs, oldVal, newVal) -> {
            newVal = newVal.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", "");
            lastNameField.setText(newVal);
            if (!newVal.isBlank())
                lastNameError.setVisible(false);
        });
    }

    public void addressField() {
        addressField = createTextField();
        addressField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank())
                addressError.setVisible(false);
        });
    }

    public void emailField() {
        emailField = createTextField();

        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal)
                validateEmail();
        });

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                emailError.setVisible(false);
            }
        });
    }

    public void phoneField() {
        phoneField = createTextField();
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            newVal = newVal.replaceAll("[^\\d]", "");
            if (newVal.length() > 10)
                newVal = newVal.substring(0, 10);
            phoneField.setText(formatPhone(newVal));
            if (newVal.length() == 10)
                phoneError.setVisible(false);
        });
    }

    private TextField createTextField() {
        TextField field = new TextField();
        ViewStyles.textFieldStyle(field);
        field.setPrefWidth(350);
        field.setPrefHeight(40);
        return field;
    }

    private VBox createFieldWithError(String labelText, TextField field, Label errorLabel) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        ViewStyles.formLabelStyle(label);

        VBox fieldContainer = new VBox(3);
        fieldContainer.getChildren().addAll(field, errorLabel);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        VBox wrapper = new VBox(5);
        wrapper.getChildren().addAll(label, fieldContainer);
        return wrapper;
    }

    private boolean validateEmail() {
        boolean isValid = emailField.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        emailError.setVisible(!isValid);
        emailError.setText(isValid ? "" : "Email inválido");
        return isValid;
    }

    private boolean validateFields() {
        boolean valid = true;

        if (idField.getText().isBlank()) {
            idError.setText("Campo obligatorio");
            idError.setVisible(true);
            valid = false;
        } else
            idError.setVisible(false);

        if (nameField.getText().isBlank()) {
            nameError.setText("Campo obligatorio");
            nameError.setVisible(true);
            valid = false;
        } else
            nameError.setVisible(false);

        if (lastNameField.getText().isBlank()) {
            lastNameError.setText("Campo obligatorio");
            lastNameError.setVisible(true);
            valid = false;
        } else
            lastNameError.setVisible(false);

        if (addressField.getText().isBlank()) {
            addressError.setText("Campo obligatorio");
            addressError.setVisible(true);
            valid = false;
        } else
            addressError.setVisible(false);

        if (!validateEmail()) {
            valid = false;
        }

        if (phoneField.getText().replaceAll("[^\\d]", "").length() != 10) {
            phoneError.setText("Debe tener 10 dígitos");
            phoneError.setVisible(true);
            valid = false;
        } else
            phoneError.setVisible(false);

        return valid;
    }

    private String formatId(String raw) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            if (i > 0 && (raw.length() - i) % 3 == 0)
                result.append(".");
            result.append(raw.charAt(i));
        }
        return result.toString();
    }

    private String formatPhone(String raw) {
        StringBuilder sb = new StringBuilder();
        int[] groups = { 3, 3, 2, 2 };
        int idx = 0;
        for (int g : groups) {
            if (raw.length() <= idx)
                break;
            int end = Math.min(idx + g, raw.length());
            sb.append(raw, idx, end);
            if (end < raw.length())
                sb.append(" ");
            idx = end;
        }
        return sb.toString();
    }

    public String[] getCustomerFromForm() {
        String[] customerData = new String[6];
        customerData[0] = idField.getText().replaceAll("\\.", "");
        customerData[1] = nameField.getText().trim();
        customerData[2] = lastNameField.getText().trim();
        customerData[3] = addressField.getText().trim();
        customerData[4] = emailField.getText().trim();
        customerData[5] = phoneField.getText().replaceAll("[^\\d]", "");
        return customerData;
    }

    public void clearForm() {
        idField.clear();
        nameField.clear();
        lastNameField.clear();
        addressField.clear();
        emailField.clear();
        phoneField.clear();

        idError.setVisible(false);
        nameError.setVisible(false);
        lastNameError.setVisible(false);
        addressError.setVisible(false);
        emailError.setVisible(false);
        phoneError.setVisible(false);
    }
}
