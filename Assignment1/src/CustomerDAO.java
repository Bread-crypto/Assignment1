import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {

    // ===== SQL commands =====

    // CREATE
    private static final String INSERT_CUSTOMER =
            "INSERT INTO CustomerTable "
          + "(firstName, surname, address, phoneNumber, deliveryAreaId) "
          + "VALUES (?, ?, ?, ?, ?)";

    // READ ONE
    private static final String SELECT_BY_ID =
            "SELECT * FROM CustomerTable WHERE customerId = ?";

    // READ BY SURNAME (User Story 26)
    private static final String SELECT_BY_SURNAME =
            "SELECT * FROM CustomerTable WHERE surname = ?";

    // READ ALL
    private static final String SELECT_ALL =
            "SELECT * FROM CustomerTable";

    // UPDATE (User Story 26)
    private static final String UPDATE_CUSTOMER =
            "UPDATE CustomerTable "
          + "SET firstName = ?, surname = ?, address = ?, phoneNumber = ?, deliveryAreaId = ? "
          + "WHERE customerId = ?";

    // CLOSE ACCOUNT (User Story 7)
    private static final String CLOSE_CUSTOMER =
            "UPDATE CustomerTable SET status = 'CLOSED' WHERE customerId = ?";

    // DELETE
    private static final String DELETE_CUSTOMER =
            "DELETE FROM CustomerTable WHERE customerId = ?";

    // ===== CREATE =====
    public boolean addCustomer(Customer customer) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CUSTOMER)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getSurname());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setInt(5, customer.getDeliveryAreaId());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== READ ONE =====
    public Customer findCustomerById(int customerId) {
        Customer customer = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customer = mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    // ===== READ BY SURNAME =====
    public ArrayList<Customer> findCustomersBySurname(String surname) {
        ArrayList<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_SURNAME)) {

            stmt.setString(1, surname);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // ===== READ ALL =====
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // ===== UPDATE =====
    public boolean updateCustomer(Customer customer) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CUSTOMER)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getSurname());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setInt(5, customer.getDeliveryAreaId());
            stmt.setInt(6, customer.getCustomerId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== CLOSE ACCOUNT =====
    public boolean closeCustomer(int customerId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CLOSE_CUSTOMER)) {

            stmt.setInt(1, customerId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== DELETE =====
    public boolean deleteCustomer(int customerId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CUSTOMER)) {

            stmt.setInt(1, customerId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Helper: turn one database row into a Customer object =====
    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customerId"));
        customer.setFirstName(rs.getString("firstName"));
        customer.setSurname(rs.getString("surname"));
        customer.setAddress(rs.getString("address"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        customer.setDeliveryAreaId(rs.getInt("deliveryAreaId"));
        customer.setStatus(rs.getString("status"));
        return customer;
    }
}