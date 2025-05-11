package spring.api.order_food_app.service;

import spring.api.order_food_app.dto.OrderDTO;
import spring.api.order_food_app.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO, Long userId);
    OrderDTO getOrderDetails(Long orderId);
    List<Order> getOrdersByUser(Long userId);
    void deleteOrder(Long orderId);
    void deleteAllOrdersByUser(Long userId);


}
