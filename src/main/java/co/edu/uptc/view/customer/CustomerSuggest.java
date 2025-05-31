package co.edu.uptc.view.customer;

import co.edu.uptc.presenter.Presenter;
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
        searchField = createTextField("Buscar por cédula...");
        listBox = new VBox(10);
        listBox.setPadding(new Insets(10));
        listBox.setStyle("-fx-background-color: #f0f0f0;");
        listBox.setPrefWidth(250);

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

        ViewStyles.buttonStyle(editButton, 200, 40);
        ViewStyles.buttonStyle(deleteButton, 200, 40);
        ViewStyles.buttonStyle(updateButton, 200, 40);
    }

    private void setupLayout() {
        VBox mainForm = new VBox(20);
        mainForm.setPadding(new Insets(20));
        mainForm.setStyle("-fx-background-color:" + ViewStyles.WITHE_COLOR + ";");

        Label title = new Label("CONSULTAR HUESPED");
        ViewStyles.titleStyle(title, 1);
        title.setPadding(new Insets(10, 0, 30, 10));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(100);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.TOP_CENTER);

        int row = 0;
        formGrid.add(fieldBox("Cédula:", idField), 0, row);
        formGrid.add(fieldBox("Nombre:", nameField), 1, row++);

        formGrid.add(fieldBox("Apellido:", lastNameField), 0, row);
        formGrid.add(fieldBox("Dirección:", addressField), 1, row++);

        formGrid.add(fieldBox("Email:", emailField), 0, row);
        formGrid.add(fieldBox("Teléfono:", phoneField), 1, row++);

        HBox buttonBox = new HBox(30, editButton, deleteButton, updateButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox rightPane = new VBox(15, title, searchField, formGrid, buttonBox);
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPrefWidth(750);

        this.getChildren().addAll(listBox, rightPane);
        this.setSpacing(10);
    }

    private VBox fieldBox(String labelText, TextField field) {
        Label label = new Label(labelText);
        ViewStyles.formLabelStyle(label);
        VBox box = new VBox(5, label, field);
        return box;
    }

    private TextField createTextField() {
        TextField field = new TextField();
        ViewStyles.textFieldStyle(field);
        field.setPrefWidth(300);
        field.setPrefHeight(35);
        return field;
    }

    private TextField createTextField(String promptText) {
        TextField field = createTextField();
        field.setPromptText(promptText);
        return field;
    }

    private void setupListeners() {
        searchField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            String input = searchField.getText().trim();
            updateSuggestions(input);
        });

        editButton.setOnAction(e -> setEditableFields(true));
        updateButton.setOnAction(e -> {
            // Actualizar en base de datos usando presenter
            setEditableFields(false);
        });
        deleteButton.setOnAction(e -> {
            // Lógica de eliminación
        });
    }

    private void setEditableFields(boolean value) {
        nameField.setEditable(value);
        lastNameField.setEditable(value);
        addressField.setEditable(value);
        emailField.setEditable(value);
        phoneField.setEditable(value);
    }

    private void updateSuggestions(String query) {
        listBox.getChildren().clear();
        List<String[]> results = presenter.findCustomersByIdPartial(query); // Método esperado
        for (String[] customer : results) {
            Label label = new Label(customer[0] + " - " + customer[1] + " " + customer[2]);
            label.setStyle("-fx-cursor: hand;");
            label.setOnMouseClicked(e -> fillForm(customer));
            listBox.getChildren().add(label);
        }

        if (query.isBlank()) {
            List<String[]> all = presenter.getAllCustomers(); // Método esperado
            for (String[] customer : all) {
                Label label = new Label(customer[0] + " - " + customer[1] + " " + customer[2]);
                label.setStyle("-fx-cursor: hand;");
                label.setOnMouseClicked(e -> fillForm(customer));
                listBox.getChildren().add(label);
            }
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
    }
}
