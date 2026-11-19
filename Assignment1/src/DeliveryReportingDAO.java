import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Combines stock-created docket lines with Patrick's recorded deliveries.
 * PENDING/OUT OF STOCK originate on docket lines; actual results originate in DeliveryTable.
 */
public class DeliveryReportingDAO {
    public List<Delivery> getDeliveriesByDate(LocalDate date) throws SQLException {
        if (date == null) throw new IllegalArgumentException("Date is required");
        Map<Integer, Delivery> byOrder = new LinkedHashMap<>();
        try (Connection c = DatabaseConnection.getConnection()) {
            // Include the initial stock status even before Patrick records delivery.
            String lines = "SELECT orderId,customerId,publicationId,deliveryDate,deliveryStatus "
                    + "FROM DeliveryDocketLineTable WHERE deliveryDate=? ORDER BY docketLineId";
            try (PreparedStatement ps = c.prepareStatement(lines)) {
                ps.setDate(1, java.sql.Date.valueOf(date));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String status = rs.getString("deliveryStatus");
                        if (!"RETURNED".equals(status)) {
                            int orderId = rs.getInt("orderId");
                            byOrder.put(orderId, new Delivery(0, orderId,
                                rs.getInt("customerId"), rs.getInt("publicationId"),
                                rs.getDate("deliveryDate").toLocalDate(), status, null));
                        }
                    }
                }
            }
            // Recorded results override initial statuses; latest record wins.
            String recorded = "SELECT * FROM DeliveryTable WHERE deliveryDate=? ORDER BY deliveryId";
            try (PreparedStatement ps = c.prepareStatement(recorded)) {
                ps.setDate(1, java.sql.Date.valueOf(date));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int orderId = rs.getInt("orderId");
                        Delivery d = new Delivery(rs.getInt("deliveryId"), orderId,
                            rs.getInt("customerId"), rs.getInt("publicationId"),
                            rs.getDate("deliveryDate").toLocalDate(),
                            rs.getString("deliveryStatus"), rs.getString("missedReason"));
                        byOrder.put(orderId, d);
                    }
                }
            }
        }
        return new ArrayList<>(byOrder.values());
    }
}
