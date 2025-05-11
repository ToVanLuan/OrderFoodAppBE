package spring.api.order_food_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime dateOfOrder;

    private double totalValue;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String paymentMethod;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails=new ArrayList<>();

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED
    }

    // Phương thức tính tổng giá trị đơn hàng
    public void updateTotalValue() {
        double total = 0;
        for (OrderDetails detail : orderDetails) {
            total += detail.getPrice() * detail.getQuantity();
        }
        this.totalValue = total;
    }
}
