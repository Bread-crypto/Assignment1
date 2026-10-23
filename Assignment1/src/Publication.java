import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Publication {

    private int publicationId;
    private String publicationName;
    private String publicationType;
    private double publicationPrice;
    private int stockQuantity;
    private String publicationFrequency = "DAILY";
    private String releaseDay = "DAILY";
    private int lowStockThreshold = 5;

    private final List<PriceChange> priceHistory = new ArrayList<>();

    private static class PriceChange {
        private final double price;
        private final LocalDate startDate;

        private PriceChange(double price, LocalDate startDate) {
            this.price = price;
            this.startDate = startDate;
        }
    }

    // Publication validation

    public boolean verifyPublicationName(String name) {
        return name != null && !name.trim().isEmpty()
                && name.trim().length() <= 60;
    }

    public boolean verifyPublicationType(String type) {
        return type != null &&
                (type.trim().equalsIgnoreCase("NEWSPAPER")
                || type.trim().equalsIgnoreCase("MAGAZINE"));
    }

    public boolean verifyPublicationPrice(double price) {
        return Double.isFinite(price) && price >= 0.01 && price <= 100.00;
    }

    public boolean verifyStockQuantity(int quantity) {
        return quantity >= 0;
    }

    // Create publication

    public boolean createPublication(String name, String type,
                                     double price, int quantity) {

        if (!verifyPublicationName(name)
                || !verifyPublicationType(type)
                || !verifyPublicationPrice(price)
                || !verifyStockQuantity(quantity)) {
            return false;
        }

        publicationName = name.trim();
        publicationType = type.trim().toUpperCase();
        publicationPrice = price;
        stockQuantity = quantity;

        publicationFrequency = "DAILY";
        releaseDay = "DAILY";
        lowStockThreshold = 5;

        priceHistory.clear();
        priceHistory.add(new PriceChange(price, LocalDate.MIN));

        return true;
    }

    // Price history

    public boolean verifyPriceStartDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now());
    }

    public boolean changePrice(double newPrice, LocalDate startDate) {

        if (!verifyPublicationPrice(newPrice)
                || !verifyPriceStartDate(startDate)
                || publicationName == null
                || priceHistory.isEmpty()) {
            return false;
        }

        PriceChange latest = priceHistory.get(priceHistory.size() - 1);

        if (!startDate.isAfter(latest.startDate)) {
            return false;
        }

        priceHistory.add(new PriceChange(newPrice, startDate));

        if (!startDate.isAfter(LocalDate.now())) {
            publicationPrice = newPrice;
        }

        return true;
    }

    public double getPriceOnDate(LocalDate date) {

        if (date == null || priceHistory.isEmpty()) {
            return 0.0;
        }

        PriceChange latest = priceHistory.get(0);

        for (PriceChange change : priceHistory) {
            if (!change.startDate.isAfter(date)
                    && change.startDate.isAfter(latest.startDate)) {
                latest = change;
            }
        }

        return latest.price;
    }

    public int getPriceHistorySize() {
        return priceHistory.size();
    }

    // Publication frequency and release day

    public boolean verifyPublicationFrequency(String frequency) {

        return frequency != null &&
                (frequency.trim().equalsIgnoreCase("DAILY")
                || frequency.trim().equalsIgnoreCase("WEEKLY")
                || frequency.trim().equalsIgnoreCase("MONTHLY"));
    }

    public boolean verifyReleaseDay(String frequency, String day) {

        if (!verifyPublicationFrequency(frequency)
                || day == null || day.trim().isEmpty()) {
            return false;
        }

        if (frequency.trim().equalsIgnoreCase("DAILY")) {
            return day.trim().equalsIgnoreCase("DAILY");
        }

        if (frequency.trim().equalsIgnoreCase("WEEKLY")) {
            try {
                DayOfWeek.valueOf(day.trim().toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        try {
            int number = Integer.parseInt(day.trim());
            return number >= 1 && number <= 28;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean setPublicationSchedule(String frequency, String day) {

        if (!verifyReleaseDay(frequency, day)) {
            return false;
        }

        publicationFrequency = frequency.trim().toUpperCase();

        if (publicationFrequency.equals("DAILY")) {
            releaseDay = "DAILY";
        } else if (publicationFrequency.equals("WEEKLY")) {
            releaseDay = day.trim().toUpperCase();
        } else {
            releaseDay = day.trim();
        }

        return true;
    }

    public boolean isDueOnDate(String frequency, String day, LocalDate date) {

        if (date == null || !verifyReleaseDay(frequency, day)) {
            return false;
        }

        if (frequency.trim().equalsIgnoreCase("DAILY")) {
            return true;
        }

        if (frequency.trim().equalsIgnoreCase("WEEKLY")) {
            return date.getDayOfWeek().name().equalsIgnoreCase(day.trim());
        }

        return date.getDayOfMonth() == Integer.parseInt(day.trim());
    }

    // Stock status

    public String getStockStatus(int quantity, int threshold) {

        if (quantity < 0 || threshold < 0) {
            throw new IllegalArgumentException(
                    "Stock and threshold cannot be negative.");
        }

        if (quantity == 0) {
            return "OUT OF STOCK";
        }

        if (quantity <= threshold) {
            return "LOW";
        }

        return "IN STOCK";
    }

    // Low stock warning

    public boolean verifyLowStockThreshold(int threshold) {
        return threshold >= 0;
    }

    public boolean isLowStockWarning(int quantity, int threshold) {

        return verifyStockQuantity(quantity)
                && verifyLowStockThreshold(threshold)
                && quantity <= threshold;
    }

    public String getLowStockWarning(String name, int quantity) {

        if (name == null || name.trim().isEmpty() || quantity < 0) {
            throw new IllegalArgumentException(
                    "A publication name and non-negative quantity are required.");
        }

        return "Low stock warning: " + name.trim()
                + " has " + quantity + " copies remaining.";
    }

    // Quantity received and returned validation

    public boolean verifyQuantityReceived(String quantity) {
        return isPositiveWholeNumber(quantity);
    }

    public boolean verifyQuantityReturned(String quantity) {
        return isPositiveWholeNumber(quantity);
    }

    private boolean isPositiveWholeNumber(String value) {

        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        try {
            return Integer.parseInt(value.trim()) >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean verifyReturnNotMoreThanIssued(int returned, int issued) {
        return returned >= 0 && issued >= 0 && returned <= issued;
    }

    // Receive and return stock

    private int addStockSafely(int currentStock, int quantity) {

        if (currentStock < 0 || quantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        return Math.addExact(currentStock, quantity);
    }

    public int receiveStock(int currentStock, int received) {
        return addStockSafely(currentStock, received);
    }

    public int returnStock(int currentStock, int returned) {
        return addStockSafely(currentStock, returned);
    }

    // User Story 13 - Stock on delivery dockets

    public boolean verifyStockAvailable(int availableStock, int requiredStock) {

        return availableStock >= 0
                && requiredStock > 0
                && availableStock >= requiredStock;
    }

    public int issueStock(int currentStock, int issued) {

        if (!verifyStockAvailable(currentStock, issued)) {
            throw new IllegalArgumentException(
                    "Insufficient stock available or invalid quantity.");
        }

        return currentStock - issued;
    }

    public int returnUndeliveredStock(int currentStock,
                                      int returned, int issued) {

        if (!verifyReturnNotMoreThanIssued(returned, issued)) {
            throw new IllegalArgumentException(
                    "Cannot return more copies than issued.");
        }

        return returnStock(currentStock, returned);
    }

    // Getters

    public int getPublicationId() {
        return publicationId;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public String getPublicationType() {
        return publicationType;
    }

    public double getPublicationPrice() {
        return publicationPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getPublicationFrequency() {
        return publicationFrequency;
    }

    public String getReleaseDay() {
        return releaseDay;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    // Setters

    public void setPublicationId(int id) {

        if (id < 0) {
            throw new IllegalArgumentException(
                    "Publication ID cannot be negative.");
        }

        publicationId = id;
    }

    public void setPublicationPrice(double price) {

        if (!verifyPublicationPrice(price)) {
            throw new IllegalArgumentException("Invalid publication price.");
        }

        publicationPrice = price;
    }

    public void setStockQuantity(int quantity) {

        if (!verifyStockQuantity(quantity)) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        stockQuantity = quantity;
    }

    public void setPublicationFrequency(String frequency) {

        if (!verifyPublicationFrequency(frequency)) {
            throw new IllegalArgumentException(
                    "Invalid publication frequency.");
        }

        publicationFrequency = frequency.trim().toUpperCase();

        if (publicationFrequency.equals("DAILY")) {
            releaseDay = "DAILY";
        }
    }

    public void setReleaseDay(String day) {

        if (!verifyReleaseDay(publicationFrequency, day)) {
            throw new IllegalArgumentException("Invalid release day.");
        }

        if (publicationFrequency.equals("DAILY")) {
            releaseDay = "DAILY";
        } else if (publicationFrequency.equals("WEEKLY")) {
            releaseDay = day.trim().toUpperCase();
        } else {
            releaseDay = day.trim();
        }
    }

    public void setLowStockThreshold(int threshold) {

        if (!verifyLowStockThreshold(threshold)) {
            throw new IllegalArgumentException(
                    "Threshold cannot be negative.");
        }

        lowStockThreshold = threshold;
    }

    // Display publication details

    @Override
    public String toString() {

        return publicationId + " | " + publicationName
                + " | " + publicationType
                + " | €" + String.format("%.2f", publicationPrice)
                + " | Stock: " + stockQuantity;
    }
}