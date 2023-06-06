package datastorage;

import model.Login;

import java.sql.*;
import java.util.ArrayList;

/**
 * The LoginDAO class provides CRUD operations for managing Login entities in the database.
 * It extends the abstract DAOimp class and implements the necessary methods for working with Login objects.
 */
public class LoginDAO extends DAOimp<Login> {

    /**
     * Constructs a LoginDAO object with the given database connection.
     *
     * @param conn The database connection to be used by the DAO.
     */
    public LoginDAO(Connection conn) {
        super(conn);
    }

    /**
     * Retrieves the SQL statement for creating a new Login entity.
     *
     * @param login The Login entity for which the SQL statement is generated.
     * @return The SQL statement for creating the Login entity.
     */
    @Override
    protected String getCreateStatementString(Login login) {
        return String.format("INSERT INTO login (cid, username, passwordhash, permissions) VALUES ('%d', '%s', '%s', '%d')",
                login.getCid(), login.getUsername(), login.getPasswordHash(), login.getPermissions());
    }

    /**
     * Retrieves the SQL statement for retrieving a Login entity by its ID.
     *
     * @param key The ID of the Login entity for which the SQL statement is generated.
     * @return The SQL statement for retrieving the Login entity by its ID.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * from login WHERE uid = %d", key);
    }

    /**
     * Maps a ResultSet to an instance of the Login entity.
     *
     * @param result The ResultSet containing the Login entity data.
     * @return An instance of the Login entity mapped from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    @Override
    protected Login getInstanceFromResultSet(ResultSet result) throws SQLException {
        Login l = null;
        result.next();
        l = new Login(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4), result.getInt(5));
        return l;
    }

    /**
     * Retrieves the SQL statement for retrieving all Login entities.
     *
     * @return The SQL statement for retrieving all Login entities.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM login";
    }

    /**
     * Maps a ResultSet to a list of instances of the Login entity.
     *
     * @param result The ResultSet containing the Login entity data.
     * @return A list of instances of the Login entity mapped from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    @Override
    protected ArrayList<Login> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Login> list = new ArrayList<>();
        Login l = null;
        while (result.next()) {
            l = new Login(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4), result.getInt(5));
            list.add(l);
        }
        return list;
    }

    /**
     * Retrieves the SQL statement for updating an existing Login entity.
     *
     * @param login The Login entity for which the SQL statement is generated.
     * @return The SQL statement for updating the Login entity.
     */
    @Override
    protected String getUpdateStatementString(Login login) {
        return String.format("UPDATE login SET cid = '%d', username = '%s', passwordhash = '%s', permissions = '%d' WHERE uid = '%d'",
                login.getCid(), login.getUsername(), login.getPasswordHash(), login.getPermissions(), login.getUid());
    }

    /**
     * Retrieves the SQL statement for deleting a Login entity by its ID.
     *
     * @param key The ID of the Login entity for which the SQL statement is generated.
     * @return The SQL statement for deleting the Login entity by its ID.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM patient WHERE uid=%d", key);
    }

    /**
     * Retrieves a Login entity by its CID.
     *
     * @param cid The CID of the Login entity to be retrieved.
     * @return The Login entity with the specified CID, or null if not found.
     * @throws SQLException if an SQL error occurs during the retrieval.
     */
    public Login getReadByCID(long cid) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT * FROM login WHERE cid = '%d'", cid));
        if (!result.isBeforeFirst()) {
            return null;
        }
        return getInstanceFromResultSet(result);
    }

    /**
     * Deletes a Login entity by its CID.
     *
     * @param cid The CID of the Login entity to be deleted.
     * @throws SQLException if an SQL error occurs during the deletion.
     */
    public void deleteByCID(long cid) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("DELETE FROM login WHERE cid = %d", cid));
    }

    /**
     * Retrieves a Login entity by its username.
     *
     * @param username The username of the Login entity to be retrieved.
     * @return The Login entity with the specified username, or null if not found.
     * @throws SQLException if an SQL error occurs during the retrieval.
     */
    public Login getReadByUsername(String username) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT * FROM login WHERE username = '%s'", username));
        if (!result.isBeforeFirst()) {
            return null;
        }
        return getInstanceFromResultSet(result);
    }
}