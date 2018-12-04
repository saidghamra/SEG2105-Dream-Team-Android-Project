package dreamteam.com.homerepair;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookingTest {
    @Test
    public void testBookingGetHomeOwnerID() {
        Booking booking = new Booking("123","abcd","plumber","monday",9,11);
        assertEquals("Testing booking getHomeOwnerID", "123", booking.getHomeOwnerID());
    }
    @Test
    public void testBookingGetServiceProviderID() {
        Booking booking = new Booking("123","abcd","plumber","monday",9,11);
        assertEquals("Testing booking getServiceProviderID", "abcd", booking.getServiceProviderID());
    }
    @Test
    public void testBookingGetService() {
        Booking booking = new Booking("123","abcd","plumber","monday",9,11);
        assertEquals("Testing booking getService", "plumber", booking.getService());
    }
    @Test
    public void testBookingGetDay() {
        Booking booking = new Booking("123","abcd","plumber","monday",9,11);
        assertEquals("Testing booking getDay", "monday", booking.getDay());
    }
    @Test
    public void testbookingGetStartTime() {
        Booking booking = new Booking("123","abcd","plumber","monday",9,11);
        assertEquals("Testing booking getStartTime", 9, booking.getStartTime());
    }
    @Test
    public void testbookingGetEndTime() {
        Booking booking = new Booking("123","abcd","plumber","monday",9,11);
        assertEquals("Testing booking getEndTime", 11, booking.getEndTime());
    }
}
