package ma.emsi.rentalservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            log.info("Rental Service initialized and ready to accept rental requests");
            log.info("Make sure car-service is running on port 8081");
        };
    }
}

