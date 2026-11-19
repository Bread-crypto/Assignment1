import java.time.LocalDate;

public class Bill {

    // ===== Fields (match the columns in BillTable) =====
    private int billId;
    private int customerId;
    private int billingYear;
    private int billingMonth;
    private double totalAmount;
    private String billStatus = "UNPAID";
    private LocalDate paymentDate;

    // ===== Constructors =====
    public Bill() {
    }

    public Bill(int customerId, int billingYear, int billingMonth) {
        this.customerId = customerId;
        this.billingYear = billingYear;
        this.billingMonth = billingMonth;
    }

    // ===== Validation and logic (TDD - not written yet) =====

    /** Billing month must be 1-12 and not in the future (US 19, 28). */
    public boolean verifyBillingMonth(int year, int month) {
        throw new RuntimeException("Not implemented yet");
    }

    /** Total = sum of the prices of all DELIVERED items; 0.00 if none (US 19). */
    public double calculateTotal(double[] prices) {
        throw new RuntimeException("Not implemented yet");
    }

    /** Payment date must be today or a past date (US 21). */
    public boolean verifyPaymentDate(LocalDate paymentDate) {
        throw new RuntimeException("Not implemented yet");
    }

    /** Marks an UNPAID bill as PAID; a PAID bill cannot be paid again (US 21). */
    public boolean recordPayment(LocalDate paymentDate) {
        throw new RuntimeException("Not implemented yet");
    }

    // ===== Getters and setters =====
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getBillingYear() { return billingYear; }
    public void setBillingYear(int billingYear) { this.billingYear = billingYear; }

    public int getBillingMonth() { return billingMonth; }
    public void setBillingMonth(int billingMonth) { this.billingMonth = billingMonth; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getBillStatus() { return billStatus; }
    public void setBillStatus(String billStatus) { this.billStatus = billStatus; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Bill ID: " + billId
             + "\nCustomer ID: " + customerId
             + "\nBilling Month: " + billingMonth + "/" + billingYear
             + "\nTotal: " + String.format("%.2f", totalAmount)
             + "\nStatus: " + billStatus
             + "\nPayment Date: " + paymentDate;
    }
}
