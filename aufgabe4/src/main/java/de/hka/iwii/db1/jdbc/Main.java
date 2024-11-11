package de.hka.iwii.db1.jdbc;

import de.hka.iwii.db1.jdbc.formatting.Formatter;
import de.hka.iwii.db1.jdbc.formatting.options.FmtOptions;

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

    /**
     * Exercise 4.2
     */
    private static void exercise4_2(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT persnr, name, ort, aufgabe FROM personal");
            Formatter.printResult(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exercise 4.3
     */
    private static void exercise4_3(Connection connection, String customersSearchTerm) {
        String sql = "SELECT k.name as kunde, k.nr as knr, lt.name as lieferant, lu.liefnr as lnr FROM kunde k " +
                "JOIN auftrag a on k.nr = a.kundnr " +
                "JOIN auftragsposten ap on a.auftrnr = ap.auftrnr " +
                "JOIN lieferung lu on ap.teilnr = lu.teilnr " +
                "JOIN lieferant lt on lu.liefnr = lt.nr " +
                "WHERE k.name LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + customersSearchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            Formatter.printResult(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getNextId(Connection connection, String tableName, String primaryKey) throws SQLException {
        String sql = "SELECT MAX(?) FROM " + tableName;
        sql = sql.replace("?", primaryKey);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt(1) + 1;
    }

    /**
     * For Exercise 4.4
     */
    private static int insertCustomer(Connection connection, String name, String street, int zipCode, String city) throws SQLException {
        String sql = "INSERT INTO kunde (nr, name, strasse, plz, ort, sperre) VALUES (?, ?, ?, ?, ?, ?) RETURNING nr";
        PreparedStatement stmt = connection.prepareStatement(sql);
        int id = getNextId(connection, "kunde", "nr");

        stmt.setInt(1, id);
        stmt.setString(2, name);
        stmt.setString(3, street);
        stmt.setInt(4, zipCode);
        stmt.setString(5, city);
        stmt.setString(6, "0");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    /**
     * For Exercise 4.4
     */
    private static int insertOrder(Connection connection, int customerId, int staffId, int count, float price, int partNr) throws SQLException {
        String sqlOrder = "INSERT INTO auftrag (auftrnr, datum, kundnr, persnr) VALUES (?, CURRENT_DATE, ?, ?) RETURNING auftrnr";

        PreparedStatement orderStmt = connection.prepareStatement(sqlOrder);
        int orderId = getNextId(connection, "auftrag", "auftrnr");

        orderStmt.setInt(1, orderId);
        orderStmt.setInt(2, customerId);
        orderStmt.setInt(3, staffId);
        ResultSet orderRs = orderStmt.executeQuery();
        orderRs.next();

        String sqlOrderPos = "INSERT INTO auftragsposten (posnr, auftrnr, teilnr, anzahl, gesamtpreis) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement orderPosStmt = connection.prepareStatement(sqlOrderPos);

        int posId = getNextId(connection, "auftragsposten", "posnr");

        orderPosStmt.setInt(1, posId);
        orderPosStmt.setInt(2, orderId);
        orderPosStmt.setInt(3, partNr);
        orderPosStmt.setInt(4, count);
        orderPosStmt.setFloat(5, price);

        orderPosStmt.executeUpdate();

        return orderRs.getInt(1);
    }

    /**
     * For Exercise 4.4
     */
    private static void exercise4_4(Connection connection) {
        try {
            int customerId = insertCustomer(connection, "Test 123", "Street", 12345, "City");
            int staffId = 3;
            int count = 10;
            float price = 69.0f;
            int partNr = 500001;
            int orderId = insertOrder(connection, customerId, staffId, count, price, partNr);

            System.out.println("Inserted order with ID: " + orderId);
            var formatter = new Formatter(connection);
            formatter.printTable("kunde", FmtOptions.highlightWhereColumn("nr").is(customerId));
            formatter.printTable("auftrag", FmtOptions.highlightWhereColumn("auftrnr").is(orderId));
            formatter.printTable("auftragsposten", FmtOptions.highlightWhereColumn("auftrnr").is(orderId));


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection conn = connect();
        JDBCBikeShop jdbcBikeShop = new JDBCBikeShop();
        jdbcBikeShop.reInitializeDB(conn);
        System.out.println("-- Initialized successfully --\n");

        exercise4_2(conn);
//        exercise4_3(conn, "Rafa");
//        exercise4_4(conn);
    }
}
