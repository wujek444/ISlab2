package db;

import javax.swing.*;
import java.sql.*;

public class DBConnector {

    private static final String DB_URL = "jdbc:odbc:auto";
    private static Connection connection;
    private static Statement statement;

    public static void loadODBCDriverClass(){
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(query);
    }
}
