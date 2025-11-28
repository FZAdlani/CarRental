package ma.emsi.rentalservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.rentalservice.client.CarServiceClient;
import ma.emsi.rentalservice.dto.CarDto;
import ma.emsi.rentalservice.dto.RentalRequest;
import ma.emsi.rentalservice.dto.RentalResponse;
import ma.emsi.rentalservice.exception.CarNotAvailableException;
import ma.emsi.rentalservice.exception.InvalidRentalDatesException;
import ma.emsi.rentalservice.exception.ResourceNotFoundException;
import ma.emsi.rentalservice.model.Rental;
import ma.emsi.rentalservice.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarServiceClient carServiceClient;

    @Transactional
    public RentalResponse createRental(RentalRequest request) {
        log.info("Creating rental for car ID: {}", request.getCarId());

        // Validate dates
        validateDates(request.getStartDate(), request.getEndDate());

        // Get car information from car-service
        CarDto car = carServiceClient.getCarById(request.getCarId());
        if (car == null) {
            throw new ResourceNotFoundException("Car not found with ID: " + request.getCarId());
        }

        // Check if car is available
        if (!car.getAvailable()) {
            throw new CarNotAvailableException("Car with ID " + request.getCarId() + " is not available");
        }

        // Calculate total price
        long numberOfDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        BigDecimal totalPrice = car.getDailyPrice().multiply(BigDecimal.valueOf(numberOfDays));

        // Create rental
        Rental rental = Rental.builder()
                .carId(request.getCarId())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(request.getCustomerPhone())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalPrice(totalPrice)
                .status(Rental.RentalStatus.CONFIRMED)
                .build();

        rental = rentalRepository.save(rental);
        log.info("Rental created with ID: {}", rental.getId());

        // Update car availability
        try {
            carServiceClient.updateCarAvailability(request.getCarId(), false);
            log.info("Car availability updated for car ID: {}", request.getCarId());
        } catch (Exception e) {
            log.error("Failed to update car availability", e);
            // Continue even if update fails - could be handled with event-driven architecture
        }

        return mapToResponse(rental, car);
    }

    public RentalResponse getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + id));

        CarDto car = null;
        try {
            car = carServiceClient.getCarById(rental.getCarId());
        } catch (Exception e) {
            log.error("Failed to fetch car details for rental ID: {}", id, e);
        }

        return mapToResponse(rental, car);
    }

    public List<RentalResponse> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .map(rental -> {
                    CarDto car = null;
                    try {
                        car = carServiceClient.getCarById(rental.getCarId());
                    } catch (Exception e) {
                        log.error("Failed to fetch car details for rental ID: {}", rental.getId(), e);
                    }
                    return mapToResponse(rental, car);
                })
                .collect(Collectors.toList());
    }

    public List<RentalResponse> getRentalsByCustomerEmail(String email) {
        List<Rental> rentals = rentalRepository.findByCustomerEmail(email);
        return rentals.stream()
                .map(rental -> {
                    CarDto car = null;
                    try {
                        car = carServiceClient.getCarById(rental.getCarId());
                    } catch (Exception e) {
                        log.error("Failed to fetch car details", e);
                    }
                    return mapToResponse(rental, car);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public RentalResponse updateRentalStatus(Long id, Rental.RentalStatus status) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + id));

        rental.setStatus(status);
        rental = rentalRepository.save(rental);

        // If rental is completed or cancelled, make car available again
        if (status == Rental.RentalStatus.COMPLETED || status == Rental.RentalStatus.CANCELLED) {
            try {
                carServiceClient.updateCarAvailability(rental.getCarId(), true);
                log.info("Car {} made available again after rental {}", rental.getCarId(), status);
            } catch (Exception e) {
                log.error("Failed to update car availability", e);
            }
        }

        CarDto car = null;
        try {
            car = carServiceClient.getCarById(rental.getCarId());
        } catch (Exception e) {
            log.error("Failed to fetch car details", e);
        }

        return mapToResponse(rental, car);
    }

    @Transactional
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with ID: " + id));

        // Make car available again
        try {
            carServiceClient.updateCarAvailability(rental.getCarId(), true);
        } catch (Exception e) {
            log.error("Failed to update car availability", e);
        }

        rentalRepository.delete(rental);
        log.info("Rental deleted with ID: {}", id);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            throw new InvalidRentalDatesException("Start date cannot be in the past");
        }

        if (endDate.isBefore(startDate)) {
            throw new InvalidRentalDatesException("End date must be after start date");
        }

        if (endDate.isEqual(startDate)) {
            throw new InvalidRentalDatesException("Rental must be for at least one day");
        }
    }

    private RentalResponse mapToResponse(Rental rental, CarDto car) {
        return RentalResponse.builder()
                .id(rental.getId())
                .carId(rental.getCarId())
                .car(car)
                .customerName(rental.getCustomerName())
                .customerEmail(rental.getCustomerEmail())
                .customerPhone(rental.getCustomerPhone())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .totalPrice(rental.getTotalPrice())
                .status(rental.getStatus())
                .createdAt(rental.getCreatedAt())
                .build();
    }
}

