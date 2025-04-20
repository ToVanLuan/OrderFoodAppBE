package spring.api.order_food_app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.api.order_food_app.entity.FoodSize;
import spring.api.order_food_app.entity.FoodSize.FoodSizeId;
import spring.api.order_food_app.repository.FoodSizeRepository;
import spring.api.order_food_app.service.FoodSizeService;
import spring.api.order_food_app.entity.Food;
import spring.api.order_food_app.repository.FoodRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoodSizeServiceImpl implements FoodSizeService {

    @Autowired
    private FoodSizeRepository foodSizeRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public FoodSize saveFoodSize(Long foodId, String size, double price) {
        Optional<Food> foodOptional = foodRepository.findById(foodId);

        if (!foodOptional.isPresent()) {
            throw new RuntimeException("Food not found with id: " + foodId);
        }

        Food food = foodOptional.get();

        // Tạo ID của FoodSize
        FoodSize.FoodSizeId foodSizeId = new FoodSize.FoodSizeId(foodId, size);

        // Tạo FoodSize
        FoodSize foodSize = new FoodSize();
        foodSize.setId(foodSizeId);
        foodSize.setPrice(price);
        foodSize.setFood(food);

        return foodSizeRepository.save(foodSize);
    }

    @Override
    public FoodSize getFoodSizeById(FoodSizeId id) {
        return foodSizeRepository.findById(id).orElse(null);
    }
    @Override
    public List<FoodSize> getFoodSizesByFoodId(Long foodId) {
        return foodSizeRepository.findByFood_Id(foodId);
    }

    @Override
    public List<FoodSize> getAllFoodSizes() {
        return foodSizeRepository.findAll();
    }

    @Override
    public void deleteFoodSize(FoodSizeId id) {
        foodSizeRepository.deleteById(id);
    }
}
