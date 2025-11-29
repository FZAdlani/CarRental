package ma.emsi.carservice.controller;

import ma.emsi.carservice.model.Car;
import ma.emsi.carservice.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*")
public class CarCustomController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) {

        try {
            System.out.println("DEBUG - Mise à jour disponibilité pour carId: " + id + ", disponible: " + available);

            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'ID: " + id));

            car.setAvailable(available);
            Car updatedCar = carRepository.save(car);

            return ResponseEntity.ok(updatedCar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur: " + e.getMessage());
        }
    }
}