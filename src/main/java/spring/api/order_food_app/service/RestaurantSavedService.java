package spring.api.order_food_app.service;

import spring.api.order_food_app.dto.RestaurantSavedDTO;
import spring.api.order_food_app.entity.RestaurantSaved;

import java.util.List;

public interface RestaurantSavedService {
    void saveRestaurant(Long userId, Long restaurantId);
    List<RestaurantSavedDTO> getSavedRestaurantsByUser(Long userId);
}
