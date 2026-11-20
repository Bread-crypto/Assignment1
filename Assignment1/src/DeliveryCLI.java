import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

// Command-line interface for recording deliveries.
public class DeliveryCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DeliveryDAO deliveryDAO = new DeliveryDAO();
    private static final OrderDAO orderDAO = new OrderDAO();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== DELIVERY MANAGEMENT MENU =====");
            System.out.println("1. Record Completed Delivery");
            System.out.println("2. Record Missed Delivery");
            System.out.println("3. Record Out of Stock");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    recordDelivery("DELIVERED");
                    break;
                case "2":
                    recordDelivery("MISSED");
                    break;
                case "3":
                    recordDelivery("OUT OF STOCK");
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 0-3.");
            }
        }

        System.out.println("Goodbye.");
    }

    // Records a delivery with the selected status.
    private static void recordDelivery(String status) {
        try {
        	System.out.print("Enter customer ID: ");
        	int customerId = Integer.parseInt(scanner.nextLine().trim());

        	// Retrieve only orders that are still active.
        	List<Order> orders = orderDAO.getOrdersByCustomer(customerId)
        	        .stream()
        	        .filter(order -> order.getOrderEndDate() == null
        	                || !order.getOrderEndDate().isBefore(LocalDate.now()))
        	        .collect(java.util.stream.Collectors.toList());

        	if (orders.isEmpty()) {
        	    System.out.println("No orders found for this customer.");
        	    return;
        	}

        	// Display the available orders.
        	System.out.println("Available orders:");
        	for (int i = 0; i < orders.size(); i++) {
        	    Order order = orders.get(i);
        	    System.out.println((i + 1) + ". Order ID: "
        	            + order.getOrderId()
        	            + ", Publication ID: " + order.getPublicationId());
        	}

        	System.out.print("Select an order number: ");
        	int selection = Integer.parseInt(scanner.nextLine().trim());

        	if (selection < 1 || selection > orders.size()) {
        	    System.out.println("Invalid order selection.");
        	    return;
        	}

        	Order selectedOrder = orders.get(selection - 1);

        	int orderId = selectedOrder.getOrderId();
        	int publicationId = selectedOrder.getPublicationId();
                   
                    
                    System.out.print("Enter delivery date (yyyy-MM-dd): ");
                    LocalDate deliveryDate =
                            LocalDate.parse(scanner.nextLine().trim());  
                    
                    // Reject future delivery dates before asking for other details.
                    if (deliveryDate.isAfter(LocalDate.now())) {
                        System.out.println(
                                "Invalid delivery date. Date cannot be in the future.");
                        return;
                    }

            String missedReason = null;

            if ("MISSED".equals(status)) {
                System.out.print("Enter missed delivery reason: ");
                missedReason = scanner.nextLine().trim();
            }

            Delivery delivery = new Delivery(
                    0, orderId, customerId, publicationId,
                    deliveryDate, status, missedReason
            );

            if (!delivery.isValidDeliveryDate()) {
                System.out.println(
                        "Invalid delivery date. Date cannot be in the future.");
                return;
            }

            if (!delivery.isValidDeliveryStatus()) {
                System.out.println("Invalid delivery status.");
                return;
            }

            if ("MISSED".equals(status)
                    && !delivery.isValidMissedReason()) {
                System.out.println(delivery.getMissedReasonErrorMessage());
                return;
            }

            if (deliveryDAO.addDelivery(delivery)) {
                System.out.println("Delivery recorded successfully.");
            } else {
                System.out.println("Delivery could not be recorded.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd.");
        }
    }
}