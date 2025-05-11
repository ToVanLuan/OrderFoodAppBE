package spring.api.order_food_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "order_details")
public class OrderDetails {

    @EmbeddedId
    private OrderDetailId id;

    private double price;
    private int quantity;

    @Column(name = "discount_code")
    private String discountCode;


    @ManyToOne
    @MapsId("orderId") // Map theo trường orderId của EmbeddedId
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "food_id", referencedColumnName = "food_id", insertable = false, updatable = false),
            @JoinColumn(name = "size", referencedColumnName = "size", insertable = false, updatable = false)
    })
    private FoodSize foodSize;

    @PrePersist
    @PreUpdate
    public void updateOrderTotalValue() {
        if (order != null) {
            order.updateTotalValue();  // Cập nhật lại tổng giá trị khi có thay đổi
        }
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailId implements Serializable {
        @Column(name = "order_id")
        private Long orderId;

        @Column(name = "food_id")
        private Long foodId;

        @Column(name = "size")
        private String size; // sửa thành String vì size là S, M, L
    }
}
