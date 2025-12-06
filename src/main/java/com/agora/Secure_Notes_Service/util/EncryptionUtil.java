package com.agora.Secure_Notes_Service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {

    private static String SECRET;

    // Constructor to inject the secret from application properties
    public EncryptionUtil(@Value("${notes.secret}") String secret) {
        SECRET = secret;
    }

/**
 * The function `getKey()` returns a SecretKeySpec object initialized with
 * a secret key encoded in AES format.
 * 
 * @return A SecretKeySpec object is being returned.
 */
    private static SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET.getBytes(), "AES");
    }

/**
 * The `encrypt` function in Java uses AES encryption to encrypt the input
 * data and returns the encrypted data encoded in Base64.
 * 
 * @param data The `encrypt` method you provided is using AES encryption to
 * encrypt the input data. The `data` parameter represents the plaintext
 * data that you want to encrypt using the AES algorithm. This method will
 * encrypt the `data` using the AES encryption algorithm and return the
 * encrypted data as a Base64 encoded string
 * @return The encrypt method returns the input data encrypted using the
 * AES encryption algorithm and encoded in Base64 format.
 */
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting content", e);
        }
    }

/**
 * The `decrypt` function decrypts the given encrypted data using AES
 * encryption algorithm and returns the decrypted content as a string.
 * 
 * @param encryptedData Please provide the value of the `encryptedData`
 * parameter so that I can assist you with decrypting it using the
 * `decrypt` method.
 * @return The `decrypt` method returns the decrypted data as a String
 * after decoding and decrypting the input `encryptedData`.
 */
    public static String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting content", e);
        }
    }
}
