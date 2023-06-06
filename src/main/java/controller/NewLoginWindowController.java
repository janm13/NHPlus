package controller;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.LoginDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import model.Login;

import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewLoginWindowController {

    @FXML
    private TextField txtCID;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField inpPassword;
    @FXML
    private ComboBox<String> cbxPermissions;
    @FXML
    private Button btnLogin;

    private ObservableList<String> cbxPermissionsData =
            FXCollections.observableArrayList();
    private Stage stage;

    public void initialize(long cid, Stage stage) {
        this.stage = stage;
        this.txtCID.setText(Long.toString(cid));
        createComboBoxData();
        cbxPermissions.setItems(cbxPermissionsData);
    }

    private void createComboBoxData() {
        this.cbxPermissionsData.add("Alle Rechte");
        this.cbxPermissionsData.add("Pfleger Rechte");
        this.cbxPermissionsData.add("Pfleger Read Only Rechte");
    }
    @FXML
    private void handleAddLogin() {
        if (!checkFields()) { return; }
        long cid = Long.parseLong(this.txtCID.getText());
        String username = this.txtUsername.getText();
        String password = this.inpPassword.getText();
        int permissions = this.cbxPermissions.getSelectionModel().getSelectedIndex();
        Login user = new Login(cid, username, password, permissions);
        LoginDAO dao = DAOFactory.getDAOFactory().createLoginDAO();
        List<Login> loginList;
        try {
            loginList = dao.readAll();

            for (Login l : loginList) {
                if (l.getUsername().equals(username)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("Nutzername ist bereits vergeben!");
                    alert.setContentText("Bitte wählen Sie einen anderen Nutzernamen!");
                    alert.showAndWait();
                    return;
                }
            }

            dao.create(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stage.close();
    }

    @FXML
    public void onEnter(ActionEvent ae){ this.btnLogin.fire(); }

    private boolean checkFields() {
        if (this.txtUsername.getText().equals("") || this.txtCID.getText().equals("") || this.inpPassword.getText().equals("") || this.cbxPermissions.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Pflichtfelder nicht befüllt!");
            alert.setContentText("Bitte befüllen Sie sowohl 'CID', 'Benutzername', 'Passwort' und 'Berechtigungen'!");
            alert.showAndWait();
            return false;
        } else if (this.inpPassword.getText().length() < 5) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Sicherheitshinweis!");
            alert.setContentText("Das Passwort muss mindestens 5 Zeichen lang sein!");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
