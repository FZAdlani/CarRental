package ma.emsi.carservice.repository;

import ma.emsi.carservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(path = "cars")
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
    List<Car> findByBrandAndAvailableTrue(String brand);
    Car findByLicensePlate(String licensePlate);
}