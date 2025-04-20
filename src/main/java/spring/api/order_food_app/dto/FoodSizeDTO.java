package spring.api.order_food_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodSizeDTO {
    private Long foodId;
    private String size;
    private double price;
}
