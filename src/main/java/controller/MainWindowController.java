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

/**
 * The controller class for the main window of the application.
 */
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

    /**
     * Initializes the controller with the specified user.
     *
     * @param user The logged-in user.
     */
    public void initialize(Login user) {
        this.user = user;
        if (this.user.getPermissions() > 1) {
            this.btnCaregiver.setVisible(false);
        }
    }

    /**
     * Handles the action when the "Show All Patients" button is clicked.
     *
     * @param e The action event.
     */
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

    /**
     * Handles the action when the "Show All Treatments" button is clicked.
     *
     * @param e The action event.
     */
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

    /**
     * Handles the action when the "Show All Caregivers" button is clicked.
     *
     * @param e The action event.
     */
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