package co.edu.uptc.view.booking;

import co.edu.uptc.model.RoomType;
import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// ... imports sin cambios

public class ShowBookingPane extends HBox {
    private Presenter presenter;
    private Stage stage;

    private CalendarView calendarView;
    private Calendar bookingCalendar;

    private VBox bookingForm;

    private ComboBox<RoomType> roomTypeBox;
    private DatePicker checkInPicker, checkOutPicker;
    private Button updateBtn, cancelBtn, editBtn;

    private Label guestTitleLabel, guestInfoLabel, idTitleLabel, idInfoLabel;

    private String currentBookingId;

    private boolean editingMode;

    public ShowBookingPane(Presenter presenter, Stage stage) {
        this.presenter = presenter;
        this.stage = stage;
        editingMode = false;

        initComponents();
        setupLayout();
        loadBookings();
    }

    private void initComponents() {
        // Calendario (sin cambios)
        calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setShowPageSwitcher(false);
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setPrefWidth(950);

        bookingCalendar = new Calendar("Reservas");
        bookingCalendar.setStyle(Calendar.Style.STYLE2);

        CalendarSource calendarSource = new CalendarSource("Reservas");
        calendarSource.getCalendars().add(bookingCalendar);
        calendarView.getCalendarSources().clear();
        calendarView.getCalendarSources().add(calendarSource);

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        // Formulario
        bookingForm = new VBox(15);
        bookingForm.setPadding(new Insets(30));
        bookingForm.setStyle("-fx-background-color:" + ViewStyles.WITHE_COLOR + ";");

        guestTitleLabel = new Label("Huésped:");
        ViewStyles.formLabelStyle(guestTitleLabel);
        guestInfoLabel = new Label();
        ViewStyles.labelTextStyle(guestInfoLabel);

        idTitleLabel = new Label("Documento de identidad:");
        ViewStyles.formLabelStyle(idTitleLabel);
        idInfoLabel = new Label();
        ViewStyles.labelTextStyle(idInfoLabel);

        roomTypeBox = new ComboBox<>();
        roomTypeBox.getItems().addAll(presenter.getRoomTypes());
        ViewStyles.comboStyle(roomTypeBox);
        roomTypeBox.setPrefWidth(300);
        roomTypeBox.setOnAction(e -> {
            RoomType selected = roomTypeBox.getValue();
            if (selected != null) {
                String[] roomInfo = presenter.getRoomInfo(selected);
                if (roomInfo == null) {
                    checkInPicker.setDisable(true);
                    checkInPicker.setValue(null);
                    checkInPicker.setDisable(true);
                    checkInPicker.setValue(null);

                    DialogMessage.showWarningDialog(stage,
                            "No hay información disponible para el tipo de habitación seleccionado.");
                } else {
                    if (editingMode) {
                        editingDatePicker(selected);
                    } else {
                        checkInPicker.setDisable(true);
                        checkOutPicker.setDisable(true);
                    }
                }
            }
        });

        checkInPicker = new DatePicker();
        checkInPicker.setDayCellFactory(getAvailableCheckOutDates(roomTypeBox.getValue(), LocalDate.now()));
        checkInPicker.setOnAction(e -> updateCheckOutAvailability());
        checkInPicker.setDisable(true);

        checkOutPicker = new DatePicker();
        checkOutPicker.setDisable(true);

        updateBtn = new Button("Actualizar Reserva");
        cancelBtn = new Button("Cancelar");
        editBtn = new Button("Editar");

        ViewStyles.buttonStyle(updateBtn, 100, 50);
        updateBtn.setMinWidth(150);
        ViewStyles.closeBtnStyle(cancelBtn);
        cancelBtn.setPrefWidth(100);
        ViewStyles.buttonStyle(editBtn, 100, 50);

        disableForm(true); // Deshabilitar por defecto

        setListeners();
    }

    public void editingDatePicker(RoomType selected) {
        checkInPicker.setDayCellFactory(getAvailableCheckInDates(selected));
        checkInPicker.setDisable(false);
        checkInPicker.setValue(null);
        checkOutPicker.setValue(null);
        checkOutPicker.setDisable(true);
    }

    private void setupLayout() {
        VBox formGrid = new VBox(10,
                guestTitleLabel,
                guestInfoLabel,
                idTitleLabel,
                idInfoLabel,
                createLabeledField("Tipo de habitación", roomTypeBox),
                createLabeledField("Fecha de Entrada", checkInPicker),
                createLabeledField("Fecha de Salida", checkOutPicker),
                createButtonRow());
        formGrid.setAlignment(Pos.TOP_LEFT);

        Label formTitle = new Label("DETALLES DE RESERVA");
        ViewStyles.titleStyle(formTitle, 1);
        formTitle.setPadding(new Insets(10, 0, 20, 0));

        bookingForm.getChildren().addAll(formTitle, formGrid);
        bookingForm.setPrefWidth(500);
        bookingForm.setAlignment(Pos.TOP_CENTER);

        HBox.setHgrow(bookingForm, Priority.ALWAYS);
        this.getChildren().addAll(calendarView, bookingForm);
        this.setSpacing(10);
    }

    private HBox createButtonRow() {
        HBox buttonBox = new HBox(10, editBtn, cancelBtn, updateBtn);
        buttonBox.setPadding(new Insets(100, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);

        // Listener para cambiar a vertical si el espacio horizontal es insuficiente
        buttonBox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if (newWidth.doubleValue() < 400) { // puedes ajustar este umbral
                buttonBox.setSpacing(5);
                buttonBox.setPadding(new Insets(50, 0, 0, 0));
                buttonBox.getChildren().setAll(new VBox(10, editBtn, cancelBtn, updateBtn));
            } else {
                buttonBox.getChildren().setAll(editBtn, cancelBtn, updateBtn);
            }
        });

        return buttonBox;
    }

    private VBox createLabeledField(String labelText, Control input) {
        Label label = new Label(labelText);
        ViewStyles.formLabelStyle(label);
        return new VBox(2, label, input);
    }

    public void loadBookings() {
        bookingCalendar.clear();
        List<String[]> bookings = presenter.getAllBookings();

        for (String[] booking : bookings) {
            Entry<Object> entry = new Entry<>(booking[1] + " - " + booking[2]);
            entry.setInterval(LocalDate.parse(booking[3]), LocalDate.parse(booking[4]));
            entry.setUserObject(booking);
            bookingCalendar.addEntry(entry);
        }

        calendarView.setEntryDetailsPopOverContentCallback(param -> {
            Entry<?> entry = param.getEntry();
            String[] bookingData = (String[]) entry.getUserObject();
            fillForm(bookingData);
            return null;
        });
    }

    private void fillForm(String[] bookingData) {
        currentBookingId = bookingData[0];
        String[] customerData = presenter.getCustomerDataById(bookingData[1]);

        guestInfoLabel.setText(customerData[1] + " " + customerData[2]);
        idInfoLabel.setText(customerData[0]);

        checkInPicker.setValue(LocalDate.parse(bookingData[3]));
        checkOutPicker.setValue(LocalDate.parse(bookingData[4]));
        roomTypeBox.setValue(presenter.roomTypeFromString(bookingData[2]));

        disableForm(true);
        updateBtn.setDisable(true);
        editBtn.setDisable(false);
        cancelBtn.setDisable(false);
    }

    private void disableForm(boolean disable) {
        roomTypeBox.setDisable(disable);
    }

    private void setListeners() {
        editBtn.setOnAction(e -> {
            editingMode = true;
            disableForm(false);
            editingDatePicker(roomTypeBox.getValue());
            updateBtn.setDisable(false);
        });

        updateBtn.setOnAction(e -> {
            String result = presenter.updateBooking(new String[] {
                    currentBookingId,
                    roomTypeBox.getValue().toString(),
                    checkInPicker.getValue().toString(),
                    checkOutPicker.getValue().toString()
            });
            if (result.contains("correctamente")) {
                DialogMessage.showSuccessDialog(stage, result);
                clearForm();
                loadBookings();
            } else {
                DialogMessage.showErrorDialog(stage, result);
            }

        });

        cancelBtn.setOnAction(e -> {
            DialogMessage.showConfirmDialog(stage, "¿Esta seguro que desea eliminar la reserva actual?", () -> {
                boolean success = presenter.cancelBooking(currentBookingId);
                if (success) {
                    DialogMessage.showInfoDialog(stage, "Reserva eliminada correctamente.");
                    clearForm();
                    loadBookings();
                } else {
                    DialogMessage.showErrorDialog(stage,
                            "Operacion Fallida.\n Reserva Inactiva.");
                }
            });

            clearForm();
            loadBookings();
        });
    }

    public void clearForm() {
        guestInfoLabel.setText("");
        idInfoLabel.setText("");
        roomTypeBox.setValue(null);
        checkInPicker.setValue(null);
        checkOutPicker.setValue(null);

        disableForm(true);
        updateBtn.setDisable(true);
        cancelBtn.setDisable(true);
        editBtn.setDisable(true);
    }

    private void updateCheckOutAvailability() {
        if (checkInPicker.getValue() != null && roomTypeBox.getValue() != null) {
            RoomType roomType = roomTypeBox.getValue();
            LocalDate checkIn = checkInPicker.getValue();
            checkOutPicker.setDisable(false);
            checkOutPicker.setValue(null);
            checkOutPicker.setDayCellFactory(getAvailableCheckOutDates(roomType, checkIn));

        }
    }

    public Callback<DatePicker, DateCell> getAvailableCheckInDates(RoomType roomType) {
        List<LocalDate> unavailableDates = editingMode && currentBookingId != null
                ? presenter.getUnavailableDates(roomType, Integer.parseInt(currentBookingId))
                : presenter.getUnavailableDates(roomType);
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
        List<LocalDate> unavailableDates = editingMode && currentBookingId != null
                ? presenter.getUnavailableDates(roomType, Integer.parseInt(currentBookingId))
                : presenter.getUnavailableDates(roomType);
        return datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date.isBefore(checkInDate.plusDays(1))) {
                    setDisable(true);
                } else {
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
                        setStyle("-fx-background-color: #ffc0cb;");
                    } else {
                        setStyle("-fx-background-color: #c8e6c9;"); // verde claro
                    }
                }
            }
        };
    }

}
