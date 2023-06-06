package controller;

import datastorage.DAOFactory;
import datastorage.LoginDAO;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Caregiver;
import datastorage.CaregiverDAO;
import model.Login;
import model.Treatment;
import utils.DateConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static utils.PermissionChecker.checkPermissions;

public class AllCaregiverController {

    @FXML
    private TableView<Caregiver> tableView;
    @FXML
    private TableColumn<Caregiver, Integer> colID;
    @FXML
    private TableColumn<Caregiver, String> colFirstName;
    @FXML
    private TableColumn<Caregiver, String> colSurname;
    @FXML
    private TableColumn<Caregiver, String> colPhoneNumber;

    @FXML
    Button btnDelete;
    @FXML
    Button btnAdd;
    @FXML
    TextField txfSurname;
    @FXML
    TextField txfFirstname;
    @FXML
    TextField txfPhonenumber;

    private ObservableList<Caregiver> tableviewContent = FXCollections.observableArrayList();
    private CaregiverDAO dao;
    private Login user;

    /**
     * initialize the tableview and delete all old entries
     *
     * @param user
     */
    public void initialize(Login user) {
        this.user = user;
        readAllAndDeleteOldEntries();
        readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<Caregiver, Integer>("cid"));

        //CellValuefactory zum Anzeigen der Daten in der TableView
        this.colFirstName.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("firstName"));
        //CellFactory zum Schreiben innerhalb der Tabelle
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colPhoneNumber.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("phoneNumber"));
        this.colPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        //Anzeigen der Daten
        this.tableView.setItems(this.tableviewContent);
    }

    /**
     * handle editing of first name
     *
     * @param event
     */
    @FXML
    public void handleOnEditFirstName(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 0)) { return; }
        event.getRowValue().setFirstName(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handle editing of surname
     *
     * @param event
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 0)) { return; }
        event.getRowValue().setSurname(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handle editing of phone number
     *
     * @param event
     */
    @FXML
    public void handleOnEditPhoneNumber(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 0)) { return; }
        event.getRowValue().setPhoneNumber(event.getNewValue());
        doUpdate(event);
    }

    /**
     * handle update of a caregiver
     *
     * @param t
     */
    private void doUpdate(TableColumn.CellEditEvent<Caregiver, String> t) {
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        try {
            this.dao.update(t.getRowValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle deleting of all old caregivers
     */
    public void readAllAndDeleteOldEntries() {
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        List<Caregiver> allCaregivers;
        try {
            allCaregivers = dao.readAll();
            for (Caregiver caregiver : allCaregivers) {
                LocalDate cArchiveDate = DateConverter.convertStringToLocalDate(caregiver.getArchiveDate());
                if (cArchiveDate != null && cArchiveDate.isBefore(java.time.LocalDate.now().minusYears(10))) {
                    delete(caregiver);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * calls readAll in {@Link CaregiverDAO} and shows caregivers in the table
     */
    private void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        List<Caregiver> allCaregivers;
        try {
            allCaregivers = dao.readAll();
            for (Caregiver c : allCaregivers) {
                if (c.getArchiveDate() == null) {
                    this.tableviewContent.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * handle adding of a new caregiver
     */
    @FXML
    public void handleAdd() {
        if (!checkPermissions(this.user, 0) || !checkTextfields()) { return; }
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        String surname = this.txfSurname.getText();
        String firstname = this.txfFirstname.getText();
        String phonenumber = this.txfPhonenumber.getText();
        try {
            Caregiver c = new Caregiver(firstname, surname, phonenumber, null);
            this.dao.create(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        readAllAndShowInTableView();
        clearTextfields();

        List<Caregiver> caregiverList;
        try {
            caregiverList = this.dao.readAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/NewLoginWindowView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            //da die primaryStage noch im Hintergrund bleiben soll
            Stage stage = new Stage();

            NewLoginWindowController controller = loader.getController();
            controller.initialize(caregiverList.get(caregiverList.size()-1).getCid(), stage);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * handle archiving or deleting of a caregiver
     */
    @FXML
    public void handleDelete() {
        if (!checkPermissions(this.user, 0)) { return; }
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        TreatmentDAO tDao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> caregiverTreatments;
        LocalDate newestTreatment = DateConverter.convertStringToLocalDate("0001-01-01");
        int index = this.tableView.getSelectionModel().getSelectedIndex();
        if (this.tableviewContent.get(index).getArchiveDate() == null) {
            Caregiver c = this.tableviewContent.remove(index);
            try {
                caregiverTreatments = tDao.readTreatmentsByCid(c.getCid());
                for (Treatment t : caregiverTreatments) {
                    if (DateConverter.convertStringToLocalDate(t.getDate()).isAfter(newestTreatment)) {
                        newestTreatment = DateConverter.convertStringToLocalDate(t.getDate());
                    }
                }
                c.setArchiveDate(newestTreatment.toString());
                this.dao.update(c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * handle deleting of a caregiver
     *
     * @param caregiver
     */
    private void delete(Caregiver caregiver) throws SQLException {
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        LoginDAO ldao = DAOFactory.getDAOFactory().createLoginDAO();
        this.dao.deleteById(caregiver.getCid());
        ldao.deleteByCID(caregiver.getCid());
    }

    /**
     * check if all textfields are filled
     *
     * @return
     */
    private boolean checkTextfields() {
        ArrayList<TextField> textFields = new ArrayList<>();
        textFields.add(txfSurname);
        textFields.add(txfFirstname);
        textFields.add(txfPhonenumber);

        for (TextField t : textFields) {
            if (t.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Pflichtfelder nicht befüllt!");
                alert.setContentText("Bitte befüllen Sie alle Felder!");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    /**
     * clear all textfields
     */
    private void clearTextfields() {
        this.txfFirstname.clear();
        this.txfSurname.clear();
        this.txfPhonenumber.clear();
    }
}
