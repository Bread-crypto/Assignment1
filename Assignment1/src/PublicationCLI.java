import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PublicationCLI {
    private final Scanner scanner = new Scanner(System.in);
    private final PublicationDAO publicationDAO = new PublicationDAO();

    public static void main(String[] args) {
        new PublicationCLI().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            System.out.println("\nPublication Menu");
            System.out.println("1. Add publication");
            System.out.println("2. Find publication");
            System.out.println("3. List publications");
            System.out.println("4. Update stock");
            System.out.println("5. Change price");
            System.out.println("6. Remove publication");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            switch (scanner.nextLine()) {
                case "1": addPublication(); break;
                case "2": findPublication(); break;
                case "3": listPublications(); break;
                case "4": updateStock(); break;
                case "5": changePrice(); break;
                case "6": removePublication(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    private void addPublication() {
        Publication publication = new Publication();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Type (NEWSPAPER/MAGAZINE): ");
        String type = scanner.nextLine();
        System.out.print("Price: ");
        double price = readDouble();
        System.out.print("Stock quantity: ");
        int stock = readInt();

        if (!publication.createPublication(name, type, price, stock)) {
            System.out.println("Invalid publication details.");
        } else if (publicationDAO.addPublication(publication)) {
            System.out.println("Publication added. ID: " + publication.getPublicationId());
        } else {
            System.out.println("Publication was not added. Check for a duplicate name or database error.");
        }
    }

    private void findPublication() {
        System.out.print("Publication ID: ");
        Publication publication = publicationDAO.findById(readInt());
        System.out.println(publication == null ? "Publication not found." : publication);
    }

    private void listPublications() {
        List<Publication> publications = publicationDAO.getAllPublications();
        if (publications.isEmpty()) System.out.println("No publications found.");
        for (Publication publication : publications) System.out.println(publication);
    }

    private void updateStock() {
        System.out.print("Publication ID: ");
        int id = readInt();
        System.out.print("New stock quantity: ");
        int quantity = readInt();
        System.out.println(publicationDAO.updateStock(id, quantity) ? "Stock updated." : "Stock not updated.");
    }

    private void changePrice() {
        System.out.print("Publication ID: ");
        int id = readInt();
        Publication publication = publicationDAO.findById(id);
        if (publication == null) {
            System.out.println("Publication not found.");
            return;
        }
        System.out.print("New price: ");
        double price = readDouble();
        System.out.print("Start date (YYYY-MM-DD): ");
        try {
            LocalDate date = LocalDate.parse(scanner.nextLine());
            if (publicationDAO.changePrice(id, price, date)) {
                System.out.println("Price updated.");
            } else {
                System.out.println("Invalid price/date, or price could not be saved.");
            }
        } catch (Exception e) {
            System.out.println("Enter the date in YYYY-MM-DD format.");
        }
    }

    private void removePublication() {
        System.out.print("Publication ID: ");
        System.out.println(publicationDAO.removePublication(readInt())
                ? "Publication removed." : "Publication not removed. It may be in use.");
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private double readDouble() {
        try { return Double.parseDouble(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
