package co.edu.uptc.view;

import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class LateralMenu {

    private VBox menuBox;
    private Consumer<Region> onPanelSelected;
    private MainView mainView;
    private boolean isVisible = true;

    public LateralMenu(MainView mainView) {
        this.mainView = mainView;
        menuBox = new VBox(20);
        menuBox.setPadding(new Insets(20, -1, 20, 20));
        menuBox.setStyle("-fx-background-color: " + ViewStyles.PRIMARY_COLOR + ";");
        menuBox.setPrefWidth(300);
        menuBox.setAlignment(Pos.TOP_LEFT);

        VBox headerBox = createHeader();
        Accordion menu = createMenu();

        Button manualBtn = createManualButton();
        Button closeBtn = createCloseButton();

        VBox bottomButtons = new VBox(10, manualBtn, closeBtn);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20, 0, 0, 0));

        VBox.setVgrow(menu, Priority.ALWAYS);

        menuBox.getChildren().addAll(headerBox, menu, bottomButtons);
    }

    private VBox createHeader() {
        ImageView logoView = new ImageView(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        logoView.setFitHeight(150);
        logoView.setPreserveRatio(true);

        Label title = new Label("SISTEMA \nGESTOR DE \nRESERVAS");
        ViewStyles.titleStyle(title);

        VBox headerBox = new VBox(5, logoView, title);
        headerBox.setAlignment(Pos.CENTER);
        return headerBox;
    }

    private Accordion createMenu() {
        TitledPane bookingTitled = createBookingPane();
        TitledPane customerTitled = createHuespedesPane();
        TitledPane roomTitled = createHabitacionesPane();

        Accordion menu = new Accordion(bookingTitled, customerTitled, roomTitled);
        VBox.setVgrow(menu, Priority.ALWAYS);

        menu.expandedPaneProperty().addListener((obs, oldPane, newPane) -> {
            if (oldPane != null) {
                oldPane.getStyleClass().remove("titled-pane-selected");
            }
            if (newPane != null) {
                newPane.getStyleClass().add("titled-pane-selected");
            }
        });
        return menu;
    }

    private TitledPane createBookingPane() {
        Button createBooking = new Button("Crear Reserva");
        ViewStyles.buttonStyle(createBooking);
        createBooking.setOnAction(e -> {
            notifyChange(mainView.getBookingPane());
            mainView.getCustomerPane().setFromReservation(false);
            mainView.getBookingPane().clean();
        });

        Button showBooking = new Button("Ver Reservas");
        ViewStyles.buttonStyle(showBooking);
        showBooking.setOnAction(e -> {
            notifyChange(mainView.getShowBookingPane());
            mainView.getShowBookingPane().clearForm();
            mainView.getShowBookingPane().loadBookings();
            toggleVisibility();
        });

        VBox reservasBox = new VBox(5, createBooking, showBooking);
        TitledPane pane = new TitledPane("Reservas", reservasBox);
        ViewStyles.titledPane(pane);
        return pane;
    }

    private TitledPane createHuespedesPane() {
        Button registrarHuesped = new Button("Registrar Huésped");
        ViewStyles.buttonStyle(registrarHuesped);
        registrarHuesped.setOnAction(e -> {
            notifyChange(
                    mainView.getCustomerPane());
            mainView.getCustomerPane().clearForm();
        });

        Button gestionarHuesped = new Button("Gestionar Huésped");
        ViewStyles.buttonStyle(gestionarHuesped);
        gestionarHuesped.setOnAction(e -> {
            notifyChange(mainView.getCustomerSuggestPane());
            mainView.getCustomerSuggestPane().clearForm();
            toggleVisibility();
        });

        VBox huespedesBox = new VBox(5, registrarHuesped, gestionarHuesped);
        TitledPane pane = new TitledPane("Huéspedes", huespedesBox);
        ViewStyles.titledPane(pane);
        return pane;
    }

    private TitledPane createHabitacionesPane() {
        Button verHabitaciones = new Button("Panel de Habitaciones");
        ViewStyles.buttonStyle(verHabitaciones);
        verHabitaciones.setOnAction(e -> notifyChange(mainView.getRoomPane()));

        VBox habitacionesBox = new VBox(verHabitaciones);
        TitledPane pane = new TitledPane("Habitaciones", habitacionesBox);
        ViewStyles.titledPane(pane);
        return pane;
    }

    private Button createManualButton() {
        Button manualBtn = new Button("Manual de Usuario");
        ViewStyles.buttonStyle(manualBtn, 50);
        manualBtn.setOnAction(e -> openManual());
        return manualBtn;
    }

    private void openManual() {
        try {
            // Cargar el recurso desde el classpath
            InputStream inputStream = getClass().getResourceAsStream("/doc/manual.pdf");
            if (inputStream == null) {
                DialogMessage.showErrorDialog(mainView.getStage(), "No se encontró el archivo manual.pdf");
                return;
            }

            // Crear un archivo temporal
            File tempFile = File.createTempFile("manual", ".pdf");
            tempFile.deleteOnExit();

            // Copiar el contenido del recurso al archivo temporal
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Abrir el archivo con el visor por defecto
            Desktop.getDesktop().open(tempFile);
        } catch (IOException ex) {
            ex.printStackTrace();
            DialogMessage.showErrorDialog(mainView.getStage(), "No se pudo abrir el manual de usuario.");
        }
    }

    private Button createCloseButton() {
        Button cerrarBtn = new Button("Cerrar");
        ViewStyles.closeBtnStyle(cerrarBtn);
        cerrarBtn.setOnAction(
                e -> DialogMessage.showConfirmDialog(mainView.getStage(), "DESEA CERRAR EL PROGRAMA?",
                        () -> System.exit(0)));
        return cerrarBtn;
    }

    public VBox getMenu() {
        return menuBox;
    }

    public void setOnPanelSelected(Consumer<Region> listener) {
        this.onPanelSelected = listener;
    }

    public void toggleVisibility() {
        mainView.toggleMenu();
    }

    public void notifyChange(Region panel) {
        if (onPanelSelected != null) {
            onPanelSelected.accept(panel);
        }
    }
}