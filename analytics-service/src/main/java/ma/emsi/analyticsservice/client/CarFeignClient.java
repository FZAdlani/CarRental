package ma.emsi.analyticsservice.client;

import ma.emsi.analyticsservice.dto.CarDTO;
import ma.emsi.analyticsservice.dto.CarsHalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "car-service", url = "${car.service.url}")
public interface CarFeignClient {

    @GetMapping("/api/cars")
    CarsHalResponse getAllCarsHal();

    @GetMapping("/api/cars/{id}")
    CarDTO getCarById(@PathVariable("id") Long id);
}