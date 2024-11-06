package de.hka.iwii.db1.jdbc.formatting.options;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Where {
    private final String column;
    private final String value;

    private Where(String column) {
        this.column = column;
        this.value = null;
    }

    private Where(String column, String value) {
        this.column = column;
        this.value = value;
    }

    public static Where column(String column) {
        return new Where(column);
    }

    public Where is(Object value) {
        return new Where(column, value.toString());
    }

    public boolean eval(ResultSet rs) throws SQLException {
        String value = rs.getString(column);
        ResultSetMetaData meta = rs.getMetaData();

        return value.equals(this.value);
    }
}
