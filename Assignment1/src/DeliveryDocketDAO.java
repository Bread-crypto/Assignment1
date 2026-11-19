
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryDocketDAO {

    // ===== SQL Statements =====

    private static final String INSERT_DOCKET =
            "INSERT INTO DeliveryDocketTable "
            + "(docketDate, deliveryAreaId, deliveryPersonId) "
            + "VALUES (?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM DeliveryDocketTable WHERE docketId = ?";

    private static final String SELECT_ALL =
            "SELECT * FROM DeliveryDocketTable";
    

    // ===== Add Delivery Docket =====

    public boolean addDeliveryDocket(DeliveryDocket docket) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_DOCKET)) {

            stmt.setDate(1, java.sql.Date.valueOf(docket.getDocketDate()));
            stmt.setInt(2, docket.getDeliveryAreaId());
            stmt.setInt(3, docket.getDeliveryPersonId());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    // ===== Find Delivery Docket By ID =====

    public DeliveryDocket findDeliveryDocketById(int docketId) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, docketId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                DeliveryDocket docket = new DeliveryDocket();

                docket.setDocketId(rs.getInt("docketId"));
                docket.setDocketDate(
                        rs.getDate("docketDate").toLocalDate());
                docket.setDeliveryAreaId(rs.getInt("deliveryAreaId"));
                docket.setDeliveryPersonId(rs.getInt("deliveryPersonId"));

                return docket;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    

    // ===== Get All Delivery Dockets =====

    public ArrayList<DeliveryDocket> getAllDeliveryDockets() {

        ArrayList<DeliveryDocket> dockets = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DeliveryDocket docket = new DeliveryDocket();

                docket.setDocketId(rs.getInt("docketId"));
                docket.setDocketDate(
                        rs.getDate("docketDate").toLocalDate());
                docket.setDeliveryAreaId(rs.getInt("deliveryAreaId"));
                docket.setDeliveryPersonId(rs.getInt("deliveryPersonId"));

                dockets.add(docket);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dockets;
    }




}
