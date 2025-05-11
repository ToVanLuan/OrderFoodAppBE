package spring.api.order_food_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.api.order_food_app.dto.OrderDTO;
import spring.api.order_food_app.dto.OrderItemDTO;
import spring.api.order_food_app.entity.*;
import spring.api.order_food_app.repository.*;
import spring.api.order_food_app.service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DiscountRepository discountRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        // Tạo mới đối tượng Order
        Order order = new Order();
        order.setUser(userOptional.get());
        order.setName(orderDTO.getName());
        order.setAddress(orderDTO.getAddress());
        order.setPhoneNumber(orderDTO.getPhoneNumber());
        order.setDateOfOrder(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentMethod(orderDTO.getPaymentMethod());

        // Không cần set discountCode cho Order nữa (mỗi item có riêng)
        order.setTotalValue(orderDTO.getTotalValue());
        System.out.println("Received totalValue from FE: " + orderDTO.getTotalValue());

        // Lưu đơn hàng tạm thời
        order = orderRepository.save(order);

        // Lưu chi tiết đơn hàng
        saveOrderDetails(orderDTO, order);

        orderRepository.save(order);
        return order;
    }

    private void saveOrderDetails(OrderDTO orderDTO, Order order) {
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Optional<Food> foodOptional = foodRepository.findById(itemDTO.getFoodId());
            if (foodOptional.isPresent()) {
                Food food = foodOptional.get();
                FoodSize foodSize = food.getFoodSizes().stream()
                        .filter(size -> size.getId().getSize().equals(itemDTO.getSize()))
                        .findFirst()
                        .orElse(null);

                if (foodSize != null) {
                    OrderDetails orderDetail = new OrderDetails();
                    OrderDetails.OrderDetailId id = new OrderDetails.OrderDetailId();
                    id.setOrderId(order.getId());
                    id.setFoodId(food.getId());
                    id.setSize(foodSize.getId().getSize());

                    orderDetail.setId(id);
                    orderDetail.setPrice(itemDTO.getPrice());
                    orderDetail.setQuantity(itemDTO.getQuantity());
                    orderDetail.setFoodSize(foodSize);
                    orderDetail.setOrder(order);

                    // Gán discountCode cho từng chi tiết đơn hàng
                    orderDetail.setDiscountCode(itemDTO.getDiscountCode());

                    order.getOrderDetails().add(orderDetail);
                    orderDetailRepository.save(orderDetail);
                }
            }
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public void deleteAllOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        orderRepository.deleteAll(orders);
    }

    @Override
    public OrderDTO getOrderDetails(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }

        Order order = orderOptional.get();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus(order.getStatus().toString());
        orderDTO.setPaymentMethod(order.getPaymentMethod());

        List<OrderItemDTO> items = new ArrayList<>();
        for (OrderDetails orderDetail : order.getOrderDetails()) {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setFoodId(orderDetail.getFoodSize().getFood().getId());
            itemDTO.setSize(orderDetail.getFoodSize().getId().getSize());
            itemDTO.setQuantity(orderDetail.getQuantity());
            itemDTO.setPrice(orderDetail.getPrice());

            // Trả lại discountCode cho từng item
            itemDTO.setDiscountCode(orderDetail.getDiscountCode());

            items.add(itemDTO);
        }

        orderDTO.setItems(items);
        return orderDTO;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
