import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.time.LocalDate;

public class DeliveryDocketGenerator {

    // ===== Check Customer Holiday =====

    public boolean isCustomerOnHoliday(Holiday holiday, LocalDate docketDate) {

        if (holiday == null || docketDate == null) {
            return false;
        }

        return !docketDate.isBefore(holiday.getHolidayStartDate())
                && !docketDate.isAfter(holiday.getHolidayEndDate());
    }
    

    // ===== Check Customer Status =====

    public boolean isCustomerActive(Customer customer) {

        if (customer == null) {
            return false;
        }

        return "ACTIVE".equals(customer.getStatus());
    }
    

    // ===== Check Customer Delivery Area =====
    public boolean isCustomerInArea(Customer customer, int deliveryAreaId) {
        if (customer == null || deliveryAreaId < 1 || deliveryAreaId > 24) {
            return false;
        }

        return customer.getDeliveryAreaId() == deliveryAreaId;
    }

    // ===== Check Order Date Range =====
    public boolean isOrderActiveOnDate(Order order, LocalDate docketDate) {
        if (order == null || docketDate == null) {
            return false;
        }

        LocalDate startDate = order.getOrderStartDate();
        LocalDate endDate = order.getOrderEndDate();

        if (startDate == null) {
            return false;
        }

        if (docketDate.isBefore(startDate)) {
            return false;
        }

        // A null end date means the order has no scheduled end date.
        return endDate == null || !docketDate.isAfter(endDate);
    }
    

    // ===== Check Scheduled Order Delivery Day =====
    public boolean isOrderDueOnDay(Order order, LocalDate docketDate) {

        if (!isOrderActiveOnDate(order, docketDate)) {
            return false;
        }

        String deliveryDays = order.getDeliveryDays();

        if (deliveryDays == null || deliveryDays.trim().isEmpty()) {
            return false;
        }

        if (deliveryDays.trim().equalsIgnoreCase("DAILY")) {
            return true;
        }

        String weekday = docketDate.getDayOfWeek().name();

        // Supports comma-separated full weekday names
        String[] days = deliveryDays.split(",");

        for (String day : days) {
            if (day.trim().equalsIgnoreCase(weekday)) {
                return true;
            }
        }

        return false;
    }

    // ===== Check Publication Release Date =====
    public boolean isPublicationDueOnDate(
            Publication publication, LocalDate docketDate) {

        if (publication == null || docketDate == null) {
            return false;
        }

        return publication.isDueOnDate(
                publication.getPublicationFrequency(),
                publication.getReleaseDay(),
                docketDate);
    }
    
    
    

    // ===== A single delivery on the daily docket =====
    public static class DeliveryItem {
        private final Customer customer;
        private final Order order;
        private final Publication publication;

        public DeliveryItem(Customer customer, Order order,
                            Publication publication) {
            this.customer = customer;
            this.order = order;
            this.publication = publication;
        }

        public Customer getCustomer() {
            return customer;
        }

        public Order getOrder() {
            return order;
        }

        public Publication getPublication() {
            return publication;
        }
    }

    // ===== Generate the Daily Delivery List =====
    public List<DeliveryItem> generateDailyDeliveries(
            LocalDate docketDate,
            int deliveryAreaId,
            List<Customer> customers,
            List<Order> orders,
            List<Publication> publications,
            List<Holiday> holidays) {

        if (docketDate == null
                || deliveryAreaId < 1 || deliveryAreaId > 24) {
            throw new IllegalArgumentException(
                    "A valid date and delivery area (1-24) are required.");
        }

        List<DeliveryItem> deliveries = new ArrayList<>();

        if (customers == null || orders == null
                || publications == null || holidays == null) {
            return deliveries;
        }

        for (Customer customer : customers) {

            if (!isCustomerActive(customer)
                    || !isCustomerInArea(customer, deliveryAreaId)) {
                continue;
            }

            // Skip customers on holiday
            boolean onHoliday = false;

            for (Holiday holiday : holidays) {
                if (holiday != null
                        && holiday.getCustomerId() == customer.getCustomerId()
                        && isCustomerOnHoliday(holiday, docketDate)) {
                    onHoliday = true;
                    break;
                }
            }

            if (onHoliday) {
                continue;
            }

            // Find orders belonging to this customer
            for (Order order : orders) {

                if (order == null
                        || order.getCustomerId() != customer.getCustomerId()
                        || !isOrderDueOnDay(order, docketDate)) {
                    continue;
                }

                // Match the order to its publication
                for (Publication publication : publications) {

                    if (publication == null
                            || publication.getPublicationId()
                                    != order.getPublicationId()) {
                        continue;
                    }

                    if (isPublicationDueOnDate(publication, docketDate)) {
                        deliveries.add(
                                new DeliveryItem(customer, order, publication));
                    }

                    break;
                }
            }
        }

        return deliveries;
    }
    

    // ===== Format a Printable Daily Delivery Docket =====
    public String formatPrintableDocket(
            LocalDate docketDate,
            int deliveryAreaId,
            List<DeliveryItem> deliveries) {

        if (docketDate == null ||
                deliveryAreaId < 1 || deliveryAreaId > 24) {
            throw new IllegalArgumentException(
                    "Valid date and delivery area (1-24) required.");
        }

        StringBuilder docket = new StringBuilder();

        docket.append("====================================\n");
        docket.append("       DAILY DELIVERY DOCKET\n");
        docket.append("====================================\n");
        docket.append("Date: ").append(docketDate).append("\n");
        docket.append("Delivery Area: ").append(deliveryAreaId).append("\n");
        docket.append("------------------------------------\n");

        if (deliveries == null || deliveries.isEmpty()) {
            docket.append("No deliveries\n");
        } else {
            int number = 1;

            for (DeliveryItem item : deliveries) {
                if (item == null) {
                    continue;
                }

                Customer customer = item.getCustomer();
                Publication publication = item.getPublication();

                if (customer == null || publication == null) {
                    continue;
                }

                docket.append(number++).append(". ");
                docket.append(customer.getFirstName())
                      .append(" ")
                      .append(customer.getSurname())
                      .append("\n");

                docket.append("   Address: ")
                      .append(customer.getAddress())
                      .append("\n");

                docket.append("   Publication: ")
                      .append(publication.getPublicationName())
                      .append("\n");

                docket.append("------------------------------------\n");
            }

            if (number == 1) {
                docket.append("No deliveries\n");
            }
        }

        return docket.toString();
    }
    

    // ===== US18 - Generate Daily Delivery Summary =====
    public String generateDailySummary(
            LocalDate docketDate,
            int deliveryAreaId,
            List<DeliveryItem> deliveries) {

        if (docketDate == null
                || deliveryAreaId < 1
                || deliveryAreaId > 24) {
            throw new IllegalArgumentException(
                    "Valid date and delivery area (1-24) required.");
        }

        Map<String, Integer> publicationCounts = new TreeMap<>();
        int totalDeliveries = 0;

        if (deliveries != null) {
            for (DeliveryItem item : deliveries) {
                if (item == null
                        || item.getPublication() == null) {
                    continue;
                }

                String publicationName =
                        item.getPublication().getPublicationName();

                if (publicationName == null) {
                    continue;
                }

                totalDeliveries++;

                publicationCounts.put(
                        publicationName,
                        publicationCounts.getOrDefault(
                                publicationName, 0) + 1);
            }
        }

        StringBuilder summary = new StringBuilder();

        summary.append("DAILY DELIVERY SUMMARY\n");
        summary.append("Date: ").append(docketDate).append("\n");
        summary.append("Delivery Area: ")
               .append(deliveryAreaId).append("\n");
        summary.append("Total Deliveries: ")
               .append(totalDeliveries).append("\n");

        if (totalDeliveries == 0) {
        	summary.append("No deliveries recorded\n");
        } else {
            summary.append("Publications:\n");

            for (Map.Entry<String, Integer> entry
                    : publicationCounts.entrySet()) {

                summary.append(entry.getKey())
                       .append(": ")
                       .append(entry.getValue())
                       .append("\n");
            }
        }

        return summary.toString();
    }

   






}

