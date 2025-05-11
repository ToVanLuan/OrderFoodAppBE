package spring.api.order_food_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.api.order_food_app.dto.FoodDTO;
import spring.api.order_food_app.entity.Food;
import spring.api.order_food_app.service.FoodService;
import spring.api.order_food_app.utils.DtoMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    // ✅ Thêm món ăn mới
    @PostMapping("/add")
    public ResponseEntity<FoodDTO> addFood(@RequestParam String name,
                                           @RequestParam String type,
                                           @RequestParam String description,
                                           @RequestParam Long restaurantId,
                                           @RequestParam List<MultipartFile> imageFiles) throws IOException {
        Food savedFood = foodService.saveFood(name, type, description, restaurantId, imageFiles);
        return ResponseEntity.ok(DtoMapper.toFoodDTO(savedFood));
    }

    // ✅ Lấy món ăn theo ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> getFood(@PathVariable Long id) {
        Food food = foodService.getFoodById(id);
        return ResponseEntity.ok(DtoMapper.toFoodDTO(food));
    }

    // ✅ Cập nhật món ăn
    @PutMapping("/{id}")
    public ResponseEntity<FoodDTO> updateFood(@PathVariable Long id,
                                              @RequestParam("name") String name,
                                              @RequestParam("type") String type,
                                              @RequestParam("description") String description,
                                              @RequestParam("restaurantId") Long restaurantId,
                                              @RequestParam("imageFiles") List<MultipartFile> imageFiles) throws IOException {
        Food updatedFood = foodService.updateFood(id, name, type, description, restaurantId, imageFiles);
        if (updatedFood == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DtoMapper.toFoodDTO(updatedFood));
    }

    // ✅ Lấy tất cả món ăn
    @GetMapping
    public ResponseEntity<List<FoodDTO>> getAllFoods() {
        List<Food> foods = foodService.getAllFoods();
        List<FoodDTO> foodDTOs = foods.stream()
                .map(DtoMapper::toFoodDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodDTOs);
    }
    // ✅ Xóa món ăn
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
    // 🔍 Tìm món ăn theo tên
    @GetMapping("/search")
    public ResponseEntity<List<FoodDTO>> searchFoods(@RequestParam String keyword) {
        List<Food> results = foodService.searchFoodsByName(keyword);
        List<FoodDTO> dtoResults = results.stream()
                .map(DtoMapper::toFoodDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoResults);
    }

}
