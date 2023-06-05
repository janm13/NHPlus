package controller;

import datastorage.DAOFactory;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Caregiver;
import datastorage.CaregiverDAO;
import model.Login;
import model.Treatment;
import utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;
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

    @FXML
    public void handleOnEditFirstName(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setFirstName(event.getNewValue());
        doUpdate(event);
    }

    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setSurname(event.getNewValue());
        doUpdate(event);
    }

    @FXML
    public void handleOnEditPhoneNumber(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 1)) { return; }
        event.getRowValue().setPhoneNumber(event.getNewValue());
        doUpdate(event);
    }

    private void doUpdate(TableColumn.CellEditEvent<Caregiver, String> t) {
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        try {
            this.dao.update(t.getRowValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    @FXML
    public void handleAdd() {
        if (!checkPermissions(this.user, 1)) { return; }
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
    }

    @FXML
    public void handleDelete() {
        if (!checkPermissions(this.user, 1)) { return; }
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

    private void delete(Caregiver caregiver) throws SQLException {
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        this.dao.deleteById(caregiver.getCid());
    }

    private void clearTextfields() {
        this.txfFirstname.clear();
        this.txfSurname.clear();
        this.txfPhonenumber.clear();
    }
}
