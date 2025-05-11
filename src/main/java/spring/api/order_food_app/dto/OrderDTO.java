package spring.api.order_food_app.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private double totalValue;
    private List<OrderItemDTO> items;
    private String paymentMethod;
    private String status;
    private LocalDateTime dateOfOrder;

}
