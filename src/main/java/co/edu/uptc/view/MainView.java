package co.edu.uptc.view;

import co.edu.uptc.view.booking.BookingPane;
import co.edu.uptc.view.booking.ShowBookingPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainView {

    private BorderPane root;
    private LateralMenu menu;
    private BookingPane bookingPane;
    private ShowBookingPane showBookingPane;

    public MainView(Stage stage) {
        root = new BorderPane();
        menu = new LateralMenu(this);
        root.setLeft(menu.getMenu());

        // Añadir barra de control ventana arriba
        WindowControlsPane windowControlsPane = new WindowControlsPane(stage);
        root.setTop(windowControlsPane);

        // Vista inicial
        root.setCenter(new Label("Bienvenido"));
        // Conectar eventos del menú
        menu.setOnPanelSelected(panel -> root.setCenter(panel));
        initComponets();
    }

    public void initComponets() {
        bookingPane = new BookingPane();
        showBookingPane = new ShowBookingPane();
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
}
