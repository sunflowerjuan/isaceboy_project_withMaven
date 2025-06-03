package co.edu.uptc.view;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;
import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.booking.BookingPane;
import co.edu.uptc.view.booking.ShowBookingPane;
import co.edu.uptc.view.customer.CustomerPane;
import co.edu.uptc.view.customer.CustomerSuggest;
import co.edu.uptc.view.room.RoomPane;
import co.edu.uptc.view.rootstyles.ViewStyles;
import co.edu.uptc.view.rootstyles.WindowControlsPane;

public class MainView {

    private BorderPane root;
    private LateralMenu menu;
    private Stage stage;
    private Tooltip toggleTooltip;

    private BookingPane bookingPane;
    private ShowBookingPane showBookingPane;
    private CustomerPane customerPane;
    private CustomerSuggest customerSuggestPane;
    private RoomPane roomPane;

    private boolean isMenuVisible = true;
    private StackPane toggleTab;

    private Presenter presenter;

    public MainView(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        menu = new LateralMenu(this);
        root.setLeft(menu.getMenu());
    }

    public void initComponets() {
        bookingPane = new BookingPane(presenter, stage);
        showBookingPane = new ShowBookingPane(presenter, stage);
        customerPane = new CustomerPane(presenter, stage);
        customerSuggestPane = new CustomerSuggest(presenter, stage);
        roomPane = new RoomPane(presenter, stage);
    }

    public void setupView() {
        // Barra superior
        WindowControlsPane windowControlsPane = new WindowControlsPane(stage);
        root.setTop(windowControlsPane);
        // Centro inicial
        root.setCenter(new Label("Bienvenido"));
        menu.setOnPanelSelected(panel -> root.setCenter(panel));
        initComponets();
        // Pestaña para mostrar/ocultar menú
        createToggleTab();
        root.setLeft(new HBox(menu.getMenu(), toggleTab));
    }

    private void createToggleTab() {
        Polygon arrow = new Polygon(10.0, 0.0, 0.0, 10.0, 10.0, 20.0);
        arrow.setFill(Color.web(ViewStyles.PRIMARY_COLOR));

        toggleTab = new StackPane(arrow);
        toggleTab.setMinWidth(25);
        toggleTab.setStyle("-fx-background-color: c2c2c2;");
        toggleTab.setAlignment(Pos.CENTER);
        toggleTab.setCursor(Cursor.HAND);

        toggleTooltip = new Tooltip("Ocultar menú");
        toggleTooltip.setShowDelay(Duration.millis(200)); // aparece rápido
        Tooltip.install(toggleTab, toggleTooltip);

        toggleTab.setOnMouseClicked(e -> toggleMenu());
    }

    public void toggleMenu() {
        isMenuVisible = !isMenuVisible;
        if (isMenuVisible) {
            root.setLeft(new HBox(menu.getMenu(), toggleTab));
            ((Polygon) toggleTab.getChildren().get(0)).getPoints().setAll(10.0, 0.0, 0.0, 10.0, 10.0, 20.0);
            toggleTooltip.setText("Ocultar menú");
        } else {
            root.setLeft(toggleTab);
            ((Polygon) toggleTab.getChildren().get(0)).getPoints().setAll(0.0, 0.0, 10.0, 10.0, 0.0, 20.0);
            toggleTooltip.setText("Mostrar menú");
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public BookingPane getBookingPane() {
        return bookingPane;
    }

    public void setBookingPane(BookingPane bookingPane) {
        this.bookingPane = bookingPane;
    }

    public ShowBookingPane getShowBookingPane() {
        return showBookingPane;
    }

    public void setShowBookingPane(ShowBookingPane showBookingPane) {
        this.showBookingPane = showBookingPane;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public CustomerPane getCustomerPane() {
        return customerPane;
    }

    public void setCustomerPane(CustomerPane customerPane) {
        this.customerPane = customerPane;
    }

    public CustomerSuggest getCustomerSuggestPane() {
        return customerSuggestPane;
    }

    public void setCustomerSuggestPane(CustomerSuggest customerSuggestPane) {
        this.customerSuggestPane = customerSuggestPane;
    }

    public RoomPane getRoomPane() {
        return roomPane;
    }

    public void setRoomPane(RoomPane roomPane) {
        this.roomPane = roomPane;
    }

    public Presenter getPresenter() {
        return presenter;
    }

    public void setPresenter(Presenter controller) {
        this.presenter = controller;
    }

    public void notifyChange(Region pane) {
        menu.notifyChange(pane);
    }

}
