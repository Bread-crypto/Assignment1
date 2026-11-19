import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class BillTest {

    // "today" is taken from the computer clock, so the tests still work next month
    private LocalDate today = LocalDate.now();

    // ===== Billing month (current month or a past month, month 1-12) =====

    // TC01 - Current month -> accepted
    @Test
    void validBillingMonthCurrent_TC01() {
        Bill bill = new Bill();
        assertTrue(bill.verifyBillingMonth(today.getYear(), today.getMonthValue()));
    }

    // TC02 - Past month -> accepted
    @Test
    void validBillingMonthPast_TC02() {
        Bill bill = new Bill();
        LocalDate lastMonth = today.minusMonths(1);
        assertTrue(bill.verifyBillingMonth(lastMonth.getYear(), lastMonth.getMonthValue()));
    }

    // TC03 - Future month -> rejected
    @Test
    void invalidBillingMonthFuture_TC03() {
        Bill bill = new Bill();
        LocalDate nextMonth = today.plusMonths(1);
        assertFalse(bill.verifyBillingMonth(nextMonth.getYear(), nextMonth.getMonthValue()));
    }

    // TC04 - Month 0 -> rejected
    @Test
    void invalidBillingMonthBelowRange_TC04() {
        Bill bill = new Bill();
        assertFalse(bill.verifyBillingMonth(today.getYear(), 0));
    }

    // TC05 - Month 13 -> rejected
    @Test
    void invalidBillingMonthAboveRange_TC05() {
        Bill bill = new Bill();
        assertFalse(bill.verifyBillingMonth(today.getYear() - 1, 13));
    }

    // TC06 - Month exactly 1 -> accepted
    @Test
    void validBillingMonthMinBoundary_TC06() {
        Bill bill = new Bill();
        assertTrue(bill.verifyBillingMonth(today.getYear() - 1, 1));
    }

    // ===== Bill total (sum of DELIVERED item prices) =====

    // TC07 - 1.50 + 2.00 + 1.50 + 2.00 = 7.00
    @Test
    void totalOfDeliveredItems_TC07() {
        Bill bill = new Bill();
        double[] prices = {1.50, 2.00, 1.50, 2.00};
        assertEquals(7.00, bill.calculateTotal(prices), 0.001);
    }

    // TC08 - No deliveries -> 0.00
    @Test
    void totalWithNoDeliveries_TC08() {
        Bill bill = new Bill();
        double[] prices = {};
        assertEquals(0.00, bill.calculateTotal(prices), 0.001);
    }

    // TC09 - One delivery -> 1.50
    @Test
    void totalWithOneDelivery_TC09() {
        Bill bill = new Bill();
        double[] prices = {1.50};
        assertEquals(1.50, bill.calculateTotal(prices), 0.001);
    }

    // ===== Payment date (today or a past date) =====

    // TC10 - Today -> accepted
    @Test
    void validPaymentDateToday_TC10() {
        Bill bill = new Bill();
        assertTrue(bill.verifyPaymentDate(today));
    }

    // TC11 - Yesterday -> accepted
    @Test
    void validPaymentDatePast_TC11() {
        Bill bill = new Bill();
        assertTrue(bill.verifyPaymentDate(today.minusDays(1)));
    }

    // TC12 - Tomorrow -> rejected
    @Test
    void invalidPaymentDateFuture_TC12() {
        Bill bill = new Bill();
        assertFalse(bill.verifyPaymentDate(today.plusDays(1)));
    }

    // TC13 - Empty date -> rejected
    @Test
    void invalidPaymentDateNull_TC13() {
        Bill bill = new Bill();
        assertFalse(bill.verifyPaymentDate(null));
    }

    // ===== Recording a payment =====

    // TC14 - UNPAID bill + valid date -> PAID
    @Test
    void recordPaymentOnUnpaidBill_TC14() {
        Bill bill = new Bill();
        assertTrue(bill.recordPayment(today));
        assertEquals("PAID", bill.getBillStatus());
    }

    // TC15 - Bill already PAID -> cannot be paid again
    @Test
    void recordPaymentOnPaidBill_TC15() {
        Bill bill = new Bill();
        bill.setBillStatus("PAID");
        assertFalse(bill.recordPayment(today));
    }

    // TC16 - Future payment date -> not recorded, still UNPAID
    @Test
    void recordPaymentWithInvalidDate_TC16() {
        Bill bill = new Bill();
        assertFalse(bill.recordPayment(today.plusDays(1)));
        assertEquals("UNPAID", bill.getBillStatus());
    }
}
