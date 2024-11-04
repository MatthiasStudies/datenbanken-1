package de.hka.iwii.db1.jdbc;

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

    private static void printResult(ResultSet rs, String title) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();

        int numColumns = meta.getColumnCount();
        int totalWidth = 0;
        int[] colSizes = new int[numColumns];

        for (int i = 1; i <= numColumns; i++) {
            int headerColumnWidth = Math.max(meta.getColumnDisplaySize(i), meta.getColumnTypeName(i).length());
            // Using max label.length as fallback for short data-value columns
            colSizes[i - 1] = Math.max(meta.getColumnLabel(i).length(), headerColumnWidth);
            totalWidth += colSizes[i - 1];
        }

        if (!title.isEmpty()) {
            printVerticalDivider(colSizes, true);
            int whitespace = totalWidth - title.length() + meta.getColumnCount() + 2; // +2 for the two vertical dividers
            System.out.println("| " + title + " ".repeat(whitespace) + "|");
        }

        printVerticalDivider(colSizes, false);

        for (int i = 1; i <= numColumns; i++) {
            String columnName = meta.getColumnName(i);
            int columnSize = colSizes[i - 1];
            String optPipe = i == numColumns ? "|" : "";
            System.out.printf("| %-" + columnSize + "s" + optPipe, columnName);
        }
        System.out.println();

        for (int i = 1; i <= numColumns; i++) {
            String columnType = meta.getColumnTypeName(i);
            int columnSize = colSizes[i - 1];
            String optPipe = i == numColumns ? "|" : "";
            System.out.printf("| %-" + columnSize + "s" + optPipe, columnType);
        }
        System.out.println();

        printVerticalDivider(colSizes, false);

        boolean anyResults = false;

        while (rs.next()) {
            for (int i = 1; i <= numColumns; i++) {
                anyResults = true;
                String columnValue = rs.getString(i);
                int columnSize = colSizes[i - 1];
                String optPipe = i == numColumns ? "|" : "";
                System.out.printf("| %-" + columnSize + "s" + optPipe, columnValue);
            }
            System.out.println();
        }

        if (anyResults) {
            printVerticalDivider(colSizes, false);
        }
    }

    private static void printVerticalDivider(int[] colSizes, boolean ignoreColumns) {
        for (int i = 0; i < colSizes.length; i++) {
            int columnSize = colSizes[i];
            String optPlus = i == colSizes.length - 1 ? "" : ignoreColumns ? "--" : "+-";

            if (i == 0) {
                System.out.print("+");
                columnSize += 1;
            }
            System.out.printf("%-" + columnSize + "s" + optPlus, "-".repeat(columnSize));
            if (i == colSizes.length - 1) {
                System.out.print("+");
            }
        }
        System.out.println();
    }

    private static void printTable(Connection connection, String tableName) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            printResult(rs, "Table: " + tableName);
        } catch (SQLException e) {
            System.out.println("Error while reading table " + tableName);
            e.printStackTrace();
        }
    }

    /**
     * Exercise 4.2
     */
    private static void readPersonal(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
//             ResultSet rs = stmt.executeQuery("SELECT * FROM personal");
            ResultSet rs = stmt.executeQuery("SELECT persnr, name, ort, aufgabe FROM personal");
            printResult(rs, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exercise 4.3
     */
    private static void readCustomersVendor(Connection connection, String customersSearchTerm) {
        String sql = "SELECT k.name as kunde, k.nr as knr, lt.name as lieferant, lu.liefnr as lnr FROM kunde k " +
                "JOIN auftrag a on k.nr = a.kundnr " +
                "JOIN auftragsposten ap on a.auftrnr = ap.auftrnr " +
                "JOIN lieferung lu on ap.teilnr = lu.teilnr " +
                "JOIN lieferant lt on lu.liefnr = lt.nr " +
                "WHERE k.name LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + customersSearchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            printResult(rs, "");
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
            printTable(connection, "kunde");
            printTable(connection, "auftrag");
            printTable(connection, "auftragsposten");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection conn = connect();
        JDBCBikeShop jdbcBikeShop = new JDBCBikeShop();
        jdbcBikeShop.reInitializeDB(conn);
        System.out.println("-- Initialized successfully --\n");

//        readCustomersVendor(conn, "Rafa");
        exercise4_4(conn);
    }
}
