import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CustomerTest {

    // ===== First name (1-50 characters) =====

    // TC01 - First name exactly 1 character -> accepted
    @Test
    void validFirstNameMinBoundary_TC01() {
        Customer customer = new Customer();
        assertTrue(customer.verifyFirstName("J"));
    }

    // TC02 - First name exactly 50 characters -> accepted
    @Test
    void validFirstNameMaxBoundary_TC02() {
        Customer customer = new Customer();
        assertTrue(customer.verifyFirstName("a".repeat(50)));
    }

    // TC03 - First name blank -> rejected
    @Test
    void invalidFirstNameBlank_TC03() {
        Customer customer = new Customer();
        assertFalse(customer.verifyFirstName(""));
    }

    // TC04 - First name 51 characters -> rejected
    @Test
    void invalidFirstNameTooLong_TC04() {
        Customer customer = new Customer();
        assertFalse(customer.verifyFirstName("a".repeat(51)));
    }

    // TC05 - First name only spaces -> rejected
    @Test
    void invalidFirstNameSpacesOnly_TC05() {
        Customer customer = new Customer();
        assertFalse(customer.verifyFirstName("   "));
    }

    // ===== Surname (1-50 characters) =====

    // TC06 - Surname exactly 1 character -> accepted
    @Test
    void validSurnameMinBoundary_TC06() {
        Customer customer = new Customer();
        assertTrue(customer.verifySurname("S"));
    }

    // TC07 - Surname exactly 50 characters -> accepted
    @Test
    void validSurnameMaxBoundary_TC07() {
        Customer customer = new Customer();
        assertTrue(customer.verifySurname("a".repeat(50)));
    }

    // TC08 - Surname blank -> rejected
    @Test
    void invalidSurnameBlank_TC08() {
        Customer customer = new Customer();
        assertFalse(customer.verifySurname(""));
    }

    // TC09 - Surname 51 characters -> rejected
    @Test
    void invalidSurnameTooLong_TC09() {
        Customer customer = new Customer();
        assertFalse(customer.verifySurname("a".repeat(51)));
    }

    // ===== Address (1-100 characters) =====

    // TC10 - Valid address -> accepted
    @Test
    void validAddress_TC10() {
        Customer customer = new Customer();
        assertTrue(customer.verifyAddress("5 Main St, Athlone"));
    }

    // TC11 - Address blank -> rejected
    @Test
    void invalidAddressBlank_TC11() {
        Customer customer = new Customer();
        assertFalse(customer.verifyAddress(""));
    }

    // TC12 - Address 101 characters -> rejected
    @Test
    void invalidAddressTooLong_TC12() {
        Customer customer = new Customer();
        assertFalse(customer.verifyAddress("a".repeat(101)));
    }

    // ===== Phone number (10 digits, starts with 0) =====

    // TC13 - Mobile number 10 digits -> accepted
    @Test
    void validPhoneNumberMobile_TC13() {
        Customer customer = new Customer();
        assertTrue(customer.verifyPhoneNumber("0871234567"));
    }

    // TC14 - Landline number 10 digits -> accepted
    @Test
    void validPhoneNumberLandline_TC14() {
        Customer customer = new Customer();
        assertTrue(customer.verifyPhoneNumber("0906412345"));
    }

    // TC15 - Phone number 9 digits -> rejected
    @Test
    void invalidPhoneNumberTooShort_TC15() {
        Customer customer = new Customer();
        assertFalse(customer.verifyPhoneNumber("087123456"));
    }

    // TC16 - Phone number 11 digits -> rejected
    @Test
    void invalidPhoneNumberTooLong_TC16() {
        Customer customer = new Customer();
        assertFalse(customer.verifyPhoneNumber("08712345678"));
    }

    // TC17 - Phone number with letters -> rejected
    @Test
    void invalidPhoneNumberLetters_TC17() {
        Customer customer = new Customer();
        assertFalse(customer.verifyPhoneNumber("08712AB567"));
    }

    // TC18 - Phone number not starting with 0 -> rejected
    @Test
    void invalidPhoneNumberNotStartingWithZero_TC18() {
        Customer customer = new Customer();
        assertFalse(customer.verifyPhoneNumber("1871234567"));
    }

    // ===== Delivery area (1-24) =====

    // TC19 - Delivery area 1 -> accepted
    @Test
    void validDeliveryAreaMinBoundary_TC19() {
        Customer customer = new Customer();
        assertTrue(customer.verifyDeliveryArea(1));
    }

    // TC20 - Delivery area 24 -> accepted
    @Test
    void validDeliveryAreaMaxBoundary_TC20() {
        Customer customer = new Customer();
        assertTrue(customer.verifyDeliveryArea(24));
    }

    // TC21 - Delivery area 0 -> rejected
    @Test
    void invalidDeliveryAreaBelowMinimum_TC21() {
        Customer customer = new Customer();
        assertFalse(customer.verifyDeliveryArea(0));
    }

    // TC22 - Delivery area 25 -> rejected
    @Test
    void invalidDeliveryAreaAboveMaximum_TC22() {
        Customer customer = new Customer();
        assertFalse(customer.verifyDeliveryArea(25));
    }

    // ===== Create customer account (end-to-end) =====

    // TC23 - All details valid -> account created
    @Test
    void createValidCustomerAccount_TC23() {
        Customer customer = new Customer();
        assertTrue(customer.createCustomerAccount(
                "John", "Smith", "5 Main St, Athlone", "0871234567", 3));
    }

    // TC24 - Invalid first name -> account not created
    @Test
    void createAccountInvalidFirstName_TC24() {
        Customer customer = new Customer();
        assertFalse(customer.createCustomerAccount(
                "", "Smith", "5 Main St, Athlone", "0871234567", 3));
    }

    // TC25 - Invalid phone number -> account not created
    @Test
    void createAccountInvalidPhoneNumber_TC25() {
        Customer customer = new Customer();
        assertFalse(customer.createCustomerAccount(
                "John", "Smith", "5 Main St, Athlone", "087123456", 3));
    }

    // TC26 - Invalid delivery area -> account not created
    @Test
    void createAccountInvalidDeliveryArea_TC26() {
        Customer customer = new Customer();
        assertFalse(customer.createCustomerAccount(
                "John", "Smith", "5 Main St, Athlone", "0871234567", 0));
    }
}