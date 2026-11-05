
public class DeliveryPerson {

    // ===== Fields =====
    private int deliveryPersonId;
    private String firstName;
    private String surname;
    private String phoneNumber;
    private String status = "ACTIVE";


    // ===== Constructors =====

    public DeliveryPerson() {
    }

    public DeliveryPerson(String firstName, String surname,
                          String phoneNumber) {

        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }


    // ===== Validation =====

    public boolean verifyFirstName(String firstName) {

        return firstName != null
                && !firstName.trim().isEmpty()
                && firstName.length() <= 50;
    }


    public boolean verifySurname(String surname) {

        return surname != null
                && !surname.trim().isEmpty()
                && surname.length() <= 50;
    }


    public boolean verifyPhoneNumber(String phoneNumber) {

        if (phoneNumber == null) {
            return false;
        }

        return phoneNumber.matches("0\\d{9}");
    }


    public boolean createDeliveryPerson(String firstName,
                                        String surname,
                                        String phoneNumber) {

        if (verifyFirstName(firstName)
                && verifySurname(surname)
                && verifyPhoneNumber(phoneNumber)) {

            this.firstName = firstName;
            this.surname = surname;
            this.phoneNumber = phoneNumber;
            this.status = "ACTIVE";

            return true;
        }

        return false;
    }


    public void markInactive() {
        status = "INACTIVE";
    }


    public boolean isActive() {
        return "ACTIVE".equals(status);
    }


    // ===== Getters and Setters =====

    public int getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(int deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {

        return "Delivery Person ID: " + deliveryPersonId
                + "\nFirst Name: " + firstName
                + "\nSurname: " + surname
                + "\nPhone Number: " + phoneNumber
                + "\nStatus: " + status;
    }
}