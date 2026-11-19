
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OrderCLI {

    private static final Scanner scanner = new Scanner(System.in);

    // DAO objects for managing orders and holidays.
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final HolidayDAO holidayDAO = new HolidayDAO();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== ORDER AND HOLIDAY MANAGEMENT MENU =====");
            System.out.println("1. Add Order");
            System.out.println("2. View Customer Orders");
            System.out.println("3. Remove Publication");
            System.out.println("4. Record Holiday");
            System.out.println("5. View Customer Holidays");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addOrder(); break;
                case "2": viewOrders(); break;
                case "3": removePublication(); break;
                case "4": addHoliday(); break;
                case "5": viewHolidays(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid option. Please choose 0-5.");
            }
        }

        System.out.println("Goodbye.");
    }

    // ===== 1. ADD ORDER (User Story 2) =====
    private static void addOrder() {
        try {
            System.out.print("Enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter publication ID: ");
            int publicationId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter delivery days (e.g. Mon,Wed,Fri): ");
            String deliveryDays = scanner.nextLine().trim();

            System.out.print("Enter start date (yyyy-MM-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());

            Order order = new Order(0, customerId, publicationId,
                    deliveryDays, startDate, null);

            if (orderDAO.addOrder(order)) {
                System.out.println("Order added successfully.");
            } else {
                System.out.println("Order could not be added. Check the details.");
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid ID or date format.");
        }
    }

    // ===== 2. VIEW ORDERS (User Story 3) =====
    private static void viewOrders() {
        try {
            System.out.print("Enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());

            List<Order> orders = orderDAO.getOrdersByCustomer(customerId);

            if (orders.isEmpty()) {
                System.out.println("No orders found for this customer.");
                return;
            }

            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId()
                        + ", Publication ID: " + order.getPublicationId()
                        + ", Delivery Days: " + order.getDeliveryDays()
                        + ", Start Date: " + order.getOrderStartDate()
                        + ", End Date: " + order.getOrderEndDate());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid customer ID.");
        }
    }

    // ===== 3. REMOVE PUBLICATION (User Story 4) =====
    private static void removePublication() {
        try {
            System.out.print("Enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter publication ID to remove: ");
            int publicationId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter end date (yyyy-MM-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());

            if (orderDAO.removePublication(customerId, publicationId, endDate)) {
                System.out.println("Publication removed successfully.");
            } else {
                System.out.println("No active matching order found, or invalid date.");
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid ID or date format.");
        }
    }

    // ===== 4. ADD HOLIDAY (User Story 5) =====
    private static void addHoliday() {
        try {
            System.out.print("Enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter holiday start date (yyyy-MM-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());

            System.out.print("Enter holiday end date (yyyy-MM-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());

            Holiday holiday = new Holiday(0, customerId, startDate, endDate);

            if (holidayDAO.addHoliday(holiday)) {
                System.out.println("Holiday recorded successfully.");
            } else {
                System.out.println("Holiday could not be recorded. Check the dates.");
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid input or date format.");
        }
    }

    // ===== 5. VIEW HOLIDAYS (User Story 6) =====
    private static void viewHolidays() {
        try {
            System.out.print("Enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());

            List<Holiday> holidays = holidayDAO.getHolidaysByCustomer(customerId);

            if (holidays.isEmpty()) {
                System.out.println("No holidays found for this customer.");
                return;
            }

            for (Holiday holiday : holidays) {
                System.out.println("Holiday ID: " + holiday.getHolidayId()
                        + ", Start Date: " + holiday.getHolidayStartDate()
                        + ", End Date: " + holiday.getHolidayEndDate());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid customer ID.");
        }
    }
}