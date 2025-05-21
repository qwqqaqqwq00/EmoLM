package hku.hk.EmoLM.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Long> codeExpiryTimes = new ConcurrentHashMap<>();
    private static final long CODE_EXPIRY_DURATION = 5 * 60 * 1000; // 5 minutes

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public EmailService() {
        // Clean expired verification codes periodically
        scheduler.scheduleAtFixedRate(this::removeExpiredCodes, 1, 1, TimeUnit.MINUTES);
    }

    public String sendVerificationCode(String toEmail) {
        String code = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("Email Verification Code");
        message.setText("Your verification code is: " + code + ", please use it within 5 minutes.");
        System.out.println(code);
        try {
            mailSender.send(message); // Send email
            verificationCodes.put(toEmail, code);
            codeExpiryTimes.put(toEmail, System.currentTimeMillis() + CODE_EXPIRY_DURATION);
            return code;
        } catch (Exception e) {
            // Add detailed logging
            System.err.println("Failed to send verification code: " + e.getMessage());
            throw new RuntimeException("Failed to send email. Please check SMTP configuration or network connection!");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate 6-digit random verification code
        return String.valueOf(code);
    }

    public boolean verifyCode(String email, String code) {
        String verifiedCode = verificationCodes.get(email);
        Long expiryTime = codeExpiryTimes.get(email);

        if (verifiedCode != null && expiryTime != null && System.currentTimeMillis() <= expiryTime) {
            boolean isValid = code.equals(verifiedCode);
            verificationCodes.remove(email);
            codeExpiryTimes.remove(email);
            return isValid;
        }
        return false;
    }

    private void removeExpiredCodes() {
        long currentTime = System.currentTimeMillis();
        codeExpiryTimes.entrySet().removeIf(entry -> currentTime > entry.getValue());
        verificationCodes.keySet().removeIf(email -> !codeExpiryTimes.containsKey(email));
    }
}