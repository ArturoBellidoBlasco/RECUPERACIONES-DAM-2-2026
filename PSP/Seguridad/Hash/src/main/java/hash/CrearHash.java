package hash;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Arrays;

public class CrearHash {

    public static void main(String[] args) {
        try{

            String password = "mi_clave_secreta";

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashValue = digest.digest(password.getBytes());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


    }
}
