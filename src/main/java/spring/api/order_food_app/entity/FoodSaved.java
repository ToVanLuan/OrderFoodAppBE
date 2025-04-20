package spring.api.order_food_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food_saved")
public class FoodSaved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "food_id", referencedColumnName = "food_id"),
            @JoinColumn(name = "size", referencedColumnName = "size")
    })
    private FoodSize foodSize;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
