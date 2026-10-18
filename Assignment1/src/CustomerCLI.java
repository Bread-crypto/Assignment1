import java.util.ArrayList;
import java.util.Scanner;

public class CustomerCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final CustomerDAO customerDAO = new CustomerDAO();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== CUSTOMER MANAGEMENT MENU =====");
            System.out.println("1. Add Customer");
            System.out.println("2. Find Customer By ID");
            System.out.println("3. Search Customers By Surname");
            System.out.println("4. List All Customers");
            System.out.println("5. Update Customer");
            System.out.println("6. Close Customer Account");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addCustomer(); break;
                case "2": findCustomerById(); break;
                case "3": searchBySurname(); break;
                case "4": listAllCustomers(); break;
                case "5": updateCustomer(); break;
                case "6": closeCustomer(); break;
                case "0": running = false; break;
                default:  System.out.println("Invalid option. Please choose 0-6.");
            }
        }
        System.out.println("Goodbye.");
    }

    // ===== 1. ADD (User Story 1) =====
    private static void addCustomer() {
        System.out.println("=== Create New Customer ===");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Surname: ");
        String surname = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        int deliveryAreaId = readInt("Delivery Area (1-24): ");

        Customer customer = new Customer();
        if (!showErrors(customer, firstName, surname, address, phoneNumber, deliveryAreaId)) {
            System.out.println("Customer account not created.");
            return;
        }

        customer.createCustomerAccount(firstName, surname, address, phoneNumber, deliveryAreaId);
        if (customerDAO.addCustomer(customer)) {
            System.out.println("Customer account created successfully.");
        } else {
            System.out.println("Failed to save customer to the database.");
        }
    }

    // ===== 2. FIND BY ID (User Story 26) =====
    private static void findCustomerById() {
        int id = readInt("Customer ID: ");
        Customer customer = customerDAO.findCustomerById(id);
        if (customer == null) {
            System.out.println("Customer not found.");
        } else {
            System.out.println(customer);
        }
    }

    // ===== 3. SEARCH BY SURNAME (User Story 26) =====
    private static void searchBySurname() {
        System.out.print("Surname: ");
        String surname = scanner.nextLine().trim();
        ArrayList<Customer> customers = customerDAO.findCustomersBySurname(surname);
        if (customers.isEmpty()) {
            System.out.println("No customers found with surname '" + surname + "'.");
        } else {
            printList(customers);
        }
    }

    // ===== 4. LIST ALL =====
    private static void listAllCustomers() {
        ArrayList<Customer> customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers in the system.");
        } else {
            printList(customers);
        }
    }

    // ===== 5. UPDATE (User Story 26) =====
    private static void updateCustomer() {
        int id = readInt("Customer ID to update: ");
        Customer existing = customerDAO.findCustomerById(id);
        if (existing == null) {
            System.out.println("Customer not found.");
            return;
        }
        System.out.println("Current details:");
        System.out.println(existing);
        System.out.println("Press Enter to keep the current value.");

        String firstName = readOrKeep("First Name", existing.getFirstName());
        String surname = readOrKeep("Surname", existing.getSurname());
        String address = readOrKeep("Address", existing.getAddress());
        String phoneNumber = readOrKeep("Phone Number", existing.getPhoneNumber());
        String areaText = readOrKeep("Delivery Area (1-24)", String.valueOf(existing.getDeliveryAreaId()));
        int deliveryAreaId = parseIntOrMinusOne(areaText);

        Customer updated = new Customer();
        if (!showErrors(updated, firstName, surname, address, phoneNumber, deliveryAreaId)) {
            System.out.println("Customer not updated.");
            return;
        }

        updated.createCustomerAccount(firstName, surname, address, phoneNumber, deliveryAreaId);
        updated.setCustomerId(id);   // the ID never changes
        if (customerDAO.updateCustomer(updated)) {
            System.out.println("Customer updated successfully.");
        } else {
            System.out.println("Failed to update customer.");
        }
    }

    // ===== 6. CLOSE ACCOUNT (User Story 7) =====
    private static void closeCustomer() {
        int id = readInt("Customer ID to close: ");
        Customer customer = customerDAO.findCustomerById(id);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        if ("CLOSED".equals(customer.getStatus())) {
            System.out.println("This account is already closed.");
            return;
        }
        System.out.print("Close account of " + customer.getFirstName() + " "
                + customer.getSurname() + "? (y/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }
        if (customerDAO.closeCustomer(id)) {
            System.out.println("Customer account closed.");
        } else {
            System.out.println("Failed to close account.");
        }
    }

    // ===== Helpers =====

    /** Prints an error message for every invalid field. Returns true if all are valid. */
    private static boolean showErrors(Customer c, String firstName, String surname,
                                      String address, String phoneNumber, int deliveryAreaId) {
        boolean valid = true;
        if (!c.verifyFirstName(firstName))        { System.out.println("Error: Invalid First Name (1-50 characters)"); valid = false; }
        if (!c.verifySurname(surname))            { System.out.println("Error: Invalid Surname (1-50 characters)"); valid = false; }
        if (!c.verifyAddress(address))            { System.out.println("Error: Invalid Address (1-100 characters)"); valid = false; }
        if (!c.verifyPhoneNumber(phoneNumber))    { System.out.println("Error: Invalid Phone Number (10 digits, starts with 0)"); valid = false; }
        if (!c.verifyDeliveryArea(deliveryAreaId)) { System.out.println("Error: Invalid Delivery Area (1-24)"); valid = false; }
        return valid;
    }

    /** Reads a whole number; returns -1 if the user types something that is not a number. */
    private static int readInt(String prompt) {
        System.out.print(prompt);
        return parseIntOrMinusOne(scanner.nextLine());
    }

    private static int parseIntOrMinusOne(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Shows the current value; Enter keeps it, anything else replaces it. */
    private static String readOrKeep(String label, String current) {
        System.out.print(label + " [" + current + "]: ");
        String input = scanner.nextLine();
        return input.isEmpty() ? current : input;
    }

    private static void printList(ArrayList<Customer> customers) {
        System.out.printf("%-4s %-15s %-15s %-25s %-11s %-5s %-7s%n",
                "ID", "First Name", "Surname", "Address", "Phone", "Area", "Status");
        for (Customer c : customers) {
            System.out.printf("%-4d %-15s %-15s %-25s %-11s %-5d %-7s%n",
                    c.getCustomerId(), c.getFirstName(), c.getSurname(), c.getAddress(),
                    c.getPhoneNumber(), c.getDeliveryAreaId(), c.getStatus());
        }
    }
}