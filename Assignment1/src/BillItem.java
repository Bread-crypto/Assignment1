import java.time.LocalDate;

/** One line on an itemised bill: a delivered publication on a date at a price (User Story 20). */
public class BillItem {

    private LocalDate deliveryDate;
    private String publicationName;
    private double price;

    public BillItem(LocalDate deliveryDate, String publicationName, double price) {
        this.deliveryDate = deliveryDate;
        this.publicationName = publicationName;
        this.price = price;
    }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public String getPublicationName() { return publicationName; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%-12s %-30s %6.2f", deliveryDate, publicationName, price);
    }
}
