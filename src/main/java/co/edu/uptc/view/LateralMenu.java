package co.edu.uptc.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.function.Consumer;

import co.edu.uptc.view.customer.CustomerSuggestPane;
import co.edu.uptc.view.customer.CustomerPane;
import co.edu.uptc.view.room.RoomPane;
import co.edu.uptc.view.rootstyles.DialogMessage;
import co.edu.uptc.view.rootstyles.ViewStyles;

public class LateralMenu {

    private VBox menuBox;
    private Consumer<Region> onPanelSelected;
    private MainView mainView;

    public LateralMenu(MainView mainView) {
        this.mainView = mainView;
        menuBox = new VBox(20);
        menuBox.setPadding(new Insets(20, -1, 20, 20));
        menuBox.setStyle("-fx-background-color: " + ViewStyles.PRIMARY_COLOR + ";");
        menuBox.setPrefWidth(300);
        menuBox.setAlignment(Pos.TOP_LEFT);

        VBox headerBox = createHeader();
        Accordion menu = createMenu();

        // Botones inferiores centrados
        Button manualBtn = createManualButton();
        Button closeBtn = createCloseButton();

        VBox bottomButtons = new VBox(10, manualBtn, closeBtn);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20, 0, 0, 0));

        VBox.setVgrow(menu, Priority.ALWAYS);

        menuBox.getChildren().addAll(headerBox, menu, bottomButtons);
    }

    private VBox createHeader() {
        ImageView logoView = new ImageView(new Image("file:src/main/resources/images/logo.png"));
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
        createBooking.setOnAction(e -> notifyChange(mainView.getBookingPane()));

        Button showBooking = new Button("Ver Reservas");
        ViewStyles.buttonStyle(showBooking);
        showBooking.setOnAction(e -> notifyChange(mainView.getShowBookingPane()));

        VBox reservasBox = new VBox(5, createBooking, showBooking);
        TitledPane pane = new TitledPane("Reservas", reservasBox);
        ViewStyles.titledPane(pane);
        return pane;
    }

    private TitledPane createHuespedesPane() {
        Button registrarHuesped = new Button("Registrar Huésped");
        ViewStyles.buttonStyle(registrarHuesped);
        registrarHuesped.setOnAction(e -> notifyChange(new CustomerPane()));

        Button gestionarHuesped = new Button("Gestionar Huésped");
        ViewStyles.buttonStyle(gestionarHuesped);
        gestionarHuesped.setOnAction(e -> notifyChange(new CustomerSuggestPane()));

        VBox huespedesBox = new VBox(5, registrarHuesped, gestionarHuesped);
        TitledPane pane = new TitledPane("Huéspedes", huespedesBox);
        ViewStyles.titledPane(pane);
        return pane;
    }

    private TitledPane createHabitacionesPane() {
        Button verHabitaciones = new Button("Panel de Habitaciones");
        ViewStyles.buttonStyle(verHabitaciones);
        verHabitaciones.setOnAction(e -> notifyChange(new RoomPane()));

        VBox habitacionesBox = new VBox(verHabitaciones);
        TitledPane pane = new TitledPane("Habitaciones", habitacionesBox);
        ViewStyles.titledPane(pane);
        return pane;
    }

    private Button createManualButton() {
        Button manualBtn = new Button("Manual de Usuario");
        ViewStyles.buttonStyle(manualBtn);
        manualBtn.setOnAction(e -> {
            DialogMessage.showConfirmDialog(mainView.getStage(), "esta seguro de que tiene fimosis",
                    () -> System.out.println("Reserva eliminada."));
        });
        return manualBtn;
    }

    private Button createCloseButton() {
        Button cerrarBtn = new Button("Cerrar");
        ViewStyles.closeBtnStyle(cerrarBtn);
        cerrarBtn.setOnAction(e -> System.exit(0));
        return cerrarBtn;
    }

    public VBox getMenu() {
        return menuBox;
    }

    public void setOnPanelSelected(Consumer<Region> listener) {
        this.onPanelSelected = listener;
    }

    private void notifyChange(Region panel) {
        if (onPanelSelected != null) {
            onPanelSelected.accept(panel);
        }
    }
}
