package spring.api.order_food_app.repository;

import spring.api.order_food_app.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByEndTimeAfterAndActiveTrue(LocalDateTime now);
    Optional<Discount> findByCode(String code);


}
