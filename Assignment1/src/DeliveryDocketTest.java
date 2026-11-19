
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class DeliveryDocketTest {

    // TC01 - Valid docket date
    @Test
    public void testValidDocketDate() {
        DeliveryDocket docket = new DeliveryDocket();

        assertTrue(docket.verifyDocketDate(LocalDate.of(2026, 11, 5)));
    }

    // TC02 - Null docket date
    @Test
    public void testNullDocketDate() {
        DeliveryDocket docket = new DeliveryDocket();

        assertFalse(docket.verifyDocketDate(null));
    }

    // TC03 - Lowest valid delivery area ID
    @Test
    public void testLowestDeliveryAreaId() {
        DeliveryDocket docket = new DeliveryDocket();

        assertTrue(docket.verifyDeliveryAreaId(1));
    }

    // TC04 - Highest valid delivery area ID
    @Test
    public void testHighestDeliveryAreaId() {
        DeliveryDocket docket = new DeliveryDocket();

        assertTrue(docket.verifyDeliveryAreaId(24));
    }

    // TC05 - Invalid delivery area ID: zero
    @Test
    public void testZeroDeliveryAreaId() {
        DeliveryDocket docket = new DeliveryDocket();

        assertFalse(docket.verifyDeliveryAreaId(0));
    }

    // TC06 - Invalid delivery area ID: above 24
    @Test
    public void testAreaIdAboveLimit() {
        DeliveryDocket docket = new DeliveryDocket();

        assertFalse(docket.verifyDeliveryAreaId(25));
    }
    

    // TC07 - Create a valid delivery docket
    @Test
    public void testCreateValidDeliveryDocket() {
        DeliveryDocket docket = new DeliveryDocket();

        boolean result = docket.createDeliveryDocket(
                LocalDate.of(2026, 11, 5), 1, 2);

        assertTrue(result);
    }
    

    // TC08 - Reject a docket with no date
    @Test
    public void testCreateDocketWithNullDate() {
        DeliveryDocket docket = new DeliveryDocket();

        boolean result = docket.createDeliveryDocket(null, 1, 2);

        assertFalse(result);
    }

    // TC09 - Reject a docket with an invalid area ID
    @Test
    public void testCreateDocketWithInvalidArea() {
        DeliveryDocket docket = new DeliveryDocket();

        boolean result = docket.createDeliveryDocket(
                LocalDate.of(2026, 11, 5), 25, 2);

        assertFalse(result);
    }

    // TC10 - Reject a docket with an invalid delivery person ID
    @Test
    public void testCreateDocketWithInvalidPerson() {
        DeliveryDocket docket = new DeliveryDocket();

        boolean result = docket.createDeliveryDocket(
                LocalDate.of(2026, 11, 5), 1, 0);

        assertFalse(result);
    }
    

    // TC11 - Verify delivery docket details are displayed
    @Test
    public void testDocketToString() {
        DeliveryDocket docket = new DeliveryDocket();

        docket.setDocketId(1);
        docket.createDeliveryDocket(LocalDate.of(2026, 11, 5), 3, 2);

        String details = docket.toString();

        assertTrue(details.contains("Docket ID: 1"));
        assertTrue(details.contains("2026-11-05"));
        assertTrue(details.contains("Delivery Area ID: 3"));
        assertTrue(details.contains("Delivery Person ID: 2"));
    }




}
