package spring.api.order_food_app.utils;

import spring.api.order_food_app.dto.*;
import spring.api.order_food_app.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static FoodDTO toFoodDTO(Food food) {
        return new FoodDTO(
                food.getId(),
                food.getName(),
                food.getType(),
                food.getImageUrls(),
                food.getDescription()
        );
    }

    public static RestaurantDTO toRestaurantDTO(Restaurant restaurant) {
        List<FoodDTO> foodDTOs = restaurant.getFoods() != null ?
                restaurant.getFoods().stream()
                        .map(DtoMapper::toFoodDTO)
                        .collect(Collectors.toList()) : null;

        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getPhone(),
                restaurant.getImageUrls(),
                foodDTOs
        );
    }
    public static FoodSizeDTO toFoodSizeDTO(FoodSize foodSize) {
        if (foodSize == null) return null;
        FoodSizeDTO dto = new FoodSizeDTO();
        dto.setFoodId(foodSize.getId().getFoodId());
        dto.setSize(foodSize.getId().getSize());
        dto.setPrice(foodSize.getPrice());
        return dto;
    }
    public static List<FoodSizeDTO> toFoodSizeDTOList(List<FoodSize> foodSizes) {
        return foodSizes.stream()
                .map(DtoMapper::toFoodSizeDTO)
                .collect(Collectors.toList());
    }
    public static CartItemDTO toCartItemDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();

        FoodSize foodSize = item.getFoodSize();
        Food food = foodSize.getFood();

        dto.setFoodId(food.getId());
        dto.setFoodName(food.getName());

        // ✅ Lấy ảnh đầu tiên từ danh sách imageUrls
        if (food.getImageUrls() != null && !food.getImageUrls().isEmpty()) {
            dto.setImageUrl(food.getImageUrls().get(0));
        } else {
            dto.setImageUrl(""); // hoặc set ảnh mặc định
        }

        dto.setSize(foodSize.getId().getSize());
        dto.setQuantity(item.getQuantity());
        dto.setFoodPrice(foodSize.getPrice());
        return dto;
    }
    public static RestaurantSavedDTO toRestaurantSaveDTO(RestaurantSaved saved) {
        if (saved == null || saved.getRestaurant() == null) return null;

        Restaurant r = saved.getRestaurant();

        return new RestaurantSavedDTO(
                r.getId(),
                r.getName(),
                r.getAddress(),
                r.getPhone(),
                r.getImageUrls()
        );
    }
    public static FoodSavedDTO toFoodSavedDTO(FoodSaved saved) {
        if (saved == null || saved.getFoodSize() == null || saved.getFoodSize().getFood() == null) return null;

        Food food = saved.getFoodSize().getFood();

        FoodSavedDTO dto = new FoodSavedDTO();
        dto.setId(saved.getId());
        dto.setFoodName(food.getName());
        dto.setImageUrls(food.getImageUrls()); // Có thể null-safe nếu cần
        dto.setSize(saved.getFoodSize().getId().getSize());
        dto.setFoodPrice(saved.getFoodSize().getPrice());
        dto.setFoodId(saved.getFoodSize().getFood().getId());
        return dto;
    }
}
