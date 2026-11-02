import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class OrderDAOTest {

    @Test
    public void testAddValidOrder() {
        OrderDAO dao = new OrderDAO();

        Order order = new Order(
            0,
            1,
            1,
            "Mon,Wed,Fri",
            LocalDate.now(),
            null
        );

        assertTrue(dao.addOrder(order));
    }
    
    @Test
    public void testRejectOrderWithoutDeliveryDays() {
        OrderDAO dao = new OrderDAO();

        Order order = new Order(
            0,
            1,
            1,
            "",
            LocalDate.now(),
            null
        );

        assertFalse(dao.addOrder(order));
    }
}