package datastorage;

import model.Treatment;
import utils.DateConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Interface <code>DAOImp</code>. Overrides methods to generate specific treatment-SQL-queries.
 */
public class TreatmentDAO extends DAOimp<Treatment> {

    /**
     * Constructs a TreatmentDAO object with the given database connection.
     *
     * @param conn The database connection to be used by the DAO.
     */
    public TreatmentDAO(Connection conn) {
        super(conn);
    }

    /**
     * Generates an SQL INSERT statement for a given treatment.
     *
     * @param treatment The treatment for which a specific INSERT statement is to be created.
     * @return The SQL INSERT statement for the treatment.
     */
    @Override
    protected String getCreateStatementString(Treatment treatment) {
        return String.format("INSERT INTO treatment (pid, cid, treatment_date, begin, end, description, remarks) VALUES " +
                        "('%d', '%d', '%s', '%s', '%s', '%s', '%s')", treatment.getPid(), treatment.getCid(), treatment.getDate(),
                treatment.getBegin(), treatment.getEnd(), treatment.getDescription(),
                treatment.getRemarks());
    }

    /**
     * Generates an SQL SELECT statement for retrieving a treatment by its ID.
     *
     * @param key The ID of the treatment for which a specific SELECT statement is to be created.
     * @return The SQL SELECT statement for retrieving the treatment by its ID.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM treatment WHERE tid = %d", key);
    }

    /**
     * Maps a ResultSet to a Treatment object.
     *
     * @param result The ResultSet with a single row. The columns will be mapped to a Treatment object.
     * @return A Treatment object with the data from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
        LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(5));
        LocalTime end = DateConverter.convertStringToLocalTime(result.getString(6));
        LocalDate archiveDate = DateConverter.convertStringToLocalDate(result.getString(9));
        Treatment m = new Treatment(result.getLong(1), result.getLong(2), result.getLong(3),
                date, begin, end, result.getString(7), result.getString(8), archiveDate);
        return m;
    }

    /**
     * Generates an SQL SELECT statement for retrieving all treatments.
     *
     * @return The SQL SELECT statement for retrieving all treatments.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM treatment";
    }

    /**
     * Maps a ResultSet to a list of Treatment objects.
     *
     * @param result The ResultSet with multiple rows. The data will be mapped to Treatment objects.
     * @return A list of Treatment objects from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment t = null;
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
            LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(5));
            LocalTime end = DateConverter.convertStringToLocalTime(result.getString(6));
            LocalDate archiveDate = DateConverter.convertStringToLocalDate(result.getString(9));
            t = new Treatment(result.getLong(1), result.getLong(2), result.getLong(3),
                    date, begin, end, result.getString(7), result.getString(8), archiveDate);
            list.add(t);
        }
        return list;
    }

    /**
     * Generates an SQL UPDATE statement for a given treatment.
     *
     * @param treatment The treatment for which a specific UPDATE statement is to be created.
     * @return The SQL UPDATE statement for the treatment.
     */
    @Override
    protected String getUpdateStatementString(Treatment treatment) {
        return String.format("UPDATE treatment SET pid = '%d', cid = '%d', treatment_date = '%s', begin = '%s', end = '%s'," +
                        "description = '%s', remarks = '%s', archive_date = '%s' WHERE tid = '%d'", treatment.getPid(), treatment.getCid(), treatment.getDate(),
                treatment.getBegin(), treatment.getEnd(), treatment.getDescription(), treatment.getRemarks(), treatment.getArchiveDate(),
                treatment.getTid());
    }

    /**
     * Generates an SQL DELETE statement for a given key.
     *
     * @param key The key for which a specific DELETE statement is to be created.
     * @return The SQL DELETE statement for the key.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM treatment WHERE tid = %d", key);
    }

    /**
     * Reads all treatments associated with a specific patient ID.
     *
     * @param pid The patient ID for which the treatments are to be retrieved.
     * @return A list of Treatment objects associated with the specified patient ID.
     * @throws SQLException if an SQL error occurs during the retrieval.
     */
    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOnePatientByPid(pid));
        list = getListFromResultSet(result);
        return list;
    }

    /**
     * Reads all treatments associated with a specific caregiver ID.
     *
     * @param cid The caregiver ID for which the treatments are to be retrieved.
     * @return A list of Treatment objects associated with the specified caregiver ID.
     * @throws SQLException if an SQL error occurs during the retrieval.
     */
    public List<Treatment> readTreatmentsByCid(long cid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOnePatientByCid(cid));
        list = getListFromResultSet(result);
        return list;
    }

    private String getReadAllTreatmentsOfOnePatientByPid(long pid) {
        return String.format("SELECT * FROM treatment WHERE pid = %d", pid);
    }

    private String getReadAllTreatmentsOfOnePatientByCid(long cid) {
        return String.format("SELECT * FROM treatment WHERE cid = %d", cid);
    }

    /**
     * Deletes all treatments associated with a specific patient ID.
     *
     * @param key The patient ID for which the treatments are to be deleted.
     * @throws SQLException if an SQL error occurs during the deletion.
     */
    public void deleteByPid(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("DELETE FROM treatment WHERE pid = %d", key));
    }
}