package spring.api.order_food_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import spring.api.order_food_app.entity.CartItem;
import spring.api.order_food_app.entity.User;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);
    int countByUserId(Long userId);
    void deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId AND c.foodSize.id.foodId = :foodId AND c.foodSize.id.size = :size")
    void deleteByUserIdAndFoodIdAndSize(@Param("userId") Long userId,
                                        @Param("foodId") Long foodId,
                                        @Param("size") String size);

}

