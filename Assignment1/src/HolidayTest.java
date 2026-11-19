import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class HolidayTest {

    @Test
    public void testHolidayDetails() {
        Holiday holiday = new Holiday(
            1, 1,
            LocalDate.of(2026, 12, 25),
            LocalDate.of(2026, 12, 26)
        );

        assertEquals(1, holiday.getHolidayId());
        assertEquals(1, holiday.getCustomerId());
        assertEquals(LocalDate.of(2026, 12, 25),
                     holiday.getHolidayStartDate());
        assertEquals(LocalDate.of(2026, 12, 26),
                     holiday.getHolidayEndDate());
    }
}