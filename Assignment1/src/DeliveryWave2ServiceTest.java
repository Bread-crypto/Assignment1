
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Acceptance-criteria tests for US16, US18, US30. No database needed. */
public class DeliveryWave2ServiceTest {

    private final LocalDate date = LocalDate.of(2026, 11, 16);
    private final DeliveryWave2Service service = new DeliveryWave2Service();

    private Customer customer(int id, String first, String last, int area) {
        Customer c = new Customer();
        c.setCustomerId(id);
        c.setFirstName(first);
        c.setSurname(last);
        c.setAddress("Main Street " + id);
        c.setDeliveryAreaId(area);
        return c;
    }

    private Publication publication(int id, String name) {
        Publication p = new Publication();
        p.createPublication(name, "NEWSPAPER", 2.50, 20);
        p.setPublicationId(id);
        return p;
    }

    private DeliveryWave2Service.DocketData docket() {
        Customer c = customer(1, "John", "Murphy", 3);
        Publication p = publication(10, "Daily News");
        Order o = new Order(
                21, 1, 10, "MONDAY",
                date.minusDays(1),
                date.plusDays(3));

        DeliveryDocketGenerator.DeliveryItem item =
                new DeliveryDocketGenerator.DeliveryItem(c, o, p);

        return new DeliveryWave2Service.DocketData(
                date, 3, "Pat Driver", Arrays.asList(item));
    }

    @Test
    void us16PrintedDocketIncludesPersonAndCheckboxes() {
        String text = service.formatPrintedDocket(docket());

        assertTrue(text.contains("2026-11-16"));
        assertTrue(text.contains("Delivery Area: 3"));
        assertTrue(text.contains("Delivery Person: Pat Driver"));
        assertTrue(text.contains("John Murphy"));
        assertTrue(text.contains("Main Street 1"));
        assertTrue(text.contains("Daily News"));
        assertTrue(text.contains("[ ] Delivered"));
        assertTrue(text.contains("[ ] Missed"));
    }

    @Test
    void us16FileNameAndContentsMatchDocket(
            @TempDir Path folder) throws Exception {

        DeliveryWave2Service.DocketData data = docket();
        Path file = service.savePrintedDocket(data, folder);

        assertEquals(
                "docket_3_2026-11-16.txt",
                file.getFileName().toString());

        assertEquals(
                service.formatPrintedDocket(data),
                Files.readString(file));
    }

    @Test
    void us16EmptyDocketDisplaysNoDeliveries() {
        DeliveryWave2Service.DocketData empty =
                new DeliveryWave2Service.DocketData(
                        date, 3, "Unassigned",
                        Collections.emptyList());

        assertTrue(
                service.formatPrintedDocket(empty)
                        .contains("No deliveries"));
    }

    @Test
    void us30PendingWithoutRecord() {
        String text = service.formatStatus(
                docket(), Collections.emptyList());

        assertTrue(text.contains("PENDING: 1"));
        assertTrue(text.contains("DELIVERED: 0"));
        assertTrue(text.contains("MISSED: 0"));
        assertTrue(text.contains("OUT OF STOCK: 0"));
    }

    @Test
    void us30StatusChangesWhenDeliveryIsRecorded() {
        Delivery d = new Delivery(
                1, 21, 1, 10, date, "DELIVERED", null);

        String text = service.formatStatus(
                docket(), Collections.singletonList(d));

        assertTrue(text.contains("DELIVERED: 1"));
        assertTrue(text.contains("PENDING: 0"));
    }

    @Test
    void us30MissedAndOutOfStockCounted() {
        Delivery missed = new Delivery(
                1, 21, 1, 10, date, "MISSED", "No answer");

        assertTrue(service.formatStatus(
                docket(), Collections.singletonList(missed))
                .contains("MISSED: 1"));

        Delivery stock = new Delivery(
                2, 21, 1, 10, date, "OUT OF STOCK", null);

        assertTrue(service.formatStatus(
                docket(), Collections.singletonList(stock))
                .contains("OUT OF STOCK: 1"));
    }

    @Test
    void us18DeliveredNamesPublicationAndCounts() {
        Customer c = customer(1, "John", "Murphy", 3);
        Publication p = publication(10, "Daily News");

        Delivery d = new Delivery(
                1, 21, 1, 10, date, "DELIVERED", null);

        String text = service.formatRecordedSummary(
                date, 3,
                Arrays.asList(c),
                Arrays.asList(p),
                Arrays.asList(d));

        assertTrue(text.contains("John Murphy"));
        assertTrue(text.contains("Daily News: 1"));
    }

    @Test
    void us18MissedListedSeparatelyWithReason() {
        Customer c = customer(1, "John", "Murphy", 3);
        Publication p = publication(10, "Daily News");

        Delivery d = new Delivery(
                1, 21, 1, 10, date, "MISSED", "Gate locked");

        String text = service.formatRecordedSummary(
                date, 3,
                Arrays.asList(c),
                Arrays.asList(p),
                Arrays.asList(d));

        assertTrue(text.contains("MISSED / NOT DELIVERED"));
        assertTrue(text.contains("Gate locked"));
        assertFalse(text.contains("Daily News: 1"));
    }

    @Test
    void us18NothingRecordedDisplaysExactMessage() {
        String text = service.formatRecordedSummary(
                date, 3,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());

        assertTrue(text.contains("No deliveries recorded"));
    }

    @Test
    void us18OtherAreasAndDatesExcluded() {
        Customer c = customer(1, "John", "Murphy", 4);
        Publication p = publication(10, "Daily News");

        Delivery d = new Delivery(
                1, 21, 1, 10, date, "DELIVERED", null);

        String text = service.formatRecordedSummary(
                date, 3,
                Arrays.asList(c),
                Arrays.asList(p),
                Arrays.asList(d));

        assertTrue(text.contains("No deliveries recorded"));
    }
}
