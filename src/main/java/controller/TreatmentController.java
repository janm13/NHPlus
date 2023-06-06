package controller;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Caregiver;
import model.Login;
import model.Patient;
import model.Treatment;
import utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;

import static utils.PermissionChecker.checkPermissions;

/**
 * The controller class for the treatment window.
 */
public class TreatmentController {
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblCarelevel;
    @FXML
    private Label lblCaregiverName;
    @FXML
    private Label lblPhoneNumber;
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
    private Button btnChange;
    @FXML
    private Button btnCancel;

    private AllTreatmentController controller;
    private Stage stage;
    private Patient patient;
    private Caregiver caregiver;
    private Treatment treatment;
    private Login user;

    /**
     * Initializes the controller with the specified parameters.
     *
     * @param controller The AllTreatmentController instance.
     * @param stage      The JavaFX stage.
     * @param treatment  The treatment associated with the window.
     * @param user       The logged-in user.
     */
    public void initializeController(AllTreatmentController controller, Stage stage, Treatment treatment, Login user) {
        this.user = user;
        this.stage = stage;
        this.controller = controller;
        PatientDAO pDao = DAOFactory.getDAOFactory().createPatientDAO();
        CaregiverDAO cDao = DAOFactory.getDAOFactory().createCaregiverDAO();
        try {
            this.patient = pDao.read((int) treatment.getPid());
            this.caregiver = cDao.read((int) treatment.getCid());
            this.treatment = treatment;
            showData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showData() {
        this.lblPatientName.setText(patient.getSurname() + ", " + patient.getFirstName());
        this.lblCarelevel.setText(patient.getCareLevel());
        this.lblCaregiverName.setText(caregiver.getSurname() + ", " + caregiver.getFirstName());
        this.lblPhoneNumber.setText(caregiver.getPhoneNumber());
        LocalDate date = DateConverter.convertStringToLocalDate(treatment.getDate());
        this.datepicker.setValue(date);
        this.txtBegin.setText(this.treatment.getBegin());
        this.txtEnd.setText(this.treatment.getEnd());
        this.txtDescription.setText(this.treatment.getDescription());
        this.taRemarks.setText(this.treatment.getRemarks());
    }

    /**
     * Handles the action when the "Change" button is clicked.
     */
    @FXML
    public void handleChange() {
        if (!checkPermissions(this.user, 1)) {
            return;
        }
        this.treatment.setDate(this.datepicker.getValue().toString());
        this.treatment.setBegin(txtBegin.getText());
        this.treatment.setEnd(txtEnd.getText());
        this.treatment.setDescription(txtDescription.getText());
        this.treatment.setRemarks(taRemarks.getText());
        doUpdate();
        controller.readAllAndShowInTableView();
        stage.close();
    }

    private void doUpdate() {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            dao.update(treatment);
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