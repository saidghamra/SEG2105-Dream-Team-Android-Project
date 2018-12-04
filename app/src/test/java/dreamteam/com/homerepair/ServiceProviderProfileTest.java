package dreamteam.com.homerepair;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServiceProviderProfileTest {
    @Test
    public void testServiceProviderProfileGetID() {
        ServiceProviderProfile serviceProviderProfile = new ServiceProviderProfile ("abc3f",  null,null, null, null, false, null , null);
        assertEquals("Testing Service Provider Profile's getID", "abc3f",serviceProviderProfile.getId());
    }
    @Test
    public void testServiceProviderProfileGetAddress() {
        ServiceProviderProfile serviceProviderProfile = new ServiceProviderProfile ("abc3f",  null,"123Street", null, null, false, null , null);
        assertEquals("Testing Service Provider Profile's getAddress", "123Street",serviceProviderProfile.getAddress());
    }
}
