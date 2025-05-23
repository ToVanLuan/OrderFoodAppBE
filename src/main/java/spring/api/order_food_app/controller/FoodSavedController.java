package spring.api.order_food_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.api.order_food_app.dto.FoodSavedDTO;
import spring.api.order_food_app.service.FoodSavedService;

import java.util.List;

@RestController
@RequestMapping("/api/food-saved")
@RequiredArgsConstructor
public class FoodSavedController {

    private final FoodSavedService foodSavedService;

    @PostMapping("/save")
    public ResponseEntity<String> saveFood(
            @RequestParam Long userId,
            @RequestParam Long foodId,
            @RequestParam String size
    ) {
        foodSavedService.saveFood(userId, foodId, size);
        return ResponseEntity.ok("Món ăn đã được lưu!");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FoodSavedDTO>> getSavedFoods(@PathVariable Long userId) {
        List<FoodSavedDTO> savedList = foodSavedService.getSavedFoodsByUser(userId);
        return ResponseEntity.ok(savedList);
    }
}

