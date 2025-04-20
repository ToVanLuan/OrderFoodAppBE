package spring.api.order_food_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.api.order_food_app.dto.RestaurantSavedDTO;
import spring.api.order_food_app.service.RestaurantSavedService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant-saved")
@RequiredArgsConstructor
public class RestaurantSavedController {

    private final RestaurantSavedService restaurantSavedService;

    @PostMapping("/save")
    public ResponseEntity<?> saveRestaurant(
            @RequestParam Long userId,
            @RequestParam Long restaurantId) {
        restaurantSavedService.saveRestaurant(userId, restaurantId);
        return ResponseEntity.ok("Nhà hàng đã được lưu");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RestaurantSavedDTO>> getSavedRestaurants(@PathVariable Long userId) {
        List<RestaurantSavedDTO> savedList = restaurantSavedService.getSavedRestaurantsByUser(userId);
        return ResponseEntity.ok(savedList);
    }

}
