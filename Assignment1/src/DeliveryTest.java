import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

// Unit tests for the Delivery model.
public class DeliveryTest {

    // TC01: Checks that the constructor stores delivery details.
    @Test
    public void testDeliveryConstructor() {

        Delivery delivery = new Delivery(
            1, 1, 1, 1,
            LocalDate.now(),
            "DELIVERED",
            null
        );

        assertEquals(1, delivery.getDeliveryId());
        assertEquals(1, delivery.getOrderId());
        assertEquals(1, delivery.getCustomerId());
        assertEquals(1, delivery.getPublicationId());
        assertEquals(LocalDate.now(), delivery.getDeliveryDate());
        assertEquals("DELIVERED", delivery.getDeliveryStatus());
        assertNull(delivery.getMissedReason());
    }
    
    // TC02: Checks that today's date is accepted as a valid delivery date.
    @Test
    public void validDeliveryDateToday_TC02() {

        Delivery delivery = new Delivery(
            1, 1, 1, 1,
            LocalDate.now(),
            "DELIVERED",
            null
        );

        assertTrue(delivery.isValidDeliveryDate());
    }
    
    // TC03: Checks that a past delivery date is valid.
    @Test
    public void validDeliveryDatePast_TC03() {

        Delivery delivery = new Delivery(
            1, 1, 1, 1,
            LocalDate.now().minusDays(1),
            "DELIVERED",
            null
        );

        assertTrue(delivery.isValidDeliveryDate());
    }
    
    // TC04: Checks that a future delivery date is invalid.
    @Test
    public void invalidDeliveryDateFuture_TC04() {
        Delivery delivery = new Delivery(
            1, 1, 1, 1,
            LocalDate.now().plusDays(1),
            "DELIVERED",
            null
        );

        assertFalse(delivery.isValidDeliveryDate());
    }
    
    // TC05: Checks that a delivery can be marked DELIVERED.
    @Test
    public void markDeliveryAsDelivered_TC05() {
        Delivery delivery = new Delivery(
            1, 1, 1, 1,
            LocalDate.now(),
            "PENDING",
            null
        );

        delivery.markDelivered();

        assertEquals("DELIVERED", delivery.getDeliveryStatus());
    }
    
    // TC06: Checks that only permitted delivery statuses are valid.
    @Test
    public void validDeliveryStatuses_TC06() {
        Delivery delivered = new Delivery(
            1, 1, 1, 1, LocalDate.now(), "DELIVERED", null);

        Delivery missed = new Delivery(
            2, 1, 1, 1, LocalDate.now(), "MISSED", "Not home");

        Delivery outOfStock = new Delivery(
            3, 1, 1, 1, LocalDate.now(), "OUT OF STOCK", null);

        Delivery invalid = new Delivery(
            4, 1, 1, 1, LocalDate.now(), "PENDING", null);

        assertTrue(delivered.isValidDeliveryStatus());
        assertTrue(missed.isValidDeliveryStatus());
        assertTrue(outOfStock.isValidDeliveryStatus());
        assertFalse(invalid.isValidDeliveryStatus());
    }
    
    // TC08: Checks that a missed delivery reason with at least one character is valid.
    @Test
    public void validMissedReasonMinimumLength_TC08() {
        Delivery delivery = new Delivery(
                1, 1, 1, 1,
                LocalDate.now(),
                "MISSED",
                "N"
        );

        assertTrue(delivery.isValidMissedReason());
    }
    
    // TC09: Checks that a missed delivery reason of exactly 100 characters is valid.
    @Test
    public void validMissedReasonMaximumLength_TC09() {
        String reason = "A".repeat(100);

        Delivery delivery = new Delivery(
                1, 1, 1, 1,
                LocalDate.now(),
                "MISSED",
                reason
        );

        assertEquals(100, reason.length());
        assertTrue(delivery.isValidMissedReason());
    }
    
    // TC10: Checks that an empty missed delivery reason is invalid.
    @Test
    public void invalidMissedReasonEmpty_TC10() {
        Delivery delivery = new Delivery(
                1, 1, 1, 1,
                LocalDate.now(),
                "MISSED",
                ""
        );

        assertFalse(delivery.isValidMissedReason());
    }
    
    // TC11: Checks that a missed delivery reason exceeding 100 characters is invalid.
    @Test
    public void invalidMissedReasonOverMaximumLength_TC11() {
        String reason = "A".repeat(101);

        Delivery delivery = new Delivery(
                1, 1, 1, 1,
                LocalDate.now(),
                "MISSED",
                reason
        );

        assertEquals(101, reason.length());
        assertFalse(delivery.isValidMissedReason());
    }
    
    // TC12: Checks that an invalid missed reason returns an error message.
    @Test
    public void invalidMissedReasonErrorMessage_TC12() {
        Delivery delivery = new Delivery(
                1, 1, 1, 1,
                LocalDate.now(),
                "MISSED",
                ""
        );

        assertEquals(
                "Invalid missed delivery reason. It must contain between 1 and 100 characters.",
                delivery.getMissedReasonErrorMessage()
        );
    }
    
    
}