package dreamteam.com.homerepair;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceTest {


    @Test
    public void testServiceGetId(){
        Service service = new Service( "paint window", 120,  "1234567890");

        assertEquals("Testing Service's getID", "1234567890",service.getId());
    }

    @Test
    public void testServiceName(){
        Service service = new Service( "paint window", 120,  "1234567890");

        assertEquals("Testing Service's getName", "paint window",service.getName());
    }

    @Test
    public void testServiceHourlyRate(){
        Service service = new Service( "paint window", 120,  "1234567890");

        assertEquals("Testing Service's getHourlyRate", 120,service.getHourlyRate());
    }

    @Test
    public void testServiceToString(){
        Service service = new Service( "paint window", 120,  "1234567890");

        assertEquals("Testing Service's tosString", "paint window: $120",service.toString());
    }
}
