package spring.api.order_food_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.api.order_food_app.entity.RestaurantSaved;

import java.util.List;

public interface RestaurantSavedRepository extends JpaRepository<RestaurantSaved, Long> {
    List<RestaurantSaved> findByUserId(Long userId);
    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
