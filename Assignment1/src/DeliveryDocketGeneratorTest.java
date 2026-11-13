
import java.util.List;
import java.util.Arrays;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;


public class DeliveryDocketGeneratorTest {

    // TC01 - Customer is on holiday
    @Test
    public void testCustomerOnHoliday() {
        Holiday holiday = new Holiday(
                1, 1,
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 15));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isCustomerOnHoliday(
                holiday, LocalDate.of(2026, 11, 12)));
    }

    // TC02 - Date is before holiday
    @Test
    public void testBeforeHoliday() {
        Holiday holiday = new Holiday(
                1, 1,
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 15));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertFalse(generator.isCustomerOnHoliday(
                holiday, LocalDate.of(2026, 11, 9)));
    }

    // TC03 - Date is after holiday
    @Test
    public void testAfterHoliday() {
        Holiday holiday = new Holiday(
                1, 1,
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 15));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertFalse(generator.isCustomerOnHoliday(
                holiday, LocalDate.of(2026, 11, 16)));
    }

    // TC04 - Holiday start date is included
    @Test
    public void testHolidayStartDate() {
        Holiday holiday = new Holiday(
                1, 1,
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 15));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isCustomerOnHoliday(
                holiday, LocalDate.of(2026, 11, 10)));
    }

    // TC05 - Holiday end date is included
    @Test
    public void testHolidayEndDate() {
        Holiday holiday = new Holiday(
                1, 1,
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 15));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isCustomerOnHoliday(
                holiday, LocalDate.of(2026, 11, 15)));
    }
    

    // TC06 - A missing customer must not receive deliveries
    @Test
    public void testNullCustomerIsNotActive() {
        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertFalse(generator.isCustomerActive(null));
    }
    

    // TC06 - Active customers can receive deliveries
    @Test
    public void testActiveCustomer() {
        Customer customer = new Customer();
        customer.setStatus("ACTIVE");

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isCustomerActive(customer));
    }

    // TC07 - Closed customers cannot receive deliveries
    @Test
    public void testClosedCustomer() {
        Customer customer = new Customer();
        customer.setStatus("CLOSED");

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertFalse(generator.isCustomerActive(customer));
    }

    // TC08 - Customer must belong to the selected area
    @Test
    public void testCustomerInCorrectArea() {
        Customer customer = new Customer();
        customer.setDeliveryAreaId(3);

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isCustomerInArea(customer, 3));
        assertFalse(generator.isCustomerInArea(customer, 4));
    }

    // TC09 - Order must be active on the docket date
    @Test
    public void testOrderActiveOnDate() {
        Order order = new Order(
                1, 1, 2, "MONDAY",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isOrderActiveOnDate(
                order, LocalDate.of(2026, 11, 16)));

        assertFalse(generator.isOrderActiveOnDate(
                order, LocalDate.of(2026, 12, 1)));
    }

    // TC10 - Order is scheduled for the selected weekday
    @Test
    public void testOrderDueOnMonday() {
        Order order = new Order(
                1, 1, 2, "MONDAY,WEDNESDAY",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isOrderDueOnDay(
                order, LocalDate.of(2026, 11, 16)));
    }

    // TC11 - Order is not scheduled for the selected weekday
    @Test
    public void testOrderNotDueOnTuesday() {
        Order order = new Order(
                1, 1, 2, "MONDAY,WEDNESDAY",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertFalse(generator.isOrderDueOnDay(
                order, LocalDate.of(2026, 11, 17)));
    }

    // TC12 - Weekly publication is due only on release day
    @Test
    public void testWeeklyPublicationDueDate() {
        Publication publication = new Publication();
        publication.setPublicationFrequency("WEEKLY");
        publication.setReleaseDay("MONDAY");

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        assertTrue(generator.isPublicationDueOnDate(
                publication, LocalDate.of(2026, 11, 16)));

        assertFalse(generator.isPublicationDueOnDate(
                publication, LocalDate.of(2026, 11, 17)));
    }
    

    // TC13 - Generate a delivery for the correct customer and area
    @Test
    public void testGenerateDailyDeliveries() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setDeliveryAreaId(3);
        customer.setStatus("ACTIVE");

        Order order = new Order(
                1, 1, 10, "MONDAY",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30));

        Publication publication = new Publication();
        publication.createPublication("Daily News", "NEWSPAPER", 2.50, 20);
        publication.setPublicationId(10);

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        List<DeliveryDocketGenerator.DeliveryItem> result =
                generator.generateDailyDeliveries(
                        LocalDate.of(2026, 11, 16),
                        3,
                        Arrays.asList(customer),
                        Arrays.asList(order),
                        Arrays.asList(publication),
                        Collections.emptyList());

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCustomer().getCustomerId());
        assertEquals("Daily News",
                result.get(0).getPublication().getPublicationName());
    }

    // TC14 - Customers on holiday must be excluded
    @Test
    public void testHolidayCustomerExcludedFromDocket() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setDeliveryAreaId(3);
        customer.setStatus("ACTIVE");

        Order order = new Order(
                1, 1, 10, "MONDAY",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30));

        Publication publication = new Publication();
        publication.createPublication("Daily News", "NEWSPAPER", 2.50, 20);
        publication.setPublicationId(10);

        Holiday holiday = new Holiday(
                1, 1,
                LocalDate.of(2026, 11, 15),
                LocalDate.of(2026, 11, 18));

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        List<DeliveryDocketGenerator.DeliveryItem> result =
                generator.generateDailyDeliveries(
                        LocalDate.of(2026, 11, 16),
                        3,
                        Arrays.asList(customer),
                        Arrays.asList(order),
                        Arrays.asList(publication),
                        Arrays.asList(holiday));

        assertEquals(0, result.size());
    }

    // TC15 - Empty data produces no deliveries
    @Test
    public void testNoDeliveries() {
        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        List<DeliveryDocketGenerator.DeliveryItem> result =
                generator.generateDailyDeliveries(
                        LocalDate.of(2026, 11, 16),
                        3,
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList());

        assertTrue(result.isEmpty());
    }
    

    // TC16 - Printed docket contains the delivery details
    @Test
    public void testPrintableDocketContainsDeliveryDetails() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setSurname("Murphy");
        customer.setAddress("12 Main Street");
        customer.setDeliveryAreaId(3);

        Order order = new Order(
                1, 1, 10, "DAILY",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30));

        Publication publication = new Publication();
        publication.createPublication(
                "Daily News", "NEWSPAPER", 2.50, 20);
        publication.setPublicationId(10);

        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        List<DeliveryDocketGenerator.DeliveryItem> deliveries =
                generator.generateDailyDeliveries(
                        LocalDate.of(2026, 11, 16),
                        3,
                        Arrays.asList(customer),
                        Arrays.asList(order),
                        Arrays.asList(publication),
                        Collections.emptyList());

        String docket = generator.formatPrintableDocket(
                LocalDate.of(2026, 11, 16), 3, deliveries);

        assertTrue(docket.contains("2026-11-16"));
        assertTrue(docket.contains("Delivery Area: 3"));
        assertTrue(docket.contains("John Murphy"));
        assertTrue(docket.contains("12 Main Street"));
        assertTrue(docket.contains("Daily News"));
    }

    // TC17 - Empty docket displays "No deliveries"
    @Test
    public void testPrintableDocketNoDeliveries() {
        DeliveryDocketGenerator generator = new DeliveryDocketGenerator();

        String docket = generator.formatPrintableDocket(
                LocalDate.of(2026, 11, 16),
                3,
                Collections.emptyList());

        assertTrue(docket.contains("No deliveries"));
    }
    

    // TC18 - Daily summary counts deliveries by publication
    @Test
    public void testDailyDeliverySummary() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setDeliveryAreaId(3);
        customer.setStatus("ACTIVE");

        Order order = new Order(
                1, 1, 10, "DAILY",
                LocalDate.of(2026, 11, 1),
                LocalDate.of(2026, 11, 30));

        Publication publication = new Publication();
        publication.createPublication(
                "Daily News", "NEWSPAPER", 2.50, 20);
        publication.setPublicationId(10);

        DeliveryDocketGenerator generator =
                new DeliveryDocketGenerator();

        List<DeliveryDocketGenerator.DeliveryItem> deliveries =
                generator.generateDailyDeliveries(
                        LocalDate.of(2026, 11, 16),
                        3,
                        Arrays.asList(customer),
                        Arrays.asList(order),
                        Arrays.asList(publication),
                        Collections.emptyList());

        String summary = generator.generateDailySummary(
                LocalDate.of(2026, 11, 16),
                3,
                deliveries);

        assertTrue(summary.contains("2026-11-16"));
        assertTrue(summary.contains("Delivery Area: 3"));
        assertTrue(summary.contains("Total Deliveries: 1"));
        assertTrue(summary.contains("Daily News: 1"));
    }
    
    // US18 AC4 - No recorded deliveries
    @Test
    public void testDailySummaryNoDeliveriesRecorded() {

        DeliveryDocketGenerator generator =
                new DeliveryDocketGenerator();

        String summary = generator.generateDailySummary(
                LocalDate.of(2026, 11, 16),
                3,
                Collections.emptyList());

        assertTrue(summary.contains("No deliveries recorded"));
    }







}
