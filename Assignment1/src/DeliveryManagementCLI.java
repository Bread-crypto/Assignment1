import java.util.ArrayList;
import java.util.Scanner;

public class DeliveryManagementCLI {

    private static final Scanner scanner = new Scanner(System.in);

    private static final DeliveryPersonDAO deliveryPersonDAO =
            new DeliveryPersonDAO();

    private static final DeliveryAreaDAO deliveryAreaDAO =
            new DeliveryAreaDAO();


    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("===== DELIVERY MANAGEMENT MENU =====");

            System.out.println("1. Add Delivery Person");
            System.out.println("2. Find Delivery Person By ID");
            System.out.println("3. List All Delivery Persons");
            System.out.println("4. Update Delivery Person");
            System.out.println("5. Mark Delivery Person Inactive");

            System.out.println("6. Add Delivery Area");
            System.out.println("7. Find Delivery Area By ID");
            System.out.println("8. List All Delivery Areas");
            System.out.println("9. Update Delivery Area");
            System.out.println("10. Assign Delivery Person To Area");

            System.out.println("0. Exit");

            System.out.print("Choose an option: ");

            String choice =
                    scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    addDeliveryPerson();
                    break;

                case "2":
                    findDeliveryPersonById();
                    break;

                case "3":
                    listAllDeliveryPersons();
                    break;

                case "4":
                    updateDeliveryPerson();
                    break;

                case "5":
                    markDeliveryPersonInactive();
                    break;

                case "6":
                    addDeliveryArea();
                    break;

                case "7":
                    findDeliveryAreaById();
                    break;

                case "8":
                    listAllDeliveryAreas();
                    break;

                case "9":
                    updateDeliveryArea();
                    break;

                case "10":
                    assignDeliveryPersonToArea();
                    break;

                case "0":
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose 0-10.");
            }
        }

        System.out.println("Goodbye.");
    }


    // ===== 1. ADD DELIVERY PERSON (User Story 32) =====

    private static void addDeliveryPerson() {

        System.out.println(
                "=== Add Delivery Person ===");

        System.out.print(
                "First Name: ");

        String firstName =
                scanner.nextLine();

        System.out.print(
                "Surname: ");

        String surname =
                scanner.nextLine();

        System.out.print(
                "Phone Number: ");

        String phoneNumber =
                scanner.nextLine();

        DeliveryPerson person =
                new DeliveryPerson();

        if (!person.createDeliveryPerson(
                firstName,
                surname,
                phoneNumber)) {

            System.out.println(
                    "Delivery person not created.");

            return;
        }

        if (deliveryPersonDAO
                .addDeliveryPerson(person)) {

            System.out.println(
                    "Delivery person added successfully.");

        } else {

            System.out.println(
                    "Failed to save delivery person.");
        }
    }


    // ===== 2. FIND DELIVERY PERSON BY ID =====

    private static void findDeliveryPersonById() {

        int id =
                readInt(
                        "Delivery Person ID: ");

        DeliveryPerson person =
                deliveryPersonDAO
                        .findDeliveryPersonById(id);

        if (person == null) {

            System.out.println(
                    "Delivery person not found.");

        } else {

            System.out.println(person);
        }
    }


    // ===== 3. LIST ALL DELIVERY PERSONS =====

    private static void listAllDeliveryPersons() {

        ArrayList<DeliveryPerson> people =
                deliveryPersonDAO
                        .getAllDeliveryPersons();

        if (people.isEmpty()) {

            System.out.println(
                    "No delivery persons found.");

        } else {

            for (DeliveryPerson person : people) {

                System.out.println();
                System.out.println(person);
            }
        }
    }


    // ===== 4. UPDATE DELIVERY PERSON (User Story 32) =====

    private static void updateDeliveryPerson() {

        int id =
                readInt(
                        "Delivery Person ID to update: ");

        DeliveryPerson existing =
                deliveryPersonDAO
                        .findDeliveryPersonById(id);

        if (existing == null) {

            System.out.println(
                    "Delivery person not found.");

            return;
        }

        System.out.println(
                "Current details:");

        System.out.println(existing);

        System.out.println(
                "Press Enter to keep the current value.");

        String firstName =
                readOrKeep(
                        "First Name",
                        existing.getFirstName());

        String surname =
                readOrKeep(
                        "Surname",
                        existing.getSurname());

        String phoneNumber =
                readOrKeep(
                        "Phone Number",
                        existing.getPhoneNumber());

        DeliveryPerson updated =
                new DeliveryPerson();

        if (!updated.createDeliveryPerson(
                firstName,
                surname,
                phoneNumber)) {

            System.out.println(
                    "Delivery person not updated.");

            return;
        }

        updated.setDeliveryPersonId(id);

        updated.setStatus(
                existing.getStatus());

        if (deliveryPersonDAO
                .updateDeliveryPerson(updated)) {

            System.out.println(
                    "Delivery person updated successfully.");

        } else {

            System.out.println(
                    "Failed to update delivery person.");
        }
    }


    // ===== 5. MARK DELIVERY PERSON INACTIVE =====

    private static void markDeliveryPersonInactive() {

        int id =
                readInt(
                        "Delivery Person ID: ");

        DeliveryPerson person =
                deliveryPersonDAO
                        .findDeliveryPersonById(id);

        if (person == null) {

            System.out.println(
                    "Delivery person not found.");

            return;
        }

        if ("INACTIVE".equals(
                person.getStatus())) {

            System.out.println(
                    "This delivery person is already inactive.");

            return;
        }

        if (deliveryPersonDAO
                .markInactive(id)) {

            System.out.println(
                    "Delivery person marked inactive.");

        } else {

            System.out.println(
                    "Failed to update delivery person.");
        }
    }


    // ===== 6. ADD DELIVERY AREA (User Story 17) =====

    private static void addDeliveryArea() {

        System.out.println(
                "=== Add Delivery Area ===");

        System.out.print(
                "Area Name: ");

        String areaName =
                scanner.nextLine();

        DeliveryArea area =
                new DeliveryArea();

        if (!area.createDeliveryArea(
                areaName)) {

            System.out.println(
                    "Delivery area not created.");

            return;
        }

        if (deliveryAreaDAO
                .addDeliveryArea(area)) {

            System.out.println(
                    "Delivery area added successfully.");

        } else {

            System.out.println(
                    "Failed to save delivery area.");
        }
    }


    // ===== 7. FIND DELIVERY AREA BY ID =====

    private static void findDeliveryAreaById() {

        int id =
                readInt(
                        "Delivery Area ID: ");

        DeliveryArea area =
                deliveryAreaDAO
                        .findDeliveryAreaById(id);

        if (area == null) {

            System.out.println(
                    "Delivery area not found.");

        } else {

            System.out.println(area);
        }
    }


    // ===== 8. LIST ALL DELIVERY AREAS =====

    private static void listAllDeliveryAreas() {

        ArrayList<DeliveryArea> areas =
                deliveryAreaDAO
                        .getAllDeliveryAreas();

        if (areas.isEmpty()) {

            System.out.println(
                    "No delivery areas found.");

        } else {

            for (DeliveryArea area : areas) {

                System.out.println();
                System.out.println(area);
            }
        }
    }


    // ===== 9. UPDATE DELIVERY AREA (User Story 17) =====

    private static void updateDeliveryArea() {

        int id =
                readInt(
                        "Delivery Area ID to update: ");

        DeliveryArea existing =
                deliveryAreaDAO
                        .findDeliveryAreaById(id);

        if (existing == null) {

            System.out.println(
                    "Delivery area not found.");

            return;
        }

        System.out.println(
                "Current details:");

        System.out.println(existing);

        System.out.println(
                "Press Enter to keep the current value.");

        String areaName =
                readOrKeep(
                        "Area Name",
                        existing.getAreaName());

        if (!existing.verifyAreaName(
                areaName)) {

            System.out.println(
                    "Delivery area not updated.");

            return;
        }

        existing.setAreaName(
                areaName);

        if (deliveryAreaDAO
                .updateDeliveryArea(existing)) {

            System.out.println(
                    "Delivery area updated successfully.");

        } else {

            System.out.println(
                    "Failed to update delivery area.");
        }
    }


    // ===== 10. ASSIGN DELIVERY PERSON (User Story 12) =====

    private static void assignDeliveryPersonToArea() {

        int areaId =
                readInt(
                        "Delivery Area ID: ");

        int personId =
                readInt(
                        "Delivery Person ID: ");

        DeliveryPerson person =
                deliveryPersonDAO
                        .findDeliveryPersonById(
                                personId);

        if (person == null) {

            System.out.println(
                    "Delivery person not found.");

            return;
        }

        if (!person.isActive()) {

            System.out.println(
                    "Inactive delivery person cannot be assigned.");

            return;
        }

        if (deliveryAreaDAO
                .assignDeliveryPerson(
                        areaId,
                        personId)) {

            System.out.println(
                    "Delivery person assigned successfully.");

        } else {

            System.out.println(
                    "Failed to assign delivery person.");
        }
    }


    // ===== Helpers =====

    private static int readInt(
            String prompt) {

        System.out.print(prompt);

        try {

            return Integer.parseInt(
                    scanner.nextLine());

        } catch (NumberFormatException e) {

            return -1;
        }
    }


    private static String readOrKeep(
            String label,
            String current) {

        System.out.print(
                label + " [" + current + "]: ");

        String input =
                scanner.nextLine();

        return input.isEmpty()
                ? current
                : input;
    }
}