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
     * Taux d'occupation de toutes les voitures
     */
    @GetMapping("/occupancy")
    public ResponseEntity<List<CarOccupancyDTO>> getAllOccupancyRates() {
        return ResponseEntity.ok(analyticsService.calculateOccupancyRates());
    }

    /**
     * Taux d'occupation d'une voiture spécifique
     */
    @GetMapping("/occupancy/car/{carId}")
    public ResponseEntity<CarOccupancyDTO> getCarOccupancy(@PathVariable Long carId) {
        return ResponseEntity.ok(analyticsService.calculateCarOccupancy(carId));
    }

    /**
     * Statistiques globales
     */
    @GetMapping("/statistics")
    public ResponseEntity<GlobalStatisticsDTO> getGlobalStatistics() {
        return ResponseEntity.ok(analyticsService.getGlobalStatistics());
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