import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryAreaDAO {

    // ===== SQL commands =====

    // CREATE
    private static final String INSERT_DELIVERY_AREA =
            "INSERT INTO DeliveryAreaTable "
          + "(areaName, deliveryPersonId) "
          + "VALUES (?, ?)";


    // READ ONE
    private static final String SELECT_BY_ID =
            "SELECT * FROM DeliveryAreaTable "
          + "WHERE deliveryAreaId = ?";


    // READ ALL
    private static final String SELECT_ALL =
            "SELECT * FROM DeliveryAreaTable";


    // UPDATE
    private static final String UPDATE_DELIVERY_AREA =
            "UPDATE DeliveryAreaTable "
          + "SET areaName = ? "
          + "WHERE deliveryAreaId = ?";


    // ASSIGN DELIVERY PERSON - User Story 12
    private static final String ASSIGN_DELIVERY_PERSON =
            "UPDATE DeliveryAreaTable "
          + "SET deliveryPersonId = ? "
          + "WHERE deliveryAreaId = ?";


    // ===== CREATE =====

    public boolean addDeliveryArea(DeliveryArea area) {

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_DELIVERY_AREA)) {

            stmt.setString(
                    1,
                    area.getAreaName());

            stmt.setInt(
                    2,
                    area.getDeliveryPersonId());

            int rowsInserted =
                    stmt.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===== READ ONE =====

    public DeliveryArea findDeliveryAreaById(
            int deliveryAreaId) {

        DeliveryArea area = null;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(
                    1,
                    deliveryAreaId);

            try (ResultSet rs =
                         stmt.executeQuery()) {

                if (rs.next()) {

                    area =
                            mapRow(rs);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return area;
    }


    // ===== READ ALL =====

    public ArrayList<DeliveryArea>
    getAllDeliveryAreas() {

        ArrayList<DeliveryArea> areas =
                new ArrayList<>();

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_ALL);

             ResultSet rs =
                     stmt.executeQuery()) {

            while (rs.next()) {

                areas.add(
                        mapRow(rs));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return areas;
    }


    // ===== UPDATE =====

    public boolean updateDeliveryArea(
            DeliveryArea area) {

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(
                             UPDATE_DELIVERY_AREA)) {

            stmt.setString(
                    1,
                    area.getAreaName());

            stmt.setInt(
                    2,
                    area.getDeliveryAreaId());

            int rowsUpdated =
                    stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===== ASSIGN DELIVERY PERSON =====

    public boolean assignDeliveryPerson(
            int deliveryAreaId,
            int deliveryPersonId) {

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(
                             ASSIGN_DELIVERY_PERSON)) {

            stmt.setInt(
                    1,
                    deliveryPersonId);

            stmt.setInt(
                    2,
                    deliveryAreaId);

            int rowsUpdated =
                    stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===== Helper =====

    private DeliveryArea mapRow(
            ResultSet rs)
            throws SQLException {

        DeliveryArea area =
                new DeliveryArea();

        area.setDeliveryAreaId(
                rs.getInt(
                        "deliveryAreaId"));

        area.setAreaName(
                rs.getString(
                        "areaName"));

        area.setDeliveryPersonId(
                rs.getInt(
                        "deliveryPersonId"));

        return area;
    }
}