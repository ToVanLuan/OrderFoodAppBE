package spring.api.order_food_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long foodId;
    private String foodName;
    private String imageUrl;
    private String size;
    private int quantity;
    private double foodPrice;
}
