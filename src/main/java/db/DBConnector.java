package db;

import model.Laptop;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class DBConnector {

    private static final String DB_URL = "jdbc:odbc:auto";
    private static Connection connection;
    private static Statement statement;

    private static final String INSERT_QUERY = "INSERT INTO `is`.`laptop`\n" +
            "(`manufacturer`, `matrixSize`,`resolution`,`matrixCoating`, `touchPad`,\n" +
            "`cpuFamily`, `coresCount`, `clockSpeed`, `ram`,`driveCapacity`,\n" +
            "`driveType`, `gpu`, `gpuMemory`, `os`, `opticalDrive`) VALUES";

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

    public static void insertLaptops(List<Laptop> laptops) throws SQLException {
        for(Laptop laptop : laptops) {
            insertLaptop(laptop);
        }
    }

    private static void insertLaptop(Laptop laptopToPersist) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeUpdate(createInsertLaptopQuery(laptopToPersist));
    }

    private static String laptopToValueString(Laptop laptop) {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("(");
        strBuilder.append(createSQLStringValue(laptop.getManufacturer())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getMatrixSize())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getResolution())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getMatrixCoating())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getTouchPad())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getCpuFamily())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getCoresCount())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getClockSpeed())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getRam())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getDriveCapacity())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getDriveType())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getGpu())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getGpuMemory())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getOs())).append(",");
        strBuilder.append(createSQLStringValue(laptop.getOpticalDrive()));
        strBuilder.append(")");

        return strBuilder.toString();
    }

    private static String createSQLStringValue(String str) {
        return new StringBuilder().append("\'").append(str).append("\'").toString();
    }

    private static String createInsertLaptopQuery(Laptop laptop) {
        return INSERT_QUERY + laptopToValueString(laptop);
    }
}
