import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/** Story 23: reads the recorded docket lines for a customer. */
public class DeliveryHistoryDAO {
    public boolean customerExists(int customerId) throws SQLException {
        if (customerId <= 0) return false;
        String sql = "SELECT 1 FROM CustomerTable WHERE customerId = ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, customerId); try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
    public List<DeliveryHistoryEntry> getHistory(int customerId, YearMonth month) throws SQLException {
        if (customerId <= 0 || !customerExists(customerId)) throw new IllegalArgumentException("Valid customer ID required.");
        String sql = "SELECT dl.deliveryDate, p.publicationName, dl.deliveryStatus " +
        		"FROM DeliveryTable dl JOIN publication p ON p.publicationId=dl.publicationId " +
          "WHERE dl.customerId=? AND dl.deliveryStatus IN ('DELIVERED','MISSED') AND (? IS NULL OR (YEAR(dl.deliveryDate)=? AND MONTH(dl.deliveryDate)=?)) " +
          "ORDER BY dl.deliveryDate DESC, p.publicationName";
        List<DeliveryHistoryEntry> out = new ArrayList<>();
        try (Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            if (month == null) { ps.setNull(2, Types.DATE); ps.setNull(3, Types.INTEGER); ps.setNull(4, Types.INTEGER); }
            else { ps.setDate(2, Date.valueOf(month.atDay(1))); ps.setInt(3, month.getYear()); ps.setInt(4, month.getMonthValue()); }
            try (ResultSet rs=ps.executeQuery()) { while(rs.next()) out.add(new DeliveryHistoryEntry(rs.getDate("deliveryDate").toLocalDate(), rs.getString("publicationName"), rs.getString("deliveryStatus"))); }
        }
        return out;
    }
}
