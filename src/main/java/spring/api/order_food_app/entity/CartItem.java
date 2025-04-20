package spring.api.order_food_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "food_id", referencedColumnName = "food_id"),
            @JoinColumn(name = "size", referencedColumnName = "size")
    })
    private FoodSize foodSize;

    @Column(nullable = false)
    private int quantity;

    public double getTotalPrice() {
        return foodSize != null ? foodSize.getPrice() * quantity : 0.0;
    }
}
