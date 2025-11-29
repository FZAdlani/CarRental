package ma.emsi.rentalservice.feign;

import ma.emsi.rentalservice.dto.CarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "car-service")
public interface CarFeignClient {

    // Spring Data REST avec base-path=/api
    @GetMapping("/api/cars/{id}")
    CarDTO getCarById(@PathVariable("id") Long id);

    // Endpoint personnalisé
    @PutMapping("/api/cars/{id}/availability")
    void updateCarAvailability(
            @PathVariable("id") Long id,
            @RequestParam("available") Boolean available
    );
}