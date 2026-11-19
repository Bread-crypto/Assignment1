import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/** Small entry point for Dylan's Wave 2 read/report features (Stories 23 and 24). */
public class DylanWave2CLI {
    private static final Scanner IN = new Scanner(System.in);
    public static void main(String[] args) {
        DeliveryHistoryDAO history = new DeliveryHistoryDAO();
        DocketStockService stock = new DocketStockService();
        while (true) {
            System.out.println("\n=== Dylan - Wave 2 ===");
            System.out.println("1. View customer delivery history");
            System.out.println("2. View out-of-stock customers for a date");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = IN.nextLine().trim();
            try {
                if ("0".equals(choice)) return;
                if ("1".equals(choice)) {
                    System.out.print("Customer ID: "); int id = Integer.parseInt(IN.nextLine().trim());
                    System.out.print("Month (YYYY-MM), or Enter for all: "); String monthText = IN.nextLine().trim();
                    YearMonth month = monthText.isEmpty() ? null : YearMonth.parse(monthText);
                    List<DeliveryHistoryEntry> entries = history.getHistory(id, month);
                    if (entries.isEmpty()) System.out.println("No delivery history");
                    else for (DeliveryHistoryEntry entry : entries) System.out.println(entry);
                } else if ("2".equals(choice)) {
                    System.out.print("Date (YYYY-MM-DD): ");
                    java.time.LocalDate date = java.time.LocalDate.parse(IN.nextLine().trim());
                    List<String> affected = stock.getOutOfStockCustomers(date);
                    if (affected.isEmpty()) System.out.println("No out-of-stock deliveries for that date.");
                    else for (String customer : affected) System.out.println(customer);
                } else System.out.println("Invalid option.");
            } catch (NumberFormatException | DateTimeParseException ex) {
                System.out.println("Please enter a valid ID, month or date.");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            } catch (SQLException ex) {
                System.out.println("Database error: " + ex.getMessage());
            }
        }
    }
}
