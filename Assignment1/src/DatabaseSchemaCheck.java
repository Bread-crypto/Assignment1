
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

// Checks that required database tables exist before a service runs.
public class DatabaseSchemaCheck {

    public static void requireTables(String... tableNames)
            throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection()) {

            DatabaseMetaData metadata = connection.getMetaData();

            // Retrieve the tables in the currently connected database.
            try (ResultSet tables = metadata.getTables(
                    connection.getCatalog(), null, "%", new String[]{"TABLE"})) {

                java.util.Set<String> existingTables =
                        new java.util.HashSet<>();

                while (tables.next()) {
                    existingTables.add(
                            tables.getString("TABLE_NAME").toLowerCase());
                }

                // Stop if any required table is missing.
                for (String tableName : tableNames) {
                    if (!existingTables.contains(tableName.toLowerCase())) {
                        throw new SQLException(
                                "Required database table is missing: "
                                        + tableName);
                    }
                }
            }
        }
    }
}
