

import java.time.LocalDate;


public class Holiday {
    private int holidayId;
    private int customerId;
    private LocalDate holidayStartDate;
    private LocalDate holidayEndDate;

    public Holiday(int holidayId, int customerId,
                   LocalDate holidayStartDate,
                   LocalDate holidayEndDate) {
        this.holidayId = holidayId;
        this.customerId = customerId;
        this.holidayStartDate = holidayStartDate;
        this.holidayEndDate = holidayEndDate;
    }

    public int getHolidayId() { return holidayId; }
    public int getCustomerId() { return customerId; }
    public LocalDate getHolidayStartDate() { return holidayStartDate; }
    public LocalDate getHolidayEndDate() { return holidayEndDate; }
}