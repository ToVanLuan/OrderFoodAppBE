package spring.api.order_food_app.service;

import org.springframework.web.multipart.MultipartFile;
import spring.api.order_food_app.entity.Food;
import spring.api.order_food_app.entity.Restaurant;

import java.io.IOException;
import java.util.List;

public interface RestaurantService {
    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurantById(Long id);
    Restaurant saveRestaurant(String name, String address, String phone, List<MultipartFile> imageFiles) throws IOException;
    Restaurant updateRestaurant(Long id, String name, String address, String phone, List<MultipartFile> imageFiles) throws IOException;
    void deleteRestaurant(Long id);
    List<Food> getFoodsByRestaurantId(Long restaurantId);
}
