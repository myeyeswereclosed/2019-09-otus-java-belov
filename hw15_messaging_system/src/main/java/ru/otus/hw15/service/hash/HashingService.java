package ru.otus.hw15.service.hash;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;

@Service
public class HashingService implements HashService {
    private static final Logger logger = LoggerFactory.getLogger(HashingService.class);
    private static final String SALT = "@GI%e<,wy]D&WHX(K^zM";

    @Override
    public Optional<String> hash(String value) {
        try {
            return
                Optional.of(
                    Base64.getEncoder().encodeToString(
                        SecretKeyFactory
                            .getInstance("PBKDF2WithHmacSHA1")
                                .generateSecret(new PBEKeySpec(value.toCharArray(), SALT.getBytes(), 65536, 128))
                                    .getEncoded()
                    )
                );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Error occured while hashing. Trace:\r\n" + ExceptionUtils.getStackTrace(e));
        }

        return Optional.empty();
    }
}
