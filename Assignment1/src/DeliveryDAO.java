import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Handles database operations for deliveries.
public class DeliveryDAO {

    // Saves a delivery to the database.
    public boolean addDelivery(Delivery delivery) {
        String sql = "INSERT INTO DeliveryTable "
                + "(orderId, customerId, publicationId, deliveryDate, "
                + "deliveryStatus, missedReason) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, delivery.getOrderId());
            statement.setInt(2, delivery.getCustomerId());
            statement.setInt(3, delivery.getPublicationId());
            statement.setDate(4,
                    java.sql.Date.valueOf(delivery.getDeliveryDate()));
            statement.setString(5, delivery.getDeliveryStatus());
            statement.setString(6, delivery.getMissedReason());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves completed deliveries for a customer's history.
    public List<Delivery> getCompletedDeliveriesByCustomer(int customerId) {
        List<Delivery> deliveries = new ArrayList<>();

        String sql = "SELECT * FROM DeliveryTable "
                + "WHERE customerId = ? AND deliveryStatus = 'DELIVERED' "
                + "ORDER BY deliveryDate DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Delivery delivery = new Delivery(
                            resultSet.getInt("deliveryId"),
                            resultSet.getInt("orderId"),
                            resultSet.getInt("customerId"),
                            resultSet.getInt("publicationId"),
                            resultSet.getDate("deliveryDate").toLocalDate(),
                            resultSet.getString("deliveryStatus"),
                            resultSet.getString("missedReason")
                    );

                    deliveries.add(delivery);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deliveries;
    }
}