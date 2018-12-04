package dreamteam.com.homerepair;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RateTest {
    @Test
    public void testRateGetRate() {
        Rate rate = new Rate( "123456", 5,  "great service!");
        assertEquals("Testing the rate's getRate", 5,rate.getRate());
    }
    @Test
    public void testRateGetProviderID(){
        Rate rate = new Rate( "123456", 5,  "great service!");
        assertEquals("Testing the rate's getProviderID", "123456",rate.getServiceProviderId());
    }
    @Test
    public void testRateGetComment(){
        Rate rate = new Rate( "123456", 5,  "great service!");
        assertEquals("Testing the rate's getComment", "great service!",rate.getComment());
    }
}

