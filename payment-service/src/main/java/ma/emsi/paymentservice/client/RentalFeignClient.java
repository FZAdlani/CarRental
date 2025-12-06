package ma.emsi.paymentservice.client;


import ma.emsi.paymentservice.dto.RentalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "rental-service", url = "${rental.service.url}")
public interface RentalFeignClient {

    @GetMapping("/api/rentals/{id}")
    RentalDTO getRentalById(@PathVariable("id") Long id);

    @PutMapping("/api/rentals/{id}/confirm")
    RentalDTO confirmRental(@PathVariable("id") Long id, @RequestParam("paymentId") String paymentId);
}