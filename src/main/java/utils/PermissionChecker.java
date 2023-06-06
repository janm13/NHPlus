package utils;

import javafx.scene.control.Alert;
import model.Login;

/**
 * The `PermissionChecker` class provides a utility method for checking user permissions.
 */
public class PermissionChecker {

    /**
     * Checks the permissions of a user against a specified permission level.
     *
     * @param user  The user to check the permissions for.
     * @param perms The required permission level.
     * @return `true` if the user has sufficient permissions, `false` otherwise.
     */
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
