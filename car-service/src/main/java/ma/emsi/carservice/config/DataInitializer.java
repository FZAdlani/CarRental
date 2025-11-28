package ma.emsi.carservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.carservice.model.Car;
import ma.emsi.carservice.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CarRepository carRepository) {
        return args -> {
            if (carRepository.count() == 0) {
                log.info("Initializing car database with sample data");

                carRepository.save(new Car("Toyota", "Camry", 2023, "ABC-123", "White", new BigDecimal("350.00")));
                carRepository.save(new Car("Honda", "Accord", 2022, "DEF-456", "Black", new BigDecimal("320.00")));
                carRepository.save(new Car("BMW", "X5", 2023, "GHI-789", "Blue", new BigDecimal("650.00")));
                carRepository.save(new Car("Mercedes", "C-Class", 2022, "JKL-012", "Silver", new BigDecimal("600.00")));
                carRepository.save(new Car("Audi", "A4", 2023, "MNO-345", "Red", new BigDecimal("550.00")));

                log.info("Initialized {} cars", carRepository.count());
            } else {
                log.info("Database already contains {} cars", carRepository.count());
            }
        };
    }
}

