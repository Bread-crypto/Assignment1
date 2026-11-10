import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class PublicationTest {

	// ===== User Story 27 - Add, update and remove publications =====

	// TC01
	@Test
	void validPublicationNameMinBoundary_TC01() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationName("T"));
	}

	// TC02
	@Test
	void validPublicationNameMaxBoundary_TC02() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationName("A".repeat(60)));
	}

	// TC03
	@Test
	void invalidPublicationNameBlank_TC03() {
		Publication publication = new Publication();
		assertFalse(publication.verifyPublicationName(""));
	}

	// TC04
	@Test
	void invalidPublicationNameTooLong_TC04() {
		Publication publication = new Publication();
		assertFalse(publication.verifyPublicationName("A".repeat(61)));
	}

	// TC05
	@Test
	void validPublicationTypeNewspaper_TC05() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationType("NEWSPAPER"));
	}

	// TC06
	@Test
	void validPublicationTypeMagazine_TC06() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationType("MAGAZINE"));
	}

	// TC07
	@Test
	void invalidPublicationType_TC07() {
		Publication publication = new Publication();
		assertFalse(publication.verifyPublicationType("BOOK"));
	}

	// TC08
	@Test
	void validPublicationPriceMinBoundary_TC08() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationPrice(0.01));
	}

	// TC09
	@Test
	void validPublicationPriceMaxBoundary_TC09() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationPrice(100.00));
	}

	// TC10
	@Test
	void invalidPublicationPriceZero_TC10() {
		Publication publication = new Publication();
		assertFalse(publication.verifyPublicationPrice(0.00));
	}

	// TC11
	@Test
	void invalidPublicationPriceTooHigh_TC11() {
		Publication publication = new Publication();
		assertFalse(publication.verifyPublicationPrice(100.01));
	}

	// TC12
	@Test
	void validStockQuantityZero_TC12() {
		Publication publication = new Publication();
		assertTrue(publication.verifyStockQuantity(0));
	}

	// TC13
	@Test
	void invalidStockQuantityNegative_TC13() {
		Publication publication = new Publication();
		assertFalse(publication.verifyStockQuantity(-1));
	}

	// TC14
	@Test
	void createValidPublication_TC14() {
		Publication publication = new Publication();
		assertTrue(publication.createPublication("Irish Times", "NEWSPAPER", 2.50, 20));
	}

	// TC15
	@Test
	void createPublicationInvalidName_TC15() {
		Publication publication = new Publication();
		assertFalse(publication.createPublication("", "NEWSPAPER", 2.50, 20));
	}

	// TC16
	@Test
	void createPublicationInvalidType_TC16() {
		Publication publication = new Publication();
		assertFalse(publication.createPublication("Irish Times", "BOOK", 2.50, 20));
	}

	// TC17
	@Test
	void createPublicationInvalidPrice_TC17() {
		Publication publication = new Publication();
		assertFalse(publication.createPublication("Irish Times", "NEWSPAPER", 0.00, 20));
	}

	// TC18
	@Test
	void createPublicationInvalidStock_TC18() {
		Publication publication = new Publication();
		assertFalse(publication.createPublication("Irish Times", "NEWSPAPER", 2.50, -1));
	}

	// TC19
	@Test
	void createPublicationDuplicateName_TC19() {
		Publication publication = new Publication();
		assertTrue(publication.createPublication("Irish Times", "NEWSPAPER", 2.50, 20));
		assertFalse(publication.createPublication("Irish Times", "NEWSPAPER", 3.00, 10));
	}

	// ===== User Story 29 - Change the price of a publication from a given date =====

	// TC20
	@Test
	void validPriceStartDateToday_TC20() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPriceStartDate(LocalDate.now()));
	}

	// TC21
	@Test
	void validPriceStartDateFuture_TC21() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPriceStartDate(LocalDate.now().plusDays(7)));
	}

	// TC22
	@Test
	void invalidPriceStartDatePast_TC22() {
		Publication publication = new Publication();
		assertFalse(publication.verifyPriceStartDate(LocalDate.now().minusDays(1)));
	}

	// TC23
	@Test
	void changePriceValid_TC23() {
		Publication publication = new Publication();
		publication.createPublication("Irish Times", "NEWSPAPER", 2.00, 20);
		LocalDate startDate = LocalDate.now().plusDays(7);
		assertTrue(publication.changePrice(2.50, startDate));
		assertEquals(2.00, publication.getPriceOnDate(startDate.minusDays(1)), 0.001);
		assertEquals(2.50, publication.getPriceOnDate(startDate), 0.001);
		assertEquals(2.50, publication.getPriceOnDate(startDate.plusDays(1)), 0.001);
	}

	// TC24
	@Test
	void changePriceInvalidPrice_TC24() {
		Publication publication = new Publication();
		publication.createPublication("Irish Times", "NEWSPAPER", 2.00, 20);
		assertFalse(publication.changePrice(0.00, LocalDate.now().plusDays(7)));
	}

	// TC25
	@Test
	void changePriceInvalidStartDate_TC25() {
		Publication publication = new Publication();
		publication.createPublication("Irish Times", "NEWSPAPER", 2.00, 20);
		assertFalse(publication.changePrice(2.50, LocalDate.now().minusDays(1)));
	}

	// TC26
	@Test
	void priceHistoryKept_TC26() {
		Publication publication = new Publication();
		publication.createPublication("Irish Times", "NEWSPAPER", 2.00, 20);
		publication.changePrice(2.50, LocalDate.now().plusDays(7));
		assertEquals(2, publication.getPriceHistorySize());
	}

	// ===== User Story 31 - Publication frequency and release day =====

	// TC27
	@Test
	void validPublicationFrequencyDaily_TC27() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationFrequency("DAILY"));
	}

	// TC28
	@Test
	void validPublicationFrequencyWeekly_TC28() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationFrequency("WEEKLY"));
	}

	// TC29
	@Test
	void validPublicationFrequencyMonthly_TC29() {
		Publication publication = new Publication();
		assertTrue(publication.verifyPublicationFrequency("MONTHLY"));
	}

	// TC30
	@Test
	void invalidPublicationFrequency_TC30() {
		Publication publication = new Publication();
		assertFalse(publication.verifyPublicationFrequency("YEARLY"));
	}

	// TC31
	@Test
	void validReleaseDayWeekly_TC31() {
		Publication publication = new Publication();
		assertTrue(publication.verifyReleaseDay("WEEKLY", "Thursday"));
	}

	// TC32
	@Test
	void invalidReleaseDayWeekly_TC32() {
		Publication publication = new Publication();
		assertFalse(publication.verifyReleaseDay("WEEKLY", "Funday"));
	}

	// TC33
	@Test
	void validReleaseDayMonthlyMinBoundary_TC33() {
		Publication publication = new Publication();
		assertTrue(publication.verifyReleaseDay("MONTHLY", "1"));
	}

	// TC34
	@Test
	void validReleaseDayMonthlyMaxBoundary_TC34() {
		Publication publication = new Publication();
		assertTrue(publication.verifyReleaseDay("MONTHLY", "28"));
	}

	// TC35
	@Test
	void invalidReleaseDayMonthlyTooLow_TC35() {
		Publication publication = new Publication();
		assertFalse(publication.verifyReleaseDay("MONTHLY", "0"));
	}

	// TC36
	@Test
	void invalidReleaseDayMonthlyTooHigh_TC36() {
		Publication publication = new Publication();
		assertFalse(publication.verifyReleaseDay("MONTHLY", "29"));
	}

	// TC37
	@Test
	void weeklyMagazineAppearsOnReleaseDay_TC37() {
		Publication publication = new Publication();
		assertTrue(publication.isDueOnDate("WEEKLY", "Thursday", LocalDate.of(2026, 10, 22)));
	}

	// TC38
	@Test
	void weeklyMagazineNotOnOtherDay_TC38() {
		Publication publication = new Publication();
		assertFalse(publication.isDueOnDate("WEEKLY", "Thursday", LocalDate.of(2026, 10, 23)));
	}

	// TC39
	@Test
	void monthlyMagazineAppearsOnReleaseDay_TC39() {
		Publication publication = new Publication();
		assertTrue(publication.isDueOnDate("MONTHLY", "1", LocalDate.of(2026, 11, 1)));
	}

	// TC40
	@Test
	void monthlyMagazineNotOnOtherDay_TC40() {
		Publication publication = new Publication();
		assertFalse(publication.isDueOnDate("MONTHLY", "1", LocalDate.of(2026, 11, 2)));
	}

	// ===== User Story 8 - Check the stock level of each publication =====

	// TC41
	@Test
	void stockStatusOutOfStock_TC41() {
		Publication publication = new Publication();
		assertEquals("OUT OF STOCK", publication.getStockStatus(0, 5));
	}

	// TC42
	@Test
	void stockStatusLowAtThreshold_TC42() {
		Publication publication = new Publication();
		assertEquals("LOW", publication.getStockStatus(5, 5));
	}

	// TC43
	@Test
	void stockStatusLowBelowThreshold_TC43() {
		Publication publication = new Publication();
		assertEquals("LOW", publication.getStockStatus(3, 5));
	}

	// TC44
	@Test
	void stockStatusInStock_TC44() {
		Publication publication = new Publication();
		assertEquals("IN STOCK", publication.getStockStatus(6, 5));
	}

	// ===== User Story 9 - Low stock warning =====

	// TC45
	@Test
	void validLowStockThresholdZero_TC45() {
		Publication publication = new Publication();
		assertTrue(publication.verifyLowStockThreshold(0));
	}

	// TC46
	@Test
	void validLowStockThreshold_TC46() {
		Publication publication = new Publication();
		assertTrue(publication.verifyLowStockThreshold(5));
	}

	// TC47
	@Test
	void invalidLowStockThresholdNegative_TC47() {
		Publication publication = new Publication();
		assertFalse(publication.verifyLowStockThreshold(-1));
	}

	// TC48
	@Test
	void warningWhenStockEqualsThreshold_TC48() {
		Publication publication = new Publication();
		assertTrue(publication.isLowStockWarning(5, 5));
	}

	// TC49
	@Test
	void noWarningWhenStockAboveThreshold_TC49() {
		Publication publication = new Publication();
		assertFalse(publication.isLowStockWarning(6, 5));
	}

	// TC50
	@Test
	void warningNamesPublicationAndStock_TC50() {
		Publication publication = new Publication();
		String warning = publication.getLowStockWarning("Irish Times", 5);
		assertTrue(warning.contains("Irish Times"));
		assertTrue(warning.contains("5"));
	}

	// ===== User Story 10 - Record copies received and returned =====

	// TC51
	@Test
	void validQuantityReceivedMinBoundary_TC51() {
		Publication publication = new Publication();
		assertTrue(publication.verifyQuantityReceived("1"));
	}

	// TC52
	@Test
	void validQuantityReceived_TC52() {
		Publication publication = new Publication();
		assertTrue(publication.verifyQuantityReceived("20"));
	}

	// TC53
	@Test
	void invalidQuantityReceivedZero_TC53() {
		Publication publication = new Publication();
		assertFalse(publication.verifyQuantityReceived("0"));
	}

	// TC54
	@Test
	void invalidQuantityReceivedNegative_TC54() {
		Publication publication = new Publication();
		assertFalse(publication.verifyQuantityReceived("-5"));
	}

	// TC55
	@Test
	void invalidQuantityReceivedDecimal_TC55() {
		Publication publication = new Publication();
		assertFalse(publication.verifyQuantityReceived("2.5"));
	}

	// TC56
	@Test
	void validQuantityReturnedMinBoundary_TC56() {
		Publication publication = new Publication();
		assertTrue(publication.verifyQuantityReturned("1"));
	}

	// TC57
	@Test
	void invalidQuantityReturnedZero_TC57() {
		Publication publication = new Publication();
		assertFalse(publication.verifyQuantityReturned("0"));
	}

	// TC58
	@Test
	void invalidQuantityReturnedNegative_TC58() {
		Publication publication = new Publication();
		assertFalse(publication.verifyQuantityReturned("-2"));
	}

	// TC59
	@Test
	void invalidQuantityReturnedDecimal_TC59() {
		Publication publication = new Publication();
		assertFalse(publication.verifyQuantityReturned("1.5"));
	}

	// TC60
	@Test
	void receiveStockIncreasesQuantity_TC60() {
		Publication publication = new Publication();
		assertEquals(25, publication.receiveStock(5, 20));
	}

	// TC61
	@Test
	void returnStockIncreasesQuantity_TC61() {
		Publication publication = new Publication();
		assertEquals(20, publication.returnStock(17, 3));
	}

	// TC62
	@Test
	void returnMoreThanIssuedRejected_TC62() {
		Publication publication = new Publication();
		assertFalse(publication.verifyReturnNotMoreThanIssued(4, 3));
	}

	// TC63
	@Test
	void returnEqualToIssuedAccepted_TC63() {
		Publication publication = new Publication();
		assertTrue(publication.verifyReturnNotMoreThanIssued(3, 3));
	}
}
