
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Connects orders to the publication records.
    private final PublicationDAO publicationDAO = new PublicationDAO();

    // Story 2: Adds an order for an existing publication.
    public boolean addOrder(Order order) {

        // Check that the order contains valid information.
        if (order == null
                || order.getDeliveryDays() == null
                || order.getDeliveryDays().isBlank()) {
            return false;
        }

        // The publication must exist in the publication table.
        if (publicationDAO.findById(order.getPublicationId()) == null) {
            System.out.println("Publication does not exist.");
            return false;
        }
        
        // Story 5: Prevent duplicate active orders for the same publication.
        for (Order existingOrder : getOrdersByCustomer(order.getCustomerId())) {
            if (existingOrder.getPublicationId() == order.getPublicationId()
                    && existingOrder.getOrderEndDate() == null) {
                System.out.println("This publication is already in the customer's active orders.");
                return false;
            }
        }

        // The start date must be today or in the future.
        if (order.getOrderStartDate() == null
                || order.getOrderStartDate().isBefore(LocalDate.now())) {
            return false;
        }

        // The end date cannot be before the start date.
        if (order.getOrderEndDate() != null
                && order.getOrderEndDate().isBefore(order.getOrderStartDate())) {
            return false;
        }

        String sql = "INSERT INTO OrderTable "
                + "(customerId, publicationId, deliveryDays, orderStartDate, orderEndDate) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getCustomerId());
            stmt.setInt(2, order.getPublicationId());
            stmt.setString(3, order.getDeliveryDays());
            stmt.setDate(4, Date.valueOf(order.getOrderStartDate()));

            if (order.getOrderEndDate() == null) {
                stmt.setNull(5, Types.DATE);
            } else {
                stmt.setDate(5, Date.valueOf(order.getOrderEndDate()));
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves all orders belonging to a customer.
    public List<Order> getOrdersByCustomer(int customerId) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM OrderTable WHERE customerId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Date endDate = rs.getDate("orderEndDate");

                    orders.add(new Order(
                            rs.getInt("orderId"),
                            rs.getInt("customerId"),
                            rs.getInt("publicationId"),
                            rs.getString("deliveryDays"),
                            rs.getDate("orderStartDate").toLocalDate(),
                            endDate == null ? null : endDate.toLocalDate()
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // Story 4: Removes a publication from a customer's active orders.
    // The order is retained in the database for billing history.
    public boolean removePublication(int customerId, int publicationId,
                                     LocalDate endDate) {

        // Check that the end date is valid.
        if (endDate == null || endDate.isBefore(LocalDate.now())) {
            return false;
        }

        String sql = "UPDATE OrderTable SET orderEndDate = ? "
                + "WHERE customerId = ? AND publicationId = ? "
                + "AND orderEndDate IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(endDate));
            stmt.setInt(2, customerId);
            stmt.setInt(3, publicationId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
