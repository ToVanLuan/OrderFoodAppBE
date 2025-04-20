package spring.api.order_food_app.service;

import spring.api.order_food_app.dto.CartItemDTO;
import spring.api.order_food_app.entity.CartItem;
import spring.api.order_food_app.entity.User;

import java.util.List;

public interface CartService {
    void addToCart(Long userId, CartItemDTO dto);
    List<CartItem> getUserCart(Long userId);
    void updateCartItem(Long userId, CartItemDTO dto);
    void deleteCartItem(Long userId, Long foodId, String size);
    void clearUserCart(Long userId);
     int countCartItems(Long userId);



}