package spring.api.order_food_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.api.order_food_app.dto.UserDTO;
import spring.api.order_food_app.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}




