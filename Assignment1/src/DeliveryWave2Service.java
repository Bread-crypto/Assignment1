import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/** Integration for US11, US16, US18 and US30; never writes delivery records. */
public class DeliveryWave2Service {
    private final DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

    public static final class DocketData {
        public final LocalDate date;
        public final int areaId;
        public final String deliveryPersonName;
        public final List<DeliveryDocketGenerator.DeliveryItem> items;
        public DocketData(LocalDate date, int areaId, String name,
                          List<DeliveryDocketGenerator.DeliveryItem> items) {
            this.date = date;
            this.areaId = areaId;
            this.deliveryPersonName = name;
            this.items = new ArrayList<>(items);
        }
    }

    /** Loads records using existing teammates' DAO APIs. */
    public DocketData generateFromDatabase(LocalDate date, int areaId) {
        validate(date, areaId);
        try {
            DatabaseSchemaCheck.requireTables("CustomerTable", "OrderTable", "HolidayTable",
                    "publication", "DeliveryAreaTable", "DeliveryPersonTable", "DeliveryDocketTable");
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException("Docket database not ready: " + ex.getMessage(), ex);
        }
        DeliveryArea area = new DeliveryAreaDAO().findDeliveryAreaById(areaId);
        if (area == null) throw new IllegalArgumentException("Delivery area not found: " + areaId);
        DeliveryPerson person = new DeliveryPersonDAO().findDeliveryPersonById(area.getDeliveryPersonId());
        String personName = person == null ? "Unassigned"
                : person.getFirstName() + " " + person.getSurname();

        List<Customer> customers = new CustomerDAO().getAllCustomers();
        List<Order> orders = new ArrayList<>();
        List<Holiday> holidays = new ArrayList<>();
        OrderDAO orderDAO = new OrderDAO();
        HolidayDAO holidayDAO = new HolidayDAO();
        for (Customer customer : customers) {
            if (customer != null && generator.isCustomerInArea(customer, areaId)) {
                orders.addAll(orderDAO.getOrdersByCustomer(customer.getCustomerId()));
                holidays.addAll(holidayDAO.getHolidaysByCustomer(customer.getCustomerId()));
            }
        }
        List<Publication> publications = new PublicationDAO().getAllPublications();
        return new DocketData(date, areaId, personName,
                generator.generateDailyDeliveries(date, areaId, customers, orders, publications, holidays));
    }

    public String formatPrintedDocket(DocketData data) {
        Objects.requireNonNull(data, "Docket data required");
        StringBuilder out = new StringBuilder("DAILY DELIVERY DOCKET\n");
        out.append("Date: ").append(data.date).append('\n');
        out.append("Delivery Area: ").append(data.areaId).append('\n');
        out.append("Delivery Person: ").append(data.deliveryPersonName).append("\n\n");
        if (data.items.isEmpty()) return out.append("No deliveries\n").toString();
        int i = 0;
        for (DeliveryDocketGenerator.DeliveryItem item : data.items) {
            Customer c = item.getCustomer();
            Publication p = item.getPublication();
            out.append(++i).append(". ").append(c.getFirstName()).append(' ').append(c.getSurname())
               .append(" | ").append(c.getAddress()).append('\n');
            out.append("   Publication: ").append(p.getPublicationName())
               .append(" | Order ID: ").append(item.getOrder().getOrderId()).append('\n');
            out.append("   [ ] Delivered     [ ] Missed\n");
        }
        return out.toString();
    }

    public Path savePrintedDocket(DocketData data, Path directory) throws IOException {
        Objects.requireNonNull(directory, "Output folder required");
        Files.createDirectories(directory);
        Path file = directory.resolve("docket_" + data.areaId + "_" + data.date + ".txt");
        Files.writeString(file, formatPrintedDocket(data), StandardCharsets.UTF_8);
        return file;
    }

    /** The last recorded row for an order/date wins, so corrected records show current status. */
    private Map<Integer, Delivery> latestByOrder(LocalDate date, List<Delivery> recorded) {
        Map<Integer, Delivery> latest = new HashMap<>();
        if (recorded == null) return latest;
        for (Delivery d : recorded) {
            if (d == null || !date.equals(d.getDeliveryDate())) continue;
            Delivery old = latest.get(d.getOrderId());
            if (old == null || d.getDeliveryId() >= old.getDeliveryId()) {
                latest.put(d.getOrderId(), d);
            }
        }
        return latest;
    }

    private static String status(Delivery d) {
        if (d == null) return "PENDING";
        String s = d.getDeliveryStatus();
        return "DELIVERED".equals(s) || "MISSED".equals(s) || "OUT OF STOCK".equals(s)
                ? s : "PENDING";
    }

    /** US30: scheduled items are PENDING until Patrick records them. */
    public String formatStatus(DocketData docket, List<Delivery> recorded) {
        Objects.requireNonNull(docket, "Docket required");
        Map<Integer, Delivery> latest = latestByOrder(docket.date, recorded);
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String s : new String[]{"PENDING", "DELIVERED", "MISSED", "OUT OF STOCK"}) counts.put(s, 0);
        StringBuilder out = new StringBuilder("DELIVERY STATUS\nDate: " + docket.date
                + "\nDelivery Area: " + docket.areaId + "\n");
        for (DeliveryDocketGenerator.DeliveryItem item : docket.items) {
            int orderId = item.getOrder().getOrderId();
            Delivery d = latest.get(orderId);
            // Reject a record whose identifiers do not match the scheduled order.
            if (d != null && (d.getCustomerId() != item.getCustomer().getCustomerId()
                    || d.getPublicationId() != item.getPublication().getPublicationId())) d = null;
            String s = status(d);
            counts.put(s, counts.get(s) + 1);
            out.append(item.getCustomer().getFirstName()).append(' ')
               .append(item.getCustomer().getSurname()).append(" — ")
               .append(item.getPublication().getPublicationName()).append(" — ").append(s).append('\n');
        }
        out.append("COUNTS\n");
        for (Map.Entry<String, Integer> e : counts.entrySet())
            out.append(e.getKey()).append(": ").append(e.getValue()).append('\n');
        return out.toString();
    }

    /** US18: only actually recorded DELIVERED items count; missed items are separate. */
    public String formatRecordedSummary(LocalDate date, int areaId, List<Customer> customers,
                                        List<Publication> publications, List<Delivery> recorded) {
        validate(date, areaId);
        Map<Integer, Customer> customerMap = new HashMap<>();
        Map<Integer, Publication> publicationMap = new HashMap<>();
        for (Customer c : customers) if (c != null) customerMap.put(c.getCustomerId(), c);
        for (Publication p : publications) if (p != null) publicationMap.put(p.getPublicationId(), p);
        Map<Integer, Delivery> latest = latestByOrder(date, recorded);
        Map<String, Integer> totals = new TreeMap<>();
        StringBuilder delivered = new StringBuilder();
        StringBuilder missed = new StringBuilder();
        int recordedCount = 0;
        for (Delivery d : latest.values()) {
            Customer c = customerMap.get(d.getCustomerId());
            if (c == null || c.getDeliveryAreaId() != areaId) continue;
            Publication p = publicationMap.get(d.getPublicationId());
            String publicationName = p == null ? "Publication #" + d.getPublicationId() : p.getPublicationName();
            String customerName = c.getFirstName() + " " + c.getSurname();
            String s = status(d);
            if ("DELIVERED".equals(s)) {
                delivered.append(customerName).append(" — ").append(publicationName).append('\n');
                totals.merge(publicationName, 1, Integer::sum);
                recordedCount++;
            } else if ("MISSED".equals(s)) {
                missed.append(customerName).append(" — ").append(publicationName)
                      .append(" — Reason: ").append(d.getMissedReason() == null ? "Not provided" : d.getMissedReason())
                      .append('\n');
                recordedCount++;
            } else if ("OUT OF STOCK".equals(s)) {
                missed.append(customerName).append(" — ").append(publicationName)
                      .append(" — OUT OF STOCK\n");
                recordedCount++;
            }
        }
        StringBuilder out = new StringBuilder("DAILY DELIVERY SUMMARY\nDate: " + date
                + "\nDelivery Area: " + areaId + "\n");
        if (recordedCount == 0) return out.append("No deliveries recorded\n").toString();
        out.append("DELIVERED\n").append(delivered);
        out.append("TOTAL COPIES DELIVERED PER PUBLICATION\n");
        for (Map.Entry<String, Integer> e : totals.entrySet())
            out.append(e.getKey()).append(": ").append(e.getValue()).append('\n');
        out.append("MISSED / NOT DELIVERED\n").append(missed);
        return out.toString();
    }

    public String recordedSummaryFromDatabase(LocalDate date, int areaId) throws java.sql.SQLException {
        DatabaseSchemaCheck.requireTables("DeliveryTable", "DeliveryDocketLineTable", "CustomerTable", "publication");
        return formatRecordedSummary(date, areaId, new CustomerDAO().getAllCustomers(),
                new PublicationDAO().getAllPublications(), new DeliveryReportingDAO().getDeliveriesByDate(date));
    }

    public String statusFromDatabase(LocalDate date, int areaId) throws java.sql.SQLException {
        DatabaseSchemaCheck.requireTables("DeliveryTable", "DeliveryDocketLineTable");
        DocketData data = generateFromDatabase(date, areaId);
        return formatStatus(data, new DeliveryReportingDAO().getDeliveriesByDate(date));
    }

    private static void validate(LocalDate date, int areaId) {
        if (date == null) throw new IllegalArgumentException("Invalid docket date");
        if (areaId < 1 || areaId > 24) throw new IllegalArgumentException("Delivery area must be 1-24");
    }
}
