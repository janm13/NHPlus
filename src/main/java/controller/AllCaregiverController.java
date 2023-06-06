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
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private TextField txfSurname;
    @FXML
    private TextField txfFirstname;
    @FXML
    private TextField txfPhonenumber;

    private ObservableList<Caregiver> tableviewContent = FXCollections.observableArrayList();
    private CaregiverDAO dao;
    private Login user;

    /**
     * Initializes the AllCaregiverController with the given user.
     *
     * @param user The currently logged-in user.
     */
    public void initialize(Login user) {
        this.user = user;
        readAllAndDeleteOldEntries();
        readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<Caregiver, Integer>("cid"));

        this.colFirstName.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("firstName"));
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colPhoneNumber.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("phoneNumber"));
        this.colPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableView.setItems(this.tableviewContent);
    }

    /**
     * Handles the event of editing the first name of a caregiver in the table.
     *
     * @param event The cell edit event.
     */
    @FXML
    public void handleOnEditFirstName(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 0)) {
            return;
        }
        event.getRowValue().setFirstName(event.getNewValue());
        doUpdate(event);
    }

    /**
     * Handles the event of editing the surname of a caregiver in the table.
     *
     * @param event The cell edit event.
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 0)) {
            return;
        }
        event.getRowValue().setSurname(event.getNewValue());
        doUpdate(event);
    }

    /**
     * Handles the event of editing the phone number of a caregiver in the table.
     *
     * @param event The cell edit event.
     */
    @FXML
    public void handleOnEditPhoneNumber(TableColumn.CellEditEvent<Caregiver, String> event) {
        if (!checkPermissions(this.user, 0)) {
            return;
        }
        event.getRowValue().setPhoneNumber(event.getNewValue());
        doUpdate(event);
    }

    /**
     * Performs the update of a caregiver in the database.
     *
     * @param t The cell edit event.
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
     * Reads all caregivers and deletes the old entries from the database.
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
     * Reads all caregivers from the database and shows them in the table view.
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
     * Handles the event of adding a new caregiver.
     */
    @FXML
    public void handleAdd() {
        if (!checkPermissions(this.user, 0) || !checkTextfields()) {
            return;
        }
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
            Stage stage = new Stage();

            NewLoginWindowController controller = loader.getController();
            controller.initialize(caregiverList.get(caregiverList.size() - 1).getCid(), stage);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event of archiving or deleting a caregiver.
     */
    @FXML
    public void handleDelete() {
        if (!checkPermissions(this.user, 0)) {
            return;
        }
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
     * Deletes a caregiver from the database.
     *
     * @param caregiver The caregiver to be deleted.
     * @throws SQLException If an SQL exception occurs.
     */
    private void delete(Caregiver caregiver) throws SQLException {
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        LoginDAO ldao = DAOFactory.getDAOFactory().createLoginDAO();
        this.dao.deleteById(caregiver.getCid());
        ldao.deleteByCID(caregiver.getCid());
    }

    /**
     * Checks if all text fields are filled.
     *
     * @return True if all text fields are filled, false otherwise.
     */
    private boolean checkTextfields() {
        ArrayList<TextField> textFields = new ArrayList<>();
        textFields.add(txfSurname);
        textFields.add(txfFirstname);
        textFields.add(txfPhonenumber);

        for (TextField t : textFields) {
            if (t.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Pflichtfelder nicht befüllt!");
                alert.setContentText("Bitte füllen Sie alle Felder aus!");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    /**
     * Clears all text fields.
     */
    private void clearTextfields() {
        this.txfFirstname.clear();
        this.txfSurname.clear();
        this.txfPhonenumber.clear();
    }
}
