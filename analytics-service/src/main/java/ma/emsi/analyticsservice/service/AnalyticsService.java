package ma.emsi.analyticsservice.service;

import ma.emsi.analyticsservice.client.CarFeignClient;
import ma.emsi.analyticsservice.client.RentalFeignClient;
import ma.emsi.analyticsservice.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private CarFeignClient carFeignClient;

    @Autowired
    private RentalFeignClient rentalFeignClient;

    /**
     * Calculer le taux d'occupation de toutes les voitures
     */
    public List<CarOccupancyDTO> calculateOccupancyRates() {
        try {
            List<CarDTO> cars = carFeignClient.getAllCarsHal().getCars();
            List<RentalDTO> rentals = rentalFeignClient.getAllRentals();

            if (cars == null || cars.isEmpty()) {
                return List.of();
            }

            return cars.stream().map(car -> {
                List<RentalDTO> carRentals = rentals.stream()
                        .filter(r -> r.getCarId().equals(car.getId()))
                        .filter(r -> !"CANCELLED".equals(r.getStatus()))
                        .collect(Collectors.toList());

                int totalRentals = carRentals.size();
                int totalDaysRented = carRentals.stream()
                        .mapToInt(r -> (int) ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()))
                        .sum();

                double totalRevenue = carRentals.stream()
                        .mapToDouble(RentalDTO::getTotalPrice)
                        .sum();

                // Calculer le taux d'occupation (sur 365 jours)
                double occupancyRate = (totalDaysRented / 365.0) * 100;

                String status = car.getAvailable() ? "Disponible" : "Louée";

                return new CarOccupancyDTO(
                        car.getId(),
                        car.getBrand(),
                        car.getModel(),
                        car.getRegistrationNumber(),
                        totalRentals,
                        totalDaysRented,
                        Math.round(occupancyRate * 100.0) / 100.0,
                        totalRevenue,
                        status
                );
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du calcul des taux d'occupation: " + e.getMessage());
        }
    }

    /**
     * Calculer le taux d'occupation pour une voiture spécifique
     */
    public CarOccupancyDTO calculateCarOccupancy(Long carId) {
        CarDTO car = carFeignClient.getCarById(carId);
        List<RentalDTO> allRentals = rentalFeignClient.getAllRentals();

        List<RentalDTO> carRentals = allRentals.stream()
                .filter(r -> r.getCarId().equals(carId))
                .filter(r -> !"CANCELLED".equals(r.getStatus()))
                .collect(Collectors.toList());

        int totalRentals = carRentals.size();
        int totalDaysRented = carRentals.stream()
                .mapToInt(r -> (int) ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()))
                .sum();

        double totalRevenue = carRentals.stream()
                .mapToDouble(RentalDTO::getTotalPrice)
                .sum();

        double occupancyRate = (totalDaysRented / 365.0) * 100;
        String status = car.getAvailable() ? "Disponible" : "Louée";

        return new CarOccupancyDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getRegistrationNumber(),
                totalRentals,
                totalDaysRented,
                Math.round(occupancyRate * 100.0) / 100.0,
                totalRevenue,
                status
        );
    }

    /**
     * Statistiques globales
     */
    public GlobalStatisticsDTO getGlobalStatistics() {
        List<CarDTO> cars = carFeignClient.getAllCarsHal().getCars();
        List<RentalDTO> rentals = rentalFeignClient.getAllRentals();

        int totalCars = cars.size();
        int availableCars = (int) cars.stream().filter(CarDTO::getAvailable).count();
        int rentedCars = totalCars - availableCars;

        int totalRentals = rentals.size();
        int activeRentals = (int) rentals.stream()
                .filter(r -> "CONFIRMED".equals(r.getStatus()) || "ACTIVE".equals(r.getStatus()))
                .count();
        int completedRentals = (int) rentals.stream()
                .filter(r -> "COMPLETED".equals(r.getStatus()))
                .count();

        double totalRevenue = rentals.stream()
                .filter(r -> !"CANCELLED".equals(r.getStatus()))
                .mapToDouble(RentalDTO::getTotalPrice)
                .sum();

        // Calculer le taux d'occupation moyen
        List<CarOccupancyDTO> occupancies = calculateOccupancyRates();
        double averageOccupancyRate = occupancies.stream()
                .mapToDouble(CarOccupancyDTO::getOccupancyRate)
                .average()
                .orElse(0.0);

        return new GlobalStatisticsDTO(
                totalCars,
                availableCars,
                rentedCars,
                totalRentals,
                activeRentals,
                completedRentals,
                Math.round(totalRevenue * 100.0) / 100.0,
                Math.round(averageOccupancyRate * 100.0) / 100.0
        );
    }

    /**
     * Analyse par période
     */
    public AnalyticsPeriodDTO analyzeByPeriod(LocalDate startDate, LocalDate endDate) {
        List<RentalDTO> allRentals = rentalFeignClient.getAllRentals();

        // Filtrer les réservations dans la période
        List<RentalDTO> periodRentals = allRentals.stream()
                .filter(r -> !r.getStartDate().isBefore(startDate) && !r.getEndDate().isAfter(endDate))
                .filter(r -> !"CANCELLED".equals(r.getStatus()))
                .collect(Collectors.toList());

        List<CarDTO> cars = carFeignClient.getAllCarsHal().getCars();

        List<CarOccupancyDTO> occupancies = cars.stream().map(car -> {
            List<RentalDTO> carRentals = periodRentals.stream()
                    .filter(r -> r.getCarId().equals(car.getId()))
                    .collect(Collectors.toList());

            int totalRentals = carRentals.size();
            int totalDaysRented = carRentals.stream()
                    .mapToInt(r -> (int) ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()))
                    .sum();

            double totalRevenue = carRentals.stream()
                    .mapToDouble(RentalDTO::getTotalPrice)
                    .sum();

            long periodDays = ChronoUnit.DAYS.between(startDate, endDate);
            double occupancyRate = periodDays > 0 ? (totalDaysRented / (double) periodDays) * 100 : 0;

            return new CarOccupancyDTO(
                    car.getId(),
                    car.getBrand(),
                    car.getModel(),
                    car.getRegistrationNumber(),
                    totalRentals,
                    totalDaysRented,
                    Math.round(occupancyRate * 100.0) / 100.0,
                    totalRevenue,
                    car.getAvailable() ? "Disponible" : "Louée"
            );
        }).collect(Collectors.toList());

        // Statistiques globales pour la période
        GlobalStatisticsDTO stats = getGlobalStatistics();

        return new AnalyticsPeriodDTO(startDate, endDate, occupancies, stats);
    }

    /**
     * Revenus par catégorie
     */
    public List<CategoryRevenueDTO> getRevenueByCategory() {
        List<CarDTO> cars = carFeignClient.getAllCarsHal().getCars();
        List<RentalDTO> rentals = rentalFeignClient.getAllRentals();

        Map<String, List<RentalDTO>> rentalsByCategory = new HashMap<>();

        for (RentalDTO rental : rentals) {
            if ("CANCELLED".equals(rental.getStatus())) continue;

            CarDTO car = cars.stream()
                    .filter(c -> c.getId().equals(rental.getCarId()))
                    .findFirst()
                    .orElse(null);

            if (car != null) {
                rentalsByCategory.computeIfAbsent(car.getCategory(), k -> new ArrayList<>()).add(rental);
            }
        }

        return rentalsByCategory.entrySet().stream()
                .map(entry -> {
                    String category = entry.getKey();
                    List<RentalDTO> categoryRentals = entry.getValue();

                    int totalRentals = categoryRentals.size();
                    double totalRevenue = categoryRentals.stream()
                            .mapToDouble(RentalDTO::getTotalPrice)
                            .sum();
                    double averageRevenue = totalRentals > 0 ? totalRevenue / totalRentals : 0;

                    return new CategoryRevenueDTO(
                            category,
                            totalRentals,
                            Math.round(totalRevenue * 100.0) / 100.0,
                            Math.round(averageRevenue * 100.0) / 100.0
                    );
                })
                .sorted((a, b) -> Double.compare(b.getTotalRevenue(), a.getTotalRevenue()))
                .collect(Collectors.toList());
    }

    /**
     * Voitures les plus rentables
     */
    public List<CarOccupancyDTO> getMostProfitableCars(int limit) {
        return calculateOccupancyRates().stream()
                .sorted((a, b) -> Double.compare(b.getTotalRevenue(), a.getTotalRevenue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Voitures les moins occupées
     */
    public List<CarOccupancyDTO> getLeastOccupiedCars(int limit) {
        return calculateOccupancyRates().stream()
                .sorted(Comparator.comparingDouble(CarOccupancyDTO::getOccupancyRate))
                .limit(limit)
                .collect(Collectors.toList());
    }
}