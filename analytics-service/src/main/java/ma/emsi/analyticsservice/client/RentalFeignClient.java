package ma.emsi.analyticsservice.client;

import ma.emsi.analyticsservice.dto.RentalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "rental-service", url = "${rental.service.url}")
public interface RentalFeignClient {

    @GetMapping("/api/rentals")
    List<RentalDTO> getAllRentals();

    @GetMapping("/api/rentals/{id}")
    RentalDTO getRentalById(@PathVariable("id") Long id);
}