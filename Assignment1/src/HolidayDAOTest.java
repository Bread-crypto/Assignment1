import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class HolidayDAOTest {

    @Test
    public void testAddValidHoliday() {
        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
            0, 1,
            LocalDate.now().plusDays(1),
            LocalDate.now().plusDays(3)
        );

        assertTrue(dao.addHoliday(holiday));
    }

    @Test
    public void testRejectPastHolidayStartDate() {
        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
            0, 1,
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(2)
        );

        assertFalse(dao.addHoliday(holiday));
    }

    @Test
    public void testRejectEndDateBeforeStartDate() {
        HolidayDAO dao = new HolidayDAO();

        Holiday holiday = new Holiday(
            0, 1,
            LocalDate.now().plusDays(3),
            LocalDate.now().plusDays(1)
        );

        assertFalse(dao.addHoliday(holiday));
    }
}