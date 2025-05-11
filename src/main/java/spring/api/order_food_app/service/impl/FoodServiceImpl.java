package spring.api.order_food_app.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.api.order_food_app.entity.Food;
import spring.api.order_food_app.entity.Restaurant;
import spring.api.order_food_app.repository.FoodRepository;
import spring.api.order_food_app.repository.RestaurantRepository;
import spring.api.order_food_app.service.FoodService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final Cloudinary cloudinary;

    @Autowired
    public FoodServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    @Override
    public void deleteFood(Long id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
        }
    }
    @Override
    public Food saveFood(String name,String type, String description, Long restaurantId, List<MultipartFile> imageFiles) throws IOException {
        // Tìm nhà hàng và danh mục
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);


        if (!restaurantOpt.isPresent()) {
            throw new RuntimeException("Restaurant or Category not found");
        }

        Restaurant restaurant = restaurantOpt.get();

        // Upload ảnh lên Cloudinary và lấy các URL ảnh
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.asMap("resource_type", "auto", "folder", "food_images"));
            imageUrls.add((String) uploadResult.get("url"));
        }

        // Tạo Food và lưu vào cơ sở dữ liệu
        Food food = new Food();
        food.setName(name);
        food.setType(type);
        food.setDescription(description);
        food.setRestaurant(restaurant);
        food.setImageUrls(imageUrls);

        return foodRepository.save(food);
    }
    @Override
    public List<Food> searchFoodsByName(String keyword) {
        return foodRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Food updateFood(Long id, String name,String type, String description, Long restaurantId,  List<MultipartFile> imageFiles) throws IOException {
        Optional<Food> foodOpt = foodRepository.findById(id);

        if (!foodOpt.isPresent()) {
            throw new RuntimeException("Food not found");
        }

        Food food = foodOpt.get();

        // Cập nhật thông tin món ăn
        food.setName(name);
        food.setDescription(description);
        food.setType(type);


        // Tìm nhà hàng và danh mục
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);

        if (!restaurantOpt.isPresent()) {
            throw new RuntimeException("Restaurant or Category not found");
        }

        food.setRestaurant(restaurantOpt.get());

        // Nếu có ảnh mới, upload và lấy các URL ảnh mới
        List<String> imageUrls = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile imageFile : imageFiles) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.asMap("resource_type", "auto", "folder", "food_images"));
                imageUrls.add((String) uploadResult.get("url"));
            }
        }

        // Nếu có ảnh mới, cập nhật ảnh cho món ăn
        if (!imageUrls.isEmpty()) {
            food.setImageUrls(imageUrls);
        }

        return foodRepository.save(food);
    }
    @Override
    public Food getFoodById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    @Override
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
}
