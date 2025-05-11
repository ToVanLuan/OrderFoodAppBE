package spring.api.order_food_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDTO {
    private Long id;
    private String name;
    private String type;
    private List<String> imageUrls;
    private double discountPercent;
    private String discountCode;
    private String description;

}
