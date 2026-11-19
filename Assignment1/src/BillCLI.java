import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class BillCLI {

    private static final Scanner scanner = new Scanner(System.in);
    private static final BillDAO billDAO = new BillDAO();
    private static final CustomerDAO customerDAO = new CustomerDAO();

    public static void main(String[] args) {
        showMenu();
    }

    /** Billing menu. Called from main() now and from the main menu later. */
    public static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== BILLING MENU =====");
            System.out.println("1. Generate Monthly Bill For A Customer");
            System.out.println("2. Generate Bills For All Active Customers");
            System.out.println("3. Record A Payment");
            System.out.println("4. View Unpaid Bills");
            System.out.println("5. View / Print A Bill");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": generateBillForCustomer(); break;
                case "2": generateBillsForAllCustomers(); break;
                case "3": recordPayment(); break;
                case "4": viewUnpaidBills(); break;
                case "5": viewBill(); break;
                case "0": running = false; break;
                default:  System.out.println("Invalid option. Please choose 0-5.");
            }
        }
    }

    // ===== 1. ONE CUSTOMER (User Stories 19, 20) =====
    private static void generateBillForCustomer() {
        System.out.println("=== Generate Monthly Bill ===");
        int customerId = readInt("Customer ID: ");
        Customer customer = customerDAO.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Error: Invalid Customer ID");
            return;
        }

        int year = readInt("Billing Year (e.g. 2026): ");
        int month = readInt("Billing Month (1-12): ");
        if (!new Bill().verifyBillingMonth(year, month)) {
            System.out.println("Error: Invalid Billing Month (current or past month only)");
            return;
        }

        Bill bill = createBill(customer, year, month);
        if (bill == null) {
            System.out.println("Error: A bill for this customer and month already exists.");
            return;
        }
        printBill(bill, customer);
    }

    // ===== 2. ALL ACTIVE CUSTOMERS (User Story 28) =====
    private static void generateBillsForAllCustomers() {
        System.out.println("=== Generate Bills For All Active Customers ===");
        int year = readInt("Billing Year (e.g. 2026): ");
        int month = readInt("Billing Month (1-12): ");
        if (!new Bill().verifyBillingMonth(year, month)) {
            System.out.println("Error: Invalid Billing Month (current or past month only)");
            return;
        }

        int created = 0;
        int skipped = 0;
        double totalAmount = 0.0;

        for (Customer customer : customerDAO.getAllCustomers()) {
            if (!customer.getStatus().equals("ACTIVE")) {
                continue;
            }
            Bill bill = createBill(customer, year, month);
            if (bill == null) {
                skipped++;                      // already billed for that month
            } else {
                created++;
                totalAmount = totalAmount + bill.getTotalAmount();
                saveBillToFile(bill, customer);
            }
        }

        System.out.println("Bills created: " + created);
        System.out.println("Customers skipped (already billed): " + skipped);
        System.out.printf("Total amount billed: %.2f%n", totalAmount);
    }

    // ===== 3. RECORD PAYMENT (User Story 21) =====
    private static void recordPayment() {
        System.out.println("=== Record Payment ===");
        int billId = readInt("Bill ID: ");
        Bill bill = billDAO.findBillById(billId);
        if (bill == null) {
            System.out.println("Error: Invalid Bill ID");
            return;
        }
        if (bill.getBillStatus().equals("PAID")) {
            System.out.println("Error: This bill is already PAID.");
            return;
        }

        System.out.print("Payment Date (yyyy-mm-dd, Enter = today): ");
        LocalDate paymentDate = readDate(scanner.nextLine().trim());

        if (!bill.recordPayment(paymentDate)) {
            System.out.println("Error: Invalid Payment Date (today or a past date only)");
            return;
        }
        if (billDAO.markBillPaid(bill)) {
            System.out.println("Payment recorded. Bill " + billId + " is now PAID.");
        } else {
            System.out.println("Failed to save the payment to the database.");
        }
    }

    // ===== 4. UNPAID BILLS (User Story 22) =====
    private static void viewUnpaidBills() {
        ArrayList<Bill> bills = billDAO.getUnpaidBills();
        if (bills.isEmpty()) {
            System.out.println("No unpaid bills.");
            return;
        }

        double outstanding = 0.0;
        System.out.printf("%-8s %-25s %-10s %10s%n", "Bill ID", "Customer", "Month", "Amount");
        for (Bill bill : bills) {
            Customer customer = customerDAO.findCustomerById(bill.getCustomerId());
            String name = (customer == null) ? "Unknown" : customer.getFirstName() + " " + customer.getSurname();
            System.out.printf("%-8d %-25s %02d/%-7d %10.2f%n", bill.getBillId(), name,
                    bill.getBillingMonth(), bill.getBillingYear(), bill.getTotalAmount());
            outstanding = outstanding + bill.getTotalAmount();
        }
        System.out.printf("Total outstanding: %.2f%n", outstanding);
    }

    // ===== 5. VIEW / PRINT A BILL (User Story 20) =====
    private static void viewBill() {
        int billId = readInt("Bill ID: ");
        Bill bill = billDAO.findBillById(billId);
        if (bill == null) {
            System.out.println("Error: Invalid Bill ID");
            return;
        }
        Customer customer = customerDAO.findCustomerById(bill.getCustomerId());
        printBill(bill, customer);
    }

    // ===== Helpers =====

    /** Builds the bill from DELIVERED items and saves it. Returns null if it already exists. */
    private static Bill createBill(Customer customer, int year, int month) {
        if (billDAO.billExists(customer.getCustomerId(), year, month)) {
            return null;
        }

        ArrayList<BillItem> items = billDAO.getDeliveredItems(customer.getCustomerId(), year, month);
        double[] prices = new double[items.size()];
        for (int i = 0; i < items.size(); i++) {
            prices[i] = items.get(i).getPrice();
        }

        Bill bill = new Bill(customer.getCustomerId(), year, month);
        bill.calculateTotal(prices);           // tested in BillTest (TC07-TC09)
        if (billDAO.addBill(bill) == -1) {
            return null;
        }
        return bill;
    }

    /** Itemised bill: customer name, address, month, bill ID, each delivery and total (US 20). */
    private static String formatBill(Bill bill, Customer customer) {
        StringBuilder text = new StringBuilder();
        text.append("========== NEWSAGENT MONTHLY BILL ==========\n");
        text.append("Bill ID: ").append(bill.getBillId()).append("\n");
        text.append("Customer: ").append(customer.getFirstName()).append(" ").append(customer.getSurname()).append("\n");
        text.append("Address: ").append(customer.getAddress()).append("\n");
        text.append(String.format("Billing Month: %02d/%d%n", bill.getBillingMonth(), bill.getBillingYear()));
        text.append("--------------------------------------------\n");

        ArrayList<BillItem> items = billDAO.getDeliveredItems(bill.getCustomerId(),
                bill.getBillingYear(), bill.getBillingMonth());
        if (items.isEmpty()) {
            text.append("No deliveries this month.\n");
        }
        for (BillItem item : items) {
            text.append(item).append("\n");
        }

        text.append("--------------------------------------------\n");
        text.append(String.format("TOTAL DUE: %.2f%n", bill.getTotalAmount()));
        text.append("Status: ").append(bill.getBillStatus()).append("\n");
        return text.toString();
    }

    private static void printBill(Bill bill, Customer customer) {
        System.out.println(formatBill(bill, customer));
        saveBillToFile(bill, customer);
    }

    /** Saves the bill as bill_<customerId>_<yyyy-mm>.txt in the project folder (US 20). */
    private static void saveBillToFile(Bill bill, Customer customer) {
        String fileName = String.format("bill_%d_%d-%02d.txt",
                bill.getCustomerId(), bill.getBillingYear(), bill.getBillingMonth());
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.print(formatBill(bill, customer));
            System.out.println("Bill saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Could not save the bill file: " + e.getMessage());
        }
    }

    private static LocalDate readDate(String text) {
        if (text.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            return null;                        // Bill.verifyPaymentDate rejects null
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
