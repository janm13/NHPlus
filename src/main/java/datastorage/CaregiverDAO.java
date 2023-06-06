package datastorage;

import model.Caregiver;
import model.Patient;
import utils.DateConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The Data Access Object (DAO) class for the Caregiver model.
 * This class provides database operations for the Caregiver model.
 */
public class CaregiverDAO extends DAOimp<Caregiver> {

    /**
     * Constructs a CaregiverDAO object and calls the constructor from DAOimp to store the connection.
     *
     * @param conn The database connection.
     */
    public CaregiverDAO(Connection conn) {
        super(conn);
    }

    /**
     * Generates an SQL INSERT statement for the given caregiver.
     *
     * @param caregiver The caregiver for which the INSERT statement is to be created.
     * @return A string with the generated SQL.
     */
    @Override
    protected String getCreateStatementString(Caregiver caregiver) {
        return String.format("INSERT INTO caregiver (firstname, surname, phonenumber) VALUES ('%s', '%s', '%s')",
                caregiver.getFirstName(), caregiver.getSurname(), caregiver.getPhoneNumber());
    }

    /**
     * Generates an SQL SELECT statement for the given key.
     *
     * @param key The key for which the SELECT statement is to be created.
     * @return A string with the generated SQL.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM caregiver WHERE cid = %d", key);
    }

    /**
     * Maps a ResultSet to a Caregiver object.
     *
     * @param result The ResultSet with a single row. Columns will be mapped to a caregiver object.
     * @return A caregiver object with the data from the ResultSet.
     */
    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet result) throws SQLException {
        Caregiver c = null;
        LocalDate archiveDate = DateConverter.convertStringToLocalDate(result.getString(5));
        c = new Caregiver(result.getInt(1), result.getString(2),
                result.getString(3), result.getString(4), archiveDate);
        return c;
    }

    /**
     * Generates an SQL SELECT statement for all caregivers.
     *
     * @return A string with the generated SQL.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM caregiver";
    }

    /**
     * Maps a ResultSet to an ArrayList of Caregiver objects.
     *
     * @param result The ResultSet with multiple rows. Data will be mapped to caregiver objects.
     * @return An ArrayList with caregivers from the ResultSet.
     */
    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<Caregiver>();
        Caregiver c = null;
        while (result.next()) {
            LocalDate archiveDate = DateConverter.convertStringToLocalDate(result.getString(5));
            c = new Caregiver(result.getInt(1), result.getString(2),
                    result.getString(3), result.getString(4), archiveDate);
            list.add(c);
        }
        return list;
    }

    /**
     * Generates an SQL UPDATE statement for the given caregiver.
     *
     * @param caregiver The caregiver for which the UPDATE statement is to be created.
     * @return A string with the generated SQL.
     */
    @Override
    protected String getUpdateStatementString(Caregiver caregiver) {
        return String.format("UPDATE caregiver SET firstname = '%s', surname = '%s', phonenumber = '%s', archive_date = '%s' WHERE cid = '%d'", caregiver.getFirstName(), caregiver.getSurname(), caregiver.getPhoneNumber(), caregiver.getArchiveDate(), caregiver.getCid());
    }

    /**
     * Generates an SQL DELETE statement for the given key.
     *
     * @param key The key for which the DELETE statement is to be created.
     * @return A string with the generated SQL.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM caregiver WHERE cid = %d", key);
    }
}