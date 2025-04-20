package spring.api.order_food_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodSavedDTO {
    private Long id;
    private String foodName;
    private List<String> imageUrls;
    private String size;
    private double foodPrice;
}
