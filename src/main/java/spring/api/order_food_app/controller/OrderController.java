package spring.api.order_food_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.api.order_food_app.dto.OrderDTO;
import spring.api.order_food_app.entity.Order;
import spring.api.order_food_app.service.OrderService;
import spring.api.order_food_app.utils.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId, @RequestBody OrderDTO orderDTO) {
        try {

            // Tạo đơn hàng từ DTO
            Order order = orderService.createOrder(orderDTO,userId);

            // Chuyển đổi đối tượng Order thành OrderDTO
            OrderDTO orderDTOResponse = DtoMapper.toOrderDTO(order);  // Chuyển từ Order sang OrderDTO

            return ResponseEntity.ok(orderDTOResponse);  // Trả về response với OrderDTO
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi ra console
            return ResponseEntity.status(400).body(null); // Trả về lỗi nếu có vấn đề khi tạo đơn hàng
        }
    }
    // Phương thức lấy chi tiết đơn hàng
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) {
        try {
            OrderDTO orderDTO = orderService.getOrderDetails(orderId);
            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null); // Trả về lỗi nếu có vấn đề khi lấy chi tiết đơn hàng
        }
    }
    // Phương thức lay tất cả đơn hàng của user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);

            // Chuyển list Order -> list OrderDTO
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(DtoMapper::toOrderDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }
    // Xóa một đơn hàng theo ID
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Đơn hàng đã được xóa thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Không thể xóa đơn hàng.");
        }
    }

    // Xóa tất cả đơn hàng của một user
    @DeleteMapping("/user/{userId}/delete-all")
    public ResponseEntity<String> deleteAllOrdersByUser(@PathVariable Long userId) {
        try {
            orderService.deleteAllOrdersByUser(userId);
            return ResponseEntity.ok("Tất cả đơn hàng của người dùng đã được xóa.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Không thể xóa đơn hàng của người dùng.");
        }
    }

}
