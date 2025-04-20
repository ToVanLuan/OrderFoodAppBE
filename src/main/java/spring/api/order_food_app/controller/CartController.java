package spring.api.order_food_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.api.order_food_app.dto.CartItemDTO;
import spring.api.order_food_app.entity.CartItem;
import spring.api.order_food_app.service.CartService;
import spring.api.order_food_app.utils.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addToCart(@PathVariable Long userId, @RequestBody CartItemDTO dto) {
        cartService.addToCart(userId, dto);
        return ResponseEntity.ok("Đã thêm vào giỏ hàng thành công");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDTO>> getUserCart(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.getUserCart(userId);
        List<CartItemDTO> cartItemDTOs = cartItems.stream()
                .map(DtoMapper::toCartItemDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cartItemDTOs);
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<String> updateCartItem(@PathVariable Long userId, @RequestBody CartItemDTO dto) {
        cartService.updateCartItem(userId, dto);
        return ResponseEntity.ok("Cart item updated successfully");
    }


    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearUserCart(@PathVariable Long userId) {
        cartService.clearUserCart(userId);
        return ResponseEntity.ok("User cart cleared successfully");
    }
    @DeleteMapping("/{userId}/item")
    public ResponseEntity<String> deleteCartItem(
            @PathVariable Long userId,
            @RequestParam Long foodId,
            @RequestParam String size) {
        cartService.deleteCartItem(userId, foodId, size);
        return ResponseEntity.ok("Xóa món ăn khỏi giỏ hàng thành công");
    }
    @GetMapping("/{userId}/count")
    public ResponseEntity<Integer> countCartItems(@PathVariable Long userId) {
        int itemCount = cartService.countCartItems(userId);
        return ResponseEntity.ok(itemCount);
    }

}
