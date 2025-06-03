package co.edu.uptc.view.booking;

import co.edu.uptc.model.RoomType;
import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class BookingPane extends VBox {
    private ComboBox<RoomType> roomTypeBox;
    private DatePicker checkInDate, checkOutDate;
    private TextField searchCustomerField;
    private ListView<String> customerSuggestions;
    private Label idLabel, nameLabel, emailLabel, nightsLabel, totalPriceLabel;
    private Button confirmButton, registerCustomerBtn;

    private Stage stage;
    private Presenter presenter;

    public BookingPane(Presenter presenter, Stage stage) {
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
    }

    private void initComponents() {
        roomTypeBox = new ComboBox<>();
        ViewStyles.comboStyle(roomTypeBox);
        roomTypeBox.setPrefWidth(300);
        roomTypeBox.getItems().addAll(presenter.getRoomTypes());
        roomTypeBox.setOnAction(e -> {
            RoomType selected = roomTypeBox.getValue();
            if (selected != null) {
                String[] roomInfo = presenter.getRoomInfo(selected);
                if (roomInfo == null) {
                    checkInDate.setDisable(true);
                    checkInDate.setValue(null);
                    checkOutDate.setDisable(true);
                    checkOutDate.setValue(null);

                    DialogMessage.showWarningDialog(stage,
                            "No hay información disponible para el tipo de habitación seleccionado.");
                    registerCustomerBtn.setDisable(true);
                } else {
                    checkInDate.setDayCellFactory(getAvailableCheckInDates(selected));
                    checkInDate.setDisable(false);
                    checkInDate.setValue(null);
                    checkOutDate.setValue(null);
                    checkOutDate.setDisable(true);
                    registerCustomerBtn.setDisable(false);
                }
            }
        });

        checkInDate = new DatePicker();
        checkInDate.setDisable(true);
        checkInDate.getEditor().setDisable(true); // No edición manual
        checkInDate.setPrefHeight(40);
        checkInDate.getEditor().setStyle("-fx-font-size: 16px;");
        checkInDate.setDayCellFactory(getAvailableCheckOutDates(roomTypeBox.getValue(), LocalDate.now()));
        checkInDate.setOnAction(e -> updateCheckOutAvailability());

        checkOutDate = new DatePicker();
        checkOutDate.setDisable(true);
        checkOutDate.getEditor().setDisable(true); // No edición manual
        checkOutDate.setPrefHeight(40);
        checkOutDate.getEditor().setStyle("-fx-font-size: 16px;");
        checkOutDate.setOnAction(e -> updateBookingSummary());

        searchCustomerField = new TextField();
        searchCustomerField.setStyle("-fx-background-color: fafafa;"
                + " -fx-border-color: " + ViewStyles.ENABLE_COLOR + ";"
                + " -fx-border-radius: 5;"
                + " -fx-background-radius: 5;"
                + " -fx-padding: 5;"
                + " -fx-font-size: 16px;");
        searchCustomerField.setPromptText("Documento de identidad");
        searchCustomerField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                searchCustomerField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            updateSuggestions(searchCustomerField.getText());
        });

        customerSuggestions = new ListView<>();
        customerSuggestions.setMaxHeight(100);
        customerSuggestions.setOnMouseClicked(e -> loadCustomerData());

        registerCustomerBtn = new Button("Registrar huésped");
        ViewStyles.buttonStyle(registerCustomerBtn, 200, 40);
        registerCustomerBtn.setDisable(true);
        registerCustomerBtn.setOnAction(e -> presenter.showCustomerForm(searchCustomerField.getText().trim()));

        idLabel = new Label();
        nameLabel = new Label();
        emailLabel = new Label();
        nightsLabel = new Label();
        totalPriceLabel = new Label();

        confirmButton = new Button("Confirmar Reserva");
        ViewStyles.buttonStyle(confirmButton, 650, 60);
        confirmButton.setOnAction(e -> confirmBooking());
    }

    private void createLayout() {
        Label title = new Label("CREAR RESERVA");
        ViewStyles.titleStyle(title, 1);
        title.setPadding(new Insets(30, 10, 40, 20));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);

        layout.getChildren().addAll(
                title,
                createSection("Datos de Reserva", createReservationForm()),
                createSection("Datos del Cliente", createCustomerSection()),
                createBookingSummary(),
                confirmButton);

        getChildren().add(layout);
    }

    private VBox createSection(String subtitle, Node content) {
        Label label = new Label(subtitle);
        ViewStyles.formLabelStyle(label);
        return new VBox(10, label, content);
    }

    private GridPane createReservationForm() {
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(15);

        grid.add(new Label("Tipo de Habitación:"), 0, 0);
        grid.add(roomTypeBox, 1, 0);

        grid.add(new Label("Fecha de Entrada:"), 0, 1);
        grid.add(checkInDate, 1, 1);

        grid.add(new Label("Fecha de Salida:"), 0, 2);
        grid.add(checkOutDate, 1, 2);

        return grid;
    }

    private VBox createCustomerSection() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Buscar por Documento:");

        searchLabel.setLabelFor(searchCustomerField);

        HBox searchBox = new HBox(10, searchLabel, searchCustomerField, registerCustomerBtn);
        VBox customerInfo = new VBox(5, idLabel, nameLabel, emailLabel);
        vbox.getChildren().addAll(searchBox, customerSuggestions, customerInfo);

        return vbox;
    }

    private VBox createBookingSummary() {
        Label summaryTitle = new Label("Resumen:");
        summaryTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        return new VBox(10, summaryTitle, nightsLabel, totalPriceLabel);
    }

    public void updateSuggestions(String query) {
        customerSuggestions.getItems().clear();
        List<String[]> results = presenter.findCustomersByIdPartial(query);
        for (String[] customer : results) {
            customerSuggestions.getItems().add(customer[1] + " " + customer[2] + " - " + customer[0]);
        }

        if (results.isEmpty()) {
            customerSuggestions.getItems().add("No se encontraron coincidencias.");
            registerCustomerBtn.setDisable(false);
        } else {
            registerCustomerBtn.setDisable(true);
        }
    }

    public void loadCustomerData() {
        String selected = customerSuggestions.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.equals("No se encontraron coincidencias.")) {
            String customerId = selected.substring(selected.lastIndexOf("-") + 2);
            String[] data = presenter.getCustomerDataById(customerId);
            if (data != null) {
                idLabel.setText("Documento de identidad: " + data[0]);
                nameLabel.setText("Nombre: " + data[1] + " " + data[2]);
                emailLabel.setText("Correo: " + data[4]);
                updateBookingSummary();
            }
        }
    }

    private void updateBookingSummary() {
        if (checkInDate.getValue() != null && checkOutDate.getValue() != null
                && customerSuggestions.getSelectionModel().getSelectedItem() != null) {
            long nights = checkInDate.getValue().until(checkOutDate.getValue()).getDays();
            double price = presenter.calculateBookingPrice(roomTypeBox.getValue(), nights);

            nightsLabel.setText("Noches: " + nights);

            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            nf.setMaximumFractionDigits(2);
            String formattedPrice = nf.format(price);

            totalPriceLabel.setText("Total: $" + formattedPrice);
        }
    }

    private void updateCheckOutAvailability() {
        if (checkInDate.getValue() != null && roomTypeBox.getValue() != null) {
            RoomType roomType = roomTypeBox.getValue();
            LocalDate checkIn = checkInDate.getValue();
            checkOutDate.setDisable(false);
            checkOutDate.setValue(null);
            checkOutDate.setDayCellFactory(getAvailableCheckOutDates(roomType, checkIn));

        }
    }

    private void confirmBooking() {
        if (checkInDate.getValue() == null || checkOutDate.getValue() == null || idLabel.getText().isBlank()) {
            DialogMessage.showWarningDialog(stage, "Complete todos los campos necesarios.");
            return;
        }

        String customerId = idLabel.getText().split(": ")[1];
        RoomType roomType = roomTypeBox.getValue();
        LocalDate checkIn = checkInDate.getValue();
        LocalDate checkOut = checkOutDate.getValue();

        long nights = checkIn.until(checkOut).getDays();
        double price = presenter.calculateBookingPrice(roomType, nights);

        boolean success = presenter.createBooking(customerId, roomType, checkIn, checkOut, String.valueOf(price));

        if (success) {
            DialogMessage.showSuccessDialog(stage, "Reserva confirmada exitosamente");
        } else {
            DialogMessage.showErrorDialog(stage, "No se pudo realizar la reserva");
        }
        clean();
    }

    public void clean() {
        roomTypeBox.getSelectionModel().clearSelection();

        checkInDate.setDisable(true);
        checkInDate.setValue(null);
        checkOutDate.setDisable(true);
        checkOutDate.setValue(null);

        searchCustomerField.clear();
        customerSuggestions.getItems().clear();

        idLabel.setText("");
        nameLabel.setText("");
        emailLabel.setText("");

        nightsLabel.setText("");
        totalPriceLabel.setText("");

        registerCustomerBtn.setDisable(true);
    }

    public Callback<DatePicker, DateCell> getAvailableCheckInDates(RoomType roomType) {
        List<LocalDate> unavailableDates = presenter.getUnavailableDates(roomType);
        LocalDate today = LocalDate.now();

        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty || date.isBefore(today) || unavailableDates.contains(date)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                    return;
                }

                // Verificar si hay al menos una fecha continua posterior disponible para salir
                boolean hasAvailableRange = false;
                LocalDate nextDate = date.plusDays(1);
                for (int i = 0; i < 30; i++) { // Máximo 30 días de búsqueda
                    if (!unavailableDates.contains(nextDate)) {
                        hasAvailableRange = true;
                        break;
                    }
                    nextDate = nextDate.plusDays(1);
                }

                if (!hasAvailableRange) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        };
    }

    public Callback<DatePicker, DateCell> getAvailableCheckOutDates(RoomType roomType, LocalDate checkInDate) {
        List<LocalDate> unavailableDates = presenter.getUnavailableDates(roomType);
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty || date.isBefore(checkInDate.plusDays(1)) || date.isAfter(checkInDate.plusDays(20))) {
                    setDisable(true);
                    return;
                }

                LocalDate cursor = checkInDate;
                boolean available = true;
                while (!cursor.isEqual(date)) {
                    if (unavailableDates.contains(cursor)) {
                        available = false;
                        break;
                    }
                    cursor = cursor.plusDays(1);
                }

                if (!available || unavailableDates.contains(date)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); // rojo claro
                } else {
                    setStyle("-fx-background-color: #c8e6c9;"); // verde claro
                }
            }
        };
    }

}
