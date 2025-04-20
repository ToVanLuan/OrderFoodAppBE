package spring.api.order_food_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.api.order_food_app.dto.FoodSizeDTO;
import spring.api.order_food_app.entity.FoodSize;
import spring.api.order_food_app.entity.FoodSize.FoodSizeId;
import spring.api.order_food_app.service.FoodSizeService;
import spring.api.order_food_app.utils.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/food-sizes")
public class FoodSizeController {

    @Autowired
    private FoodSizeService foodSizeService;

    // ✅ Lấy tất cả food sizes (trả về DTO)
    @GetMapping
    public List<FoodSizeDTO> getAllFoodSizes() {
        List<FoodSize> foodSizes = foodSizeService.getAllFoodSizes();
        return foodSizes.stream()
                .map(DtoMapper::toFoodSizeDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lấy food size theo foodId và size (trả về DTO)
    @GetMapping("/{foodId}/{size}")
    public ResponseEntity<FoodSizeDTO> getFoodSizeById(@PathVariable Long foodId, @PathVariable String size) {
        FoodSizeId id = new FoodSizeId(foodId, size);
        FoodSize foodSize = foodSizeService.getFoodSizeById(id);
        if (foodSize == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DtoMapper.toFoodSizeDTO(foodSize));
    }


    // ✅ Thêm mới food size (trả về DTO)
    @PostMapping("/add")
    public ResponseEntity<FoodSizeDTO> createFoodSize(@RequestParam Long foodId,
                                                      @RequestParam String size,
                                                      @RequestParam double price) {
        FoodSize savedFoodSize = foodSizeService.saveFoodSize(foodId, size, price);
        return ResponseEntity.ok(DtoMapper.toFoodSizeDTO(savedFoodSize));
    }

    // ✅ Xóa food size
    @DeleteMapping("/{foodId}/{size}")
    public ResponseEntity<Void> deleteFoodSize(@PathVariable Long foodId, @PathVariable String size) {
        FoodSizeId id = new FoodSizeId(foodId, size);
        foodSizeService.deleteFoodSize(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{foodId}")
    public ResponseEntity<List<FoodSizeDTO>> getFoodSizesByFoodId(@PathVariable Long foodId) {
        List<FoodSize> sizes = foodSizeService.getFoodSizesByFoodId(foodId);
        return ResponseEntity.ok(DtoMapper.toFoodSizeDTOList(sizes));

}
}
