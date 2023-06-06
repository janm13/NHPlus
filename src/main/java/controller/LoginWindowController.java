package controller;

import datastorage.DAOFactory;
import datastorage.LoginDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Login;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.Stage;

import static java.lang.Long.parseLong;

public class LoginWindowController {

    @FXML
    private TextField txtUsernameCID;
    @FXML
    private PasswordField inpPassword;
    @FXML
    private Button btnLogin;

    private Main mainClass;
    private Stage stage;

    public void initialize(Main mainClass, Stage stage) {
        this.mainClass = mainClass;
        this.stage = stage;
    }

    /**
     * Handle the login button.
     */
    @FXML
    public void handleLogin() {
        LoginDAO dao = DAOFactory.getDAOFactory().createLoginDAO();
        Login user;
        try {
            if (this.txtUsernameCID.getText().equals("") || this.inpPassword.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Pflichtfelder nicht befüllt!");
                alert.setContentText("Bitte befüllen Sie sowohl 'Benutzername/CID', als auch 'Passwort'!");
                alert.showAndWait();
                return;
            } else if (this.txtUsernameCID.getText().matches("[0-9]+") && this.txtUsernameCID.getText().length() <= 4) {
                user = dao.getReadByCID(parseLong(this.txtUsernameCID.getText()));
            } else {
                user = dao.getReadByUsername(this.txtUsernameCID.getText());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user != null) {
            if (Login.validatePassword(this.inpPassword.getText(), user.getPasswordHash())) {
                mainClass.setUser(user);
                stage.close();
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Anmeldung fehlgeschlagen");
        alert.setContentText("Benutzername/CID und/oder Passwort inkorrekt!");
        alert.showAndWait();
    }

    /**
     * Fires an event when the user presses the Enter key
     *
     * @param ae
     */
    @FXML
    public void onEnter(ActionEvent ae){
        this.btnLogin.fire();
    }
}
