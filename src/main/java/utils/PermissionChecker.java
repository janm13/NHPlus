package utils;

import javafx.scene.control.Alert;
import model.Login;

public class PermissionChecker {

    public static boolean checkPermissions(Login user, int perms) {
        if (user.getPermissions() > perms) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Unzureichende Berechtigungen!");
            alert.setContentText("Sie verfügen nicht über die nötigen Berechtigung, um diese Aktion auszuführen!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
