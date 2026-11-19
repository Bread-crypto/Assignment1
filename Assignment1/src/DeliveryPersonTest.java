import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DeliveryPersonTest {

    // ===== First Name =====

    // TC01 - First name exactly 1 character -> accepted
    @Test
    void validFirstNameMinBoundary_TC01() {

        DeliveryPerson person = new DeliveryPerson();

        assertTrue(person.verifyFirstName("A"));
    }


    // TC02 - First name exactly 50 characters -> accepted
    @Test
    void validFirstNameMaxBoundary_TC02() {

        DeliveryPerson person = new DeliveryPerson();

        assertTrue(person.verifyFirstName("a".repeat(50)));
    }


    // TC03 - Blank first name -> rejected
    @Test
    void invalidFirstNameBlank_TC03() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifyFirstName(""));
    }


    // TC04 - First name 51 characters -> rejected
    @Test
    void invalidFirstNameTooLong_TC04() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifyFirstName("a".repeat(51)));
    }


    // ===== Surname =====

    // TC05 - Surname exactly 1 character -> accepted
    @Test
    void validSurnameMinBoundary_TC05() {

        DeliveryPerson person = new DeliveryPerson();

        assertTrue(person.verifySurname("S"));
    }


    // TC06 - Surname exactly 50 characters -> accepted
    @Test
    void validSurnameMaxBoundary_TC06() {

        DeliveryPerson person = new DeliveryPerson();

        assertTrue(person.verifySurname("a".repeat(50)));
    }


    // TC07 - Blank surname -> rejected
    @Test
    void invalidSurnameBlank_TC07() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifySurname(""));
    }


    // TC08 - Surname 51 characters -> rejected
    @Test
    void invalidSurnameTooLong_TC08() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifySurname("a".repeat(51)));
    }


    // ===== Phone Number =====

    // TC09 - Valid phone number -> accepted
    @Test
    void validPhoneNumber_TC09() {

        DeliveryPerson person = new DeliveryPerson();

        assertTrue(person.verifyPhoneNumber("0871234567"));
    }


    // TC10 - Phone number too short -> rejected
    @Test
    void invalidPhoneNumberTooShort_TC10() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifyPhoneNumber("087123456"));
    }


    // TC11 - Phone number too long -> rejected
    @Test
    void invalidPhoneNumberTooLong_TC11() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifyPhoneNumber("08712345678"));
    }


    // TC12 - Phone number not starting with 0 -> rejected
    @Test
    void invalidPhoneNumberNoLeadingZero_TC12() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifyPhoneNumber("1871234567"));
    }


    // TC13 - Phone number with letters -> rejected
    @Test
    void invalidPhoneNumberLetters_TC13() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.verifyPhoneNumber("08712AB567"));
    }


    // ===== Create Delivery Person =====

    // TC14 - All details valid -> delivery person created
    @Test
    void createValidDeliveryPerson_TC14() {

        DeliveryPerson person = new DeliveryPerson();

        assertTrue(person.createDeliveryPerson(
                "Ahmad",
                "Sawan",
                "0871234567"));
    }


    // TC15 - Invalid first name -> delivery person not created
    @Test
    void createInvalidDeliveryPerson_TC15() {

        DeliveryPerson person = new DeliveryPerson();

        assertFalse(person.createDeliveryPerson(
                "",
                "Sawan",
                "0871234567"));
    }


    // ===== Status =====

    // TC16 - New delivery person starts ACTIVE
    @Test
    void newDeliveryPersonIsActive_TC16() {

        DeliveryPerson person = new DeliveryPerson();

        person.createDeliveryPerson(
                "Ahmad",
                "Sawan",
                "0871234567");

        assertEquals(
                "ACTIVE",
                person.getStatus());
    }


    // TC17 - Delivery person can be marked INACTIVE
    @Test
    void deliveryPersonCanBeMarkedInactive_TC17() {

        DeliveryPerson person = new DeliveryPerson();

        person.createDeliveryPerson(
                "Ahmad",
                "Sawan",
                "0871234567");

        person.markInactive();

        assertEquals(
                "INACTIVE",
                person.getStatus());

        assertFalse(
                person.isActive());
    }
}