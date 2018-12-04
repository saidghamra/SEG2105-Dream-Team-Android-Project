package dreamteam.com.homerepair;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static dreamteam.com.homerepair.Sha1.hash;
import static org.junit.Assert.assertEquals;

public class Sha1Test {
    @Test
    public void testHash() {
        String plainCourseCode = "SEG2105";
        String encryptedCourseCode = "8C30BB0FE154084F3238E101A21202E1B0580977";
        String calculatedEncryptedHash = null;
        try {
            calculatedEncryptedHash =  Sha1.hash(plainCourseCode);

        }
        catch (UnsupportedEncodingException e){

        }
        finally {
            assertEquals("Testing hashing algorithm", encryptedCourseCode, calculatedEncryptedHash);
        }

    }
}
