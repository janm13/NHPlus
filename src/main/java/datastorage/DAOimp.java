package datastorage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The DAOimp class is an abstract class that provides the basic implementation of a Data Access Object (DAO).
 * It implements the common CRUD (Create, Read, Update, Delete) operations for a specific entity type.
 * Subclasses need to implement the abstract methods to provide the specific SQL statements and object mapping.
 *
 * @param <T> The type of the entity to be managed by the DAO.
 */
public abstract class DAOimp<T> implements DAO<T> {
    protected Connection conn;

    /**
     * Constructs a DAOimp object with the given database connection.
     *
     * @param conn The database connection to be used by the DAO.
     */
    public DAOimp(Connection conn) {
        this.conn = conn;
    }

    /**
     * Creates a new entity in the database.
     *
     * @param t The entity to be created.
     * @throws SQLException if an SQL error occurs during the creation.
     */
    @Override
    public void create(T t) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(getCreateStatementString(t));
    }

    /**
     * Retrieves an entity from the database by its key.
     *
     * @param key The key of the entity to be retrieved.
     * @return The retrieved entity, or null if not found.
     * @throws SQLException if an SQL error occurs during the retrieval.
     */
    @Override
    public T read(long key) throws SQLException {
        T object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadByIDStatementString(key));
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    /**
     * Retrieves all entities of the managed type from the database.
     *
     * @return A list of all entities.
     * @throws SQLException if an SQL error occurs during the retrieval.
     */
    @Override
    public List<T> readAll() throws SQLException {
        ArrayList<T> list = new ArrayList<>();
        T object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllStatementString());
        list = getListFromResultSet(result);
        return list;
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param t The entity to be updated.
     * @throws SQLException if an SQL error occurs during the update.
     */
    @Override
    public void update(T t) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(getUpdateStatementString(t));
    }

    /**
     * Deletes an entity from the database by its key.
     *
     * @param key The key of the entity to be deleted.
     * @throws SQLException if an SQL error occurs during the deletion.
     */
    @Override
    public void deleteById(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(getDeleteStatementString(key));
    }

    /**
     * Retrieves the SQL statement for creating an entity.
     *
     * @param t The entity for which the SQL statement is generated.
     * @return The SQL statement for creating the entity.
     */
    protected abstract String getCreateStatementString(T t);

    /**
     * Retrieves the SQL statement for retrieving an entity by its key.
     *
     * @param key The key of the entity for which the SQL statement is generated.
     * @return The SQL statement for retrieving the entity by its key.
     */
    protected abstract String getReadByIDStatementString(long key);

    /**
     * Maps a ResultSet to an instance of the entity.
     *
     * @param set The ResultSet containing the entity data.
     * @return An instance of the entity mapped from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    protected abstract T getInstanceFromResultSet(ResultSet set) throws SQLException;

    /**
     * Retrieves the SQL statement for retrieving all entities of the managed type.
     *
     * @return The SQL statement for retrieving all entities.
     */
    protected abstract String getReadAllStatementString();

    /**
     * Maps a ResultSet to a list of instances of the entity.
     *
     * @param set The ResultSet containing the entity data.
     * @return A list of instances of the entity mapped from the ResultSet.
     * @throws SQLException if an SQL error occurs during the mapping.
     */
    protected abstract ArrayList<T> getListFromResultSet(ResultSet set) throws SQLException;

    /**
     * Retrieves the SQL statement for updating an existing entity.
     *
     * @param t The entity for which the SQL statement is generated.
     * @return The SQL statement for updating the entity.
     */
    protected abstract String getUpdateStatementString(T t);

    /**
     * Retrieves the SQL statement for deleting an entity by its key.
     *
     * @param key The key of the entity for which the SQL statement is generated.
     * @return The SQL statement for deleting the entity by its key.
     */
    protected abstract String getDeleteStatementString(long key);
}