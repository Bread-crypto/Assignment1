
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class HolidayDAOTest {

    // ===== ADD HOLIDAY TESTS =====

    // TC01 - Valid holiday period -> accepted
    @Test
    public void validHoliday_TC01() {

        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
                0,
                1,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        assertTrue(dao.addHoliday(holiday));
    }
    
    // TC07 - Holiday starting today -> accepted
    @Test
    public void holidayStartingToday_TC07() {
        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
                0,
                1,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        assertTrue(dao.addHoliday(holiday));
    }
    
    // TC08 - Holiday for a customer who does not exist -> rejected
    @Test
    public void invalidCustomerHoliday_TC08() {
        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
                0,
                -999,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        assertFalse(dao.addHoliday(holiday));
    }

    // TC02 - Holiday start date in the past -> rejected
    @Test
    public void invalidHolidayPastStartDate_TC02() {

        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
                0,
                1,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(2)
        );

        assertFalse(dao.addHoliday(holiday));
    }

    // TC03 - Holiday end date before start date -> rejected
    @Test
    public void invalidHolidayEndDate_TC03() {

        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
                0,
                1,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(1)
        );

        assertFalse(dao.addHoliday(holiday));
    }
    
    // TC04 - Retrieve holidays belonging to a customer.
    @Test
    public void retrieveHolidaysByCustomer_TC04() {

        HolidayDAO dao = new HolidayDAO();

        // Create a unique holiday period for this test.
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(12);

        Holiday holiday = new Holiday(
                0,
                1,
                startDate,
                endDate
        );

        // First, save the holiday.
        assertTrue(dao.addHoliday(holiday));

        // Retrieve holidays for customer ID 1.
        java.util.List<Holiday> holidays = dao.getHolidaysByCustomer(1);

        // Check that the saved holiday can be found.
        assertTrue(holidays.stream().anyMatch(h ->
                h.getHolidayStartDate().equals(startDate)
                && h.getHolidayEndDate().equals(endDate)
        ));
    }
    
    // TC05 - Missing holiday start date -> rejected.
    @Test
    public void invalidHolidayMissingStartDate_TC05() {

        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
                0,
                1,
                null,
                LocalDate.now().plusDays(3)
        );

        assertFalse(dao.addHoliday(holiday));
    }

    // TC06 - Missing holiday end date -> rejected.
    @Test
    public void invalidHolidayMissingEndDate_TC06() {

        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
                0,
                1,
                LocalDate.now().plusDays(1),
                null
        );

        assertFalse(dao.addHoliday(holiday));
    }
    
    // TC09 - Null holiday object -> rejected
    @Test
    public void nullHoliday_TC09() {
        HolidayDAO dao = new HolidayDAO();

        assertFalse(dao.addHoliday(null));
    }
    
    // TC10 - Holiday starting and ending on the same day -> accepted
    @Test
    public void sameDayHoliday_TC10() {
        HolidayDAO dao = new HolidayDAO();

        LocalDate today = LocalDate.now().plusDays(1);

        Holiday holiday = new Holiday(
                0,
                1,
                today,
                today
        );

        assertTrue(dao.addHoliday(holiday));
    }
}
