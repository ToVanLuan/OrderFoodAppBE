package spring.api.order_food_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.api.order_food_app.dto.UserDTO;
import spring.api.order_food_app.service.IUserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO, @RequestParam String password) {
        try {
            // Đăng ký người dùng
            UserDTO registeredUser = userService.registerUser(userDTO, password);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            // Trả về lỗi nếu có vấn đề
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String input, @RequestParam String password) {
        if (input == null || password == null || input.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email/SĐT và mật khẩu không được để trống"));
        }
        try {
            String userId = userService.loginUser(input, password);
            return ResponseEntity.ok(Map.of("userId", userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
    // Lấy thông tin người dùng
    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            UserDTO userDTO = userService.getUserById(userId);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // Đăng xuất
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token không hợp lệ"));
        }
        userService.logoutUser(token);
        return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công"));
    }

    // Lấy danh sách tất cả user
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Cập nhật thông tin người dùng
    @PutMapping("/{userId}/update")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUser(userId, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // Quên mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok(Map.of("message", "Mật khẩu mới đã được gửi đến email của bạn."));
    }

    // Đổi mật khẩu
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (email == null || oldPassword == null || newPassword == null || email.isBlank() || oldPassword.isBlank() || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Tất cả các trường đều bắt buộc"));
        }
        try {
            userService.changePassword(email, oldPassword, newPassword);
            return ResponseEntity.ok(Map.of("message", "Mật khẩu đã được thay đổi thành công!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login-gg")
    public ResponseEntity<Map<String, String>> loginWithGoogle(@RequestBody UserDTO userDTO) {
        try {
            // Đăng nhập hoặc đăng ký người dùng qua Google
            String userId = userService.loginUserWithGoogle(userDTO);

            // Trả về userId (có thể là token hoặc id) dưới dạng Map
            return ResponseEntity.ok(Map.of("userId", userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}
