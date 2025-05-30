package co.edu.uptc.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.function.Consumer;

import co.edu.uptc.view.booking.BookingPane;
import co.edu.uptc.view.booking.ShowBookingPane;
import co.edu.uptc.view.customer.CustomerSuggestPane;
import co.edu.uptc.view.customer.CustomerPane;
import co.edu.uptc.view.room.RoomPane;

public class LateralMenu {

    private VBox menuBox;
    private Consumer<Region> onPanelSelected;

    public LateralMenu() {
        menuBox = new VBox(20);
        menuBox.setPadding(new Insets(20, -1, 20, 20));
        menuBox.setStyle("-fx-background-color: " + ViewStyles.PRIMARY_COLOR + ";");
        menuBox.setPrefWidth(300);
        menuBox.setAlignment(Pos.TOP_LEFT);

        // Logo e Identificación
        ImageView logoView = new ImageView(new Image("file:src/main/resources/images/logo.png"));
        logoView.setFitHeight(200);
        logoView.setPreserveRatio(true);

        Label titulo = new Label("IASCEBOY");
        ViewStyles.titleStyle(titulo);

        Label subtitle = new Label("SISTEMA GESTOR DE RESERVAS");
        ViewStyles.subtitleStyle(subtitle);

        VBox headerBox = new VBox(5, logoView, titulo, subtitle);
        headerBox.setAlignment(Pos.CENTER);

        // Reservas
        Button crearReserva = new Button("Crear Reserva");
        ViewStyles.buttonStyle(crearReserva);
        crearReserva.setOnAction(e -> notificarCambio(new BookingPane()));

        Button verReservas = new Button("Ver Reservas");
        ViewStyles.buttonStyle(verReservas);
        verReservas.setOnAction(e -> notificarCambio(new ShowBookingPane()));

        VBox reservasBox = new VBox(5, crearReserva, verReservas);
        TitledPane reservasPane = new TitledPane("Reservas", reservasBox);
        ViewStyles.titledPane(reservasPane);

        // Huéspedes
        Button registrarHuesped = new Button("Registrar Huésped");
        ViewStyles.buttonStyle(registrarHuesped);
        registrarHuesped.setOnAction(e -> notificarCambio(new CustomerPane()));

        Button gestionarHuesped = new Button("Gestionar Huésped");
        ViewStyles.buttonStyle(gestionarHuesped);
        gestionarHuesped.setOnAction(e -> notificarCambio(new CustomerSuggestPane()));

        VBox huespedesBox = new VBox(5, registrarHuesped, gestionarHuesped);
        TitledPane huespedesPane = new TitledPane("Huéspedes", huespedesBox);
        ViewStyles.titledPane(huespedesPane);

        // Habitaciones
        Button verHabitaciones = new Button("Panel de Habitaciones");
        ViewStyles.buttonStyle(verHabitaciones);
        verHabitaciones.setOnAction(e -> notificarCambio(new RoomPane()));

        VBox habitacionesBox = new VBox(verHabitaciones);
        TitledPane habitacionesPane = new TitledPane("Habitaciones", habitacionesBox);
        ViewStyles.titledPane(habitacionesPane);

        Accordion menu = new Accordion(reservasPane, huespedesPane, habitacionesPane);
        VBox.setVgrow(menu, Priority.ALWAYS);
        menu.expandedPaneProperty().addListener((obs, oldPane, newPane) -> {
            if (oldPane != null) {
                oldPane.getStyleClass().remove("titled-pane-selected");
            }
            if (newPane != null) {
                newPane.getStyleClass().add("titled-pane-selected");
            }
        });

        // Botón de cerrar
        Button cerrarBtn = new Button("Cerrar");
        ViewStyles.closeBtnStyle(cerrarBtn);
        cerrarBtn.setOnAction(e -> System.exit(0));

        menuBox.getChildren().addAll(headerBox, menu, cerrarBtn);
    }

    public VBox getMenu() {
        return menuBox;
    }

    public void setOnPanelSelected(Consumer<Region> listener) {
        this.onPanelSelected = listener;
    }

    private void notificarCambio(Region panel) {
        if (onPanelSelected != null) {
            onPanelSelected.accept(panel);
        }
    }
}
