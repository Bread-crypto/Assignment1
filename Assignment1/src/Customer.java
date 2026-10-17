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
    	if (surname == null) {
            return false;
        }
        String trimmed = surname.trim();
        return trimmed.length() >= 1 && surname.length() <= 50;
    }

    /** Address must be 1 to 100 characters (not blank). */
    public boolean verifyAddress(String address) {
    	if (address == null) {
            return false;
        }
        String trimmed = address.trim();
        return trimmed.length() >= 1 && address.length() <= 100;
    }

    /** Phone number must be exactly 10 digits and start with 0. */
    public boolean verifyPhoneNumber(String phoneNumber) {
    	if (phoneNumber == null) {
            return false;
        }
        return phoneNumber.matches("0\\d{9}");
    }

    /** Delivery area must be between 1 and 24. */
    public boolean verifyDeliveryArea(int deliveryAreaId) {
    	return deliveryAreaId >= 1 && deliveryAreaId <= 24;
    }

    /** Creates the account only if every detail is valid. */
    public boolean createCustomerAccount(String firstName, String surname, String address,
                                         String phoneNumber, int deliveryAreaId) {
    	if (verifyFirstName(firstName)
                && verifySurname(surname)
                && verifyAddress(address)
                && verifyPhoneNumber(phoneNumber)
                && verifyDeliveryArea(deliveryAreaId)) {

            this.firstName = firstName;
            this.surname = surname;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.deliveryAreaId = deliveryAreaId;
            return true;
        }
        return false;
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