package hku.hk.EmoLM.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class PasswordService {

    private static final String SALT = "^09n!H~0,)"; // Fixed salt value

    // Encrypt password with fixed salt value
    public String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String saltedPassword = password + SALT;
            byte[] hash = md.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}