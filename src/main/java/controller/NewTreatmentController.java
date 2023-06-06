package controller;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Caregiver;
import model.Patient;
import model.Treatment;
import utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The controller class for the new treatment window.
 */
public class NewTreatmentController {
    @FXML
    private Label lblSurname;
    @FXML
    private Label lblFirstname;
    @FXML
    private TextField txtBegin;
    @FXML
    private TextField txtEnd;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextArea taRemarks;
    @FXML
    private DatePicker datepicker;
    @FXML
    private ComboBox<String> comboBox;

    private AllTreatmentController controller;
    private Patient patient;
    private Caregiver caregiver;
    private Stage stage;
    private ObservableList<String> myComboBoxData =
            FXCollections.observableArrayList();
    private ArrayList<Caregiver> caregiverList;

    /**
     * Initializes the controller with the specified parameters.
     *
     * @param controller The AllTreatmentController instance.
     * @param stage      The JavaFX stage.
     * @param patient    The patient associated with the treatment.
     */
    public void initialize(AllTreatmentController controller, Stage stage, Patient patient) {
        this.controller = controller;
        this.patient = patient;
        this.stage = stage;
        createComboBoxData();
        comboBox.setItems(myComboBoxData);
        showPatientData();
    }

    private void showPatientData() {
        this.lblFirstname.setText(patient.getFirstName());
        this.lblSurname.setText(patient.getSurname());
    }

    private void createComboBoxData() {
        CaregiverDAO dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        try {
            caregiverList = (ArrayList<Caregiver>) dao.readAll();
            for (Caregiver caregiver : caregiverList) {
                this.myComboBoxData.add(caregiver.getSurname());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when an item is selected in the combo box.
     */
    @FXML
    public void handleComboBox() {
        String c = this.comboBox.getSelectionModel().getSelectedItem();
        this.caregiver = searchInList(c);
    }

    private Caregiver searchInList(String surname) {
        for (int i = 0; i < this.caregiverList.size(); i++) {
            if (this.caregiverList.get(i).getSurname().equals(surname)) {
                return this.caregiverList.get(i);
            }
        }
        return null;
    }

    /**
     * Handles the action when the "Add" button is clicked.
     */
    @FXML
    public void handleAdd() {
        LocalDate date = this.datepicker.getValue();
        String s_begin = txtBegin.getText();
        LocalTime begin = DateConverter.convertStringToLocalTime(txtBegin.getText());
        LocalTime end = DateConverter.convertStringToLocalTime(txtEnd.getText());
        String description = txtDescription.getText();
        String remarks = taRemarks.getText();
        try {
            Treatment treatment = new Treatment(patient.getPid(), caregiver.getCid(), date,
                    begin, end, description, remarks, null);
            createTreatment(treatment);
            controller.readAllAndShowInTableView();
            stage.close();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Pfleger für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Pfleger aus!");
            alert.showAndWait();
        }
    }

    private void createTreatment(Treatment treatment) {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            dao.create(treatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     */
    @FXML
    public void handleCancel() {
        stage.close();
    }
}