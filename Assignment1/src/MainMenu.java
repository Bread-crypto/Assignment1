import java.util.Scanner;

/**
 * Main entry point of the News Agent Delivery System.
 * Asks the newsagent to log in (User Story 25) and then opens the menu of each area.
 * Every sub-menu returns here when the user chooses 0.
 */
public class MainMenu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public static void main(String[] args) {
        if (!login()) {
            System.out.println("Too many failed attempts. Program closed.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("========== NEWS AGENT DELIVERY SYSTEM ==========");
            System.out.println("1. Customers");
            System.out.println("2. Publications & Stock");
            System.out.println("3. Orders & Holidays");
            System.out.println("4. Delivery Areas & Delivery People");
            System.out.println("5. Delivery Dockets & Daily Reports");
            System.out.println("6. Billing");
            System.out.println("7. Record Deliveries");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": CustomerCLI.main(args); break;
                case "2": PublicationCLI.main(args); break;
                case "3": OrderCLI.main(args); break;
                case "4": DeliveryManagementCLI.main(args); break;
                case "5": DeliveryWave2CLI.main(args); break;
                case "6": BillCLI.showMenu(); break;
                case "7": DeliveryCLI.main(args); break;
                case "0": running = false; break;
                default:  System.out.println("Invalid option. Please choose 0-6.");
            }
        }
        System.out.println("Goodbye.");
    }

    // ===== LOGIN (User Story 25) =====
    private static boolean login() {
        Login login = new Login();
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            System.out.println("===== LOGIN =====");
            System.out.print("Username (6-12 characters): ");
            String username = scanner.nextLine().trim();
            System.out.print("Password (4-6 digits): ");
            String password = scanner.nextLine().trim();

            if (login.login(username, password)) {
                System.out.println("Login successful. Welcome, " + username + ".");
                return true;
            }
            if (!login.verifyUserName(username)) {
                System.out.println("Error: Invalid Username");
            }
            if (!login.verifyPassword(password)) {
                System.out.println("Error: Invalid Password");
            }
        }
        return false;
    }
}
