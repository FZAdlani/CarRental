package ma.emsi.rentalservice.repository;

import ma.emsi.rentalservice.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByCarId(Long carId);

    List<Rental> findByCustomerEmail(String customerEmail);

    List<Rental> findByStatus(Rental.RentalStatus status);
}

