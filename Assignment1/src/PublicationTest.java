import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class PublicationTest {

    // User Story 27 - Add, update and remove publications

    @Test
    void validPublicationNameMinBoundary_TC01() {
        assertTrue(new Publication().verifyPublicationName("T"));
    }

    @Test
    void validPublicationNameMaxBoundary_TC02() {
        assertTrue(new Publication().verifyPublicationName("A".repeat(60)));
    }

    @Test
    void invalidPublicationNameBlank_TC03() {
        assertFalse(new Publication().verifyPublicationName(""));
    }

    @Test
    void invalidPublicationNameTooLong_TC04() {
        assertFalse(new Publication().verifyPublicationName("A".repeat(61)));
    }

    @Test
    void validPublicationTypeNewspaper_TC05() {
        assertTrue(new Publication().verifyPublicationType("NEWSPAPER"));
    }

    @Test
    void validPublicationTypeMagazine_TC06() {
        assertTrue(new Publication().verifyPublicationType("MAGAZINE"));
    }

    @Test
    void invalidPublicationType_TC07() {
        assertFalse(new Publication().verifyPublicationType("BOOK"));
    }

    @Test
    void validPublicationPriceMinBoundary_TC08() {
        assertTrue(new Publication().verifyPublicationPrice(0.01));
    }

    @Test
    void validPublicationPriceMaxBoundary_TC09() {
        assertTrue(new Publication().verifyPublicationPrice(100.00));
    }

    @Test
    void invalidPublicationPriceZero_TC10() {
        assertFalse(new Publication().verifyPublicationPrice(0));
    }

    @Test
    void invalidPublicationPriceTooHigh_TC11() {
        assertFalse(new Publication().verifyPublicationPrice(100.01));
    }

    @Test
    void validStockQuantityZero_TC12() {
        assertTrue(new Publication().verifyStockQuantity(0));
    }

    @Test
    void invalidStockQuantityNegative_TC13() {
        assertFalse(new Publication().verifyStockQuantity(-1));
    }

    @Test
    void createValidPublication_TC14() {
        assertTrue(new Publication().createPublication(
                "Irish Times", "NEWSPAPER", 2.50, 20));
    }

    @Test
    void createPublicationInvalidName_TC15() {
        assertFalse(new Publication().createPublication(
                "", "NEWSPAPER", 2.50, 20));
    }

    @Test
    void createPublicationInvalidType_TC16() {
        assertFalse(new Publication().createPublication(
                "Irish Times", "BOOK", 2.50, 20));
    }

    @Test
    void createPublicationInvalidPrice_TC17() {
        assertFalse(new Publication().createPublication(
                "Irish Times", "NEWSPAPER", 0, 20));
    }

    @Test
    void createPublicationInvalidStock_TC18() {
        assertFalse(new Publication().createPublication(
                "Irish Times", "NEWSPAPER", 2.50, -1));
    }

    // Duplicate-name checking belongs in the DAO/database.

    @Test
    void sameNameCanExistInSeparateObjectsBeforeDatabaseCheck_TC19() {

        Publication first = new Publication();
        Publication second = new Publication();

        assertTrue(first.createPublication(
                "Irish Times", "NEWSPAPER", 2.50, 20));

        assertTrue(second.createPublication(
                "Irish Times", "NEWSPAPER", 3.00, 10));
    }

    // User Story 29 - Change publication price from a given date

    @Test
    void validPriceStartDateToday_TC20() {
        assertTrue(new Publication().verifyPriceStartDate(LocalDate.now()));
    }

    @Test
    void validPriceStartDateFuture_TC21() {
        assertTrue(new Publication().verifyPriceStartDate(
                LocalDate.now().plusDays(7)));
    }

    @Test
    void invalidPriceStartDatePast_TC22() {
        assertFalse(new Publication().verifyPriceStartDate(
                LocalDate.now().minusDays(1)));
    }

    @Test
    void changePriceValid_TC23() {

        Publication p = new Publication();
        p.createPublication("Irish Times", "NEWSPAPER", 2.00, 20);

        LocalDate start = LocalDate.now().plusDays(7);

        assertTrue(p.changePrice(2.50, start));
        assertEquals(2.00, p.getPriceOnDate(start.minusDays(1)), 0.001);
        assertEquals(2.50, p.getPriceOnDate(start), 0.001);
        assertEquals(2.50, p.getPriceOnDate(start.plusDays(1)), 0.001);
    }

    @Test
    void changePriceInvalidPrice_TC24() {

        Publication p = new Publication();
        p.createPublication("Irish Times", "NEWSPAPER", 2, 20);

        assertFalse(p.changePrice(0, LocalDate.now().plusDays(7)));
    }

    @Test
    void changePriceInvalidStartDate_TC25() {

        Publication p = new Publication();
        p.createPublication("Irish Times", "NEWSPAPER", 2, 20);

        assertFalse(p.changePrice(
                2.50, LocalDate.now().minusDays(1)));
    }

    @Test
    void priceHistoryKept_TC26() {

        Publication p = new Publication();
        p.createPublication("Irish Times", "NEWSPAPER", 2, 20);

        assertTrue(p.changePrice(2.50, LocalDate.now().plusDays(7)));
        assertEquals(2, p.getPriceHistorySize());
    }

    // User Story 31 - Publication frequency and release day

    @Test
    void validPublicationFrequencyDaily_TC27() {
        assertTrue(new Publication().verifyPublicationFrequency("DAILY"));
    }

    @Test
    void validPublicationFrequencyWeekly_TC28() {
        assertTrue(new Publication().verifyPublicationFrequency("WEEKLY"));
    }

    @Test
    void validPublicationFrequencyMonthly_TC29() {
        assertTrue(new Publication().verifyPublicationFrequency("MONTHLY"));
    }

    @Test
    void invalidPublicationFrequency_TC30() {
        assertFalse(new Publication().verifyPublicationFrequency("YEARLY"));
    }

    @Test
    void validReleaseDayWeekly_TC31() {
        assertTrue(new Publication().verifyReleaseDay("WEEKLY", "Thursday"));
    }

    @Test
    void invalidReleaseDayWeekly_TC32() {
        assertFalse(new Publication().verifyReleaseDay("WEEKLY", "Funday"));
    }

    @Test
    void validReleaseDayMonthlyMinBoundary_TC33() {
        assertTrue(new Publication().verifyReleaseDay("MONTHLY", "1"));
    }

    @Test
    void validReleaseDayMonthlyMaxBoundary_TC34() {
        assertTrue(new Publication().verifyReleaseDay("MONTHLY", "28"));
    }

    @Test
    void invalidReleaseDayMonthlyTooLow_TC35() {
        assertFalse(new Publication().verifyReleaseDay("MONTHLY", "0"));
    }

    @Test
    void invalidReleaseDayMonthlyTooHigh_TC36() {
        assertFalse(new Publication().verifyReleaseDay("MONTHLY", "29"));
    }

    @Test
    void weeklyMagazineAppearsOnReleaseDay_TC37() {
        assertTrue(new Publication().isDueOnDate(
                "WEEKLY", "Thursday", LocalDate.of(2026, 10, 22)));
    }

    @Test
    void weeklyMagazineNotOnOtherDay_TC38() {
        assertFalse(new Publication().isDueOnDate(
                "WEEKLY", "Thursday", LocalDate.of(2026, 10, 23)));
    }

    @Test
    void monthlyMagazineAppearsOnReleaseDay_TC39() {
        assertTrue(new Publication().isDueOnDate(
                "MONTHLY", "1", LocalDate.of(2026, 11, 1)));
    }

    @Test
    void monthlyMagazineNotOnOtherDay_TC40() {
        assertFalse(new Publication().isDueOnDate(
                "MONTHLY", "1", LocalDate.of(2026, 11, 2)));
    }

    // User Story 8 - Check stock level

    @Test
    void stockStatusOutOfStock_TC41() {
        assertEquals("OUT OF STOCK",
                new Publication().getStockStatus(0, 5));
    }

    @Test
    void stockStatusLowAtThreshold_TC42() {
        assertEquals("LOW",
                new Publication().getStockStatus(5, 5));
    }

    @Test
    void stockStatusLowBelowThreshold_TC43() {
        assertEquals("LOW",
                new Publication().getStockStatus(3, 5));
    }

    @Test
    void stockStatusInStock_TC44() {
        assertEquals("IN STOCK",
                new Publication().getStockStatus(6, 5));
    }

    // User Story 9 - Low stock warning

    @Test
    void validLowStockThresholdZero_TC45() {
        assertTrue(new Publication().verifyLowStockThreshold(0));
    }

    @Test
    void validLowStockThreshold_TC46() {
        assertTrue(new Publication().verifyLowStockThreshold(5));
    }

    @Test
    void invalidLowStockThresholdNegative_TC47() {
        assertFalse(new Publication().verifyLowStockThreshold(-1));
    }

    @Test
    void warningWhenStockEqualsThreshold_TC48() {
        assertTrue(new Publication().isLowStockWarning(5, 5));
    }

    @Test
    void noWarningWhenStockAboveThreshold_TC49() {
        assertFalse(new Publication().isLowStockWarning(6, 5));
    }

    @Test
    void warningNamesPublicationAndStock_TC50() {

        String warning =
                new Publication().getLowStockWarning("Irish Times", 5);

        assertTrue(warning.contains("Irish Times"));
        assertTrue(warning.contains("5"));
    }

    // User Story 10 - Record copies received and returned

    @Test
    void validQuantityReceivedMinBoundary_TC51() {
        assertTrue(new Publication().verifyQuantityReceived("1"));
    }

    @Test
    void validQuantityReceived_TC52() {
        assertTrue(new Publication().verifyQuantityReceived("20"));
    }

    @Test
    void invalidQuantityReceivedZero_TC53() {
        assertFalse(new Publication().verifyQuantityReceived("0"));
    }

    @Test
    void invalidQuantityReceivedNegative_TC54() {
        assertFalse(new Publication().verifyQuantityReceived("-5"));
    }

    @Test
    void invalidQuantityReceivedDecimal_TC55() {
        assertFalse(new Publication().verifyQuantityReceived("2.5"));
    }

    @Test
    void validQuantityReturnedMinBoundary_TC56() {
        assertTrue(new Publication().verifyQuantityReturned("1"));
    }

    @Test
    void invalidQuantityReturnedZero_TC57() {
        assertFalse(new Publication().verifyQuantityReturned("0"));
    }

    @Test
    void invalidQuantityReturnedNegative_TC58() {
        assertFalse(new Publication().verifyQuantityReturned("-2"));
    }

    @Test
    void invalidQuantityReturnedDecimal_TC59() {
        assertFalse(new Publication().verifyQuantityReturned("1.5"));
    }

    @Test
    void receiveStockIncreasesQuantity_TC60() {
        assertEquals(25, new Publication().receiveStock(5, 20));
    }

    @Test
    void returnStockIncreasesQuantity_TC61() {
        assertEquals(20, new Publication().returnStock(17, 3));
    }

    @Test
    void returnMoreThanIssuedRejected_TC62() {
        assertFalse(new Publication().verifyReturnNotMoreThanIssued(4, 3));
    }

    @Test
    void returnEqualToIssuedAccepted_TC63() {
        assertTrue(new Publication().verifyReturnNotMoreThanIssued(3, 3));
    }

    // User Story 13 - Stock on delivery dockets

    @Test
    void stockAvailableWhenEnoughStock_TC64() {
        assertTrue(new Publication().verifyStockAvailable(20, 10));
    }

    @Test
    void stockUnavailableWhenInsufficient_TC65() {
        assertFalse(new Publication().verifyStockAvailable(5, 10));
    }

    @Test
    void issueStockReducesQuantity_TC66() {
        assertEquals(10, new Publication().issueStock(20, 10));
    }

    @Test
    void issueStockRejectsInsufficientStock_TC67() {
        assertThrows(IllegalArgumentException.class,
                () -> new Publication().issueStock(5, 10));
    }

    @Test
    void returnUndeliveredStock_TC68() {
        assertEquals(15,
                new Publication().returnUndeliveredStock(10, 5, 8));
    }

    @Test
    void returnMoreThanIssuedRejected_TC69() {
        assertThrows(IllegalArgumentException.class,
                () -> new Publication().returnUndeliveredStock(10, 9, 8));
    }

    // Additional edge-case tests

    @Test
    void nullPublicationNameRejected_TC70() {
        assertFalse(new Publication().verifyPublicationName(null));
    }

    @Test
    void nullPublicationTypeRejected_TC71() {
        assertFalse(new Publication().verifyPublicationType(null));
    }

    @Test
    void infinitePriceRejected_TC72() {
        assertFalse(new Publication().verifyPublicationPrice(
                Double.POSITIVE_INFINITY));
    }

    @Test
    void nanPriceRejected_TC73() {
        assertFalse(new Publication().verifyPublicationPrice(Double.NaN));
    }

    @Test
    void negativeReceivedStockRejected_TC74() {
        assertThrows(IllegalArgumentException.class,
                () -> new Publication().receiveStock(10, -1));
    }

    @Test
    void negativeReturnedStockRejected_TC75() {
        assertThrows(IllegalArgumentException.class,
                () -> new Publication().returnStock(10, -1));
    }

    @Test
    void negativeLowStockThresholdRejected_TC76() {
        assertFalse(new Publication().isLowStockWarning(5, -1));
    }

    @Test
    void nullDateRejectedForPriceChange_TC77() {

        Publication p = new Publication();
        p.createPublication("Irish Times", "NEWSPAPER", 2, 20);

        assertFalse(p.changePrice(2.50, null));
    }

    @Test
    void duplicatePriceStartDateRejected_TC78() {

        Publication p = new Publication();
        p.createPublication("Irish Times", "NEWSPAPER", 2, 20);

        LocalDate date = LocalDate.now().plusDays(5);

        assertTrue(p.changePrice(2.50, date));
        assertFalse(p.changePrice(3.00, date));
    }

    @Test
    void earlierScheduledPriceChangeRejected_TC79() {

        Publication p = new Publication();
        p.createPublication("Irish Times", "NEWSPAPER", 2, 20);

        assertTrue(p.changePrice(2.50, LocalDate.now().plusDays(10)));
        assertFalse(p.changePrice(2.25, LocalDate.now().plusDays(5)));
    }

    @Test
    void invalidScheduleRejected_TC80() {
        assertFalse(new Publication().setPublicationSchedule(
                "WEEKLY", "Funday"));
    }

    @Test
    void validScheduleSaved_TC81() {

        Publication p = new Publication();

        assertTrue(p.setPublicationSchedule("WEEKLY", "Thursday"));
        assertEquals("WEEKLY", p.getPublicationFrequency());
        assertEquals("THURSDAY", p.getReleaseDay());
    }

    @Test
    void negativeStockStatusRejected_TC82() {
        assertThrows(IllegalArgumentException.class,
                () -> new Publication().getStockStatus(-1, 5));
    }

    @Test
    void stockAdditionOverflowRejected_TC83() {
        assertThrows(ArithmeticException.class,
                () -> new Publication().receiveStock(Integer.MAX_VALUE, 1));
    }
}