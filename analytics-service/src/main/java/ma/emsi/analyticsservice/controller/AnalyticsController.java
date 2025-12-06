package ma.emsi.analyticsservice.controller;

import ma.emsi.analyticsservice.dto.*;
import ma.emsi.analyticsservice.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Endpoint de test
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Analytics Service is running!");
    }

    /**
     * Taux d'occupation de toutes les voitures
     */
    @GetMapping("/occupancy")
    public ResponseEntity<?> getAllOccupancyRates() {
        try {
            return ResponseEntity.ok(analyticsService.calculateOccupancyRates());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Taux d'occupation d'une voiture spécifique
     */
    @GetMapping("/occupancy/car/{carId}")
    public ResponseEntity<?> getCarOccupancy(@PathVariable Long carId) {
        try {
            return ResponseEntity.ok(analyticsService.calculateCarOccupancy(carId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Statistiques globales
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getGlobalStatistics() {
        try {
            return ResponseEntity.ok(analyticsService.getGlobalStatistics());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Analyse par période
     */
    @GetMapping("/period")
    public ResponseEntity<AnalyticsPeriodDTO> analyzeByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.analyzeByPeriod(startDate, endDate));
    }

    /**
     * Revenus par catégorie
     */
    @GetMapping("/revenue/category")
    public ResponseEntity<List<CategoryRevenueDTO>> getRevenueByCategory() {
        return ResponseEntity.ok(analyticsService.getRevenueByCategory());
    }

    /**
     * Top voitures les plus rentables
     */
    @GetMapping("/top/profitable")
    public ResponseEntity<List<CarOccupancyDTO>> getMostProfitableCars(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(analyticsService.getMostProfitableCars(limit));
    }

    /**
     * Voitures les moins occupées
     */
    @GetMapping("/least/occupied")
    public ResponseEntity<List<CarOccupancyDTO>> getLeastOccupiedCars(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(analyticsService.getLeastOccupiedCars(limit));
    }
}