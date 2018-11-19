package dreamteam.com.homerepair;
import org.junit.Test;
import static org.junit.Assert.*;
public class UserTest {

    @Test
    public void testUserGetName(){
        User user1 = new User( "1","Admin", "tim_cook",  "123456");
        assertEquals("Testing User's getUsername", "tim_cook",user1.getUsername());
    }

    @Test
    public void testUserGetAccountType(){
        User user1 = new User( "1","Admin", "tim_cook",  "123456");
        assertEquals("Testing User's getAccountType", "Admin",user1.getAccountType());
    }

    @Test
    public void testUserGetPassword(){
        User user1 = new User( "1","Admin", "tim_cook",  "123456");
        assertEquals("Testing User's getAccountPassword", "123456",user1.getPassword());
    }


}
