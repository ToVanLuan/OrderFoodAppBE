package spring.api.order_food_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.api.order_food_app.entity.FoodSize;

import java.util.List;

public interface FoodSizeRepository extends JpaRepository<FoodSize, FoodSize.FoodSizeId> {
    List<FoodSize> findByFood_Id(Long foodId);
}
