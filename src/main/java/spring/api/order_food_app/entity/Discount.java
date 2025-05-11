package spring.api.order_food_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // mã giảm giá, ví dụ: FOOD20OFF

    private double discountPercent; // tỷ lệ giảm giá: 0.2 = 20%

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food; // món ăn áp dụng

    private boolean active; // có còn hiệu lực không
}
