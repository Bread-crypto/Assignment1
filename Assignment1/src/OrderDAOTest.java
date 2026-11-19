
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class OrderDAOTest {

    // ===== ADD ORDER TESTS =====

	// TC01 - Valid order with an existing publication -> accepted
	@Test
	public void validOrder_TC01() {

	    OrderDAO orderDAO = new OrderDAO();
	    PublicationDAO publicationDAO = new PublicationDAO();

	    // Create a unique publication for this test.
	    Publication publication = new Publication();
	    String name = "Valid Order Test " + System.nanoTime();

	    assertTrue(publication.createPublication(
	            name, "NEWSPAPER", 2.50, 10));

	    assertTrue(publicationDAO.addPublication(publication));

	    int publicationId = publication.getPublicationId();

	    // Create an order using the newly created publication.
	    Order order = new Order(
	            0,
	            1,
	            publicationId,
	            "Mon,Wed,Fri",
	            LocalDate.now(),
	            null
	    );

	    assertTrue(orderDAO.addOrder(order));
	}

    // TC02 - Delivery days blank -> rejected
    @Test
    public void invalidOrderBlankDeliveryDays_TC02() {

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

    // TC03 - Publication ID does not exist -> rejected
    @Test
    public void invalidPublicationId_TC03() {

        OrderDAO dao = new OrderDAO();

        Order order = new Order(
                0,
                1,
                -999,
                "Mon,Wed,Fri",
                LocalDate.now(),
                null
        );

        assertFalse(dao.addOrder(order));
    }

    // TC04 - Order start date in the past -> rejected
    @Test
    public void invalidOrderStartDate_TC04() {

        OrderDAO dao = new OrderDAO();

        Order order = new Order(
                0,
                1,
                1,
                "Mon,Wed,Fri",
                LocalDate.now().minusDays(1),
                null
        );

        assertFalse(dao.addOrder(order));
    }

    // TC05 - End date before start date -> rejected
    @Test
    public void invalidOrderEndDate_TC05() {

        OrderDAO dao = new OrderDAO();

        Order order = new Order(
                0,
                1,
                1,
                "Mon,Wed,Fri",
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(2)
        );

        assertFalse(dao.addOrder(order));
    }
    
    // ===== RETRIEVE AND REMOVE ORDER TESTS =====

 // TC06 - Retrieve orders belonging to a customer -> accepted
    @Test
    public void retrieveOrdersByCustomer_TC06() {

        OrderDAO orderDAO = new OrderDAO();
        PublicationDAO publicationDAO = new PublicationDAO();

        // Create a unique publication for this test.
        Publication publication = new Publication();
        String name = "Retrieve Test " + System.nanoTime();

        assertTrue(publication.createPublication(
                name, "NEWSPAPER", 2.50, 10));

        assertTrue(publicationDAO.addPublication(publication));

        int publicationId = publication.getPublicationId();

        // Add an order for customer ID 1.
        Order order = new Order(
                0,
                1,
                publicationId,
                "Tue,Thu",
                LocalDate.now(),
                null
        );

        assertTrue(orderDAO.addOrder(order));

        // Retrieve the customer's orders and check this order is present.
        java.util.List<Order> orders = orderDAO.getOrdersByCustomer(1);

        assertTrue(orders.stream()
                .anyMatch(o -> o.getPublicationId() == publicationId));
    }

 // TC07 - Remove an active publication -> accepted
    @Test
    public void removePublication_TC07() {

        OrderDAO orderDAO = new OrderDAO();
        PublicationDAO publicationDAO = new PublicationDAO();

        // Create a unique publication for this test.
        Publication publication = new Publication();

        String name = "Removal Test " + System.nanoTime();

        assertTrue(publication.createPublication(
                name, "NEWSPAPER", 2.50, 10));

        assertTrue(publicationDAO.addPublication(publication));

        int publicationId = publication.getPublicationId();

        // Add the publication to customer 1's order.
        Order order = new Order(
                0,
                1,
                publicationId,
                "Mon,Wed,Fri",
                LocalDate.now(),
                null
        );

        assertTrue(orderDAO.addOrder(order));

        // Remove the publication with an end date of tomorrow.
        assertTrue(orderDAO.removePublication(
                1,
                publicationId,
                LocalDate.now().plusDays(1)
        ));
    }

    // TC08 - Removal date in the past -> rejected
    @Test
    public void invalidRemovalDate_TC08() {

        OrderDAO dao = new OrderDAO();

        assertFalse(dao.removePublication(
                1,
                1,
                LocalDate.now().minusDays(1)
        ));
    }

    // TC09 - Remove publication with no matching active order -> rejected
    @Test
    public void removeNonExistingActiveOrder_TC09() {

        OrderDAO dao = new OrderDAO();

        assertFalse(dao.removePublication(
                1,
                999999,
                LocalDate.now().plusDays(1)
        ));
    }
    
 // TC10 - Same publication cannot be added twice to one customer's active orders
    @Test
    public void duplicateActivePublication_TC10() {

        OrderDAO orderDAO = new OrderDAO();
        PublicationDAO publicationDAO = new PublicationDAO();

        // Create a unique publication for this test.
        Publication publication = new Publication();

        String publicationName = "Duplicate Test " + System.nanoTime();

        assertTrue(publication.createPublication(
                publicationName, "NEWSPAPER", 2.50, 10));

        assertTrue(publicationDAO.addPublication(publication));

        int publicationId = publication.getPublicationId();

        // Add the publication to the customer's order for the first time.
        Order firstOrder = new Order(
                0,
                1,
                publicationId,
                "Mon,Wed,Fri",
                LocalDate.now(),
                null
        );

        assertTrue(orderDAO.addOrder(firstOrder));

        // Attempt to add the same publication again.
        Order duplicateOrder = new Order(
                0,
                1,
                publicationId,
                "Tue,Thu",
                LocalDate.now(),
                null
        );

        assertFalse(orderDAO.addOrder(duplicateOrder));
    }
}
