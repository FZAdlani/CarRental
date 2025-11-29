package ma.emsi.rentalservice.service;

import ma.emsi.rentalservice.feign.CarFeignClient;
import ma.emsi.rentalservice.dto.CarDTO;
import ma.emsi.rentalservice.dto.RentalRequestDTO;
import ma.emsi.rentalservice.model.Rental;
import ma.emsi.rentalservice.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarFeignClient carFeignClient;
public Rental createRental(RentalRequestDTO request) {
    // Validation et log du carId
    if (request.getCarId() == null) {
        throw new RuntimeException("L'ID de la voiture est requis");
    }
    
    System.out.println("DEBUG - CarId reçu: " + request.getCarId());
    
    // Vérifier la disponibilité de la voiture via FeignClient
    CarDTO car = carFeignClient.getCarById(request.getCarId());

    if (car == null) {
        throw new RuntimeException("Voiture non trouvée avec l'ID: " + request.getCarId());
    }

    if (!car.getAvailable()) {
        throw new RuntimeException("Cette voiture n'est pas disponible");
    }

    // Calculer le nombre de jours et le prix total
    long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
    if (days <= 0) {
        throw new RuntimeException("La date de fin doit être après la date de début");
    }

    double totalPrice = days * car.getPricePerDay();

    // Créer la réservation
    Rental rental = new Rental();
    rental.setCarId(request.getCarId());
    rental.setClientName(request.getClientName());
    rental.setClientEmail(request.getClientEmail());
    rental.setClientPhone(request.getClientPhone());
    rental.setStartDate(request.getStartDate());
    rental.setEndDate(request.getEndDate());
    rental.setTotalPrice(totalPrice);
    rental.setStatus(Rental.RentalStatus.PENDING);

    // Sauvegarder la réservation
    Rental savedRental = rentalRepository.save(rental);

    // Log avant l'appel Feign
    System.out.println("DEBUG - Mise à jour disponibilité pour carId: " + request.getCarId());
    
    // Mettre à jour la disponibilité de la voiture
    try {
        carFeignClient.updateCarAvailability(request.getCarId(), false);
    } catch (Exception e) {
        System.err.println("Erreur lors de la mise à jour de disponibilité: " + e.getMessage());
        throw new RuntimeException("Erreur lors de la mise à jour de la voiture: " + e.getMessage());
    }

    return savedRental;
}

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));
    }

    public List<Rental> getRentalsByClientEmail(String email) {
        return rentalRepository.findByClientEmail(email);
    }

    public Rental cancelRental(Long id) {
        Rental rental = getRentalById(id);

        if (rental.getStatus() == Rental.RentalStatus.COMPLETED) {
            throw new RuntimeException("Impossible d'annuler une réservation terminée");
        }

        rental.setStatus(Rental.RentalStatus.CANCELLED);

        // Remettre la voiture disponible
        carFeignClient.updateCarAvailability(rental.getCarId(), true);

        return rentalRepository.save(rental);
    }

    public Rental confirmRental(Long id, String paymentId) {
        Rental rental = getRentalById(id);
        rental.setStatus(Rental.RentalStatus.CONFIRMED);
        rental.setPaymentId(paymentId);
        return rentalRepository.save(rental);
    }
}