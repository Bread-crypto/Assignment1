import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryPersonDAO {

    // ===== SQL commands =====

    // CREATE
    private static final String INSERT_DELIVERY_PERSON =
            "INSERT INTO DeliveryPersonTable "
          + "(firstName, surname, phoneNumber, status) "
          + "VALUES (?, ?, ?, ?)";


    // READ ONE
    private static final String SELECT_BY_ID =
            "SELECT * FROM DeliveryPersonTable "
          + "WHERE deliveryPersonId = ?";


    // READ ALL
    private static final String SELECT_ALL =
            "SELECT * FROM DeliveryPersonTable";


    // UPDATE
    private static final String UPDATE_DELIVERY_PERSON =
            "UPDATE DeliveryPersonTable "
          + "SET firstName = ?, surname = ?, phoneNumber = ?, status = ? "
          + "WHERE deliveryPersonId = ?";


    // MARK INACTIVE
    private static final String MARK_INACTIVE =
            "UPDATE DeliveryPersonTable "
          + "SET status = 'INACTIVE' "
          + "WHERE deliveryPersonId = ?";


    // ===== CREATE =====

    public boolean addDeliveryPerson(DeliveryPerson person) {

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(INSERT_DELIVERY_PERSON)) {

            stmt.setString(
                    1,
                    person.getFirstName());

            stmt.setString(
                    2,
                    person.getSurname());

            stmt.setString(
                    3,
                    person.getPhoneNumber());

            stmt.setString(
                    4,
                    person.getStatus());

            int rowsInserted =
                    stmt.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===== READ ONE =====

    public DeliveryPerson findDeliveryPersonById(
            int deliveryPersonId) {

        DeliveryPerson person = null;

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(
                    1,
                    deliveryPersonId);

            try (ResultSet rs =
                         stmt.executeQuery()) {

                if (rs.next()) {

                    person =
                            mapRow(rs);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return person;
    }


    // ===== READ ALL =====

    public ArrayList<DeliveryPerson>
    getAllDeliveryPersons() {

        ArrayList<DeliveryPerson> people =
                new ArrayList<>();

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_ALL);

             ResultSet rs =
                     stmt.executeQuery()) {

            while (rs.next()) {

                people.add(
                        mapRow(rs));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return people;
    }


    // ===== UPDATE =====

    public boolean updateDeliveryPerson(
            DeliveryPerson person) {

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(
                             UPDATE_DELIVERY_PERSON)) {

            stmt.setString(
                    1,
                    person.getFirstName());

            stmt.setString(
                    2,
                    person.getSurname());

            stmt.setString(
                    3,
                    person.getPhoneNumber());

            stmt.setString(
                    4,
                    person.getStatus());

            stmt.setInt(
                    5,
                    person.getDeliveryPersonId());

            int rowsUpdated =
                    stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===== MARK INACTIVE =====

    public boolean markInactive(
            int deliveryPersonId) {

        try (Connection conn =
                     DatabaseConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(MARK_INACTIVE)) {

            stmt.setInt(
                    1,
                    deliveryPersonId);

            int rowsUpdated =
                    stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // ===== Helper =====

    private DeliveryPerson mapRow(
            ResultSet rs)
            throws SQLException {

        DeliveryPerson person =
                new DeliveryPerson();

        person.setDeliveryPersonId(
                rs.getInt(
                        "deliveryPersonId"));

        person.setFirstName(
                rs.getString(
                        "firstName"));

        person.setSurname(
                rs.getString(
                        "surname"));

        person.setPhoneNumber(
                rs.getString(
                        "phoneNumber"));

        person.setStatus(
                rs.getString(
                        "status"));

        return person;
    }
}