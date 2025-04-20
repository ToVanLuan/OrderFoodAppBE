package spring.api.order_food_app.service;

import org.springframework.web.multipart.MultipartFile;
import spring.api.order_food_app.entity.Food;

import java.io.IOException;
import java.util.List;

public interface FoodService {
    Food saveFood(String name,String type, String description, Long restaurantId, List<MultipartFile> imageFiles) throws IOException;
    Food getFoodById(Long id);
    List<Food> getAllFoods();
    void deleteFood(Long id);
    Food updateFood(Long id, String name,String type, String description, Long restaurantId, List<MultipartFile> imageFiles) throws IOException;
}
