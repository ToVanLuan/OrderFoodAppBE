package spring.api.order_food_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long foodId;
    private String name;
    private String discountCode; // Mã giảm giá (có thể null)
    private String urlImg;
    private String size;
    private int quantity;
    private double price;
}
