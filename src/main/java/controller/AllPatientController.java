package controller;

import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Caregiver;
import model.Login;
import model.Patient;
import model.Treatment;
import utils.DateConverter;
import datastorage.DAOFactory;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static utils.PermissionChecker.checkPermissions;


/**
 * The <code>AllPatientController</code> contains the entire logic of the patient view. It determines which data is displayed and how to react to events.
 */
public class AllPatientController {
    @FXML
    private TableView<Patient> tableView;
    @FXML
    private TableColumn<Patient, Integer> colID;
    @FXML
    private TableColumn<Patient, String> colFirstName;
    @FXML
    private TableColumn<Patient, String> colSurname;
    @FXML
    private TableColumn<Patient, String> colDateOfBirth;
    @FXML
    private TableColumn<Patient, String> colCareLevel;
    @FXML
    private TableColumn<Patient, String> colRoom;

    @FXML
    Button btnDelete;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtSurname;
    @FXML
    TextField txtFirstname;
    @FXML
    TextField txtBirthday;
    @FXML
    TextField txtCarelevel;
    @FXML
    TextField txtRoom;
    @FXML
    private TextField txtAssets;

    private ObservableList<Patient> tableviewContent = FXCollections.observableArrayList();
    private PatientDAO dao;
    private Login user;

    /**
     * Initializes the corresponding fields. Is called as soon as the corresponding FXML file is to be displayed.
     */
    public void initialize(Login user) {
        this.user = user;
        readAllAndDeleteOldEntries();
        readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("pid"));

        //CellValuefactory zum Anzeigen der Daten in der TableView
        this.colFirstName.setCellValueFactory(new PropertyValueFactory<Patient, String>("firstName"));
        //CellFactory zum Schreiben innerhalb der Tabelle
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colDateOfBirth.setCellValueFactory(new PropertyValueFactory<Patient, String>("dateOfBirth"));
        this.colDateOfBirth.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colCareLevel.setCellValueFactory(new PropertyValueFactory<Patient, String>("careLevel"));
        this.colCareLevel.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colRoom.setCellValueFactory(new PropertyValueFactory<Patient, String>("roomnumber"));
        this.colRoom.setCellFactory(TextFieldTableCell.forTableColumn());

        //Anzeigen der Daten
        this.tableView.setItems(this.tableviewContent);
    }

    /**
     * handles new firstname value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Patient, String> event){
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setFirstName(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new surname value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Patient, String> event){
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setSurname(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new birthdate value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditDateOfBirth(TableColumn.CellEditEvent<Patient, String> event){
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setDateOfBirth(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new carelevel value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditCareLevel(TableColumn.CellEditEvent<Patient, String> event){
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setCareLevel(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handles new roomnumber value
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditRoomnumber(TableColumn.CellEditEvent<Patient, String> event){
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setRoomnumber(event.getNewValue());
        doUpdate(event);
    }

    /**
     * updates a patient by calling the update-Method in the {@link PatientDAO}
     * @param t row to be updated by the user (includes the patient)
     */
    private void doUpdate(TableColumn.CellEditEvent<Patient, String> t) {
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        try {
            this.dao.update(t.getRowValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readAllAndDeleteOldEntries() {
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        List<Patient> allPatients;
        try {
            allPatients = this.dao.readAll();
            for (Patient patient : allPatients) {
                LocalDate pArchiveDate = DateConverter.convertStringToLocalDate(patient.getArchiveDate());
                if (pArchiveDate != null && pArchiveDate.isBefore(java.time.LocalDate.now().minusYears(10))) {
                    delete(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * calls readAll in {@link PatientDAO} and shows patients in the table
     */
    private void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        List<Patient> allPatients;
        try {
            allPatients = this.dao.readAll();
            for (Patient p : allPatients) {
                if (p.getArchiveDate() == null) {
                    this.tableviewContent.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * handles a delete-click-event. Calls the delete methods in the {@link PatientDAO} and {@link TreatmentDAO}
     */
    @FXML
    public void handleDeleteRow() {
        if (!checkPermissions(this.user, 1)) { return; }
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        int index = this.tableView.getSelectionModel().getSelectedIndex();
        Patient selectedItem = this.tableviewContent.remove(index);
        TreatmentDAO tDao = DAOFactory.getDAOFactory().createTreatmentDAO();
        selectedItem.setArchiveDate(java.time.LocalDate.now().toString());
        List<Treatment> archiveTreatments;
        try {
            archiveTreatments = tDao.readTreatmentsByPid(selectedItem.getPid());
            for (Treatment t : archiveTreatments) {
                if (t.getArchiveDate() == null) {
                    t.setArchiveDate(selectedItem.getArchiveDate());
                    tDao.update(t);
                }
            }
            this.dao.update(selectedItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void delete(Patient patient) throws SQLException {
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        TreatmentDAO tDao = DAOFactory.getDAOFactory().createTreatmentDAO();
        tDao.deleteByPid(patient.getPid());
        this.dao.deleteById(patient.getPid());
    }

    /**
     * handles a add-click-event. Creates a patient and calls the create method in the {@link PatientDAO}
     */
    @FXML
    public void handleAdd() {
        if (!checkPermissions(this.user, 1)) { return; }
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        String surname = this.txtSurname.getText();
        String firstname = this.txtFirstname.getText();
        String birthday = this.txtBirthday.getText();
        LocalDate date = DateConverter.convertStringToLocalDate(birthday);
        String carelevel = this.txtCarelevel.getText();
        String room = this.txtRoom.getText();
        try {
            Patient p = new Patient(firstname, surname, date, carelevel, room, null);
            this.dao.create(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();
    }

    /**
     * removes content from all textfields
     */
    private void clearTextfields() {
        this.txtFirstname.clear();
        this.txtSurname.clear();
        this.txtBirthday.clear();
        this.txtCarelevel.clear();
        this.txtRoom.clear();
    }
}