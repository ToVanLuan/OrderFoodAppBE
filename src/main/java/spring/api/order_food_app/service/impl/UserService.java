package spring.api.order_food_app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spring.api.order_food_app.dto.UserDTO;
import spring.api.order_food_app.entity.User;
import spring.api.order_food_app.repository.UserRepository;
import spring.api.order_food_app.service.IUserService;
import spring.api.order_food_app.utils.EmailService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDTO registerUser(UserDTO userDTO, String password) {
        // ✨ Chuẩn hóa dữ liệu: nếu chuỗi rỗng -> gán null
        String email = (userDTO.getEmail() != null && !userDTO.getEmail().trim().isEmpty()) ? userDTO.getEmail().trim() : null;
        String phone = (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().trim().isEmpty()) ? userDTO.getPhoneNumber().trim() : null;

        userDTO.setEmail(email);
        userDTO.setPhoneNumber(phone);

        // Kiểm tra nếu cả email và phoneNumber đều rỗng
        if (email == null && phone == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phải cung cấp ít nhất email hoặc số điện thoại!");
        }

        // Kiểm tra nếu email đã tồn tại
        if (email != null) {
            if (userRepository.existsByEmail(email)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại!");
            }
        }

        // Kiểm tra nếu số điện thoại đã tồn tại
        if (phone != null) {
            if (userRepository.existsByPhoneNumber(phone)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại!");
            }
        }

        // Tạo và lưu người dùng
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(phone);

        if (!user.isValidRegistration()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phải cung cấp ít nhất email hoặc số điện thoại!");
        }

        User savedUser = userRepository.save(user);

        // Gửi email nếu có
        if (email != null) {
            sendEmail(email, "Chào mừng đến với Order Food App!",
                    "<h3>Xin chào " + userDTO.getUsername() + ",</h3>" +
                            "<p>Cảm ơn bạn đã đăng ký tài khoản tại Order Food App.</p>" +
                            "<p>Chúc bạn có những trải nghiệm tuyệt vời!</p>");
        }

        return mapToDTO(savedUser);
    }

    @Override
    public String loginUser(String input, String password) {
        User user = userRepository.findByEmail(input)
                .or(() -> userRepository.findByPhoneNumber(input))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email/SĐT hoặc mật khẩu không đúng!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email/SĐT hoặc mật khẩu không đúng!");
        }

        return String.valueOf(user.getId());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Không tìm thấy người dùng!"));
    }

    @Override
    public void logoutUser(String token) {
        // Implement nếu cần xử lý token
    }
    @Override
    // Phương thức đăng nhập qua Google
    public String loginUserWithGoogle(UserDTO userDTO) {
        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail().trim());

        // Nếu người dùng đã tồn tại, trả về userId
        if (existingUser.isPresent()) {
            return String.valueOf(existingUser.get().getId()); // Trả về userId
        }

        // Nếu người dùng chưa tồn tại, thực hiện đăng ký mới
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword("");  // Không cần mật khẩu vì đăng nhập qua Google
        newUser.setAddress("");   // Có thể không cần thiết khi đăng nhập qua Google
        newUser.setPhoneNumber(""); // Có thể không cần thiết khi đăng nhập qua Google

        // Lưu người dùng mới vào database
        User savedUser = userRepository.save(newUser);

        // Trả về userId của người dùng mới tạo
        return String.valueOf(savedUser.getId());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id,UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Không tìm thấy người dùng!"));

        user.setUsername(userDTO.getUsername());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Email không tồn tại!"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(BAD_REQUEST, "Mật khẩu cũ không đúng!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        sendEmail(email, "Xác nhận đổi mật khẩu thành công",
                "<h3>Xin chào " + user.getUsername() + ",</h3>" +
                        "<p>Bạn vừa đổi mật khẩu thành công.</p>" +
                        "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng liên hệ hỗ trợ ngay!</p>");
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Email không tồn tại!"));

        String tempPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        sendEmail(email, "Đặt lại mật khẩu thành công",
                "<h3>Xin chào " + user.getUsername() + ",</h3>" +
                        "<p>Bạn vừa yêu cầu đặt lại mật khẩu.</p>" +
                        "<p>Mật khẩu mới của bạn là: <b>" + tempPassword + "</b></p>" +
                        "<p>Vui lòng đăng nhập và thay đổi mật khẩu ngay để bảo mật tài khoản của bạn.</p>");
    }

    // Chuyển entity -> DTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getAddress());
    }

    // Gửi email
    private void sendEmail(String to, String subject, String message) {
        emailService.sendEmail(to, subject, message);
    }

    // Tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
