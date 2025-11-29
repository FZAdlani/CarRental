package ma.emsi.rentalservice.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PaymentConfig {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.stripe.com/v1")
                .defaultHeader("Authorization", "Bearer " + stripeApiKey)
                .build();
    }
}