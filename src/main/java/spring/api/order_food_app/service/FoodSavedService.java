package spring.api.order_food_app.service;

import spring.api.order_food_app.dto.FoodSavedDTO;
import spring.api.order_food_app.entity.FoodSaved;

import java.util.List;

public interface FoodSavedService {
    List<FoodSavedDTO> getSavedFoodsByUser(Long userId);
    void saveFood(Long userId, Long foodId, String size);
}
