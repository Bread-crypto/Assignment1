import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class OrderTest {

    @Test
    public void testOrderDetails() {
        Order order = new Order(
            1, 1, 1, "Mon,Wed,Fri",
            LocalDate.now(), null
        );

        assertEquals(1, order.getOrderId());
        assertEquals(1, order.getCustomerId());
        assertEquals(1, order.getPublicationId());
        assertEquals("Mon,Wed,Fri", order.getDeliveryDays());
        assertNull(order.getOrderEndDate());
    }
    
    @Test
    public void testOrderHasDeliveryDays() {
        Order order = new Order(
            1, 1, 1, "Mon,Wed,Fri",
            LocalDate.now(), null
        );

        assertFalse(order.getDeliveryDays().isBlank());
    }
}
