package spring.api.order_food_app.service;

import spring.api.order_food_app.entity.FoodSize;
import spring.api.order_food_app.entity.FoodSize.FoodSizeId;

import java.util.List;

public interface FoodSizeService {
    FoodSize saveFoodSize(Long foodId, String size, double price);

    FoodSize getFoodSizeById(FoodSizeId id);

    List<FoodSize> getAllFoodSizes();

    void deleteFoodSize(FoodSizeId id);
    List<FoodSize> getFoodSizesByFoodId(Long foodId);

}
