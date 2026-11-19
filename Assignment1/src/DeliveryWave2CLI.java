import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Standalone US11/US16/US18/US30 menu; can be linked into Rodion's main menu later. */
public class DeliveryWave2CLI {
    public static void main(String[] args) {
        DeliveryWave2Service service = new DeliveryWave2Service();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("\n1 Preview/print docket | 2 Recorded summary | 3 Today's status  | 0 Exit");
            String choice = in.nextLine().trim();
            if ("0".equals(choice)) return;
            if (!"1".equals(choice) && !"2".equals(choice) && !"3".equals(choice)) {
                System.out.println("Invalid option");
                continue;
            }
            try {
                System.out.print("Delivery area ID (1-24): ");
                int area = Integer.parseInt(in.nextLine().trim());
                LocalDate date;
                if ("3".equals(choice)) date = LocalDate.now();
                else {
                    System.out.print("Date (yyyy-MM-dd): ");
                    date = LocalDate.parse(in.nextLine().trim());
                }
                if ("1".equals(choice)) {
                    DeliveryWave2Service.DocketData data = service.generateFromDatabase(date, area);
                    System.out.println(service.formatPrintedDocket(data));
                    Path saved = service.savePrintedDocket(data, Path.of("dockets"));
                    System.out.println("Saved to: " + saved.toAbsolutePath());
                    
                } else if ("2".equals(choice)) {
                	
                    System.out.println(service.recordedSummaryFromDatabase(date, area));
                } else System.out.println(service.statusFromDatabase(date, area));
            } catch (NumberFormatException | DateTimeParseException e) {
                System.out.println("Invalid date or area ID: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Could not complete request: " + e.getMessage());
            }
        }
    }
}
