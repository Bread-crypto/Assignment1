import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Fail early if a required integration table is missing (rather than printing an empty docket). */
public class DatabaseSchemaCheck {
    public static void requireTables(String... tableNames) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) throw new SQLException("Database connection is null");
            DatabaseMetaData meta = conn.getMetaData();
            for (String table : tableNames) {
                boolean found = false;
                try (ResultSet rs = meta.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})) {
                    while (rs.next()) {
                        if (table.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) throw new SQLException("Required table missing: " + table);
            }
        }
    }
}
