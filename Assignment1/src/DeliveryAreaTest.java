import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DeliveryAreaTest {

    // ===== Area Name =====

    // TC01 - Area name exactly 1 character -> accepted
    @Test
    void validAreaNameMinBoundary_TC01() {

        DeliveryArea area = new DeliveryArea();

        assertTrue(area.verifyAreaName("A"));
    }


    // TC02 - Area name exactly 30 characters -> accepted
    @Test
    void validAreaNameMaxBoundary_TC02() {

        DeliveryArea area = new DeliveryArea();

        assertTrue(area.verifyAreaName("a".repeat(30)));
    }


    // TC03 - Blank area name -> rejected
    @Test
    void invalidAreaNameBlank_TC03() {

        DeliveryArea area = new DeliveryArea();

        assertFalse(area.verifyAreaName(""));
    }


    // TC04 - Spaces only -> rejected
    @Test
    void invalidAreaNameSpacesOnly_TC04() {

        DeliveryArea area = new DeliveryArea();

        assertFalse(area.verifyAreaName("   "));
    }


    // TC05 - Area name 31 characters -> rejected
    @Test
    void invalidAreaNameTooLong_TC05() {

        DeliveryArea area = new DeliveryArea();

        assertFalse(area.verifyAreaName("a".repeat(31)));
    }


    // ===== Delivery Area ID =====

    // TC06 - Area ID 1 -> accepted
    @Test
    void validDeliveryAreaIdMinBoundary_TC06() {

        DeliveryArea area = new DeliveryArea();

        assertTrue(area.verifyDeliveryAreaId(1));
    }


    // TC07 - Area ID 24 -> accepted
    @Test
    void validDeliveryAreaIdMaxBoundary_TC07() {

        DeliveryArea area = new DeliveryArea();

        assertTrue(area.verifyDeliveryAreaId(24));
    }


    // TC08 - Area ID 0 -> rejected
    @Test
    void invalidDeliveryAreaIdBelowMinimum_TC08() {

        DeliveryArea area = new DeliveryArea();

        assertFalse(area.verifyDeliveryAreaId(0));
    }


    // TC09 - Area ID 25 -> rejected
    @Test
    void invalidDeliveryAreaIdAboveMaximum_TC09() {

        DeliveryArea area = new DeliveryArea();

        assertFalse(area.verifyDeliveryAreaId(25));
    }


    // ===== Create Delivery Area =====

    // TC10 - Valid area name -> delivery area created
    @Test
    void createValidDeliveryArea_TC10() {

        DeliveryArea area = new DeliveryArea();

        assertTrue(
                area.createDeliveryArea("Athlone East"));
    }


    // TC11 - Invalid area name -> delivery area not created
    @Test
    void createInvalidDeliveryArea_TC11() {

        DeliveryArea area = new DeliveryArea();

        assertFalse(
                area.createDeliveryArea(""));
    }
}