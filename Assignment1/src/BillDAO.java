import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BillDAO {

    // ===== SQL commands =====

    // CREATE (User Story 19)
    private static final String INSERT_BILL =
            "INSERT INTO BillTable "
          + "(customerId, billingYear, billingMonth, totalAmount, billStatus) "
          + "VALUES (?, ?, ?, ?, 'UNPAID')";

    // READ ONE
    private static final String SELECT_BY_ID =
            "SELECT * FROM BillTable WHERE billId = ?";

    // A bill for the same customer and month cannot be generated twice (User Stories 19, 28)
    private static final String SELECT_BY_CUSTOMER_AND_MONTH =
            "SELECT * FROM BillTable WHERE customerId = ? AND billingYear = ? AND billingMonth = ?";

    // UNPAID BILLS (User Story 22)
    private static final String SELECT_UNPAID =
            "SELECT * FROM BillTable WHERE billStatus = 'UNPAID' "
          + "ORDER BY billingYear, billingMonth, customerId";

    // RECORD PAYMENT (User Story 21)
    private static final String UPDATE_PAID =
            "UPDATE BillTable SET billStatus = 'PAID', paymentDate = ? WHERE billId = ?";

    // DELIVERED ITEMS FOR ONE CUSTOMER AND MONTH (User Stories 19, 20)
    // - only DELIVERED lines are charged: MISSED and OUT OF STOCK are left out (US 15, 24)
    // - customers on holiday get no docket line, so holiday days are never charged (US 3)
    // - the price is the one valid on the delivery date (US 29): the latest price in
    //   publication_price_history that started on or before that date, otherwise the
    //   current price in the publication table
    private static final String SELECT_DELIVERED_ITEMS =
            "SELECT d.deliveryDate, p.publicationName, "
          + "COALESCE((SELECT h.price FROM publication_price_history h "
          + "          WHERE h.publicationId = d.publicationId AND h.startDate <= d.deliveryDate "
          + "          ORDER BY h.startDate DESC LIMIT 1), p.publicationPrice) AS price "
          + "FROM DeliveryTable d "
          + "JOIN publication p ON d.publicationId = p.publicationId "
          + "WHERE d.customerId = ? AND d.deliveryStatus = 'DELIVERED' "
          + "AND YEAR(d.deliveryDate) = ? AND MONTH(d.deliveryDate) = ? "
          + "ORDER BY d.deliveryDate";

    // ===== CREATE - returns the new billId, or -1 if it failed =====
    public int addBill(Bill bill) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_BILL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bill.getCustomerId());
            stmt.setInt(2, bill.getBillingYear());
            stmt.setInt(3, bill.getBillingMonth());
            stmt.setDouble(4, bill.getTotalAmount());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    bill.setBillId(newId);
                    return newId;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ===== READ ONE =====
    public Bill findBillById(int billId) {
        Bill bill = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, billId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bill = mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }

    // ===== DOES A BILL ALREADY EXIST FOR THIS CUSTOMER AND MONTH? =====
    public boolean billExists(int customerId, int year, int month) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CUSTOMER_AND_MONTH)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, year);
            stmt.setInt(3, month);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== UNPAID BILLS =====
    public ArrayList<Bill> getUnpaidBills() {
        ArrayList<Bill> bills = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_UNPAID);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bills.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    // ===== RECORD PAYMENT =====
    public boolean markBillPaid(Bill bill) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PAID)) {

            stmt.setDate(1, Date.valueOf(bill.getPaymentDate()));
            stmt.setInt(2, bill.getBillId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== DELIVERED ITEMS =====
    public ArrayList<BillItem> getDeliveredItems(int customerId, int year, int month) {
        ArrayList<BillItem> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_DELIVERED_ITEMS)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, year);
            stmt.setInt(3, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new BillItem(
                            rs.getDate("deliveryDate").toLocalDate(),
                            rs.getString("publicationName"),
                            rs.getDouble("price")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // ===== Helper: one row of BillTable -> one Bill object =====
    private Bill mapRow(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("billId"));
        bill.setCustomerId(rs.getInt("customerId"));
        bill.setBillingYear(rs.getInt("billingYear"));
        bill.setBillingMonth(rs.getInt("billingMonth"));
        bill.setTotalAmount(rs.getDouble("totalAmount"));
        bill.setBillStatus(rs.getString("billStatus"));
        Date paid = rs.getDate("paymentDate");
        if (paid != null) {
            bill.setPaymentDate(paid.toLocalDate());
        }
        return bill;
    }
}
