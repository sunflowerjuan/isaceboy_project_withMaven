// CustomerSuggest.java

package co.edu.uptc.view.customer;

import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerSuggest extends HBox {
    private TextField searchField;
    private VBox listBox;
    private TextField idField, nameField, lastNameField, addressField, emailField, phoneField;
    private Label nameError, lastNameError, emailError, phoneError, addresError;
    private Button editButton, deleteButton, updateButton;
    private Presenter presenter;
    private Stage stage;
    private final Map<TextField, Label> validationMap = new HashMap<>();

    public CustomerSuggest(Presenter presenter, Stage stage) {
        this.presenter = presenter;
        this.stage = stage;
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        searchField = createTextField();
        listBox = new VBox(10);
        listBox.setPadding(new Insets(10));
        listBox.setStyle("-fx-background-color: #f0f0f0;");
        listBox.setPrefWidth(300);

        idField = createTextField();
        idField.setEditable(false);

        nameField = createTextField();
        lastNameField = createTextField();
        addressField = createTextField();
        emailField = createTextField();
        phoneField = createTextField();

        nameError = createErrorLabel();
        lastNameError = createErrorLabel();
        addresError = createErrorLabel();
        emailError = createErrorLabel();
        phoneError = createErrorLabel();

        validationMap.put(nameField, nameError);
        validationMap.put(lastNameField, lastNameError);
        validationMap.put(emailField, emailError);
        validationMap.put(phoneField, phoneError);
        validationMap.put(addressField, addresError);

        editButton = new Button("Editar");
        deleteButton = new Button("Eliminar");
        updateButton = new Button("Actualizar");
        updateButton.setDisable(true);

        ViewStyles.buttonStyle(editButton, 200, 50);
        ViewStyles.closeBtnStyle(deleteButton);
        ViewStyles.buttonStyle(updateButton, 200, 50);
    }

    private Label createErrorLabel() {
        Label label = new Label();
        ViewStyles.errorLabelStyle(label);
        label.setVisible(false);
        return label;
    }

    private void setupLayout() {
        Label titleRight = new Label("Búsqueda de usuarios");
        titleRight.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label searchLabel = new Label("Buscar por Documento:");
        searchLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox searchBox = new VBox(5, searchLabel, searchField);
        searchBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(listBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPrefHeight(650);

        VBox searchPane = new VBox(15, titleRight, searchBox, scrollPane);
        searchPane.setPadding(new Insets(15));
        searchPane.setPrefWidth(300);
        searchPane.setStyle("-fx-background-color: #e8e8e8;");

        VBox mainForm = new VBox(20);
        mainForm.setPadding(new Insets(20));
        mainForm.setStyle("-fx-background-color:" + ViewStyles.WITHE_COLOR + ";");

        Label title = new Label("GESTION DE HUESPEDES");
        ViewStyles.titleStyle(title, 1);
        title.setPadding(new Insets(30, 0, 125, 0));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(100);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.TOP_CENTER);

        int row = 0;
        formGrid.add(fieldBox("Documento de Identidad:", idField, null), 0, row);
        formGrid.add(fieldBox("Nombre:", nameField, nameError), 1, row++);

        formGrid.add(fieldBox("Apellido:", lastNameField, lastNameError), 0, row);
        formGrid.add(fieldBox("Dirección:", addressField, addresError), 1, row++);

        formGrid.add(fieldBox("Email:", emailField, emailError), 0, row);
        formGrid.add(fieldBox("Teléfono:", phoneField, phoneError), 1, row++);

        HBox buttonBox = new HBox(50, editButton, deleteButton, updateButton);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);

        Label shortcuts = new Label("Atajos: E = Editar | D = Eliminar | Enter = Actualizar");
        ViewStyles.formLabelStyle(shortcuts);
        VBox leftPane = new VBox(15, title, formGrid, buttonBox, shortcuts);
        leftPane.setAlignment(Pos.TOP_CENTER);
        leftPane.setPrefWidth(750);

        HBox.setHgrow(leftPane, Priority.ALWAYS);
        this.getChildren().addAll(leftPane, searchPane);
        this.setSpacing(10);
    }

    private VBox fieldBox(String labelText, TextField field, Label errorLabel) {
        Label label = new Label(labelText);
        ViewStyles.formLabelStyle(label);
        VBox box = new VBox(2, label, field);
        if (errorLabel != null) {
            box.getChildren().add(errorLabel);
        }
        return box;
    }

    private TextField createTextField() {
        TextField field = new TextField();
        ViewStyles.textFieldNoEdStyle(field);
        field.setPrefWidth(300);
        field.setPrefHeight(35);
        return field;
    }

    private void setupListeners() {
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                searchField.setText(newText.replaceAll("[^\\d]", ""));
            }
        });

        searchField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            String input = searchField.getText().trim();
            updateSuggestions(input);
        });

        nameField.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            if (!e.getCharacter().matches("[a-zA-ZáéíóúÁÉÍÓÚ\\s]"))
                e.consume();
        });

        lastNameField.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            if (!e.getCharacter().matches("[a-zA-ZáéíóúÁÉÍÓÚ\\s]"))
                e.consume();
        });

        // TELÉFONO: solo 10 dígitos + formato en vivo
        phoneField.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            String character = e.getCharacter();
            String currentText = phoneField.getText().replaceAll("\\D", "");
            if (!character.matches("\\d") || currentText.length() >= 10) {
                e.consume();
            }
        });

        phoneField.textProperty().addListener((obs, oldText, newText) -> {
            String digitsOnly = newText.replaceAll("\\D", "");
            if (digitsOnly.length() > 10) {
                digitsOnly = digitsOnly.substring(0, 10);
            }

            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < digitsOnly.length(); i++) {
                if (i == 3 || i == 6 || i == 8) {
                    formatted.append(" ");
                }
                formatted.append(digitsOnly.charAt(i));
            }

            if (!phoneField.getText().equals(formatted.toString())) {
                phoneField.setText(formatted.toString());
                phoneField.positionCaret(formatted.length());
            }

            validateField(phoneField); // actualiza errores en vivo
        });

        // Validación en vivo
        for (TextField field : validationMap.keySet()) {
            field.textProperty().addListener((obs, oldVal, newVal) -> validateField(field));
        }

        editButton.setOnAction(e -> {
            if (areFieldsFilled()) {
                setEditableFields(true);
                updateButton.setDisable(false);
            } else {
                DialogMessage.showWarningDialog(stage, "CAMPOS VACIOS\nDebe seleccionar un usuario antes de editar.");
            }
        });

        updateButton.setOnAction(e -> {
            if (validateAll()) {
                String msg = presenter.updateCustomer(getCustomerFromForm());
                DialogMessage.showInfoDialog(stage, msg);
                setEditableFields(false);
                clearForm();
            } else {
                DialogMessage.showErrorDialog(stage, "Error de validación\nVerifique los campos marcados en rojo.");
            }
        });

        deleteButton.setOnAction(e -> {
            if (areFieldsFilled()) {
                DialogMessage.showConfirmDialog(stage, "¿Esta seguro que desea eliminar el huesped actual?", () -> {
                    boolean confirm = presenter.deleteCustomer(idField.getText());
                    if (confirm) {
                        DialogMessage.showInfoDialog(stage, "Usuario eliminado correctamente.");
                        clearForm();
                    } else {
                        DialogMessage.showErrorDialog(stage,
                                "Operacion Fallida.\n Verifique que no tenga reservas asociadas.");
                    }
                });
            } else {
                DialogMessage.showErrorDialog(stage, "CAMPOS VACIOS\nDebe seleccionar un usuario antes de eliminarlo.");
            }
        });

        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.E)
                editButton.fire();
            if (e.getCode() == KeyCode.D)
                deleteButton.fire();
            if (e.getCode() == KeyCode.ENTER)
                updateButton.fire();
        });
    }

    private boolean validateAll() {
        boolean valid = true;
        for (TextField field : validationMap.keySet()) {
            if (!validateField(field))
                valid = false;
        }
        return valid;
    }

    private boolean validateField(TextField field) {
        Label errorLabel = validationMap.get(field);
        String value = field.getText().trim();
        boolean isValid = !value.isEmpty();

        if (field == emailField) {
            isValid = value.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            if (!isValid)
                errorLabel.setText("Correo inválido");
        } else if (field == phoneField) {
            String digits = value.replaceAll("\\D", "");
            isValid = digits.matches("\\d{10}");
            if (!isValid)
                errorLabel.setText("El teléfono debe tener 10 dígitos");
        } else if (!isValid) {
            errorLabel.setText("Campo obligatorio");
        }

        errorLabel.setVisible(!isValid);
        return isValid;
    }

    private boolean areFieldsFilled() {
        return !nameField.getText().isBlank() &&
                !lastNameField.getText().isBlank() &&
                !addressField.getText().isBlank() &&
                !emailField.getText().isBlank() &&
                !phoneField.getText().isBlank();
    }

    private void setEditableFields(boolean value) {
        nameField.setEditable(value);
        lastNameField.setEditable(value);
        addressField.setEditable(value);
        emailField.setEditable(value);
        phoneField.setEditable(value);

        if (value) {
            ViewStyles.textFieldStyle(nameField);
            ViewStyles.textFieldStyle(lastNameField);
            ViewStyles.textFieldStyle(addressField);
            ViewStyles.textFieldStyle(emailField);
            ViewStyles.textFieldStyle(phoneField);
        } else {
            ViewStyles.textFieldNoEdStyle(nameField);
            ViewStyles.textFieldNoEdStyle(lastNameField);
            ViewStyles.textFieldNoEdStyle(addressField);
            ViewStyles.textFieldNoEdStyle(emailField);
            ViewStyles.textFieldNoEdStyle(phoneField);
        }
    }

    private void updateSuggestions(String query) {
        listBox.getChildren().clear();
        List<String[]> results = query.isBlank()
                ? presenter.getAllCustomers()
                : presenter.findCustomersByIdPartial(query);

        for (String[] customer : results) {
            VBox customerCard = new VBox(2);
            Label nameLabel = new Label(customer[1] + " " + customer[2]);
            nameLabel.setStyle(
                    "-fx-font-weight: bold; -fx-font-size: 14px;-fx-text-fill: " + ViewStyles.PRIMARY_COLOR + ";");

            Label idLabel = new Label("Documento de Identidad: " + customer[0]);
            idLabel.setStyle("-fx-text-fill: #555555;");

            customerCard.getChildren().addAll(nameLabel, idLabel);
            customerCard.setPadding(new Insets(10));
            customerCard.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5px;");

            customerCard.setOnMouseEntered(e -> customerCard.setStyle("-fx-background-color:" + ViewStyles.THIRD_COLOR
                    + "; -fx-border-color: #999999; -fx-border-radius: 5px; -fx-cursor: hand;"));
            customerCard.setOnMouseExited(e -> customerCard
                    .setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5px;"));
            customerCard.setOnMouseClicked(e -> fillForm(customer));

            listBox.getChildren().add(customerCard);
        }
    }

    private void fillForm(String[] customerData) {
        idField.setText(customerData[0]);
        nameField.setText(customerData[1]);
        lastNameField.setText(customerData[2]);
        addressField.setText(customerData[3]);
        emailField.setText(customerData[4]);
        phoneField.setText(formatPhone(customerData[5]));
        setEditableFields(false);
        updateButton.setDisable(true);
    }

    private String formatPhone(String raw) {
        String digits = raw.replaceAll("\\D", "");
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i == 3 || i == 6 || i == 8) {
                formatted.append(" ");
            }
            formatted.append(digits.charAt(i));
        }
        return formatted.toString();
    }

    public void clearForm() {
        idField.clear();
        nameField.clear();
        lastNameField.clear();
        addressField.clear();
        emailField.clear();
        phoneField.clear();
        searchField.clear();

        // Ocultar todos los errores de validación
        for (Label errorLabel : validationMap.values()) {
            errorLabel.setVisible(false);
        }

        setEditableFields(false);
        updateButton.setDisable(true);
        updateSuggestions("");
    }

    public String[] getCustomerFromForm() {
        return new String[] {
                idField.getText(),
                nameField.getText().trim(),
                lastNameField.getText().trim(),
                addressField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().replaceAll("\\D", "") // guarda sin espacios
        };
    }
}
