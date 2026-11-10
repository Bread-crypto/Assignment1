

import java.time.LocalDate;

public class Order {
    private int orderId;
    private int customerId;
    private int publicationId;
    private String deliveryDays;
    private LocalDate orderStartDate;
    private LocalDate orderEndDate;

    public Order(int orderId, int customerId, int publicationId,
                 String deliveryDays, LocalDate orderStartDate,
                 LocalDate orderEndDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.publicationId = publicationId;
        this.deliveryDays = deliveryDays;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
    }

    public int getOrderId() { return orderId; }
    public int getCustomerId() { return customerId; }
    public int getPublicationId() { return publicationId; }
    public String getDeliveryDays() { return deliveryDays; }
    public LocalDate getOrderStartDate() { return orderStartDate; }
    public LocalDate getOrderEndDate() { return orderEndDate; }
}