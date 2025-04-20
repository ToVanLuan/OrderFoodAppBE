package spring.api.order_food_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.api.order_food_app.dto.CartItemDTO;
import spring.api.order_food_app.entity.CartItem;
import spring.api.order_food_app.entity.FoodSize;
import spring.api.order_food_app.entity.User;
import spring.api.order_food_app.repository.CartItemRepository;
import spring.api.order_food_app.repository.FoodSizeRepository;
import spring.api.order_food_app.repository.UserRepository;
import spring.api.order_food_app.service.CartService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final FoodSizeRepository foodSizeRepository;
    private final UserRepository userRepository;

    @Override
    public void addToCart(Long userId, CartItemDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FoodSize.FoodSizeId id = new FoodSize.FoodSizeId(dto.getFoodId(), dto.getSize());
        FoodSize foodSize = foodSizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid food size"));

        List<CartItem> existingItems = cartItemRepository.findByUser(user);
        for (CartItem item : existingItems) {
            if (item.getFoodSize().getId().equals(id)) {
                item.setQuantity(item.getQuantity() + dto.getQuantity());
                cartItemRepository.save(item);
                return;
            }
        }

        CartItem newItem = new CartItem();
        newItem.setUser(user);
        newItem.setFoodSize(foodSize);
        newItem.setQuantity(dto.getQuantity());
        cartItemRepository.save(newItem);
    }

    @Override
    public List<CartItem> getUserCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartItemRepository.findByUser(user);
    }

    @Override
    public void updateCartItem(Long userId, CartItemDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FoodSize.FoodSizeId id = new FoodSize.FoodSizeId(dto.getFoodId(), dto.getSize());
        List<CartItem> items = cartItemRepository.findByUser(user);
        for (CartItem item : items) {
            if (item.getFoodSize().getId().equals(id)) {
                item.setQuantity(dto.getQuantity());
                cartItemRepository.save(item);
                return;
            }
        }

        throw new RuntimeException("Cart item not found");
    }
    @Override
    public void deleteCartItem(Long userId, Long foodId, String size) {
        cartItemRepository.deleteByUserIdAndFoodIdAndSize(userId, foodId, size);
    }
    @Override
    public int countCartItems(Long userId) {
        return cartItemRepository.countByUserId(userId);
    }


    @Override
    public void clearUserCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartItemRepository.deleteByUser(user);
    }
}
