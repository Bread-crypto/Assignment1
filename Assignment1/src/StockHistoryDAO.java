
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class StockHistoryDAO {

    private static final String INSERT_STOCK_HISTORY =
            "INSERT INTO StockHistoryTable " +
            "(publicationId, movementType, quantityChanged, " +
            "previousStock, newStock, docketId, reason) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public void record(Connection conn, int publicationId,
                       String movementType, int quantityChanged,
                       int previousStock, int newStock,
                       Integer docketId, String reason)
                       throws SQLException {

        try (PreparedStatement stmt =
                conn.prepareStatement(INSERT_STOCK_HISTORY)) {

            stmt.setInt(1, publicationId);
            stmt.setString(2, movementType);
            stmt.setInt(3, quantityChanged);
            stmt.setInt(4, previousStock);
            stmt.setInt(5, newStock);

            if (docketId == null) {
                stmt.setNull(6, Types.INTEGER);
            } else {
                stmt.setInt(6, docketId);
            }

            stmt.setString(7, reason);
            stmt.executeUpdate();
        }
    }
}
