package de.hka.iwii.db1.jdbc;

import java.io.IOException;
import java.sql.*;


public class Main {
    public static Connection connect() throws ClassNotFoundException, SQLException {
        String defaultPGDatabase = "db";

        String databaseUser = "root";
        String databasePassword = "password";
        String databaseName = "jdbc";

        Class.forName("org.postgresql.Driver");

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + defaultPGDatabase, databaseUser, databasePassword);

        boolean databaseExists = false;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + databaseName + "'")) {
            if (rs.next()) {
                databaseExists = true;
            }
        }

        // Create the database if it does not exist
        if (!databaseExists) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE " + databaseName);
                System.out.println("Database " + databaseName + " created.");
            }
        } else {
            System.out.println("Database " + databaseName + " already exists.");
        }

        // Connect to the target database
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + databaseName, databaseUser, databasePassword);

    }

    private static void printResult(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();

        int numColumns = meta.getColumnCount();
        int[] colSizes = new int[numColumns];

        for (int i = 1; i <= numColumns; i++) {
            int headerColumnWidth = Math.max(meta.getColumnDisplaySize(i), meta.getColumnTypeName(i).length());
            // Using max label.length as fallback for short data-value columns
            colSizes[i - 1] = Math.max(meta.getColumnLabel(i).length(), headerColumnWidth);
        }

        for (int i = 1; i <= numColumns; i++) {
            String columnName = meta.getColumnName(i);
            int columnSize = colSizes[i - 1];
            String optPipe = i == numColumns ? "" : "| ";
            System.out.printf("%-" + columnSize + "s" + optPipe, columnName);
        }
        System.out.println();

        for (int i = 1; i <= numColumns; i++) {
            String columnType = meta.getColumnTypeName(i);
            int columnSize = colSizes[i - 1];
            String optPipe = i == numColumns ? "" : "| ";
            System.out.printf("%-" + columnSize + "s" + optPipe, columnType);
        }
        System.out.println();

        for (int i = 1; i <= numColumns; i++) {
            int columnSize = colSizes[i - 1];
            String optPlus = i == numColumns ? "" : "+-";
            System.out.printf("%-" + columnSize + "s" + optPlus, "-".repeat(columnSize));
        }
        System.out.println();

        while (rs.next()) {
            for (int i = 1; i <= numColumns; i++) {
                String columnValue = rs.getString(i);
                int columnSize = colSizes[i - 1];
                String optPipe = i == numColumns ? "" : "| ";
                System.out.printf("%-" + columnSize + "s" + optPipe, columnValue);
            }
            System.out.println();
        }
    }

    /**
     * Exercise 4.2
     */
    private static void readPersonal(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
//             ResultSet rs = stmt.executeQuery("SELECT * FROM personal");
            ResultSet rs = stmt.executeQuery("SELECT persnr, name, ort, aufgabe FROM personal");
            printResult(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exercise 4.3
     */
    private static void readCustomersVendor(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // ????????????????
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection conn = connect();
        JDBCBikeShop jdbcBikeShop = new JDBCBikeShop();
        jdbcBikeShop.reInitializeDB(conn);
        System.out.println("-- Initialized successfully --\n");

        readPersonal(conn);
    }
}
