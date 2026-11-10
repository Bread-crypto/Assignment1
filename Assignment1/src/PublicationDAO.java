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
        createTablesIfNeeded();
    }

    private void createTablesIfNeeded() {
        String publicationTable = "CREATE TABLE IF NOT EXISTS publication ("
                + "publicationId INT AUTO_INCREMENT PRIMARY KEY,"
                + "publicationName VARCHAR(60) NOT NULL UNIQUE,"
                + "publicationType VARCHAR(20) NOT NULL,"
                + "publicationPrice DECIMAL(5,2) NOT NULL,"
                + "stockQuantity INT NOT NULL DEFAULT 0,"
                + "publicationFrequency VARCHAR(10) NOT NULL DEFAULT 'DAILY',"
                + "releaseDay VARCHAR(15) NOT NULL DEFAULT 'DAILY',"
                + "lowStockThreshold INT NOT NULL DEFAULT 5)";
        String priceTable = "CREATE TABLE IF NOT EXISTS publication_price_history ("
                + "priceHistoryId INT AUTO_INCREMENT PRIMARY KEY,"
                + "publicationId INT NOT NULL, price DECIMAL(5,2) NOT NULL,"
                + "startDate DATE NOT NULL,"
                + "FOREIGN KEY (publicationId) REFERENCES publication(publicationId) ON DELETE CASCADE)";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(publicationTable);
            statement.executeUpdate(priceTable);
        } catch (SQLException e) {
            System.out.println("Could not prepare publication tables: " + e.getMessage());
        }
    }

    public boolean addPublication(Publication publication) {
        if (publication == null) return false;
        String sql = "INSERT INTO publication (publicationName, publicationType, publicationPrice, "
                + "stockQuantity, publicationFrequency, releaseDay, lowStockThreshold) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, publication.getPublicationName());
            statement.setString(2, publication.getPublicationType());
            statement.setDouble(3, publication.getPublicationPrice());
            statement.setInt(4, publication.getStockQuantity());
            statement.setString(5, publication.getPublicationFrequency());
            statement.setString(6, publication.getReleaseDay());
            statement.setInt(7, publication.getLowStockThreshold());
            if (statement.executeUpdate() != 1) return false;
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) publication.setPublicationId(keys.getInt(1));
            }
            savePriceHistory(connection, publication.getPublicationId(), publication.getPublicationPrice(), LocalDate.now());
            return true;
        } catch (SQLException e) {
            System.out.println("Could not add publication: " + e.getMessage());
            return false;
        }
    }

    private void savePriceHistory(Connection connection, int id, double price, LocalDate startDate) throws SQLException {
        String sql = "INSERT INTO publication_price_history (publicationId, price, startDate) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setDouble(2, price);
            statement.setDate(3, Date.valueOf(startDate));
            statement.executeUpdate();
        }
    }

    public Publication findById(int id) {
        String sql = "SELECT * FROM publication WHERE publicationId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readPublication(result) : null;
            }
        } catch (SQLException e) {
            System.out.println("Could not find publication: " + e.getMessage());
            return null;
        }
    }

    public Publication findByName(String name) {
        String sql = "SELECT * FROM publication WHERE LOWER(publicationName) = LOWER(?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readPublication(result) : null;
            }
        } catch (SQLException e) {
            System.out.println("Could not find publication: " + e.getMessage());
            return null;
        }
    }

    public List<Publication> getAllPublications() {
        List<Publication> publications = new ArrayList<>();
        String sql = "SELECT * FROM publication ORDER BY publicationName";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) publications.add(readPublication(result));
        } catch (SQLException e) {
            System.out.println("Could not list publications: " + e.getMessage());
        }
        return publications;
    }

    public boolean updatePublication(Publication publication) {
        String sql = "UPDATE publication SET publicationName=?, publicationType=?, publicationPrice=?, "
                + "stockQuantity=?, publicationFrequency=?, releaseDay=?, lowStockThreshold=? WHERE publicationId=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, publication.getPublicationName());
            statement.setString(2, publication.getPublicationType());
            statement.setDouble(3, publication.getPublicationPrice());
            statement.setInt(4, publication.getStockQuantity());
            statement.setString(5, publication.getPublicationFrequency());
            statement.setString(6, publication.getReleaseDay());
            statement.setInt(7, publication.getLowStockThreshold());
            statement.setInt(8, publication.getPublicationId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Could not update publication: " + e.getMessage());
            return false;
        }
    }


    public boolean changePrice(int id, double newPrice, LocalDate startDate) {
        Publication publication = findById(id);
        if (publication == null || !publication.verifyPublicationPrice(newPrice)
                || !publication.verifyPriceStartDate(startDate)) return false;

        String updateSql = "UPDATE publication SET publicationPrice=? WHERE publicationId=?";
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement update = connection.prepareStatement(updateSql)) {
                update.setDouble(1, newPrice);
                update.setInt(2, id);
                if (update.executeUpdate() != 1) {
                    connection.rollback();
                    return false;
                }
                savePriceHistory(connection, id, newPrice, startDate);
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Could not save price change: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStock(int id, int quantity) {
        if (quantity < 0) return false;
        String sql = "UPDATE publication SET stockQuantity=? WHERE publicationId=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, quantity);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Could not update stock: " + e.getMessage());
            return false;
        }
    }

    public boolean removePublication(int id) {
        // The database should use a foreign key from customer orders to prevent unsafe removal.
        String sql = "DELETE FROM publication WHERE publicationId=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Could not remove publication: " + e.getMessage());
            return false;
        }
    }

    private Publication readPublication(ResultSet result) throws SQLException {
        Publication publication = new Publication();
        publication.setPublicationId(result.getInt("publicationId"));
        publication.createPublication(result.getString("publicationName"),
                result.getString("publicationType"), result.getDouble("publicationPrice"),
                result.getInt("stockQuantity"));
        publication.setPublicationFrequency(result.getString("publicationFrequency"));
        publication.setReleaseDay(result.getString("releaseDay"));
        publication.setLowStockThreshold(result.getInt("lowStockThreshold"));
        return publication;
    }
}
