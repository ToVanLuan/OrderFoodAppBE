package spring.api.order_food_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private String code;            // Mã giảm giá
    private double discountPercent; // Tỷ lệ giảm giá
    private LocalDateTime startTime; // Thời gian bắt đầu
    private LocalDateTime endTime;   // Thời gian kết thúc
    private Long foodId;
}
