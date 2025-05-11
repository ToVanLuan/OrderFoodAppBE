package spring.api.order_food_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.api.order_food_app.entity.Food;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findById(Long id);
    List<Food> findByNameContainingIgnoreCase(String keyword);


}
