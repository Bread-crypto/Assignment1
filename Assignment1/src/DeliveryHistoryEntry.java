import java.time.LocalDate;

/** A customer's recorded delivery line. */
public class DeliveryHistoryEntry {
    private final LocalDate deliveryDate;
    private final String publicationName;
    private final String status;
    public DeliveryHistoryEntry(LocalDate deliveryDate, String publicationName, String status) {
        this.deliveryDate = deliveryDate; this.publicationName = publicationName; this.status = status;
    }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public String getPublicationName() { return publicationName; }
    public String getStatus() { return status; }
    @Override public String toString() { return deliveryDate + " | " + publicationName + " | " + status; }
}
