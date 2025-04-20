package spring.api.order_food_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.api.order_food_app.dto.RestaurantSavedDTO;
import spring.api.order_food_app.entity.Restaurant;
import spring.api.order_food_app.entity.RestaurantSaved;
import spring.api.order_food_app.entity.User;
import spring.api.order_food_app.repository.RestaurantRepository;
import spring.api.order_food_app.repository.RestaurantSavedRepository;
import spring.api.order_food_app.repository.UserRepository;
import spring.api.order_food_app.service.RestaurantSavedService;
import spring.api.order_food_app.utils.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantSavedServiceImpl implements RestaurantSavedService {

    private final RestaurantSavedRepository restaurantSavedRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    public void saveRestaurant(Long userId, Long restaurantId) {
        boolean alreadySaved = restaurantSavedRepository.existsByUserIdAndRestaurantId(userId, restaurantId);
        if (alreadySaved) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        RestaurantSaved saved = new RestaurantSaved();
        saved.setUser(user);
        saved.setRestaurant(restaurant);

        restaurantSavedRepository.save(saved);
    }

    @Override
    public List<RestaurantSavedDTO> getSavedRestaurantsByUser(Long userId) {
        List<RestaurantSaved> savedList = restaurantSavedRepository.findByUserId(userId);

        return savedList.stream()
                .map(DtoMapper::toRestaurantSaveDTO)
                .collect(Collectors.toList());
    }
}
