package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;

import java.awt.*;

public class NewLoginWindowController {

    @FXML
    private TextField txtCID;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField inpPassword;
    @FXML
    private ComboBox<Integer> cbxPermissions;

    public void initialize() {

    }
}
