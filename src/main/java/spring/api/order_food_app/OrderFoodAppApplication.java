package spring.api.order_food_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrderFoodAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderFoodAppApplication.class, args);
	}

}
