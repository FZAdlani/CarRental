package ma.emsi.rentalservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.rentalservice.dto.RentalRequest;
import ma.emsi.rentalservice.dto.RentalResponse;
import ma.emsi.rentalservice.model.Rental;
import ma.emsi.rentalservice.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<RentalResponse> createRental(@Valid @RequestBody RentalRequest request) {
        RentalResponse response = rentalService.createRental(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Long id) {
        RentalResponse response = rentalService.getRentalById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RentalResponse>> getAllRentals(
            @RequestParam(required = false) String customerEmail) {

        if (customerEmail != null && !customerEmail.isEmpty()) {
            List<RentalResponse> rentals = rentalService.getRentalsByCustomerEmail(customerEmail);
            return ResponseEntity.ok(rentals);
        }

        List<RentalResponse> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RentalResponse> updateRentalStatus(
            @PathVariable Long id,
            @RequestParam Rental.RentalStatus status) {

        RentalResponse response = rentalService.updateRentalStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }
}

