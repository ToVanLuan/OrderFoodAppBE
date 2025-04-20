package spring.api.order_food_app.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dbrcpumvv");
        config.put("api_key", "172397877143126");
        config.put("api_secret", "6feLF8hPnaF21VssnSf5nXEcFx4");
        return new Cloudinary(config);
    }
}

