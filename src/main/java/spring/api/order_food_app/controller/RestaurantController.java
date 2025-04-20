package spring.api.order_food_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.api.order_food_app.dto.FoodDTO;
import spring.api.order_food_app.dto.RestaurantDTO;
import spring.api.order_food_app.entity.Restaurant;
import spring.api.order_food_app.service.RestaurantService;
import spring.api.order_food_app.utils.DtoMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    // ✅ Lấy tất cả nhà hàng (dùng DTO)
    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return restaurants.stream()
                .map(DtoMapper::toRestaurantDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lấy thông tin nhà hàng theo ID (dùng DTO)
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DtoMapper.toRestaurantDTO(restaurant));
    }

    // ✅ Thêm mới nhà hàng (trả về DTO)
    @PostMapping("/add")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestParam String name,
                                                          @RequestParam String address,
                                                          @RequestParam String phone,
                                                          @RequestParam List<MultipartFile> imageFiles) throws IOException {
        Restaurant createdRestaurant = restaurantService.saveRestaurant(name, address, phone, imageFiles);
        return ResponseEntity.ok(DtoMapper.toRestaurantDTO(createdRestaurant));
    }

    // ✅ Cập nhật nhà hàng (trả về DTO)
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id,
                                                          @RequestParam String name,
                                                          @RequestParam String address,
                                                          @RequestParam String phone,
                                                          @RequestParam List<MultipartFile> imageFiles) throws IOException {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, name, address, phone, imageFiles);
        if (updatedRestaurant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(DtoMapper.toRestaurantDTO(updatedRestaurant));
    }

    // ✅ Xóa nhà hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy danh sách món ăn theo nhà hàng ID (dùng DTO)
    @GetMapping("/{id}/foods")
    public ResponseEntity<List<FoodDTO>> getFoodsByRestaurant(@PathVariable Long id) {
        List<FoodDTO> foodDTOs = restaurantService.getFoodsByRestaurantId(id)
                .stream()
                .map(DtoMapper::toFoodDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodDTOs);
    }
}
