package spring.api.order_food_app.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.api.order_food_app.entity.Food;
import spring.api.order_food_app.entity.Restaurant;
import spring.api.order_food_app.repository.RestaurantRepository;
import spring.api.order_food_app.service.RestaurantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private final Cloudinary cloudinary;

    @Autowired
    public RestaurantServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public Restaurant saveRestaurant(String name, String address, String phone, List<MultipartFile> imageFiles) throws IOException {
        // Upload ảnh lên Cloudinary và lấy các URL ảnh
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.asMap("resource_type", "auto", "folder", "restaurant_images"));
            imageUrls.add((String) uploadResult.get("url"));
        }

        // Tạo Restaurant và lưu vào cơ sở dữ liệu
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setPhone(phone);
        restaurant.setImageUrls(imageUrls);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long id, String name, String address, String phone, List<MultipartFile> imageFiles) throws IOException {
        Restaurant existingRestaurant = restaurantRepository.findById(id).orElse(null);
        if (existingRestaurant == null) {
            return null;
        }

        // Cập nhật thông tin nhà hàng
        existingRestaurant.setName(name);
        existingRestaurant.setAddress(address);
        existingRestaurant.setPhone(phone);

        // Upload các hình ảnh mới nếu có
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.asMap("resource_type", "auto", "folder", "restaurant_images"));
                imageUrls.add((String) uploadResult.get("url"));
            }
            existingRestaurant.setImageUrls(imageUrls);
        }

        return restaurantRepository.save(existingRestaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
        }
    }
    @Override
    public List<Food> getFoodsByRestaurantId(Long restaurantId) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        return restaurantOpt.map(Restaurant::getFoods).orElse(new ArrayList<>());
    }
}
