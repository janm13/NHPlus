package datastorage;

import model.Login;
import model.Patient;

import java.sql.*;
import java.util.ArrayList;

public class LoginDAO extends DAOimp<Login>{

    public LoginDAO(Connection conn) { super(conn); }

    @Override
    protected String getCreateStatementString(Login login) {
        return String.format("INSERT INTO login (cid, username, passwordhash, permissions) VALUES ('%d' '%s' '%s' '%d')",
                login.getCid(), login.getUsername(), login.getPasswordHash(), login.getPermissions());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * from login WHERE uid = %d", key);
    }

    @Override
    protected Login getInstanceFromResultSet(ResultSet result) throws SQLException {
        Login l = null;
        result.next();
        l = new Login(result.getInt(1), result.getInt(2), result.getString(3), result.getString(4), result.getInt(5));
        return l;
    }

    @Override
    protected String getReadAllStatementString() { return "SELECT * FROM login"; }

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

    @Override
    protected String getUpdateStatementString(Login login) {
        return String.format("UPDATE login SET cid = '%d', username = '%s', passwordhash = '%s', permissions = '%d' WHERE uid = '%d'", login.getCid(), login.getUsername(), login.getPasswordHash(), login.getPermissions(), login.getUid());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM patient WHERE uid=%d", key);
    }

    public Login getReadByCID(long cid) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT * FROM login WHERE cid = '%d'", cid));
        if (!result.isBeforeFirst()) {
            return null;
        }
        return getInstanceFromResultSet(result);
    }

    public Login getReadByUsername(String username) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT * FROM login WHERE username = '%s'", username));
        if (!result.isBeforeFirst()) {
            return null;
        }
        return getInstanceFromResultSet(result);
    }
}
