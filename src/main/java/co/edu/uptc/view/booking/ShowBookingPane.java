package co.edu.uptc.view.booking;

import co.edu.uptc.model.Booking;
import co.edu.uptc.model.RoomType;
import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.utils.DateUtil;
import co.edu.uptc.view.rootstyles.ViewStyles;
import com.calendarfx.view.CalendarView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ShowBookingPane extends BorderPane {
    private Presenter presenter;
    private CalendarView calendarView;
    private TextField searchBookingField;
    private ListView<String> bookingSuggestions;
    private TextField customerField, emailField, priceField;
    private DatePicker checkInDate, checkOutDate;
    private Button editButton, saveButton;

    private Stage stage;

    private Booking selectedBooking;

    public ShowBookingPane(Presenter presenter, Stage stage) {
        this.stage = stage;
        this.presenter = presenter;
        initComponents();
        layoutComponents();
        setupActions();
    }

    private void initComponents() {
        calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowPageSwitcher(true);
        calendarView.setShowSourceTray(false);

        searchBookingField = new TextField();
        searchBookingField.setPromptText("Buscar por cédula");

        bookingSuggestions = new ListView<>();

        customerField = new TextField();
        customerField.setEditable(false);
        emailField = new TextField();
        emailField.setEditable(false);
        priceField = new TextField();
        priceField.setEditable(false);

        checkInDate = new DatePicker();
        checkInDate.setDisable(true);

        checkOutDate = new DatePicker();
        checkOutDate.setDisable(true);

        editButton = new Button("Modificar");
        saveButton = new Button("Guardar cambios");
        saveButton.setDisable(true);
    }

    private void layoutComponents() {
        VBox leftPanel = new VBox(10, new Label("Calendario"), calendarView);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(400);

        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(20));

        Label searchLabel = new Label("Buscar reserva por cédula:");
        VBox searchBox = new VBox(5, searchLabel, searchBookingField, bookingSuggestions);

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(15);
        infoGrid.setVgap(10);

        infoGrid.addRow(0, new Label("Cliente:"), customerField);
        infoGrid.addRow(1, new Label("Email:"), emailField);
        infoGrid.addRow(2, new Label("Precio Total:"), priceField);
        infoGrid.addRow(3, new Label("Entrada:"), checkInDate);
        infoGrid.addRow(4, new Label("Salida:"), checkOutDate);

        HBox buttons = new HBox(10, editButton, saveButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        rightPanel.getChildren().addAll(searchBox, new Separator(), infoGrid, buttons);

        setLeft(leftPanel);
        setCenter(rightPanel);
    }

    private void setupActions() {
        calendarView.dateProperty().addListener((obs, oldDate, newDate) -> {
            List<Booking> bookings = presenter.getBookingsByDate(newDate);
            bookingSuggestions.getItems().clear();
            for (Booking b : bookings) {
                bookingSuggestions.getItems()
                        .add(b.getCustomer().getName() + " - " + b.getCustomer().getIdentification());
            }
        });

        searchBookingField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateSuggestions(newVal);
        });

        bookingSuggestions.setOnMouseClicked(e -> loadSelectedBooking());

        editButton.setOnAction(e -> enableEditMode());
        saveButton.setOnAction(e -> saveBookingChanges());
    }

    private void updateSuggestions(String query) {
        bookingSuggestions.getItems().clear();
        List<Booking> results = presenter.findBookingsByCustomerId(query);
        for (Booking b : results) {
            bookingSuggestions.getItems().add(b.getCustomer().getName() + " - " + b.getCustomer().getEmail());
        }
    }

    private void loadSelectedBooking() {
        String selected = bookingSuggestions.getSelectionModel().getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            String customerId = selected.substring(selected.lastIndexOf("-") + 2);
            selectedBooking = presenter.getBookingByCustomerId(customerId);
            if (selectedBooking != null) {
                customerField.setText(selectedBooking.getCustomer().getName());
                emailField.setText(selectedBooking.getCustomer().getEmail());
                priceField.setText(String.valueOf(selectedBooking.getTotalPrice()));
                checkInDate.setValue(DateUtil.toLocalDate(selectedBooking.getStartDate()));
                checkOutDate.setValue(DateUtil.toLocalDate(selectedBooking.getEndDate()));
            }
        }
    }

    private void enableEditMode() {
        if (selectedBooking == null)
            return;
        checkInDate.setDisable(false);
        checkOutDate.setDisable(false);
        checkInDate.setDayCellFactory(
                new BookingPane(presenter, null).getAvailableCheckInDates(selectedBooking.getRoom().getRoomType()));
        checkOutDate.setDayCellFactory(new BookingPane(presenter, null).getAvailableCheckOutDates(
                selectedBooking.getRoom().getRoomType(), DateUtil.toLocalDate(selectedBooking.getStartDate())));
        saveButton.setDisable(false);
    }

    private void saveBookingChanges() {
        if (checkInDate.getValue() == null || checkOutDate.getValue() == null)
            return;
        selectedBooking.setStartDate(DateUtil.toDate(checkInDate.getValue()));
        selectedBooking.setEndDate(DateUtil.toDate(checkOutDate.getValue()));
        // presenter.updateBooking(selectedBooking);
        saveButton.setDisable(true);
        checkInDate.setDisable(true);
        checkOutDate.setDisable(true);
    }
}
