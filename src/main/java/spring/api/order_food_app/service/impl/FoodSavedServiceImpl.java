package spring.api.order_food_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.api.order_food_app.dto.FoodSavedDTO;
import spring.api.order_food_app.entity.FoodSaved;
import spring.api.order_food_app.entity.FoodSize;
import spring.api.order_food_app.entity.User;
import spring.api.order_food_app.repository.FoodSavedRepository;
import spring.api.order_food_app.repository.UserRepository;
import spring.api.order_food_app.service.FoodSavedService;
import spring.api.order_food_app.utils.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodSavedServiceImpl implements FoodSavedService {

    private final FoodSavedRepository foodSavedRepository;
    private final UserRepository userRepository;

    @Override
    public List<FoodSavedDTO> getSavedFoodsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Không tìm thấy user"));

        List<FoodSaved> savedList = foodSavedRepository.findByUser(user);

        return savedList.stream()
                .map(DtoMapper::toFoodSavedDTO)
                .collect(Collectors.toList());
    }
    @Override
    public void saveFood(Long userId, Long foodId, String size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // Tạo composite key cho FoodSize
        FoodSize.FoodSizeId foodSizeId = new FoodSize.FoodSizeId(foodId, size);

        FoodSize foodSize = new FoodSize();
        foodSize.setId(foodSizeId);

        FoodSaved foodSaved = new FoodSaved();
        foodSaved.setUser(user);
        foodSaved.setFoodSize(foodSize); // Đặt thông tin foodSize với ID (chỉ cần set ID, không cần fetch nếu không cần dùng thêm)

        foodSavedRepository.save(foodSaved);
    }

}