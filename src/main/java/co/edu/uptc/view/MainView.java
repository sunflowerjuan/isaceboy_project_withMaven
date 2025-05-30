package co.edu.uptc.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainView {

    private BorderPane root;
    private LateralMenu menu;

    public MainView() {
        root = new BorderPane();
        menu = new LateralMenu();

        root.setLeft(menu.getMenu());

        // Vista inicial
        root.setCenter(new Label("Bienvenido"));

        // Conectar eventos del menú
        menu.setOnPanelSelected(panel -> root.setCenter(panel));
    }

    public BorderPane getRoot() {
        return root;
    }
}
