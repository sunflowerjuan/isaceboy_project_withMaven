package co.edu.uptc.view.room;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class RoomPane extends StackPane {
    public RoomPane() {
        this.getChildren().add(new Label("Vista: Habitaciones"));
    }
}