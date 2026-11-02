import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDAO {

    public boolean addOrder(Order order) {
    	
    	if (order.getDeliveryDays() == null
    	        || order.getDeliveryDays().isBlank()) {
    	    return false;
    	}

    	if (order.getOrderStartDate() == null
    	        || order.getOrderStartDate().isBefore(
    	                java.time.LocalDate.now())) {
    	    return false;
    	}

    	if (order.getOrderEndDate() != null
    	        && order.getOrderEndDate().isBefore(
    	                order.getOrderStartDate())) {
    	    return false;
    	}

        // Story 2: At least one delivery day is required
        if (order.getDeliveryDays() == null
                || order.getDeliveryDays().isBlank()) {
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
            stmt.setDate(4,
                    java.sql.Date.valueOf(order.getOrderStartDate()));

            if (order.getOrderEndDate() == null) {
                stmt.setNull(5, java.sql.Types.DATE);
            } else {
                stmt.setDate(5,
                        java.sql.Date.valueOf(order.getOrderEndDate()));
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public java.util.List<Order> getOrdersByCustomer(int customerId) {
        java.util.List<Order> orders = new java.util.ArrayList<>();

        String sql = "SELECT * FROM OrderTable WHERE customerId = ?";

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date endDate = rs.getDate("orderEndDate");

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

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
    
    // Removes a publication from a customer's active orders.
    // Sets the end date instead of deleting the record, preserving
    // the order history for billing purposes.
    // Rejects past dates and returns false if no active order matches.
    public boolean removePublication(int customerId, int publicationId,
                                     java.time.LocalDate endDate) {

        // Validate that the end date is today or in the future.
        if (endDate == null
                || endDate.isBefore(java.time.LocalDate.now())) {
            return false;
        }

        // Update only an active order for this customer and publication.
        String sql = "UPDATE OrderTable SET orderEndDate = ? "
                   + "WHERE customerId = ? AND publicationId = ? "
                   + "AND orderEndDate IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Supply the end date and identify the order to update.
            stmt.setDate(1, java.sql.Date.valueOf(endDate));
            stmt.setInt(2, customerId);
            stmt.setInt(3, publicationId);

            // Return true if an order was successfully updated.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Report database errors and indicate the operation failed.
            e.printStackTrace();
            return false;
        }
    }
}