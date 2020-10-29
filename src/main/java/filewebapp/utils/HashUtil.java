package filewebapp.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public class HashUtil {
    public byte[] generateHash(String password) {
        byte[] hash = null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[1], 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hash == null)
            return new byte[0];
        else
            return hash;
    }
}
