package controller;

import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Login;
import model.Patient;
import model.Treatment;
import datastorage.DAOFactory;
import utils.DateConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import static utils.PermissionChecker.checkPermissions;

public class AllTreatmentController {
    @FXML
    private TableView<Treatment> tableView;
    @FXML
    private TableColumn<Treatment, Integer> colID;
    @FXML
    private TableColumn<Treatment, Integer> colPid;
    @FXML
    private TableColumn<Treatment, String> colDate;
    @FXML
    private TableColumn<Treatment, String> colBegin;
    @FXML
    private TableColumn<Treatment, String> colEnd;
    @FXML
    private TableColumn<Treatment, String> colDescription;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button btnNewTreatment;
    @FXML
    private Button btnArchive;

    private ObservableList<Treatment> tableviewContent = FXCollections.observableArrayList();
    private TreatmentDAO dao;
    private ObservableList<String> myComboBoxData = FXCollections.observableArrayList();
    private ArrayList<Patient> patientList;
    private Login user;

    /**
     * initialize the tableview
     *
     * @param user
     */
    public void initialize(Login user) {
        this.user = user;
        readAllAndDeleteOldEntries();
        readAllAndShowInTableView();
        comboBox.setItems(myComboBoxData);
        comboBox.getSelectionModel().select(0);

        this.colID.setCellValueFactory(new PropertyValueFactory<Treatment, Integer>("tid"));
        this.colPid.setCellValueFactory(new PropertyValueFactory<Treatment, Integer>("pid"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<Treatment, String>("date"));
        this.colBegin.setCellValueFactory(new PropertyValueFactory<Treatment, String>("begin"));
        this.colEnd.setCellValueFactory(new PropertyValueFactory<Treatment, String>("end"));
        this.colDescription.setCellValueFactory(new PropertyValueFactory<Treatment, String>("description"));
        this.tableView.setItems(this.tableviewContent);
        createComboBoxData();
    }

    /**
     * read all and delete old entries
     */
    public void readAllAndDeleteOldEntries() {
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        try {
            allTreatments = this.dao.readAll();
            for (Treatment treatment : allTreatments) {
                LocalDate tArchiveDate = DateConverter.convertStringToLocalDate(treatment.getArchiveDate());
                if (tArchiveDate != null && tArchiveDate.isBefore(java.time.LocalDate.now().minusYears(10))) {
                    delete(treatment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * read all and show in tableview
     */
    public void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        comboBox.getSelectionModel().select(0);
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        try {
            allTreatments = this.dao.readAll();
            for (Treatment treatment : allTreatments) {
                if (treatment.getArchiveDate() == null) {
                    this.tableviewContent.add(treatment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * create combobox data from patient list
     */
    private void createComboBoxData(){
        PatientDAO dao = DAOFactory.getDAOFactory().createPatientDAO();
        try {
            patientList = (ArrayList<Patient>) dao.readAll();
            this.myComboBoxData.add("alle");
            for (Patient patient: patientList) {
                this.myComboBoxData.add(patient.getSurname());
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * update tableview content according to combobox selection
     */
    @FXML
    public void handleComboBox(){
        String p = this.comboBox.getSelectionModel().getSelectedItem();
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        if(p.equals("alle")){
            try {
                allTreatments= this.dao.readAll();
                for (Treatment treatment : allTreatments) {
                    this.tableviewContent.add(treatment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Patient patient = searchInList(p);
        if(patient !=null){
            try {
                allTreatments = this.dao.readTreatmentsByPid(patient.getPid());
                for (Treatment treatment : allTreatments) {
                    this.tableviewContent.add(treatment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * find patient in list
     *
     * @param surname
     * @return
     */
    private Patient searchInList(String surname){
        for (int i =0; i<this.patientList.size();i++){
            if(this.patientList.get(i).getSurname().equals(surname)){
                return this.patientList.get(i);
            }
        }
        return null;
    }

    /**
     * set archive date for selected patient
     */
    @FXML
    public void handleArchive(){
        if (!checkPermissions(this.user, 1)) { return; }
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        int index = this.tableView.getSelectionModel().getSelectedIndex();
        if (this.tableviewContent.get(index).getArchiveDate() == null) {
            this.tableviewContent.get(index).setArchiveDate(java.time.LocalDate.now().toString());
            Treatment t = this.tableviewContent.remove(index);
            try {
                this.dao.update(t);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * delete given treatment
     *
     * @param treatment
     * @throws SQLException
     */
    public void delete(Treatment treatment) throws SQLException {
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        this.dao.deleteById(treatment.getTid());
    }

    /**
     * create new treatment window
     */
    @FXML
    public void handleNewTreatment() {
        if (!checkPermissions(this.user, 1)) { return; }
        try{
            String p = this.comboBox.getSelectionModel().getSelectedItem();
            Patient patient = searchInList(p);
            newTreatmentWindow(patient);
        }
        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten aus!");
            alert.showAndWait();
        }
    }

    /**
     * handle mouse click on tableview
     */
    @FXML
    public void handleMouseClick(){
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedItem() != null)) {
                int index = this.tableView.getSelectionModel().getSelectedIndex();
                Treatment treatment = this.tableviewContent.get(index);

                treatmentWindow(treatment);
            }
        });
    }

    /**
     * create new treatment window
     *
     * @param patient
     */
    public void newTreatmentWindow(Patient patient){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/NewTreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            //da die primaryStage noch im Hintergrund bleiben soll
            Stage stage = new Stage();

            NewTreatmentController controller = loader.getController();
            controller.initialize(this, stage, patient);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * create treatment window
     *
     * @param treatment
     */
    public void treatmentWindow(Treatment treatment){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/TreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            //da die primaryStage noch im Hintergrund bleiben soll
            Stage stage = new Stage();
            TreatmentController controller = loader.getController();

            controller.initializeController(this, stage, treatment, this.user);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}