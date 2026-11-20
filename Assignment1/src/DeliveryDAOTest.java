import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

// Unit tests for DeliveryDAO.
public class DeliveryDAOTest {

    // TC07: Checks that completed deliveries appear in customer history.
    @Test
    public void completedDeliveryCustomerHistory_TC07() {

        Publication publication = new Publication();
        PublicationDAO publicationDAO = new PublicationDAO();

        String name = "Delivery History Test " + System.nanoTime();

        assertTrue(publication.createPublication(
                name, "NEWSPAPER", 2.50, 10));

        assertTrue(publicationDAO.addPublication(publication));

        OrderDAO orderDAO = new OrderDAO();

        Order order = new Order(
                0,
                1,
                publication.getPublicationId(),
                "Mon,Wed,Fri",
                LocalDate.now(),
                null
        );

        assertTrue(orderDAO.addOrder(order));

        List<Order> orders = orderDAO.getOrdersByCustomer(1);

        Order savedOrder = orders.stream()
                .filter(o -> o.getPublicationId()
                        == publication.getPublicationId())
                .findFirst()
                .orElse(null);

        assertNotNull(savedOrder);

        Delivery delivery = new Delivery(
                0,
                savedOrder.getOrderId(),
                1,
                publication.getPublicationId(),
                LocalDate.now(),
                "DELIVERED",
                null
        );

        DeliveryDAO deliveryDAO = new DeliveryDAO();

        assertTrue(deliveryDAO.addDelivery(delivery));

        List<Delivery> history =
                deliveryDAO.getCompletedDeliveriesByCustomer(1);

        assertTrue(history.stream().anyMatch(d ->
                d.getOrderId() == savedOrder.getOrderId()));
    }
}