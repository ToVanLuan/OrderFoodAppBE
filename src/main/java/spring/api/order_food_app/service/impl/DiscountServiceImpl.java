package spring.api.order_food_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.api.order_food_app.entity.Discount;
import spring.api.order_food_app.entity.Food;
import spring.api.order_food_app.repository.DiscountRepository;
import spring.api.order_food_app.repository.FoodRepository;
import spring.api.order_food_app.service.DiscountService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final FoodRepository foodRepository;
    private final Random random = new Random();

    @Override
    public void generateDailyDiscounts() {
        List<Food> foods = foodRepository.findAll();
        if (foods.isEmpty()) return;

        // Shuffle danh sách món ăn
        Collections.shuffle(foods);

        int numOfDiscounts = Math.min(5, foods.size());

        for (int i = 0; i < numOfDiscounts; i++) {
            Food randomFood = foods.get(i);

            double[] discountRates = {0.1, 0.2, 0.25, 0.27, 0.3};
            double discountPercent = discountRates[random.nextInt(discountRates.length)];

            Discount discount = new Discount();
            discount.setCode(generateDiscountCode(discountPercent));
            discount.setDiscountPercent(discountPercent);
            discount.setFood(randomFood);
            discount.setStartTime(LocalDateTime.now());
            discount.setEndTime(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59));
            discount.setActive(true);

            discountRepository.save(discount);
        }
    }

    private String generateDiscountCode(double discountPercent) {
        // Ví dụ: 0.1 → 10, 0.25 → 25
        int percent = (int) (discountPercent * 100);
        return "Giảm giá: " + percent+ "%";
    }


    // Scheduled method to generate daily discounts at 9 AM every day
//    @Scheduled(cron = "0 0 9 * * *")  // Cron expression: every day at 9:00 AM
//    public void scheduleDailyDiscounts() {
//        generateDailyDiscounts();
//    }
}
