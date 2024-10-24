package de.hka.iwii.db1.jdbc;

import java.sql.*;
import java.util.Properties;

public class Main {
    public static Connection connect() throws ClassNotFoundException, SQLException {
        String databaseUser = "root";
        String databasePassword = "password";
        String databaseName = "jdbc";

        // Load PostgreSQL driver
        Class.forName("org.postgresql.Driver");

        // Connect to PostgreSQL server without specifying a database
        Properties props = new Properties();
        props.put("user", databaseUser);
        props.put("password", databasePassword);
        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/",
                props);

        // Check if the database exists
        boolean databaseExists = false;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + databaseName + "'")) {
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
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/" + databaseName,
                props);

    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection conn = connect();

        System.out.println(conn);
    }
}
