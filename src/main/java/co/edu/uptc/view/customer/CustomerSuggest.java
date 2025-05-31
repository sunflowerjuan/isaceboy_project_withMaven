package co.edu.uptc.view.customer;

import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class CustomerSuggest extends HBox {
    private TextField searchField;
    private VBox listBox;
    private TextField idField, nameField, lastNameField, addressField, emailField, phoneField;
    private Button editButton, deleteButton, updateButton;

    private Presenter presenter;
    private Stage stage;

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
        nameField.setEditable(false);
        lastNameField = createTextField();
        lastNameField.setEditable(false);
        addressField = createTextField();
        addressField.setEditable(false);
        emailField = createTextField();
        emailField.setEditable(false);
        phoneField = createTextField();
        phoneField.setEditable(false);

        editButton = new Button("Editar");
        deleteButton = new Button("Eliminar");
        updateButton = new Button("Actualizar");
        updateButton.setDisable(true);

        ViewStyles.buttonStyle(editButton, 200, 50);
        ViewStyles.closeBtnStyle(deleteButton);
        ViewStyles.buttonStyle(updateButton, 200, 50);
    }

    private void setupLayout() {
        Label titleRight = new Label("Búsqueda de usuarios");
        titleRight.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label searchLabel = new Label("Buscar por cédula:");
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
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.TOP_CENTER);

        int row = 0;
        formGrid.add(fieldBox("Cédula:", idField), 0, row);
        formGrid.add(fieldBox("Nombre:", nameField), 1, row++);

        formGrid.add(fieldBox("Apellido:", lastNameField), 0, row);
        formGrid.add(fieldBox("Dirección:", addressField), 1, row++);

        formGrid.add(fieldBox("Email:", emailField), 0, row);
        formGrid.add(fieldBox("Teléfono:", phoneField), 1, row++);

        HBox buttonBox = new HBox(50, editButton, deleteButton, updateButton);
        buttonBox.setPadding(new Insets(100, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);

        VBox leftPane = new VBox(15, title, formGrid, buttonBox);
        leftPane.setAlignment(Pos.TOP_CENTER);
        leftPane.setPrefWidth(750);

        HBox.setHgrow(leftPane, Priority.ALWAYS);

        this.getChildren().addAll(leftPane, searchPane);
        this.setSpacing(10);
    }

    private VBox fieldBox(String labelText, TextField field) {
        Label label = new Label(labelText);
        ViewStyles.formLabelStyle(label);
        return new VBox(5, label, field);
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

        editButton.setOnAction(e -> {
            if (areFieldsFilled()) {
                setEditableFields(true);
                updateButton.setDisable(false);
            } else {
                DialogMessage.showWarningDialog(stage, "CAMPOS VACIOS\nDebe seleccionar un usuario antes de editar.");
            }
        });

        updateButton.setOnAction(e -> {
            String msg = presenter.updateCustomer(getCustomerFromForm());
            DialogMessage.showInfoDialog(stage, msg);
            setEditableFields(false);
            clearForm();
        });

        deleteButton.setOnAction(e -> {
            if (areFieldsFilled()) {
                boolean confirm = presenter.deleteCustomer(idField.getText());
                if (confirm) {
                    DialogMessage.showInfoDialog(stage, "Usuario eliminado correctamente.");
                    clearForm();
                } else {
                    DialogMessage.showErrorDialog(stage,
                            "Operacion Fallida.\n Verifique que no tenga reservas asociadas.");
                }
            } else {
                DialogMessage.showErrorDialog(stage, "CAMPOS VACIOS\nDebe seleccionar un usuario antes de eliminarlo.");
            }
        });
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

            Label idLabel = new Label("Cédula: " + customer[0]);
            idLabel.setStyle("-fx-text-fill: #555555;");

            customerCard.getChildren().addAll(nameLabel, idLabel);
            customerCard.setPadding(new Insets(10));
            customerCard.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5px;");

            customerCard.setOnMouseEntered(e -> customerCard.setStyle(
                    "-fx-background-color:" + ViewStyles.THIRD_COLOR
                            + "; -fx-border-color: #999999; -fx-border-radius: 5px; -fx-cursor: hand;"));
            customerCard.setOnMouseExited(e -> customerCard.setStyle(
                    "-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5px;"));

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
        phoneField.setText(customerData[5]);
        setEditableFields(false);
        updateButton.setDisable(true);
    }

    public void clearForm() {
        idField.clear();
        nameField.clear();
        lastNameField.clear();
        addressField.clear();
        emailField.clear();
        phoneField.clear();
        searchField.clear();
        setEditableFields(false);
        updateButton.setDisable(true);
        updateSuggestions("");
    }

    public String[] getCustomerFromForm() {
        String[] customerData = new String[6];
        customerData[0] = idField.getText();
        customerData[1] = nameField.getText().trim();
        customerData[2] = lastNameField.getText().trim();
        customerData[3] = addressField.getText().trim();
        customerData[4] = emailField.getText().trim();
        customerData[5] = phoneField.getText().trim();
        return customerData;
    }
}
