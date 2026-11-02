import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class HolidayDAO {

    // Adds a holiday period for a customer.
    // The start date must be today or in the future.
    // The end date must be on or after the start date.
    public boolean addHoliday(Holiday holiday) {

        // Validate the holiday dates before accessing the database.
        if (holiday.getHolidayStartDate() == null
                || holiday.getHolidayEndDate() == null) {
            return false;
        }

        if (holiday.getHolidayStartDate().isBefore(LocalDate.now())) {
            return false;
        }

        if (holiday.getHolidayEndDate().isBefore(
                holiday.getHolidayStartDate())) {
            return false;
        }

        // SQL statement to save the holiday period.
        String sql = "INSERT INTO HolidayTable "
                + "(customerId, holidayStartDate, holidayEndDate) "
                + "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the customer and holiday dates.
            stmt.setInt(1, holiday.getCustomerId());
            stmt.setDate(2, java.sql.Date.valueOf(
                    holiday.getHolidayStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(
                    holiday.getHolidayEndDate()));

            // Return true if the holiday was saved.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Report database errors and indicate the operation failed.
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves all holiday periods belonging to a customer.
    public List<Holiday> getHolidaysByCustomer(int customerId) {

        List<Holiday> holidays = new ArrayList<>();

        // Find holiday records for the specified customer.
        String sql = "SELECT * FROM HolidayTable WHERE customerId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            // Convert each database record into a Holiday object.
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    holidays.add(new Holiday(
                        rs.getInt("holidayId"),
                        rs.getInt("customerId"),
                        rs.getDate("holidayStartDate").toLocalDate(),
                        rs.getDate("holidayEndDate").toLocalDate()
                    ));
                }
            }

        } catch (SQLException e) {
            // Report database errors.
            e.printStackTrace();
        }

        return holidays;
    }
}