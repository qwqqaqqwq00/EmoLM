package hku.hk.EmoLM.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    public String sendVerificationCode(String toEmail) {
        String code = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("邮箱验证码");
        message.setText("您的验证码是：" + code + "，请在5分钟内使用。");
        mailSender.send(message);
        return code;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位随机验证码
        return String.valueOf(code);
    }

    public boolean verifyCode(String email, String code) {
        return code.equals(verificationCodes.get(email)); // 验证验证码
    }
}