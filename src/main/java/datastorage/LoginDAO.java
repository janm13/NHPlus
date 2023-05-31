package datastorage;

import model.Login;
import model.Patient;

import java.sql.*;
import java.util.ArrayList;

public class LoginDAO extends DAOimp<Login>{

    public LoginDAO(Connection conn) { super(conn); }

    @Override
    protected String getCreateStatementString(Login login) {
        return String.format("INSERT INTO login (cid, username, passwordhash) VALUES ('%d' '%s' '%s')",
                login.getCid(), login.getUsername(), login.getPasswordHash());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * from login WHERE uid = %d", key);
    }

    @Override
    protected Login getInstanceFromResultSet(ResultSet result) throws SQLException {
        Login l = null;
        l = new Login(result.getInt(1), result.getString(2), result.getString(3));
        return l;
    }

    @Override
    protected String getReadAllStatementString() { return "SELECT * FROM login"; }

    @Override
    protected ArrayList<Login> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Login> list = new ArrayList<>();
        Login l = null;
        while (result.next()) {
            l = new Login(result.getInt(1), result.getString(2), result.getString(3));
            list.add(l);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Login login) {
        return String.format("UPDATE login SET cid = '%d', username = '%s', passwordhash = '%s' WHERE uid = '%d'", login.getCid(), login.getUsername(), login.getPasswordHash(), login.getUid());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM patient WHERE uid=%d", key);
    }

    public Login getReadByCID(long cid) throws SQLException {
        Statement st = conn.createStatement();
        return getInstanceFromResultSet(st.executeQuery(String.format("SELECT * FROM login WHERE cid = '%d'", cid)));
    }

    public Login getReadByUsername(String username) throws SQLException {
        Statement st = conn.createStatement();
        return getInstanceFromResultSet(st.executeQuery(String.format("SELECT * FROM login WHERE username = '%s'", username)));
    }
}
