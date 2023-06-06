package datastorage;

import model.Patient;
import utils.DateConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Implements the Interface <code>DAOImp</code>. Overrides methods to generate specific patient-SQL-queries.
 */
public class PatientDAO extends DAOimp<Patient> {

    /**
     * Constructs a PatientDAO object with the given database connection.
     *
     * @param conn The database connection to be used by the DAO.
     */
    public PatientDAO(Connection conn) {
        super(conn);
    }

    /**
     * Generates an SQL INSERT statement for a given patient.
     *
     * @param patient The patient for which a specific INSERT statement is to be created.
     * @return The SQL INSERT statement for the patient.
     */
    @Override
    protected String getCreateStatementString(Patient patient) {
        return String.format("INSERT INTO patient (firstname, surname, dateOfBirth, carelevel, roomnumber) VALUES ('%s', '%s', '%s', '%s', '%s')",
                patient.getFirstName(), patient.getSurname(), patient.getDateOfBirth(), patient.getCareLevel(), patient.getRoomnumber());
    }

    /**
     * Generates an SQL SELECT statement for retrieving a patient by its ID.
     *
     * @param key The ID of the patient for which a specific SELECT statement is to be created.
     * @return The SQL SELECT statement for retrieving the patient by its ID.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM patient WHERE pid = %d", key);
    }

    /**
     * Maps a ResultSet to a Patient object.
     *
     * @param result The ResultSet with a single row. The columns will be mapped to a Patient object.
     * @return A Patient object with the data from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    @Override
    protected Patient getInstanceFromResultSet(ResultSet result) throws SQLException {
        Patient p = null;
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
        LocalDate archiveDate = DateConverter.convertStringToLocalDate(result.getString(7));
        p = new Patient(result.getInt(1), result.getString(2),
                result.getString(3), date, result.getString(5),
                result.getString(6), archiveDate);
        return p;
    }

    /**
     * Generates an SQL SELECT statement for retrieving all patients.
     *
     * @return The SQL SELECT statement for retrieving all patients.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM patient";
    }

    /**
     * Maps a ResultSet to an ArrayList of Patient objects.
     *
     * @param result The ResultSet with multiple rows. The data will be mapped to Patient objects.
     * @return An ArrayList of Patient objects from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    @Override
    protected ArrayList<Patient> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Patient> list = new ArrayList<Patient>();
        Patient p = null;
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
            LocalDate archiveDate = DateConverter.convertStringToLocalDate(result.getString(7));
            p = new Patient(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5), result.getString(6), archiveDate);
            list.add(p);
        }
        return list;
    }

    /**
     * Generates an SQL UPDATE statement for a given patient.
     *
     * @param patient The patient for which a specific UPDATE statement is to be created.
     * @return The SQL UPDATE statement for the patient.
     */
    @Override
    protected String getUpdateStatementString(Patient patient) {
        return String.format("UPDATE patient SET firstname = '%s', surname = '%s', dateOfBirth = '%s', carelevel = '%s', " +
                        "roomnumber = '%s', archive_date = '%s' WHERE pid = '%d'", patient.getFirstName(), patient.getSurname(), patient.getDateOfBirth(),
                patient.getCareLevel(), patient.getRoomnumber(), patient.getArchiveDate(), patient.getPid());
    }

    /**
     * Generates an SQL DELETE statement for a given key.
     *
     * @param key The key for which a specific DELETE statement is to be created.
     * @return The SQL DELETE statement for the key.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM patient WHERE pid=%d", key);
    }
}