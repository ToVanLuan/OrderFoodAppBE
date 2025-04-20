package spring.api.order_food_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food_size")
public class FoodSize {

    @EmbeddedId
    private FoodSizeId id; // Sử dụng EmbeddedId để kết hợp foodId và size

    private double price;  // Giá của món ăn cho từng kích thước

    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Food food;  // Liên kết với món ăn

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodSizeId implements Serializable {

        @Column(name = "food_id")
        private Long foodId;  // ID món ăn

        private String size;
    }
}
