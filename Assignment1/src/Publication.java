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

    public boolean verifyPublicationName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 60;
    }

    public boolean verifyPublicationType(String type) {
        return type != null && (type.equalsIgnoreCase("NEWSPAPER")
                || type.equalsIgnoreCase("MAGAZINE"));
    }

    public boolean verifyPublicationPrice(double price) {
        return Double.isFinite(price) && price >= 0.01 && price <= 100.00;
    }

    public boolean verifyStockQuantity(int quantity) {
        return quantity >= 0;
    }

    public boolean createPublication(String name, String type, double price, int quantity) {
        if (!verifyPublicationName(name) || !verifyPublicationType(type)
                || !verifyPublicationPrice(price) || !verifyStockQuantity(quantity)) {
            return false;
        }
        publicationName = name.trim();
        publicationType = type.trim().toUpperCase();
        publicationPrice = price;
        stockQuantity = quantity;
        priceHistory.clear();
        priceHistory.add(new PriceChange(price, LocalDate.MIN));
        return true;
    }

    public boolean verifyPriceStartDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now());
    }

    public boolean changePrice(double newPrice, LocalDate startDate) {
        if (!verifyPublicationPrice(newPrice) || !verifyPriceStartDate(startDate)
                || publicationName == null) {
            return false;
        }
        priceHistory.add(new PriceChange(newPrice, startDate));
        publicationPrice = newPrice;
        return true;
    }

    public double getPriceOnDate(LocalDate date) {
        if (date == null || priceHistory.isEmpty()) return 0.0;
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

    public boolean verifyPublicationFrequency(String frequency) {
        return frequency != null && (frequency.equalsIgnoreCase("DAILY")
                || frequency.equalsIgnoreCase("WEEKLY") || frequency.equalsIgnoreCase("MONTHLY"));
    }

    public boolean verifyReleaseDay(String frequency, String day) {
        if (!verifyPublicationFrequency(frequency) || day == null) return false;
        if (frequency.equalsIgnoreCase("DAILY")) return true;
        if (frequency.equalsIgnoreCase("WEEKLY")) {
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

    public boolean isDueOnDate(String frequency, String day, LocalDate date) {
        if (date == null || !verifyReleaseDay(frequency, day)) return false;
        if (frequency.equalsIgnoreCase("DAILY")) return true;
        if (frequency.equalsIgnoreCase("WEEKLY")) {
            return date.getDayOfWeek().name().equalsIgnoreCase(day.trim());
        }
        return date.getDayOfMonth() == Integer.parseInt(day.trim());
    }

    public String getStockStatus(int quantity, int threshold) {
        if (quantity <= 0) return "OUT OF STOCK";
        if (quantity <= threshold) return "LOW";
        return "IN STOCK";
    }

    public boolean verifyLowStockThreshold(int threshold) {
        return threshold >= 0;
    }

    public boolean isLowStockWarning(int quantity, int threshold) {
        return verifyStockQuantity(quantity) && verifyLowStockThreshold(threshold)
                && quantity <= threshold;
    }

    public String getLowStockWarning(String name, int quantity) {
        return "Low stock warning: " + name + " has " + quantity + " copies remaining.";
    }

    public boolean verifyQuantityReceived(String quantity) {
        return isPositiveWholeNumber(quantity);
    }

    public boolean verifyQuantityReturned(String quantity) {
        return isPositiveWholeNumber(quantity);
    }

    private boolean isPositiveWholeNumber(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            return Integer.parseInt(value.trim()) >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean verifyReturnNotMoreThanIssued(int returned, int issued) {
        return returned >= 0 && issued >= 0 && returned <= issued;
    }

    public int receiveStock(int currentStock, int received) {
        if (currentStock < 0 || received < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        return currentStock + received;
    }

    public int returnStock(int currentStock, int returned) {
        if (currentStock < 0 || returned < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        return currentStock + returned;
    }

    public int getPublicationId() { return publicationId; }
    public void setPublicationId(int id) { publicationId = id; }
    public String getPublicationName() { return publicationName; }
    public String getPublicationType() { return publicationType; }
    public double getPublicationPrice() { return publicationPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public String getPublicationFrequency() { return publicationFrequency; }
    public String getReleaseDay() { return releaseDay; }
    public int getLowStockThreshold() { return lowStockThreshold; }

    public void setPublicationPrice(double price) { publicationPrice = price; }
    public void setStockQuantity(int quantity) { stockQuantity = quantity; }
    public void setPublicationFrequency(String frequency) { publicationFrequency = frequency; }
    public void setReleaseDay(String day) { releaseDay = day; }
    public void setLowStockThreshold(int threshold) { lowStockThreshold = threshold; }

    @Override
    public String toString() {
        return publicationId + " | " + publicationName + " | " + publicationType
                + " | €" + String.format("%.2f", publicationPrice) + " | Stock: " + stockQuantity;
    }
}
