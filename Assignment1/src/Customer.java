public class Customer {

    // ===== Fields (match the columns in CustomerTable) =====
    private int customerId;
    private String firstName;
    private String surname;
    private String address;
    private String phoneNumber;
    private int deliveryAreaId;
    private String status = "ACTIVE";

    // ===== Constructors =====
    public Customer() {
    }

    public Customer(String firstName, String surname, String address,
                    String phoneNumber, int deliveryAreaId) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.deliveryAreaId = deliveryAreaId;
    }

    // ===== Validation (TDD - not written yet) =====

    /** First name must be 1 to 50 characters (not blank). */
   public boolean verifyFirstName(String firstName) {
       if (firstName == null) {
           return false;
       }
       String trimmed = firstName.trim();
       return trimmed.length() >= 1 && firstName.length() <= 50;
    }
    /** Surname must be 1 to 50 characters (not blank). */
    public boolean verifySurname(String surname) {
        throw new RuntimeException("No Product Written Yet"); // TODO: Implement
    }

    /** Address must be 1 to 100 characters (not blank). */
    public boolean verifyAddress(String address) {
        throw new RuntimeException("No Product Written Yet"); // TODO: Implement
    }

    /** Phone number must be exactly 10 digits and start with 0. */
    public boolean verifyPhoneNumber(String phoneNumber) {
        throw new RuntimeException("No Product Written Yet"); // TODO: Implement
    }

    /** Delivery area must be between 1 and 24. */
    public boolean verifyDeliveryArea(int deliveryAreaId) {
        throw new RuntimeException("No Product Written Yet"); // TODO: Implement
    }

    /** Creates the account only if every detail is valid. */
    public boolean createCustomerAccount(String firstName, String surname, String address,
                                         String phoneNumber, int deliveryAreaId) {
        throw new RuntimeException("No Product Written Yet"); // TODO: Implement
    }

    // ===== Getters and setters =====
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getDeliveryAreaId() { return deliveryAreaId; }
    public void setDeliveryAreaId(int deliveryAreaId) { this.deliveryAreaId = deliveryAreaId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Customer ID: " + customerId
             + "\nFirst Name: " + firstName
             + "\nSurname: " + surname
             + "\nAddress: " + address
             + "\nPhone Number: " + phoneNumber
             + "\nDelivery Area: " + deliveryAreaId
             + "\nStatus: " + status;
    }
}