package spring.api.order_food_app.utils;

import spring.api.order_food_app.dto.*;
import spring.api.order_food_app.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static FoodDTO toFoodDTO(Food food) {
        FoodDTO dto = new FoodDTO(
                food.getId(),
                food.getName(),
                food.getType(),
                food.getImageUrls(),
                0.0,
                null,
                food.getDescription()
        );

        // Kiểm tra xem có giảm giá không
        if (food.getDiscounts() != null) {
            food.getDiscounts().stream()
                    .filter(d -> d.isActive() && d.getEndTime().isAfter(LocalDateTime.now()))
                    .findFirst()
                    .ifPresent(discount -> {
                        dto.setDiscountPercent(discount.getDiscountPercent());
                        dto.setDiscountCode(discount.getCode());
                    });
        }

        return dto;
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

        // Kiểm tra xem có giảm giá không
        if (food.getDiscounts() != null) {
            food.getDiscounts().stream()
                    .filter(d -> d.isActive() && d.getEndTime().isAfter(LocalDateTime.now()))
                    .findFirst()
                    .ifPresent(discount -> {
                        dto.setDiscountPercent(discount.getDiscountPercent());
                        dto.setDiscountCode(discount.getCode());
                    });
        }
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

    public static DiscountDTO toDiscountDTO(Discount discount) {
        return new DiscountDTO(
                discount.getCode(),
                discount.getDiscountPercent(),
                discount.getStartTime(),
                discount.getEndTime(),
                discount.getFood().getId()
        );
    }


    public static OrderDTO toOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDateOfOrder(order.getDateOfOrder());
        orderDTO.setId(order.getId());
        orderDTO.setTotalValue(order.getTotalValue());
        orderDTO.setName(order.getName());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setPhoneNumber(order.getPhoneNumber());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setStatus(order.getStatus().name());  // Sử dụng Enum để lấy giá trị status
        List<OrderItemDTO> items = order.getOrderDetails().stream()
                .map(orderDetail -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setFoodId(orderDetail.getFoodSize().getFood().getId());
                    itemDTO.setName(orderDetail.getFoodSize().getFood().getName());
                    itemDTO.setUrlImg(orderDetail.getFoodSize().getFood().getImageUrls().get(0));
                    itemDTO.setSize(orderDetail.getFoodSize().getId().getSize());
                    itemDTO.setQuantity(orderDetail.getQuantity());
                    itemDTO.setDiscountCode(orderDetail.getDiscountCode());
                    itemDTO.setPrice(orderDetail.getPrice());

                    return itemDTO;
                })
                .collect(Collectors.toList());

        orderDTO.setItems(items);

        return orderDTO;
    }
}