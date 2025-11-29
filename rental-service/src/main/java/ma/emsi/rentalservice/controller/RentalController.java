package ma.emsi.rentalservice.controller;

import ma.emsi.rentalservice.dto.RentalRequestDTO;
import ma.emsi.rentalservice.model.Rental;
import ma.emsi.rentalservice.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(origins = "*")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<?> createRental(@RequestBody RentalRequestDTO request) {
        try {
            Rental rental = rentalService.createRental(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable Long id) {
        try {
            Rental rental = rentalService.getRentalById(id);
            return ResponseEntity.ok(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/client/{email}")
    public ResponseEntity<List<Rental>> getRentalsByClient(@PathVariable String email) {
        return ResponseEntity.ok(rentalService.getRentalsByClientEmail(email));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRental(@PathVariable Long id) {
        try {
            Rental rental = rentalService.cancelRental(id);
            return ResponseEntity.ok(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmRental(@PathVariable Long id, @RequestParam String paymentId) {
        try {
            Rental rental = rentalService.confirmRental(id, paymentId);
            return ResponseEntity.ok(rental);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur: " + e.getMessage());
        }
    }
}