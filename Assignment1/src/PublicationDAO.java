import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PublicationDAO {

    public PublicationDAO() {
        createPriceHistoryTable();
    }

    // Create price history table.
    // PublicationTable is created by the database SQL script.

    private void createPriceHistoryTable() {

        String sql = "CREATE TABLE IF NOT EXISTS PublicationPriceHistoryTable ("
                + "priceHistoryId INT AUTO_INCREMENT PRIMARY KEY, "
                + "publicationId INT NOT NULL, "
                + "price DECIMAL(6,2) NOT NULL, "
                + "startDate DATE NOT NULL, "
                + "FOREIGN KEY (publicationId) "
                + "REFERENCES PublicationTable(publicationId) "
                + "ON DELETE CASCADE, "
                + "UNIQUE (publicationId, startDate))";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("Could not create price history table: "
                    + e.getMessage());
        }
    }

    // Add publication

    public boolean addPublication(Publication publication) {

        if (publication == null
                || publication.getPublicationName() == null) {
            return false;
        }

        String sql = "INSERT INTO PublicationTable "
                + "(publicationName, publicationType, publicationPrice, "
                + "stockQuantity, lowStockThreshold, "
                + "publicationFrequency, releaseDay) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                    connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, publication.getPublicationName());
                statement.setString(2, publication.getPublicationType());
                statement.setDouble(3, publication.getPublicationPrice());
                statement.setInt(4, publication.getStockQuantity());
                statement.setInt(5, publication.getLowStockThreshold());
                statement.setString(6, publication.getPublicationFrequency());
                statement.setString(7, publication.getReleaseDay());

                if (statement.executeUpdate() != 1) {
                    connection.rollback();
                    return false;
                }

                try (ResultSet keys = statement.getGeneratedKeys()) {

                    if (!keys.next()) {
                        connection.rollback();
                        return false;
                    }

                    publication.setPublicationId(keys.getInt(1));
                }

                savePriceHistory(connection,
                        publication.getPublicationId(),
                        publication.getPublicationPrice(),
                        LocalDate.now());

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Could not add publication: "
                        + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: "
                    + e.getMessage());
            return false;
        }
    }

    // Save price history

    private void savePriceHistory(Connection connection,
                                  int id,
                                  double price,
                                  LocalDate startDate)
            throws SQLException {

        String sql = "INSERT INTO PublicationPriceHistoryTable "
                + "(publicationId, price, startDate) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setDouble(2, price);
            statement.setDate(3, Date.valueOf(startDate));

            statement.executeUpdate();
        }
    }

    // Find publication by ID

    public Publication findById(int id) {

        String sql = "SELECT * FROM PublicationTable WHERE publicationId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return readPublication(result);
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not find publication: "
                    + e.getMessage());
        }

        return null;
    }

    // Find publication by name

    public Publication findByName(String name) {

        String sql = "SELECT * FROM PublicationTable "
                + "WHERE LOWER(publicationName) = LOWER(?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return readPublication(result);
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not find publication: "
                    + e.getMessage());
        }

        return null;
    }

    // Get all publications

    public List<Publication> getAllPublications() {

        List<Publication> publications = new ArrayList<>();

        String sql = "SELECT * FROM PublicationTable ORDER BY publicationName";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                publications.add(readPublication(result));
            }

        } catch (SQLException e) {
            System.out.println("Could not list publications: "
                    + e.getMessage());
        }

        return publications;
    }

    // Update publication

    public boolean updatePublication(Publication publication) {

        if (publication == null) {
            return false;
        }

        String sql = "UPDATE PublicationTable SET "
                + "publicationName=?, "
                + "publicationType=?, "
                + "publicationPrice=?, "
                + "stockQuantity=?, "
                + "lowStockThreshold=?, "
                + "publicationFrequency=?, "
                + "releaseDay=? "
                + "WHERE publicationId=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, publication.getPublicationName());
            statement.setString(2, publication.getPublicationType());
            statement.setDouble(3, publication.getPublicationPrice());
            statement.setInt(4, publication.getStockQuantity());
            statement.setInt(5, publication.getLowStockThreshold());
            statement.setString(6, publication.getPublicationFrequency());
            statement.setString(7, publication.getReleaseDay());
            statement.setInt(8, publication.getPublicationId());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Could not update publication: "
                    + e.getMessage());
            return false;
        }
    }

    // Change publication price and record history

    public boolean changePrice(int id,
                               double newPrice,
                               LocalDate startDate) {

        Publication publication = findById(id);

        if (publication == null
                || !publication.verifyPublicationPrice(newPrice)
                || !publication.verifyPriceStartDate(startDate)) {
            return false;
        }

        String latestDateSql =
                "SELECT MAX(startDate) FROM PublicationPriceHistoryTable "
                + "WHERE publicationId=?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                LocalDate latestDate = null;

                try (PreparedStatement check =
                        connection.prepareStatement(latestDateSql)) {

                    check.setInt(1, id);

                    try (ResultSet result = check.executeQuery()) {

                        if (result.next() && result.getDate(1) != null) {
                            latestDate = result.getDate(1).toLocalDate();
                        }
                    }
                }

                if (latestDate != null
                        && !startDate.isAfter(latestDate)) {
                    connection.rollback();
                    return false;
                }

                savePriceHistory(connection, id, newPrice, startDate);

                if (!startDate.isAfter(LocalDate.now())) {

                    String updateSql =
                            "UPDATE PublicationTable "
                            + "SET publicationPrice=? WHERE publicationId=?";

                    try (PreparedStatement update =
                            connection.prepareStatement(updateSql)) {

                        update.setDouble(1, newPrice);
                        update.setInt(2, id);

                        if (update.executeUpdate() != 1) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Could not save price change: "
                        + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: "
                    + e.getMessage());
            return false;
        }
    }

    // Update stock quantity

    public boolean updateStock(int id, int quantity) {

        if (quantity < 0) {
            return false;
        }

        String sql = "UPDATE PublicationTable "
                + "SET stockQuantity=? WHERE publicationId=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, id);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Could not update stock: "
                    + e.getMessage());
            return false;
        }
    }

    // Remove publication

    public boolean removePublication(int id) {

        String sql = "DELETE FROM PublicationTable WHERE publicationId=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Could not remove publication: "
                    + e.getMessage());
            return false;
        }
    }

    // Convert database record into Publication object

    private Publication readPublication(ResultSet result)
            throws SQLException {

        Publication publication = new Publication();

        publication.setPublicationId(
                result.getInt("publicationId"));

        publication.createPublication(
                result.getString("publicationName"),
                result.getString("publicationType"),
                result.getDouble("publicationPrice"),
                result.getInt("stockQuantity"));

        publication.setLowStockThreshold(
                result.getInt("lowStockThreshold"));

        publication.setPublicationSchedule(
                result.getString("publicationFrequency"),
                result.getString("releaseDay"));

        return publication;
    }
}