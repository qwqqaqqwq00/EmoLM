package hku.hk.EmoLM.controller;

import hku.hk.EmoLM.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hku.hk.EmoLM.service.UserService;
import hku.hk.EmoLM.service.EmailService;

import java.util.Map;

@RestController // 使用 @RestController 代替 @Controller，自动将返回值转换为 JSON
@RequestMapping("/api") // 为所有接口添加统一的前缀
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@RequestParam String username, @RequestParam String password) {
        if (userService.authenticateUser(username, passwordService.encryptPassword(password))) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "登录成功"));
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "error", "用户名或密码错误，请重试！"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> handleRegister(@RequestParam String username, @RequestParam String password,
                                            @RequestParam String confirmPassword,
                                            @RequestParam String email, @RequestParam String verifyCode) {
        if (password.equals(confirmPassword)) {
            boolean isValid = emailService.verifyCode(email, verifyCode);
            if (!isValid) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "验证码错误或已过期！"));
            }
            if (userService.registerUser(username, passwordService.encryptPassword(password), email)) {
                return ResponseEntity.ok().body(Map.of("success", true, "message", "注册成功，请登录！"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "error", "密码不匹配或注册失败，请重试！"));
    }

    @PostMapping("/isAuthenticated")
    public ResponseEntity<?> isAuthenticated(@RequestParam String token) {
        if (token != null && !token.isEmpty()) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "已登录"));
        } else {
            return ResponseEntity.ok().body(Map.of("success", false, "message", "未登录"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> handleLogout() {
        // 实现登出逻辑（这里可以添加清理会话等操作）
        return ResponseEntity.ok().body(Map.of("success", true, "message", "已成功退出登录"));
    }

    @PostMapping("/sendVerifyCode")
    public ResponseEntity<?> sendVerifyCode(@RequestParam String email) {
        String emailRegex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (!email.matches(emailRegex)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "邮箱格式不正确，请重试！"));
        }
        String code = emailService.sendVerificationCode(email);
        // 这里可以将验证码存储到缓存或数据库中以供后续验证
        return ResponseEntity.ok().body(Map.of("success", true, "message", "验证码已发送！"));
    }
}