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
@Table(name = "order_details")
public class OrderDetails {
    @EmbeddedId
    private OrderDetailId id;

    private double price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id", insertable = false, updatable = false)
    private Food food;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailId implements Serializable {
        @Column(name = "order_id")  // Ánh xạ tên cột cho orderId
        private Long orderId;

        @Column(name = "food_id")  // Ánh xạ tên cột cho foodId
        private Long foodId;

        private int size;
    }
}
