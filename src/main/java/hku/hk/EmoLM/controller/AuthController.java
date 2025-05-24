package hku.hk.EmoLM.controller;

import hku.hk.EmoLM.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hku.hk.EmoLM.service.UserService;
import hku.hk.EmoLM.service.EmailService;

import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestParam String username, @RequestParam String password) {
        String encryptedPassword = passwordService.encryptPassword(password);
        if (userService.authenticateUser(username, encryptedPassword)) {
            // JWT token
            String token = userService.JWTToken(username, encryptedPassword);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Login successful", "token", token));
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Invalid username or password, please try again!"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> handleRegister(@RequestParam String username, @RequestParam String password,
                                            @RequestParam String confirmPassword,
                                            @RequestParam String email, @RequestParam String verifyCode) {
        if (password.equals(confirmPassword)) {
            boolean isValid = emailService.verifyCode(email, verifyCode);
            if (!isValid) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Invalid verification code or expired!"));
            }
            if (userService.registerUser(username, passwordService.encryptPassword(password), email)) {
                return ResponseEntity.ok().body(Map.of("success", true, "message", "Registration successful, please login!"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Password mismatch or registration failed, please try again!"));
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<?> isAuthenticated(@RequestHeader("Authorization") String token) {
        if (token != null && !token.isEmpty()) {
            Object uid = userService.UserToken2Uid(token);
            if(uid != null){
                return ResponseEntity.ok().body(Map.of("success", true, "message", "Logged in"));
            }
            return ResponseEntity.ok().body(Map.of("success", false, "message", "Not logged in"));
        } else {
            return ResponseEntity.ok().body(Map.of("success", false, "message", "Not logged in"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> handleLogout() {
        // Implement logout logic (add cleanup session operations here)
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Logged out successfully"));
    }

    @PostMapping("/sendVerifyCode")
    public ResponseEntity<?> sendVerifyCode(@RequestParam String email) {
        String emailRegex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (!email.matches(emailRegex)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Invalid email format, please try again!"));
        }
        emailService.sendVerificationCode(email);
        // Store verification code in cache or database for later verification
        return ResponseEntity.ok().body(Map.of("success", true, "message", "Verification code sent!"));
    }
}