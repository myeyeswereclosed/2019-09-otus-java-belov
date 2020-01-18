package ru.otus.hw12.services.hash;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class HashService {
    private static final String SALT = "@GI%e<,wy]D&WHX(K^zM";

    public static String hash(String value) {
        try {
            return
                new String(
                    SecretKeyFactory
                        .getInstance("PBKDF2WithHmacSHA1")
                            .generateSecret(new PBEKeySpec(value.toCharArray(), SALT.getBytes(), 65536, 128))
                                .getEncoded()
                );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new HashServiceException("Error occured while hashing");
        }
    }
}
