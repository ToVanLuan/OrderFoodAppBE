package spring.api.order_food_app.service;

import spring.api.order_food_app.dto.UserDTO;

import java.util.List;

public interface IUserService {
    UserDTO registerUser(UserDTO userDTO, String password) throws Exception;
    String loginUser(String email, String password);
    void logoutUser(String token);
    UserDTO updateUser(Long id,UserDTO userDTO);
    void forgotPassword(String email);
    List<UserDTO> getAllUsers();
    void changePassword(String email, String oldPassword, String newPassword);
    UserDTO getUserById(Long userId);
    String loginUserWithGoogle(UserDTO userDTO);
}
