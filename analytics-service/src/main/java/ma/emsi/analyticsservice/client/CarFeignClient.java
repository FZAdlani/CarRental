package ma.emsi.analyticsservice.client;

import ma.emsi.analyticsservice.dto.CarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "car-service", url = "${car.service.url}")
public interface CarFeignClient {

    @GetMapping("/api/cars")
    List<CarDTO> getAllCars();

    @GetMapping("/api/cars/{id}")
    CarDTO getCarById(@PathVariable("id") Long id);
}