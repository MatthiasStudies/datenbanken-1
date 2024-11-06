package de.hka.iwii.db1.jdbc;

import java.sql.*;

public class Formatter {
    private final Connection connection;

    public Formatter(Connection connection) {
        this.connection = connection;
    }

    public static void printResult(ResultSet rs, String title) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();

        int numColumns = meta.getColumnCount();
        int totalWidth = 0;
        int[] colSizes = new int[numColumns];

        for (int i = 1; i <= numColumns; i++) {
            int headerColumnWidth = Math.max(meta.getColumnDisplaySize(i), meta.getColumnTypeName(i).length());
            // Using max label.length as fallback for short data-value columns
            colSizes[i - 1] = Math.max(meta.getColumnLabel(i).length(), headerColumnWidth) + 1;
            totalWidth += colSizes[i - 1];
        }

        if (!title.isEmpty()) {
            printVerticalDivider(colSizes, true);
            int whitespace = totalWidth - title.length() + meta.getColumnCount() * 2  - 2; // +2 for the two vertical dividers
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

    public static void printVerticalDivider(int[] colSizes, boolean ignoreColumns) {
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

    public void printTable(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            printResult(rs, "Table: " + tableName);
        } catch (SQLException e) {
            System.out.println("Error while reading table " + tableName);
            e.printStackTrace();
        }
    }

}
