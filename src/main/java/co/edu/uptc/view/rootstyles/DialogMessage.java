package co.edu.uptc.view.rootstyles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DialogMessage {

    private static final String ICON_PATH_BASE = "/images/dialog/";

    private DialogMessage() {
    }

    public static void showSuccessDialog(Stage owner, String message) {
        ImageView icon = loadIcon("check.png");
        CustomDialog dialog = new CustomDialog(owner, "Éxito", message, icon, false, null);
        dialog.show();
    }

    public static void showInfoDialog(Stage owner, String message) {
        ImageView icon = loadIcon("info.png");
        CustomDialog dialog = new CustomDialog(owner, "Información", message, icon, false, null);
        dialog.show();
    }

    public static void showWarningDialog(Stage owner, String message) {
        ImageView icon = loadIcon("warning.png");
        CustomDialog dialog = new CustomDialog(owner, "Advertencia", message, icon, false, null);
        dialog.show();
    }

    public static void showErrorDialog(Stage owner, String message) {
        ImageView icon = loadIcon("error.png");
        CustomDialog dialog = new CustomDialog(owner, "Alerta", message, icon, false, null);
        dialog.show();
    }

    public static void showConfirmDialog(Stage owner, String message, Runnable onAccept) {
        ImageView icon = loadIcon("edit.png");
        CustomDialog dialog = new CustomDialog(owner, "Confirmación", message, icon, true, onAccept);
        dialog.show();
    }

    private static ImageView loadIcon(String fileName) {
        try {
            return new ImageView(
                    new Image(DialogMessage.class.getResource(ICON_PATH_BASE + fileName).toExternalForm()));
        } catch (Exception e) {
            return null;
        }
    }

}
