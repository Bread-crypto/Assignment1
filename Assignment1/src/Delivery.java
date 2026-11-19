import java.time.LocalDate;

// Represents a delivery in the News Agent system.
public class Delivery {

    private int deliveryId;
    private int orderId;
    private int customerId;
    private int publicationId;
    private LocalDate deliveryDate;
    private String deliveryStatus;
    private String missedReason;

    // Constructor stores all supplied delivery details.
    public Delivery(int deliveryId, int orderId, int customerId,
                    int publicationId, LocalDate deliveryDate,
                    String deliveryStatus, String missedReason) {

        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.publicationId = publicationId;
        this.deliveryDate = deliveryDate;
        this.deliveryStatus = deliveryStatus;
        this.missedReason = missedReason;
    }

    // Returns the delivery ID.
    public int getDeliveryId() {
        return deliveryId;
    }

    // Returns the order ID.
    public int getOrderId() {
        return orderId;
    }

    // Returns the customer ID.
    public int getCustomerId() {
        return customerId;
    }

    // Returns the publication ID.
    public int getPublicationId() {
        return publicationId;
    }

    // Returns the delivery date.
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    // Returns the delivery status.
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    // Returns the missed delivery reason.
    public String getMissedReason() {
        return missedReason;
    }
    
    // Checks that the delivery date is today or in the past.
    public boolean isValidDeliveryDate() {
        return deliveryDate != null
                && !deliveryDate.isAfter(LocalDate.now());
    }
    
    // Marks the delivery as completed.
    public void markDelivered() {
        this.deliveryStatus = "DELIVERED";
    }
    
    // Checks whether the delivery status is permitted.
    public boolean isValidDeliveryStatus() {
        return "DELIVERED".equals(deliveryStatus)
                || "MISSED".equals(deliveryStatus)
                || "OUT OF STOCK".equals(deliveryStatus);
    }
    
    // Checks that a missed delivery reason contains between 1 and 100 characters.
    public boolean isValidMissedReason() {
        return missedReason != null
                && !missedReason.trim().isEmpty()
                && missedReason.length() <= 100;
    }
    
    // Returns an error message when the missed delivery reason is invalid.
    public String getMissedReasonErrorMessage() {
        if (!isValidMissedReason()) {
            return "Invalid missed delivery reason. It must contain between 1 and 100 characters.";
        }
        return null;
    }
    
   
}