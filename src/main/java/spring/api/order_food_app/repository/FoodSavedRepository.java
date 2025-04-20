package spring.api.order_food_app.repository;

import spring.api.order_food_app.entity.FoodSaved;
import spring.api.order_food_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodSavedRepository extends JpaRepository<FoodSaved, Long> {
    List<FoodSaved> findByUser(User user);


}
