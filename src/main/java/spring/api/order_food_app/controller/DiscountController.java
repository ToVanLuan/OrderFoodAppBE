package spring.api.order_food_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.api.order_food_app.dto.DiscountDTO;
import spring.api.order_food_app.entity.Discount;
import spring.api.order_food_app.repository.DiscountRepository;
import spring.api.order_food_app.service.DiscountService;
import spring.api.order_food_app.utils.DtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountRepository discountRepository;
    private final DiscountService discountService;

    @PostMapping("/generate-daily")
    public String generateDailyDiscounts() {
        discountService.generateDailyDiscounts();
        return "Generated 5 daily discounts successfully!";
    }
    // Endpoint để lấy ra tất cả mã giảm giá có hiệu lực
    @GetMapping("/active")
    public ResponseEntity<List<DiscountDTO>> getActiveDiscounts() {
        // Lấy danh sách mã giảm giá có hiệu lực
        List<Discount> discounts = discountRepository.findByEndTimeAfterAndActiveTrue(LocalDateTime.now());

        // Chuyển đổi danh sách Discount thành danh sách DiscountDTO
        List<DiscountDTO> discountDTOs = discounts.stream()
                .map(DtoMapper::toDiscountDTO)
                .collect(Collectors.toList());

        // Trả về ResponseEntity với mã HTTP 200 và dữ liệu là danh sách DiscountDTO
        return ResponseEntity.ok(discountDTOs);
    }

    }
