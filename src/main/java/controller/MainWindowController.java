package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Login;
import model.Patient;

import java.io.IOException;

public class MainWindowController {

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Button btnPatient;
    @FXML
    private Button btnCaregiver;
    @FXML
    private Button btnTreatment;

    private Login user;
    public void initialize(Login user) {
        this.user = user;
        if (this.user.getPermissions() > 1) {
            this.btnCaregiver.setVisible(false);
        }
    }

    @FXML
    private void handleShowAllPatient(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllPatientView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllPatientController controller = loader.getController();
        controller.initialize(this.user);
    }

    @FXML
    private void handleShowAllTreatments(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllTreatmentView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllTreatmentController controller = loader.getController();
        controller.initialize(this.user);
    }

    @FXML
    private void handleShowAllCaregiver(ActionEvent e) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllCaregiverView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllCaregiverController controller = loader.getController();
        controller.initialize(this.user);
    }
}
