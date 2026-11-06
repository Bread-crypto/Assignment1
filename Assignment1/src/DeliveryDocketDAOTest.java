
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class DeliveryDocketDAOTest {

    // TC12 - Check that a docket can be saved to MySQL
    @Test
    public void testAddDeliveryDocket() {

        DeliveryDocketDAO dao = new DeliveryDocketDAO();

        DeliveryDocket docket = new DeliveryDocket();
        docket.createDeliveryDocket(
                LocalDate.of(2026, 11, 5), 1, 1);

        boolean result = dao.addDeliveryDocket(docket);

        assertTrue(result);
    }
}
